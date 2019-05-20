package com.william.parksystem.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
SQLiteOpenHelper is an abstract function.
Inherit and extend SQLiteOpenHelper to create the database and corresponding tables.
 */
public class DBHelper {

    Context context;
    private static final String TABLE_USER = "user";
    private static final String TABLE_PARK = "park";

    /*
    Create a helper object to create, open, and/or manage a database.
     */
    public DBHelper(Context context) {
        this.context = context;
    }

    public static class SystemOpenHelper extends SQLiteOpenHelper {

        private static String dbName = "PackSystem.db";
        private static final int currentVersion = 1;

        /*
        Provides methods for creating a database
         */
        public SystemOpenHelper(Context context) {
            super(context, dbName, null, currentVersion);
        }

        /*
        Called when the database is created for the first time.
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            // A table of user
            String USER_SQL = "CREATE TABLE " + TABLE_USER + "(" +
                    "id INTEGER primary key AUTOINCREMENT, " +
                    "username varchar(20) not null, " +
                    "password varchar(20) not null," +
                    "phone varchar(11) not null," +
                    "name varchar(20) null,"+
                    "sex char(2) null," +
                    "age varchar(3) null," +
                    "email varchar(50) null," +
                    "photo TEXT null," +
                    "licenseNumber TEXT null" +
                    ");";
            String PARK_SQL = "CREATE TABLE " + TABLE_PARK + "(" +
                    "id INTEGER primary key AUTOINCREMENT, " +
                    "place INTEGER null, " +
                    "licenseNumber TEXT null," +
                    "startTime TEXT null," +
                    "price double null" +
                    ");";
            // Insert table
            db.execSQL(USER_SQL);
            Log.i("sql", "create table user:" + USER_SQL);
            db.execSQL(PARK_SQL);
            Log.i("sql", "create table pack:" + PARK_SQL);
        }

        /*
        Called when the database needs to be upgraded.
        The implementation should use this method to drop tables, add tables, or do anything else it needs to upgrade to the new schema version.
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

        /*
        Write the super administrator's data to the database as soon as the database is created
         */
        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            Log.i("login","open");
        }
    }
}