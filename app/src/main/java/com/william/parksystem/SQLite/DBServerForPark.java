package com.william.parksystem.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class DBServerForPark {

    private static final String DB_TABLE = "park";
    private static final String KEY_PLACE = "place";
    private static final String KEY_LICENSE_NUMBER = "licenseNumber";
    private static final String KEY_TIME = "startTime";
    private static final String KEY_PRICE = "price";

    SQLiteDatabase db;
    Context context;

    public DBServerForPark(Context context) {
        this.context = context;
    }

    /*
    Open database
     */
    public void open() {
        DBHelper.SystemOpenHelper dbHelper = new DBHelper.SystemOpenHelper(context);
        /*
        Try to open the database read-write.
        Open the database read-only if errors occurred such as bad permissions or a full disk.
        */
        try {
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            db = dbHelper.getReadableDatabase();
        }
    }

    /*
    Close database
    */
    public void close() {
        if (db != null) {
            db.close();
            db = null;
        }
    }

    /*
    Register and add data to the database
    */
    public boolean insert(String licenseNumber) {
        boolean result = false;
        // Instantiate content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_LICENSE_NUMBER, licenseNumber);
        if (db.insert(DB_TABLE, null, contentValues) > 0) {
            result = true;
        }
        return result;
    }

    /*
    Query parking spaces data
    */
    public boolean selectPlace(int place) {
        boolean result = false;
        Cursor cursor = null;
        // Get a cursor object
        cursor = (db.query(DB_TABLE, new String[]{KEY_PLACE},
                KEY_PLACE + "='" + place + "'",
                null, null, null, null));
        if (cursor.getCount() == 0) {
            result = true;
        }
        return result;
    }

    /*
    Query parking spaces data by license number
    */
    public int selectPlaceByLN(String licenseNumber) {
        int place = -1;
        Cursor cursor = null;
        // Get a cursor object
        cursor = (db.query(DB_TABLE, new String[]{KEY_PLACE},
                KEY_LICENSE_NUMBER + "='" + licenseNumber + "'",
                null, null, null, null));
        while (cursor.moveToNext()) {
            String getPlace = cursor.getString(cursor.getColumnIndex("place"));
            place = Integer.parseInt(getPlace);
            Log.i("place", "time:" + place);
        }
        return place;
    }

    /*
    Query Time
    */
    public String selectTime(int place) {
        String time = null;
        Cursor cursor = null;
        // Get a cursor object
        cursor = db.query(DB_TABLE, new String[]{KEY_TIME},
                KEY_PLACE + "='" + place + "'",
                null, null, null, null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                time = cursor.getString(cursor.getColumnIndex("startTime"));
            }
        }
        return time;
    }

    public boolean updataPackPlace(int place, String licenseNumber, String time) {
        boolean result = false;
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PLACE, place);
        contentValues.put(KEY_LICENSE_NUMBER, licenseNumber);
        contentValues.put(KEY_TIME, time);
        Log.i("upplace", place + licenseNumber + time);
        int n = db.update(DB_TABLE, contentValues, KEY_LICENSE_NUMBER + "='" + licenseNumber + "'", null);
        if (n == 1) {
            result = true;
            Log.i("sql", "Update database succeeded");
        } else {
            Log.i("sql", "Update database failed");
        }
        return result;
    }

    public boolean pay(String licenseNumber, double price) {
        boolean result = false;
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_PLACE, 0);
        contentValues.put(KEY_PRICE, price);

        if (db !=null){
            System.out.println("test");
        }
        int n = db.update(DB_TABLE, contentValues, KEY_LICENSE_NUMBER + "='" + licenseNumber + "'", null);
        if (n != 1) {
            result = true;
            Log.i("sql", "Update database succeeded");
        } else {
            Log.i("sql", "Update database failed");
        }
        return result;
    }

}
