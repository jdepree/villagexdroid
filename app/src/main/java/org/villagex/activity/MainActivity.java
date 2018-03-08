package org.villagex.activity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

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
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.villagex.R;
import org.villagex.model.Config;
import org.villagex.model.Project;
import org.villagex.model.Village;
import org.villagex.model.database.DatabaseHelper;
import org.villagex.model.database.DatabaseSchema;
import org.villagex.model.database.ProjectCursorWrapper;
import org.villagex.model.database.VillageCursorWrapper;
import org.villagex.network.DataService;
import org.villagex.util.AppUtils;
import org.villagex.view.ProjectAdapter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int MAP_PADDING_PIXELS = 200;

    private GoogleMap mMap;
    private ClusterManager mClusterManager;
    private Stack<LatLngBounds> mLevelBounds = new Stack<>();
    private DataService mService;
    private SQLiteDatabase mDatabase;
    private Config mConfig;
    private Hashtable<Integer, Village> mVillageMapping = new Hashtable<>();
    private ClusterItem mSelectedVillage = null;
    private List<Marker> mCurrentVillageMarkers = null;

    private LinearLayout mVillageDetailsContainer;
    private NestedScrollView mVillageDetailsScrollView;
    private BottomSheetBehavior<LinearLayout> mBottomSheetBehavior;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        prepareRecyclerView();

        prepareBottomSheet();
    }

    private void prepareRecyclerView() {
        mRecyclerView = findViewById(R.id.project_recycler);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
    }

    private void prepareBottomSheet() {
        mVillageDetailsContainer = findViewById(R.id.details_container);
        mVillageDetailsScrollView = findViewById(R.id.details_scroll_view);

        mBottomSheetBehavior = BottomSheetBehavior.from(mVillageDetailsContainer);
        mBottomSheetBehavior.setPeekHeight(0);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        mVillageDetailsContainer.getLayoutParams().height =  (int)(height * .7);
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
            if (mSelectedVillage != null) {
                mClusterManager.addItem(mSelectedVillage);
                mSelectedVillage = clusterItem;
                mClusterManager.removeItem(clusterItem);
            }

            zoomToVillage((Village)clusterItem);

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

        loadData();
    }

    @Override
    public void onBackPressed() {
        if (mCurrentVillageMarkers != null) {
            clearVillageMarkers();
        }
        if (!mLevelBounds.isEmpty()) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mLevelBounds.pop(), 0));
        } else {
            super.onBackPressed();
        }
    }

    private void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mService = retrofit.create(DataService.class);

        mDatabase = new DatabaseHelper(this).getWritableDatabase();

        Config dbConfig = getVersionsFromDatabase();
        mService.getConfig()
                .flatMap(config -> {
                    mConfig = config;
                    if (config.getVillagesVersion() > dbConfig.getVillagesVersion()) {
                        return mService.getVillages();
                    } else {
                        return Observable.fromArray(getVillagesFromDatabase());
                    }
                })
                .flatMap(villages -> {
                    if (mConfig.getVillagesVersion() > dbConfig.getVillagesVersion()) {
                        saveVillagesToDatabase(villages);
                        saveVersionsToDatabase(mConfig);
                    }
                    mClusterManager.addItems(villages);
                    for (Village village : villages) {
                        mVillageMapping.put(village.getId(), village);
                    }
                    if (mConfig.getProjectsVersion() > dbConfig.getVillagesVersion()) {
                        return mService.getProjects();
                    } else {
                        return Observable.fromArray(getProjectsFromDatabase());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(projects -> {
                    populateMarkers(projects);
                    if (mConfig.getProjectsVersion() > dbConfig.getProjectsVersion()) {
                        saveProjectsToDatabase(projects);
                        if (mConfig.getVillagesVersion() == dbConfig.getVillagesVersion()) {
                            saveVersionsToDatabase(mConfig);
                        }
                    }
                }, error -> {
                    if (dbConfig.getVillagesVersion() == 0) {
                        // TODO Error message.
                    }
                    mClusterManager.addItems(getVillagesFromDatabase());
                    populateMarkers(getProjectsFromDatabase());
                });
    }

    private void populateMarkers(List<Project> projects){
        mAdapter = new ProjectAdapter(projects, project -> {
            if (mSelectedVillage != null) {
                mClusterManager.addItem(mSelectedVillage);
                mSelectedVillage = project.getVillage();
                mClusterManager.removeItem(project.getVillage());
            }
            zoomToVillage(project.getVillage());
        });
        mRecyclerView.setAdapter(mAdapter);

        LatLng malawi = new LatLng(-13.5, 34.5);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(malawi, 6.5f), 2000, null);

        for (Project project : projects) {
            Village village = mVillageMapping.get(project.getVillageId());
            project.setVillage(village);
            List<Project> projectList = village.getProjects();
            if (projectList == null) {
                projectList = new ArrayList<>();
                village.setProjects(projectList);
            }
            projectList.add(project);
        }
    }

    private void clearVillageMarkers() {
        for (Marker marker : mCurrentVillageMarkers) {
            marker.remove();
        }
        mCurrentVillageMarkers = null;
    }

    private void zoomToVillage(Village village) {
        List<Project> projects = village.getProjects();
        if (mCurrentVillageMarkers != null) {
            clearVillageMarkers();
        } else {
            mLevelBounds.push(mMap.getProjection().getVisibleRegion().latLngBounds);
        }
        mClusterManager.removeItem(village);
        mCurrentVillageMarkers = new ArrayList<>();
        mSelectedVillage = village;

        LatLngBounds.Builder builder = LatLngBounds.builder();
        builder.include(village.getPosition());
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
    }

    private void saveProjectsToDatabase(List<Project> projects) {
        mDatabase.beginTransaction();
        mDatabase.delete(DatabaseSchema.ProjectTable.NAME, null, null);
        for (Project project : projects) {
            mDatabase.insert(DatabaseSchema.ProjectTable.NAME, null, DatabaseSchema.createContentValues(project));
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    private List<Project> getProjectsFromDatabase() {
        Cursor cursor = mDatabase.query(DatabaseSchema.ProjectTable.NAME, null, null, null, null, null, null, null);
        return new ProjectCursorWrapper(cursor).getProjects();
    }

    private void saveVillagesToDatabase(List<Village> villages) {
        mDatabase.beginTransaction();
        mDatabase.delete(DatabaseSchema.VillageTable.NAME, null, null);
        for (Village village : villages) {
            mDatabase.insert(DatabaseSchema.VillageTable.NAME, null, DatabaseSchema.createContentValues(village));
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    private List<Village> getVillagesFromDatabase() {
        Cursor cursor = mDatabase.query(DatabaseSchema.VillageTable.NAME, null, null, null, null, null, null, null);
        return new VillageCursorWrapper(cursor).getVillages();
    }

    private Config getVersionsFromDatabase() {
        Config dbConfig = new Config();
        Cursor cursor = mDatabase.query(DatabaseSchema.VersionTable.NAME, null, null, null, null, null, null);
        if (cursor.moveToNext()) {
            dbConfig.setProjectsVersion(cursor.getInt(cursor.getColumnIndex(DatabaseSchema.VersionTable.Cols.PROJECTS)));
            dbConfig.setVillagesVersion(cursor.getInt(cursor.getColumnIndex(DatabaseSchema.VersionTable.Cols.VILLAGES)));
        }
        return dbConfig;
    }

    private void saveVersionsToDatabase(Config config) {
        mDatabase.update(DatabaseSchema.VersionTable.NAME, DatabaseSchema.createContentValues(config), null, null);
    }

    private class VillageRenderer extends DefaultClusterRenderer<Village> {

        public VillageRenderer(Context context, GoogleMap map, ClusterManager<Village> manager) {
            super(context, map, manager);
            setMinClusterSize(1);
        }

        @Override
        protected void onBeforeClusterItemRendered(Village village, MarkerOptions markerOptions) {
            //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_village));

            BitmapDescriptor descriptor = AppUtils.buildLabeledIcon(MainActivity.this, R.drawable.icon_village, village.getName());
            markerOptions.icon(descriptor);
        }
    }

}
