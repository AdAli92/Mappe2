package com.example.mappe2.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.example.mappe2.Activity.MainActivity;
import com.example.mappe2.Controller.DatabaseHandler;
import com.example.mappe2.Modul.Mote;
import com.example.mappe2.Modul.Person;
import com.example.mappe2.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class NotifcationService extends Service {
    SharedPreferences sp;
    DatabaseHandler db;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        db = new DatabaseHandler(getApplicationContext());
        boolean sjekk = sp.getBoolean("aktivert", false);
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        Log.d("DAto som vi tester",currentDate);
        List<Mote> moter = db.HenteAlleMoter();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Log.d("Vi er før for løkke",currentDate);

      for (Mote mote : moter) {
            if (mote.getDato().equals(currentDate)) {
                buildNotification(pIntent, notificationManager);
                if (sjekk) {
                    List<Person> personList = db.HenteAllePersonerIMote(mote.getMoteId());
                    if (personList != null) {
                        for (Person person : personList) {
                          sendSms(person.getTelefonnr());
                        }
                    }
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    private void buildNotification(PendingIntent pIntent, NotificationManager notificationManager) {
        String contentText = "Husk du har et møte idag ";
        Notification notification = new NotificationCompat.Builder(this, "channel_id")
                .setContentTitle("Møte idag!")
                .setContentText(contentText)
                .setSmallIcon(R.drawable.add)
                .setContentIntent(pIntent)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        Objects.requireNonNull(   notificationManager).notify(0, notification);
    }


    private void sendSms(String phonenumber) {
        String message = sp.getString("melding", "Husk! vi har et møte. Takk");
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phonenumber, null, message, null, null);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Ikke fungerer ", Toast.LENGTH_SHORT).show();
        }
    }


}