package org.villagex.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.villagex.model.database.DatabaseSchema.VersionTable;
import org.villagex.model.database.DatabaseSchema.ProjectTable;
import org.villagex.model.database.DatabaseSchema.VillageTable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "villagex.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ProjectTable.NAME + " ("
                + ProjectTable.Cols.ID + " integer primary key,"
                + ProjectTable.Cols.NAME + ", "
                + ProjectTable.Cols.LAT + ", "
                + ProjectTable.Cols.LNG + ", "
                + ProjectTable.Cols.TYPE + ", "
                + ProjectTable.Cols.VILLAGE_ID + ", "
                + ProjectTable.Cols.PICTURE + ", "
                + ProjectTable.Cols.BUDGET + ", "
                + ProjectTable.Cols.FUNDED + ")"
        );

        db.execSQL("create table " + VillageTable.NAME + " ("
                + VillageTable.Cols.ID + " integer primary key,"
                + VillageTable.Cols.NAME + ", "
                + VillageTable.Cols.LAT + ", "
                + VillageTable.Cols.LNG + ")"
        );

        db.execSQL("create table " + VersionTable.NAME + " ("
                + VersionTable.Cols.PROJECTS + ", "
                + VersionTable.Cols.VILLAGES + ")"
        );
        ContentValues versions = new ContentValues();
        versions.put(VersionTable.Cols.PROJECTS, 0);
        versions.put(VersionTable.Cols.VILLAGES, 0);
        db.insert(VersionTable.NAME, null, versions);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
