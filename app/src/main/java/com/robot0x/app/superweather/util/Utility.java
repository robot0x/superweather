package com.robot0x.app.superweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.ArrayAdapter;

import com.robot0x.app.superweather.db.SuperWeatherDB;
import com.robot0x.app.superweather.model.City;
import com.robot0x.app.superweather.model.County;
import com.robot0x.app.superweather.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Jackie on 2016/2/25.
 */
public class Utility {
    public synchronized static boolean handleProvincesResponse(SuperWeatherDB superWeatherDB, String response) {
        if (TextUtils.isEmpty(response)) {
            return false;
        }

        String[] allProvinces = response.split(",");
        if (allProvinces.length <= 0) {
            return false;
        }

        for (String p : allProvinces) {
            String[] array = p.split("\\|");
            Province province = new Province();
            province.setProvinceCode(array[0]);
            province.setProvinceName(array[1]);
            superWeatherDB.saveProvince(province);
        }
        return true;
    }

    public static boolean handleCitiesResponse(SuperWeatherDB superWeatherDB, String response, int provinceId) {
        if (TextUtils.isEmpty(response)) {
            return false;
        }

        String[] allCities = response.split(",");
        if (allCities.length <= 0) {
            return false;
        }

        for (String c : allCities) {
            String[] array = c.split("\\|");
            City city = new City();
            city.setCityCode(array[0]);
            city.setCityName(array[1]);
            city.setProvinceId(provinceId);
            superWeatherDB.saveCity(city);
        }
        return true;
    }

    public static boolean handleCountiesResponse(SuperWeatherDB superWeatherDB, String response, int cityId) {
        if (TextUtils.isEmpty(response)) {
            return false;
        }

        String[] allCounties = response.split(",");
        if (allCounties.length <= 0) {
            return false;
        }

        for (String c : allCounties) {
            String[] array = c.split("\\|");
            County county = new County();
            county.setCountyCode(array[0]);
            county.setCountyName(array[1]);
            county.setCityId(cityId);
            superWeatherDB.saveCounty(county);
        }
        return true;
    }

    public static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");
            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1, String temp2,
                                        String weatherDesp, String publishTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date", sdf.format(new Date()));
        editor.commit();
    }
}
