package org.villagex.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import org.villagex.model.Project;

import java.util.List;

public class ProjectRecyclerView extends RecyclerView {
    private RecyclerView.Adapter mAdapter;

    public ProjectRecyclerView(Context context) {
        this(context, null);
    }

    public ProjectRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProjectRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setHasFixedSize(true);

        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(manager);
    }

    public void addProjects(List<Project> projects, ProjectAdapter.ItemClickListener listener) {
        mAdapter = new ProjectAdapter(projects, listener);
        setAdapter(mAdapter);
    }

}
