package com.robot0x.app.superweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jackie on 2016/2/25.
 */
public class SuperWeatherOpenHelper extends SQLiteOpenHelper {

    private final String CREATE_PROVINCE = "create table Province ("
                        + "id integer primary key autoincrement, "
                        + "province_name text, "
                        + "province_code text)";

    private final String CREATE_CITY = "create table City ("
                        + "id integer primary key autoincrement, "
                        + "city_name text, "
                        + "city_code text, "
                        + "province_id integer)";

    private final String CREATE_COUNTY = "create table County ("
                        + "id integer primary key autoincrement, "
                        + "county_name text, "
                        + "county_code text, "
                        + "city_id integer)";

    public SuperWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
