package com.example.mappe2.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mappe2.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class SettingFragment extends Fragment {

    private SwitchMaterial slo_sms;
    private TextInputEditText noti, melding;
    View v;
    SharedPreferences.Editor endre;
    String smsInnhold;
    boolean aktivert;
    private Calendar calendar;
    SharedPreferences sp;
    String tid;
    private   static int send;


    public SettingFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_setting, container, false);
        sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        endre = sp.edit();
      send = (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS));




        slo_sms = v.findViewById(R.id.slo_SMS);
        noti = v.findViewById(R.id.not_tid);
        melding = v.findViewById(R.id.melding);

        noti.setShowSoftInputOnFocus(false);
        calendar = Calendar.getInstance();
        smsInnhold = sp.getString("melding", "Husk! vi har et møte. Takk");
        Log.d(TAG, "onCreate: " + smsInnhold);

       // aktivert = smsPermission();

        aktivert=sp.getBoolean("aktivert",false );



        tid = sp.getString("tid", "23:59");

        melding.setText(smsInnhold);


        slo_sms.setChecked(aktivert);
        noti.setText(tid);


        if(aktivert==false){
            send= -1;
        }
        final int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        final int minut = calendar.get(java.util.Calendar.MINUTE);


        noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int min) {
                        tid = hour + ":" + min;
                        noti.setText(tid);
                        endre.putString("tid", tid);
                        endre.apply();
                    }
                }, hour, minut, DateFormat.is24HourFormat(getContext()));
                timePickerDialog.show();

                Log.d(TAG, "inni noti onclicklistner: tid " + tid);

            }

        });


        slo_sms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    Log.d("inside onCheckedChanged","inside onCheckedChanged når den er true");
              //      ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                //   if (!(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.SEND_SMS))) {
                        if (send == -1) {
                            requestSmsPermission();

                            return;
                        }



                }
                else if (b==false){
                    send = -1;
                }

            }
        });


        return v;
    }

    private void restartReminderService() {
        Intent intent = new Intent();
        intent.setAction("com.example.mappe2.service.NotificationBrodcastReceiver");//try to endre
        getActivity().sendBroadcast(intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (melding != null) {
            smsInnhold = melding.getText().toString();
        }
        Log.d(TAG, "onDestroy: " + smsInnhold);
        endre.putString("melding", smsInnhold);
        endre.apply();
        endre.putBoolean("aktivert", aktivert);
        endre.apply();
        Log.d(TAG, "onDestroy: " + sp.getString("tid", "23:59"));
        restartReminderService();

    }

    private void requestSmsPermission() {
        new AlertDialog.Builder(getActivity())
                .setTitle("SMS Permission Needed")
                .setMessage("We need your permission to send SMS in order to turn this on")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      ActivityCompat.requestPermissions(SettingFragment.this.getActivity(), new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE}, 1);
                        aktivert=true;
                    }
                })

                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(SettingFragment.this.getActivity(), new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE}, 0);
                        aktivert=false;
                        send=-1;
                        dialog.dismiss();

                    }
                })
                .create()
                .show();
    }

    @Override
    public void onPause() {
        super.onPause();
        smsInnhold = melding.getText().toString();
        aktivert = slo_sms.isChecked();
        Log.d(TAG, "onDestroy: " + aktivert);
        Log.d(TAG, "onDestroy: " + smsInnhold);
        endre.putString("melding", smsInnhold);
        endre.apply();
        endre.putBoolean("aktivert", aktivert);
        endre.apply();

        restartReminderService();

    }


}
