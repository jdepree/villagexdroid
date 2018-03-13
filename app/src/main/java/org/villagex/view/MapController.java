package org.villagex.view;

import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import org.villagex.R;
import org.villagex.model.Project;
import org.villagex.model.Village;
import org.villagex.util.AppUtils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

public class MapController implements ProjectAdapter.ItemClickListener {
    private static final int MAP_PADDING_PIXELS = 200;

    private Context mContext;
    private GoogleMap mMap;
    private ClusterManager mClusterManager;

    private Hashtable<Integer, Village> mVillageMapping = new Hashtable<>();
    private Stack<LatLngBounds> mLevelBounds = new Stack<>();
    private ClusterItem mSelectedVillage = null;
    private List<Marker> mCurrentVillageMarkers = null;

    private ProjectSelectedListener mProjectSelectedListener;

    public MapController(Context context, GoogleMap map, GoogleMap.OnMapClickListener mapClickListener, ProjectSelectedListener projectSelectedListener) {
        mContext = context;
        mMap = map;
        mProjectSelectedListener = projectSelectedListener;
        init(mapClickListener);
    }

    private void init(GoogleMap.OnMapClickListener mapClickListener) {
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        LatLng africa = new LatLng(10f,10f);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(africa, 2f));

        mClusterManager = new ClusterManager<Village>(mContext, mMap);
        mClusterManager.setRenderer(new VillageRenderer(mMap, mClusterManager));

        mClusterManager.setOnClusterItemClickListener(clusterItem -> {
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
        mMap.setOnMarkerClickListener(marker -> {
            Project project = (Project)marker.getTag();
            if (project != null) {
                mProjectSelectedListener.onProjectSelected(project);
            } else {
                mClusterManager.onMarkerClick(marker);
            }
            return true;
        });

        mMap.setOnMapClickListener(mapClickListener);
    }

    @Override
    public void itemClicked(Project project) {
        mProjectSelectedListener.onProjectSelected(project);
        zoomToVillage(project.getVillage());
    }

    public boolean zoomToLast() {
        if (mCurrentVillageMarkers != null) {
            clearVillageMarkers();
        }
        if (!mLevelBounds.isEmpty()) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mLevelBounds.pop(), 0));
            return true;
        }
        return false;
    }

    private void clearVillageMarkers() {
        for (Marker marker : mCurrentVillageMarkers) {
            marker.remove();
        }
        mCurrentVillageMarkers = null;
    }

    public void addVillages(List<Village> villages) {
        mClusterManager.addItems(villages);
        for (Village village : villages) {
            mVillageMapping.put(village.getId(), village);
        }
    }

    public void addProjects(List<Project> projects){

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

    public void zoomToVillage(Village village) {
        List<Project> projects = village.getProjects();
        if (mCurrentVillageMarkers != null) {
            clearVillageMarkers();
        } else {
            mLevelBounds.push(mMap.getProjection().getVisibleRegion().latLngBounds);
        }
        mCurrentVillageMarkers = new ArrayList<>();
        mSelectedVillage = village;

        LatLngBounds.Builder builder = LatLngBounds.builder();
        builder.include(village.getPosition());
        for (Project project : projects) {
            Marker nextMarker = mMap.addMarker(new MarkerOptions()
                    .position(project.getPosition())
                    .icon(BitmapDescriptorFactory.fromResource(mContext.getResources().getIdentifier(
                            "type_" + project.getType(), "drawable", mContext.getPackageName()))
                    )
            );
            nextMarker.setTag(project);
            builder.include(nextMarker.getPosition());
            mCurrentVillageMarkers.add(nextMarker);
        }
        final LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, MAP_PADDING_PIXELS));
    }

    public interface ProjectSelectedListener {
        void onProjectSelected(Project project);
    }

    private class VillageRenderer extends DefaultClusterRenderer<Village> {

        public VillageRenderer(GoogleMap map, ClusterManager<Village> manager) {
            super(mContext, map, manager);
            setMinClusterSize(1);
        }

        @Override
        protected void onBeforeClusterItemRendered(Village village, MarkerOptions markerOptions) {
            BitmapDescriptor descriptor = AppUtils.buildLabeledIcon(mContext, R.drawable.icon_village, village.getName());
            markerOptions.icon(descriptor);
        }
    }
}
