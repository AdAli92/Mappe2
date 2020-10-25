package com.example.mappe2.Fragments;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.CompoundButton;
import android.widget.TimePicker;

import com.example.mappe2.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class SettingFragment extends Fragment {
    private SwitchMaterial slo_sms;
    private TextInputEditText noti, melding;
    private View v;
    private SharedPreferences.Editor endre;
    private String smsInnhold;
    boolean aktivert;
    private Calendar calendar;
    private SharedPreferences sp;
    private String tid;
    private static int send; //Denne variablen sjekker om man har permession til å sende sms

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
        aktivert = sp.getBoolean("aktivert", false);
        tid = sp.getString("tid", "23:59");
        melding.setText(smsInnhold);
        slo_sms.setChecked(aktivert);
        noti.setText(tid);
        if (aktivert == false) { //Hvis slo_sms er disblet,så det byter at man ikke aktivert send sms
            send = -1;
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
            }

        });


        slo_sms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true) {
                    if (send == -1) {
                        beOmTillatelse();
                    }
                } else if (b == false) {
                    send = -1;
                }
            }
        });

        return v;
    }

    private void EnableNotificationService() {
        Intent intent = new Intent();
        intent.setAction("com.example.mappe2.service.NotificationBrodcastReceiver");
        getActivity().sendBroadcast(intent);
    }


    private void beOmTillatelse() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Vi trenger din tillatelse")
                .setMessage("Vil du gi tillatelse til denen appen til å sende sms?")
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(SettingFragment.this.getActivity(),
                                new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE}, 1);
                        aktivert = true;
                    }
                })

                .setNegativeButton("Nei", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(SettingFragment.this.getActivity(),
                                new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE}, 0);
                        aktivert = false;
                        slo_sms.setChecked(aktivert);
                        send = -1;
                        dialog.dismiss();

                    }
                })
                .create()
                .show();
    }

    // Vi lager alle attributer i sharedpreferanser i distroy og pause.
    @Override
    public void onPause() {
        super.onPause();
        smsInnhold = melding.getText().toString();
        aktivert = slo_sms.isChecked();
        endre.putString("melding", smsInnhold);
        endre.apply();
        endre.putBoolean("aktivert", aktivert);
        endre.apply();
        EnableNotificationService();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

            smsInnhold = melding.getText().toString();
        if(smsInnhold !=null) {
            endre.putString("melding", smsInnhold);
            endre.apply();
        }
        endre.putBoolean("aktivert", aktivert);
        endre.apply();
        EnableNotificationService();
    }

}
