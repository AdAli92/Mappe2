package com.example.mappe2.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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
    private static int MY_PERMISSIONS_REQUEST_SEND_SMS;
    private static int MY_PHONE_STATE_PERMISSION;
    private SharedPreferences sp;
    private DatabaseHandler db;

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
        String dagensDato = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        List<Mote> moter = db.HenteAlleMoter();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        for (Mote mote : moter) {
            if (mote.getDato().equals(dagensDato)) {
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
        db.closeDB();
        return super.onStartCommand(intent, flags, startId);
    }

    private void buildNotification(PendingIntent pIntent, NotificationManager notificationManager) {
        String contentText = "Husk du har et møte idag ";
        Notification notification = new NotificationCompat.Builder(this,"channel_id")
                .setContentTitle("Møte idag!")
                .setContentText(contentText)
                .setSmallIcon(R.mipmap.moteapp1)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.mipmap.moteapp1))
                .setContentIntent(pIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        Objects.requireNonNull(notificationManager).notify(0, notification);
    }

    @SuppressLint("LongLogTag")
    private void sendSms(String phonenumber) {
        SharedPreferences.Editor endre;
        endre = sp.edit();
        //nullstille tiden etter notification
        endre.putString("tid", "23:59");
        endre.apply();
        String message = sp.getString("melding", "Husk! vi har et møte. Takk");

        MY_PERMISSIONS_REQUEST_SEND_SMS =
                ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        MY_PHONE_STATE_PERMISSION = ActivityCompat.checkSelfPermission
                (this, Manifest.permission.READ_PHONE_STATE);
        if (MY_PERMISSIONS_REQUEST_SEND_SMS == PackageManager.PERMISSION_GRANTED &&
                MY_PHONE_STATE_PERMISSION == PackageManager.PERMISSION_GRANTED) {
            SmsManager smsMan = SmsManager.getDefault();
            smsMan.sendTextMessage(phonenumber,
                    null, message, null, null);
            Toast.makeText(this, "Har sendt sms", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Har ikke sendt sms", Toast.LENGTH_SHORT).show();
        }
    }
}



