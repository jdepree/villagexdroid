package org.villagex.view;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
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
    private ItemClickListener mListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImageView;
        private ProgressBar mProgressBar;
        private TextView mProgressTextView;
        private TextView mProjectNameTextView;
        public ViewHolder(LinearLayout layout) {
            super(layout);

            mImageView = layout.findViewById(R.id.project_card_image);
            mProgressBar = layout.findViewById(R.id.project_card_progress);
            mProgressTextView = layout.findViewById(R.id.project_card_progress_text);
            mProjectNameTextView = layout.findViewById(R.id.project_card_text);
        }
    }

    public ProjectAdapter(List<Project> projects, ItemClickListener listener) {
        mProjects = projects;
        mListener = listener;
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
        Picasso.get()
                .load(context.getString(R.string.base_url) + context.getString(R.string.pictures_dir) + project.getPicture())
                .resizeDimen(R.dimen.project_recycler_image_width, R.dimen.project_recycler_image_width)
                .centerCrop()
                .transform(new RoundedCornersTransformation(15, 1, RoundedCornersTransformation.CornerType.TOP))
                .into(holder.mImageView);
        int progress = Math.round(100 * project.getFunded() / project.getBudget());
        holder.mProgressBar.setProgress(progress);
        holder.mProjectNameTextView.setText(project.getVillage().getName() + "\n" + project.getName());
        holder.mProgressTextView.setText(progress == 100 ? context.getString(R.string.fully_funded)
                : context.getString(R.string.partially_funded, progress, Math.round(project.getBudget())));
        holder.itemView.setOnClickListener(v -> mListener.itemClicked(project));
    }

    public int getItemCount() {
        return mProjects.size();
    }

    public interface ItemClickListener {
        void itemClicked(Project project);
    }
}
