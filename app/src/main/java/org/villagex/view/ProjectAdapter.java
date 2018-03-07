package org.villagex.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.villagex.R;
import org.villagex.model.Project;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {
    private List<Project> mProjects;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private ProgressBar mProgressBar;
        private TextView mTextView;
        public ViewHolder(LinearLayout layout) {
            super(layout);

            mImageView = layout.findViewById(R.id.project_card_image);
            mProgressBar = layout.findViewById(R.id.project_card_progress);
            mTextView = layout.findViewById(R.id.project_card_text);
        }
    }

    public ProjectAdapter(List<Project> projects) {
        mProjects = projects;
    }

    public ProjectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        LinearLayout layout = (LinearLayout)LayoutInflater.from(parent.getContext()).inflate(R.layout.project_item, parent, false);


        ProjectAdapter.ViewHolder vh = new ProjectAdapter.ViewHolder(layout);
        return vh;
    }

    public void onBindViewHolder(ProjectAdapter.ViewHolder holder, int position) {
        Context context = holder.mImageView.getContext();
        Project project = mProjects.get(position);
        Picasso.with(context)
                .load(context.getString(R.string.base_url) + context.getString(R.string.pictures_dir) + project.getPicture())
                .resize(150, 125)
                .transform(new RoundedCornersTransformation(15, 1, RoundedCornersTransformation.CornerType.TOP))
                .centerCrop()
                .into(holder.mImageView);
        holder.mProgressBar.setProgress(Math.round(100 * project.getFunded() / project.getBudget()));
        holder.mTextView.setText(project.getName());

    }

    public int getItemCount() {
        return mProjects.size();
    }
}
