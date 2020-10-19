package com.example.mappe2.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.mappe2.ListActivity;
import com.example.mappe2.MoteActivity;
import com.example.mappe2.R;
import com.google.android.material.textfield.TextInputEditText;

public class MoteInfo extends Fragment {

    private TextInputEditText navn, type, sted, dato, tid;
    private Button button;
    private Calendar calendar;
    View v;

    public MoteInfo() {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_mote_info, container, false);

        navn = v.findViewById(R.id.navn1);
        type = v.findViewById(R.id.type1);
        sted = v.findViewById(R.id.sted1);
        dato = v.findViewById(R.id.dato1);
        tid = v.findViewById(R.id.tid1);
        button = v.findViewById(R.id.add_personer2);

        dato.setShowSoftInputOnFocus(false);
        tid.setShowSoftInputOnFocus(false);
        calendar = Calendar.getInstance();
        final int aar = calendar.get(Calendar.YEAR);
        final int moneder = calendar.get(Calendar.MONTH);
        final int dag = calendar.get(Calendar.DAY_OF_MONTH);
        final int hour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        final int minut = calendar.get(java.util.Calendar.MINUTE);

        dato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String date = day+"/"+month+"/"+year;
                        dato.setText(date);
                    }
                }, aar, moneder, dag);
                datePickerDialog.show();
            }
        });

        tid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int min) {
                        hour = hour+1;
                        String tiden = hour+":"+min;
                        tid.setText(tiden);
                    }
                }, hour, minut,  DateFormat.is24HourFormat(getContext()));
                timePickerDialog.show();

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ListActivity.class);
                startActivity(intent);
            }
        });

        Bundle bundle = this.getArguments();



        if(bundle != null){
            navn.setText(bundle.get("navn8").toString());
            type.setText(bundle.get("type8").toString());
            sted.setText(bundle.get("sted8").toString());
            dato.setText(bundle.get("dato8").toString());//Todo legge til Tid
        }

        return v;
    }

}