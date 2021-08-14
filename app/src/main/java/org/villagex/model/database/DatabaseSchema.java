package org.villagex.model.database;

import android.content.ContentValues;

import org.villagex.model.Config;
import org.villagex.model.Project;
import org.villagex.model.Village;

public class DatabaseSchema {
    public static final class VersionTable {
        public static final String NAME = "versions";

        public static final class Cols {
            public static final String VILLAGES = "version_villages";
            public static final String PROJECTS = "version_projects";
        }
    }

    public static final class VillageTable {
        public static final String NAME = "villages";

        public static final class Cols {
            public static final String ID = "village_id";
            public static final String NAME = "village_name";
            public static final String LAT = "village_lat";
            public static final String LNG = "village_lng";
        }
    }

    public static final class ProjectTable {
        public static final String NAME = "projects";

        public static final class Cols {
            public static final String ID = "project_id";
            public static final String NAME = "project_name";
            public static final String LAT = "project_lat";
            public static final String LNG = "project_lng";
            public static final String VILLAGE_ID = "project_village_id";
            public static final String TYPE = "project_type";
            public static final String BUDGET = "project_budget";
            public static final String FUNDED = "project_funded";
            public static final String PICTURE = "project_picture_filename";
        }
    }

    public static ContentValues createContentValues(Village village) {
        ContentValues values = new ContentValues();
        values.put(VillageTable.Cols.ID, village.getId());
        values.put(VillageTable.Cols.NAME, village.getName());
        values.put(VillageTable.Cols.LAT, village.getLat());
        values.put(VillageTable.Cols.LNG, village.getLng());

        return values;
    }

    public static ContentValues createContentValues(Project project) {
        ContentValues values = new ContentValues();
        values.put(ProjectTable.Cols.ID, project.getId());
        values.put(ProjectTable.Cols.NAME, project.getName());
        values.put(ProjectTable.Cols.LAT, project.getLat());
        values.put(ProjectTable.Cols.LNG, project.getLng());
        values.put(ProjectTable.Cols.VILLAGE_ID, project.getVillageId());
        values.put(ProjectTable.Cols.TYPE, project.getType());
        values.put(ProjectTable.Cols.BUDGET, project.getBudget());
        values.put(ProjectTable.Cols.FUNDED, project.getFunded());
        values.put(ProjectTable.Cols.PICTURE, project.getPicture());
        return values;
    }

    public static ContentValues createContentValues(Config config) {
        ContentValues values = new ContentValues();
        values.put(VersionTable.Cols.VILLAGES, config.getVillagesVersion());
        values.put(VersionTable.Cols.PROJECTS, config.getProjectsVersion());
        return values;
    }
}
