package com.example.mappe2.Fragments;

import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TimePicker;
import com.example.mappe2.R;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;


public class SettingFragment extends Fragment {

    private SwitchMaterial slo_sms;
    private TextInputEditText noti, melding;
    View v ;
    private Calendar calendar;

    public SettingFragment(){
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_setting, container, false);

        slo_sms = v.findViewById(R.id.slo_SMS);
        noti = v.findViewById(R.id.not_tid);
        melding = v.findViewById(R.id.melding);
        noti.setShowSoftInputOnFocus(false);

        calendar = Calendar.getInstance();

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


        return v;
    }
}