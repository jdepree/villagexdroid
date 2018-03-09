package org.villagex.activity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
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
import org.villagex.model.database.DataLayer;
import org.villagex.model.database.DatabaseHelper;
import org.villagex.model.database.DatabaseSchema;
import org.villagex.model.database.ProjectCursorWrapper;
import org.villagex.model.database.VillageCursorWrapper;
import org.villagex.network.NetworkService;
import org.villagex.util.AppUtils;
import org.villagex.view.MapController;
import org.villagex.view.ProjectAdapter;
import org.villagex.view.ProjectRecyclerView;

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

    private MapController mMapController;
    private DataLayer mDataLayer;
    private NetworkService mService;

    private LinearLayout mVillageDetailsContainer;
    private NestedScrollView mVillageDetailsScrollView;
    private BottomSheetBehavior<LinearLayout> mBottomSheetBehavior;
    private ProjectRecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mRecyclerView = findViewById(R.id.project_recycler);
        prepareBottomSheet();
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
        mMapController = new MapController(this, googleMap);

        loadData();
    }

    @Override
    public void onBackPressed() {
        if (!mMapController.zoomToLast()) {
            super.onBackPressed();
        }
    }

    private void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mService = retrofit.create(NetworkService.class);

        mDataLayer = new DataLayer(this);

        Config dbConfig = mDataLayer.getVersions();

        mService.getConfig()
                .subscribeOn(Schedulers.io())
                .subscribe(config -> {
                    retrieveVillages(config, dbConfig)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(villages -> {
                        mMapController.addVillages(villages);
                        if (config.getVillagesVersion() > dbConfig.getVillagesVersion()) {
                            mDataLayer.saveVillages(villages);
                        }
                    });

                    retrieveProjects(config, dbConfig).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(projects -> {
                        mMapController.addProjects(projects);
                        mRecyclerView.addProjects(projects, mMapController);
                        if (config.getProjectsVersion() > dbConfig.getProjectsVersion()) {
                            mDataLayer.saveProjects(projects);
                        }
                    });

                    if (config.getProjectsVersion() > dbConfig.getProjectsVersion()
                            || config.getVillagesVersion() > dbConfig.getVillagesVersion()) {
                        mDataLayer.saveVersions(config);
                    }
                }, error -> {
                    Log.e("Exception loading", error.getMessage());
                    if (dbConfig.getVillagesVersion() == 0) {
                        // TODO Error message.
                    }
                    mMapController.addVillages(mDataLayer.getVillages());

                    List<Project> projects = mDataLayer.getProjects();
                    mMapController.addProjects(projects);
                    mRecyclerView.addProjects(projects, mMapController);
                });
    }

    private Observable<List<Village>> retrieveVillages(Config config, Config dbConfig) {
        if (config.getVillagesVersion() > dbConfig.getVillagesVersion()) {
            return mService.getVillages();
        } else {
            return Observable.fromArray(mDataLayer.getVillages());
        }
    }

    private Observable<List<Project>> retrieveProjects(Config config, Config dbConfig) {
        if (config.getProjectsVersion() > dbConfig.getVillagesVersion()) {
            return mService.getProjects();
        } else {
            return Observable.fromArray(mDataLayer.getProjects());
        }
    }
}
