package org.villagex.model.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import org.villagex.model.Project;
import org.villagex.model.database.DatabaseSchema.ProjectTable.Cols;

import java.util.ArrayList;
import java.util.List;

public class ProjectCursorWrapper extends CursorWrapper {
    public ProjectCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Project getProject() {
        Project project = new Project();
        project.setId(getInt(getColumnIndex(Cols.ID)));
        project.setName(getString(getColumnIndex(Cols.NAME)));
        project.setLat(getFloat(getColumnIndex(Cols.LAT)));
        project.setLng(getFloat(getColumnIndex(Cols.LNG)));
        project.setVillageId(getInt(getColumnIndex(Cols.VILLAGE_ID)));
        project.setType(getString(getColumnIndex(Cols.TYPE)));
        project.setBudget(getInt(getColumnIndex(Cols.BUDGET)));
        project.setFunded(getInt(getColumnIndex(Cols.FUNDED)));
        project.setPicture(getString(getColumnIndex(Cols.PICTURE)));

        return project;
    }

    public List<Project> getProjects() {
        List<Project> projects = new ArrayList<>();
        while (moveToNext()) {
            projects.add(getProject());
        }
        return projects;
    }
}
