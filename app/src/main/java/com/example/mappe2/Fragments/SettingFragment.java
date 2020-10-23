package com.example.mappe2.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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

import static androidx.constraintlayout.widget.Constraints.TAG;


public class SettingFragment extends Fragment {

    private SwitchMaterial slo_sms;
    private TextInputEditText noti, melding;
    boolean sjekk;
    View v ;
    SharedPreferences.Editor endre;
    String smsInnhold;
    boolean aktivert;
    private Calendar calendar;
    SharedPreferences sp;

    public SettingFragment(){
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_setting, container, false);
       sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        endre =sp.edit();




        slo_sms = v.findViewById(R.id.slo_SMS);
        noti = v.findViewById(R.id.not_tid);
        melding = v.findViewById(R.id.melding);
        noti.setShowSoftInputOnFocus(false);
     //  sjekk  = !ActivityCompat.shouldShowRequestPermissionRationale((getActivity()), Manifest.permission.SEND_SMS);
        calendar = Calendar.getInstance();
        smsInnhold=sp.getString("melding","Husk! vi har et møte. Takk");
        Log.d(TAG, "onCreate: "+smsInnhold);
        aktivert =sp.getBoolean("aktivert",false);


            melding.setText(smsInnhold);


            slo_sms.setChecked(aktivert);



        final int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        final int minut = calendar.get(java.util.Calendar.MINUTE);

        noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int min) {
                        hour = hour + 1;
                        String tiden = hour + ":" + min;
                        noti.setText(tiden);
                    }
                }, hour, minut, DateFormat.is24HourFormat(getContext()));
                timePickerDialog.show();

            }
        });



        slo_sms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true){






                }else{
                    Toast.makeText(getActivity(), "Du har false", Toast.LENGTH_SHORT).show();
                }
            }
        });




        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        smsInnhold= melding.getText().toString();
        Log.d(TAG, "onDestroy: "+smsInnhold);
        endre.putString("melding",smsInnhold);
        endre.apply();
        endre.putBoolean("aktivert",aktivert);
        endre.apply();

    }

    @Override
    public void onPause() {
        super.onPause();
        smsInnhold= melding.getText().toString();
        aktivert=slo_sms.isChecked();
        Log.d(TAG, "onDestroy: "+aktivert);
        Log.d(TAG, "onDestroy: "+smsInnhold);
        endre.putString("melding",smsInnhold);
        endre.apply();
        endre.putBoolean("aktivert",aktivert);
        endre.apply();
    }
}