package org.villagex.model.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import org.villagex.model.Village;
import org.villagex.model.database.DatabaseSchema.VillageTable.Cols;

import java.util.ArrayList;
import java.util.List;

public class VillageCursorWrapper extends CursorWrapper {

    public VillageCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Village getVillage() {
        Village village = new Village();
        village.setId(getInt(getColumnIndex(Cols.ID)));
        village.setName(getString(getColumnIndex(Cols.NAME)));
        village.setLat(getFloat(getColumnIndex(Cols.LAT)));
        village.setLng(getFloat(getColumnIndex(Cols.LNG)));

        return village;
    }

    public List<Village> getVillages() {
        List<Village> villages = new ArrayList<>();
        while (moveToNext()) {
            villages.add(getVillage());
        }
        return villages;
    }
}
