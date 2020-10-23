package com.example.mappe2.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationBrodcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Vil sende det videre til å velge tidspunkt til å sende sms
        Intent intent1 = new Intent(context,PerioderService.class);
        context.startService(intent1);
    }
}
