package org.villagex.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.villagex.R;
import org.villagex.model.TimeLineModel;
import org.villagex.model.UpdatePhoto;

import java.util.ArrayList;
import java.util.List;

public class VillageDetailsView extends LinearLayout {

    private RecyclerView mTimeLineRecycler;
    private TimeLineAdapter mTimeLineAdapter;
    private List<TimeLineModel> mTimeLineEvents;

    private RecyclerView mPhotoRecycler;
    private PhotoAdapter mPhotoAdapter;
    private List<UpdatePhoto> mPhotoList;

    private TextView mProblemLink;
    private TextView mProblemExtended;

    public VillageDetailsView(Context context) {
        super(context);
        init(context);
    }

    public VillageDetailsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VillageDetailsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        setPadding(0, 0, 0, 0);
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));

        inflate(context, R.layout.village_details_layout, this);

        mPhotoRecycler = findViewById(R.id.photo_recycler);
        mPhotoRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        mPhotoRecycler.setHasFixedSize(true);

        setPhotos();
        mPhotoAdapter = new PhotoAdapter(mPhotoList);
        mPhotoRecycler.setAdapter(mPhotoAdapter);

        mTimeLineRecycler = findViewById(R.id.timeline_recycler);
        mTimeLineRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        mTimeLineRecycler.setHasFixedSize(true);

        setTimeLineItems();
        mTimeLineAdapter = new TimeLineAdapter(mTimeLineEvents);
        mTimeLineRecycler.setAdapter(mTimeLineAdapter);

        mProblemLink = findViewById(R.id.details_problem_text);
        mProblemExtended = findViewById(R.id.details_problem_extended_text);

        findViewById(R.id.details_problem_text).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mProblemExtended.getVisibility() == View.GONE) {
                    mProblemExtended.setVisibility(View.VISIBLE);
                    mProblemLink.setText("- Problem");
                } else {
                    mProblemExtended.setVisibility(View.GONE);
                    mProblemLink.setText("+ Problem");
                }
            }
        });
    }

    private void setTimeLineItems(){
        mTimeLineEvents = new ArrayList<>();
        mTimeLineEvents.add(new TimeLineModel("First proposed", "2016-05-17", TimeLineModel.TimeLineStatus.COMPLETED));
        mTimeLineEvents.add(new TimeLineModel("Proposal approved", "2016-05-21", TimeLineModel.TimeLineStatus.COMPLETED));
        mTimeLineEvents.add(new TimeLineModel("Funding phase started", "2016-05-25", TimeLineModel.TimeLineStatus.COMPLETED));
        mTimeLineEvents.add(new TimeLineModel("Funding complete", "2016-06-05", TimeLineModel.TimeLineStatus.COMPLETED));
        mTimeLineEvents.add(new TimeLineModel("Construction", "2016-07-29", TimeLineModel.TimeLineStatus.IN_PROGRESS));
        mTimeLineEvents.add(new TimeLineModel("Well complete", "2016-09-01", TimeLineModel.TimeLineStatus.UNSTARTED));
    }

    private void setPhotos() {
        mPhotoList = new ArrayList<>();
        mPhotoList.add(new UpdatePhoto("This is the portrait of a community-led project. Good job, Mwanga!",
                "http://villagexapp.com/uploads/media/default/0001/02/thumb_1233_default_buy_list.jpeg",
                "http://villagexapp.com/uploads/media/default/0001/02/thumb_1233_default_see_800x600.jpeg", "June 1st"));

    }
}