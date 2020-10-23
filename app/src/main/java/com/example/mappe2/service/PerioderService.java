package com.example.mappe2.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import java.util.Calendar;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class PerioderService extends Service {
    SharedPreferences sp;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final int[] userPrefTime = splitTime(  sp.getString("tid","10:00"));

        java.util.Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, userPrefTime[0]);
        cal.set(Calendar.MINUTE, userPrefTime[1]);
        Log.d(TAG, "onStartCommand: timer er " + userPrefTime[0]);
        Log.d(TAG, "onStartCommand: minuter er  " + userPrefTime[1]);


        Intent serviceIntent = new Intent(this, NotifcationService.class);
        PendingIntent pIntent = PendingIntent.getService(this, 0, serviceIntent, 0);

        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Objects.requireNonNull(alarm).setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pIntent);

        return super.onStartCommand(intent, flag, startId);
    }

    private int[] splitTime(String time) {
        String[] userSplit = time.split(":");
        return new int[] {Integer.parseInt(userSplit[0]), Integer.parseInt(userSplit[1])};
    }


}
