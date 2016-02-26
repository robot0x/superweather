package com.robot0x.app.superweather.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.robot0x.app.superweather.R;
import com.robot0x.app.superweather.activity.ChooseAreaActivity;
import com.robot0x.app.superweather.receiver.AutoUpdateReceiver;
import com.robot0x.app.superweather.util.HttpCallbackListener;
import com.robot0x.app.superweather.util.HttpUtil;
import com.robot0x.app.superweather.util.Utility;

/**
 * Created by Jackie on 2016/2/25.
 */
public class AutoUpdateService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();

        Intent notificationIntent = new Intent(this, ChooseAreaActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(this)
                .setAutoCancel(false)
                .setContentTitle("今日天气")
                .setContentText("")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .build();
        startForeground(1, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
            }
        }).start();
        AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        int anHour = 8*60*60*1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }

    private void updateWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherCode = prefs.getString("weather_code", "");
        String address = "http://www.weather.com.cn/data/cityinfo/" + weatherCode + ".html";
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(AutoUpdateService.this, response);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
