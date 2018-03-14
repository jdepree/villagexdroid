package org.villagex.activity;

import android.graphics.Point;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.villagex.R;
import org.villagex.model.Config;
import org.villagex.model.Project;
import org.villagex.model.Village;
import org.villagex.model.database.DataLayer;
import org.villagex.network.NetworkService;
import org.villagex.view.MapController;
import org.villagex.view.PaymentView;
import org.villagex.view.ProjectDetailsView;
import org.villagex.view.ProjectRecyclerView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, MapController.ProjectSelectedListener {

    private MapController mMapController;
    private DataLayer mDataLayer;
    private NetworkService mService;

    private LinearLayout mVillageDetailsContainer;
    private ProjectDetailsView mDetailsView;
    private BottomSheetBehavior<LinearLayout> mBottomSheetBehavior;
    private ProjectRecyclerView mRecyclerView;
    private Button mSubmitButton;

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
        mDetailsView = findViewById(R.id.project_details_view);

        mBottomSheetBehavior = BottomSheetBehavior.from(mVillageDetailsContainer);
        mBottomSheetBehavior.setPeekHeight(0);

        mSubmitButton = findViewById(R.id.submit_button);
        mSubmitButton.setOnClickListener(v -> {
            new MaterialDialog.Builder(MainActivity.this)
                    .customView(new PaymentView(MainActivity.this), false)
                    .canceledOnTouchOutside(false)
                    .show();
        });

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        mVillageDetailsContainer.getLayoutParams().height =  (int)(height * .7);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapController = new MapController(this, googleMap, latLng -> {
            if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }, this);

        loadData();
    }

    @Override
    public void onBackPressed() {
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (!mMapController.zoomToLast()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onProjectSelected(Project project) {
        mDetailsView.bindData(project);
        mSubmitButton.setText(getResources().getString(project.getFunded() < project.getBudget() ? R.string.donate_now : R.string.fully_funded));
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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
                        if (!isDestroyed()) {
                            new AlertDialog.Builder(MainActivity.this).setMessage(R.string.network_error_message)
                                    .setOnDismissListener(dialog -> finish()).create().show();
                        }
                        return;
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
