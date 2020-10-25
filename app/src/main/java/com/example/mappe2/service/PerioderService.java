package com.example.mappe2.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import java.util.Calendar;
import java.util.Objects;

public class PerioderService extends Service {
    private SharedPreferences sp;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int startId) {
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final int[] splittTidListe = splitTid(sp.getString("tid", "10:00"));

        java.util.Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, splittTidListe[0]);
        cal.set(Calendar.MINUTE, splittTidListe[1]);

        Intent serviceIntent = new Intent(this, NotifcationService.class);
        PendingIntent pIntent =
                PendingIntent.getService(this, 0, serviceIntent, 0);
        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Objects.requireNonNull(alarm).
                setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pIntent);

        return super.onStartCommand(intent, flag, startId);
    }

    private int[] splitTid(String tid) {
        String[] splittTidListe = tid.split(":");
        return new int[]{Integer.parseInt(splittTidListe[0]), Integer.parseInt(splittTidListe[1])};
    }

}
