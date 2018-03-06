package org.villagex.villagex.activity;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.villagex.villagex.R;
import org.villagex.villagex.data.Project;
import org.villagex.villagex.data.Village;
import org.villagex.villagex.network.MarkerService;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int MAP_PADDING_PIXELS = 100;

    private GoogleMap mMap;
    private ClusterManager mClusterManager;
    private Stack<LatLngBounds> mLevelBounds = new Stack<>();
    private MarkerService mService;
    private Hashtable<Integer, List<Project>> mVillageProjectMapping = new Hashtable<>();
    private List<Marker> mCurrentVillageMarkers = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        LatLng africa = new LatLng(10f,10f);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(africa, 2f));

        mClusterManager = new ClusterManager<Village>(this, mMap);
        mClusterManager.setRenderer(new VillageRenderer(this, mMap, mClusterManager));

        mClusterManager.setOnClusterItemClickListener(clusterItem -> {
            int id = Integer.parseInt(clusterItem.getTitle());
            List<Project> projects = mVillageProjectMapping.get(id);
            mCurrentVillageMarkers = new ArrayList<>();

            mLevelBounds.push(mMap.getProjection().getVisibleRegion().latLngBounds);
            LatLngBounds.Builder builder = LatLngBounds.builder();

            for (Project project : projects) {
                Marker nextMarker = mMap.addMarker(new MarkerOptions()
                        .position(project.getPosition())
                        .icon(BitmapDescriptorFactory.fromResource(getResources().getIdentifier(
                                "type_" + project.getType(), "drawable", getPackageName()))
                        )
                );
                builder.include(nextMarker.getPosition());
                mCurrentVillageMarkers.add(nextMarker);
            }
            final LatLngBounds bounds = builder.build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, MAP_PADDING_PIXELS));

            return true;
        });

        mClusterManager.setOnClusterClickListener(cluster -> {
                mLevelBounds.push(mMap.getProjection().getVisibleRegion().latLngBounds);
                LatLngBounds.Builder builder = LatLngBounds.builder();
                for (Object item : cluster.getItems()) {
                    builder.include(((ClusterItem)item).getPosition());
                }
                final LatLngBounds bounds = builder.build();
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, MAP_PADDING_PIXELS));

                return true;
        });

        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        doNetworkSetup();
    }

    @Override
    public void onBackPressed() {
        if (mCurrentVillageMarkers != null) {
            for (Marker marker : mCurrentVillageMarkers) {
                marker.remove();
            }
            mCurrentVillageMarkers = null;
        }
        if (!mLevelBounds.isEmpty()) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mLevelBounds.pop(), 0));
        } else {
            super.onBackPressed();
        }
    }

    private void doNetworkSetup() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mService = retrofit.create(MarkerService.class);

        loadVillageMarkers();
    }

    private void loadVillageMarkers() {
        mService.getVillages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(villages -> {
                    mClusterManager.addItems(villages);

                    LatLng malawi = new LatLng(-13.5, 34.5);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(malawi, 6.5f), 2000, null);

                    loadProjects();
                });
    }

    private void loadProjects() {
        mService.getProjects()
                .flatMap(projects -> Observable.fromIterable(projects))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(project -> {
                    List<Project> projectList = mVillageProjectMapping.get(project.getVillageId());
                    if (projectList == null) {
                        projectList = new ArrayList<>();
                        mVillageProjectMapping.put(project.getVillageId(), projectList);
                    }
                    projectList.add(project);
                });
    }

    private class VillageRenderer extends DefaultClusterRenderer<Village> {

        public VillageRenderer(Context context, GoogleMap map, ClusterManager<Village> manager) {
            super(context, map, manager);
            setMinClusterSize(1);
        }

        @Override
        protected void onBeforeClusterItemRendered(Village village, MarkerOptions markerOptions) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_village));
        }
    }
}
