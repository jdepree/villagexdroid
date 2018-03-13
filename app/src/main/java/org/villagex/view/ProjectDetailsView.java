package org.villagex.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.villagex.R;
import org.villagex.model.Project;
import org.villagex.model.TimeLineModel;
import org.villagex.model.UpdatePhoto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectDetailsView extends LinearLayout {

    private RecyclerView mTimeLineRecycler;
    private TimeLineAdapter mTimeLineAdapter;
    private List<TimeLineModel> mTimeLineEvents;

    private ProgressBar mFundingBar;
    private TextView mFundingText;
    private RecyclerView mPhotoRecycler;
    private PhotoAdapter mPhotoAdapter;
    private List<UpdatePhoto> mPhotoList;
    private TextView mProblemExtended;
    private TextView mSolutionExtended;
    private TextView mImpactExtended;
    private TextView mPartnersExtended;

    public ProjectDetailsView(Context context) {
        super(context);
        init(context);
    }

    public ProjectDetailsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProjectDetailsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);
        setPadding(0, 0, 0, 0);
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.white));

        inflate(context, R.layout.village_details_layout, this);

        mFundingBar = findViewById(R.id.project_funding_bar);
        mFundingText = findViewById(R.id.project_funding_text);

        mPhotoRecycler = findViewById(R.id.photo_recycler);
        mPhotoRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        mPhotoRecycler.setHasFixedSize(true);

        mTimeLineRecycler = findViewById(R.id.timeline_recycler);
        mTimeLineRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        mTimeLineRecycler.setHasFixedSize(true);

        TextView problemLink = findViewById(R.id.details_problem_text);
        mProblemExtended = findViewById(R.id.details_problem_extended_text);
        problemLink.setOnClickListener(view -> {
            if (mProblemExtended.getVisibility() == View.GONE) {
                mProblemExtended.setVisibility(View.VISIBLE);
                problemLink.setText(getResources().getString(R.string.project_details_subheading_problem, "-"));
            } else {
                mProblemExtended.setVisibility(View.GONE);
                problemLink.setText(getResources().getString(R.string.project_details_subheading_problem, "+"));
            }
        });
        problemLink.setText(getResources().getString(R.string.project_details_subheading_problem, "+"));

        TextView solutionLink = findViewById(R.id.details_solution_text);
        mSolutionExtended = findViewById(R.id.details_solution_extended_text);
        solutionLink.setOnClickListener(view -> {
            if (mSolutionExtended.getVisibility() == View.GONE) {
                mSolutionExtended.setVisibility(View.VISIBLE);
                solutionLink.setText(getResources().getString(R.string.project_details_subheading_solution, "-"));
            } else {
                mSolutionExtended.setVisibility(View.GONE);
                solutionLink.setText(getResources().getString(R.string.project_details_subheading_solution, "+"));
            }
        });
        solutionLink.setText(getResources().getString(R.string.project_details_subheading_solution, "+"));

        TextView impactLink = findViewById(R.id.details_impact_text);
        mImpactExtended = findViewById(R.id.details_impact_extended_text);
        impactLink.setOnClickListener(view -> {
            if (mImpactExtended.getVisibility() == View.GONE) {
                mImpactExtended.setVisibility(View.VISIBLE);
                impactLink.setText(getResources().getString(R.string.project_details_subheading_impact, "-"));
            } else {
                mImpactExtended.setVisibility(View.GONE);
                impactLink.setText(getResources().getString(R.string.project_details_subheading_impact, "+"));
            }
        });
        impactLink.setText(getResources().getString(R.string.project_details_subheading_impact, "+"));

        TextView partnersLink = findViewById(R.id.details_partners_text);
        mPartnersExtended = findViewById(R.id.details_partners_extended_text);
        partnersLink.setOnClickListener(view -> {
            if (mPartnersExtended.getVisibility() == View.GONE) {
                mPartnersExtended.setVisibility(View.VISIBLE);
                partnersLink.setText(getResources().getString(R.string.project_details_subheading_partners, "-"));
            } else {
                mPartnersExtended.setVisibility(View.GONE);
                partnersLink.setText(getResources().getString(R.string.project_details_subheading_partners, "+"));
            }
        });
        partnersLink.setText(getResources().getString(R.string.project_details_subheading_partners, "+"));
    }

    public void bindData(Project project) {
        int fundingPercent = (int)Math.ceil(100 * project.getFunded() / project.getBudget());
        mFundingBar.setProgress(fundingPercent);
        mFundingText.setText(getResources().getString(R.string.project_details_funding_text,
                fundingPercent, project.getBudget(), project.getDonorCount()));
        setPhotos(project.getPhotos());
        mPhotoAdapter = new PhotoAdapter(mPhotoList);
        mPhotoRecycler.setAdapter(mPhotoAdapter);

        setTimeLineItems(project.getEventLabels(), project.getDates());
        mTimeLineAdapter = new TimeLineAdapter(mTimeLineEvents);
        mTimeLineRecycler.setAdapter(mTimeLineAdapter);

        TextView summary = findViewById(R.id.details_summary);
        summary.setText(project.getSummary());

        mImpactExtended.setText(project.getProjectImpact());
        mPartnersExtended.setText(project.getCommunityPartners());
        mProblemExtended.setText(project.getCommunityProblem());
        mSolutionExtended.setText(project.getCommunitySolution());
    }

    private void setTimeLineItems(String[] timelineEvents, String[] timelineDates){
        mTimeLineEvents = new ArrayList<>();
        for (int i = 0; i < timelineEvents.length; i++) {
            mTimeLineEvents.add(new TimeLineModel(timelineEvents[i], timelineDates[i], TimeLineModel.TimeLineStatus.COMPLETED));
        }
    }

    private void setPhotos(UpdatePhoto[] photos) {
        mPhotoList = Arrays.asList(photos);
    }
}