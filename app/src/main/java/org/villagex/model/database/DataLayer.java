package org.villagex.model.database;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.villagex.model.Config;
import org.villagex.model.Project;
import org.villagex.model.Village;

import java.util.List;

public class DataLayer {
    private SQLiteDatabase mDatabase;

    public DataLayer(Context context) {
        mDatabase = new DatabaseHelper(context).getWritableDatabase();
    }

    public void saveProjects(List<Project> projects) {
        mDatabase.beginTransaction();
        mDatabase.delete(DatabaseSchema.ProjectTable.NAME, null, null);
        for (Project project : projects) {
            mDatabase.insert(DatabaseSchema.ProjectTable.NAME, null, DatabaseSchema.createContentValues(project));
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    public List<Project> getProjects() {
        Cursor cursor = mDatabase.query(DatabaseSchema.ProjectTable.NAME, null, null, null, null, null, null, null);
        return new ProjectCursorWrapper(cursor).getProjects();
    }

    public void saveVillages(List<Village> villages) {
        mDatabase.beginTransaction();
        mDatabase.delete(DatabaseSchema.VillageTable.NAME, null, null);
        for (Village village : villages) {
            mDatabase.insert(DatabaseSchema.VillageTable.NAME, null, DatabaseSchema.createContentValues(village));
        }
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }


    public List<Village> getVillages() {
        Cursor cursor = mDatabase.query(DatabaseSchema.VillageTable.NAME, null, null, null, null, null, null, null);
        return new VillageCursorWrapper(cursor).getVillages();
    }

    public Config getVersions() {
        Config dbConfig = new Config();
        Cursor cursor = mDatabase.query(DatabaseSchema.VersionTable.NAME, null, null, null, null, null, null);
        if (cursor.moveToNext()) {
            dbConfig.setProjectsVersion(cursor.getInt(cursor.getColumnIndex(DatabaseSchema.VersionTable.Cols.PROJECTS)));
            dbConfig.setVillagesVersion(cursor.getInt(cursor.getColumnIndex(DatabaseSchema.VersionTable.Cols.VILLAGES)));
        }
        return dbConfig;
    }

    public void saveVersions(Config config) {
        mDatabase.update(DatabaseSchema.VersionTable.NAME, DatabaseSchema.createContentValues(config), null, null);
    }

}
