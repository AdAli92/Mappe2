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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mappe2.Activity.ListActivity;
import com.example.mappe2.Activity.MainActivity;
import com.example.mappe2.Controller.DatabaseHandler;
import com.example.mappe2.Modul.Mote;
import com.example.mappe2.Modul.Person;
import com.example.mappe2.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class MoteInfo extends Fragment {
    private TextInputEditText navn, type, sted, dato, tid;
    private Button button, endre;
    private Calendar calendar;
    private TextView personer_view;
    private DatabaseHandler db;
    private View v;
    private int id;

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
        endre = v.findViewById(R.id.endre);
        button = v.findViewById(R.id.add_personer2);
        personer_view = v.findViewById(R.id.person_view);
        db = new DatabaseHandler(getActivity().getApplicationContext());
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
                        month = month + 1;
                        String date = day + "-" + month + "-" + year;
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
                        String tiden = hour + ":" + min;
                        tid.setText(tiden);
                    }
                }, hour, minut, DateFormat.is24HourFormat(getContext()));
                timePickerDialog.show();

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validerNavn() | !validerType() | !validerSted() | !validerDato() | !validerTid()) {
                    return;
                }
                Intent intent = new Intent(getContext(), ListActivity.class);
                String innNavn = navn.getText().toString();
                String innType = type.getText().toString();
                String innSted = sted.getText().toString();
                String innDato = dato.getText().toString();
                String innTid = tid.getText().toString();
                //Sette tekst i elementer
                intent.putExtra("navn", innNavn);
                intent.putExtra("type", innType);
                intent.putExtra("sted", innSted);
                intent.putExtra("dato", innDato);
                intent.putExtra("tid", innTid);
                if (id != 0) {
                    intent.putExtra("id", id);
                }
                startActivity(intent);
            }
        });

        Bundle bundle = this.getArguments();
        endre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validerNavn() | !validerType() | !validerSted() | !validerDato() | !validerTid()) {
                    return;
                }
                String innNavn1 = navn.getText().toString();
                String innType1 = type.getText().toString();
                String innSted1 = sted.getText().toString();
                String innDato1 = dato.getText().toString();
                String innTid = tid.getText().toString();

                if (id != 0) {
                    Mote mote = new Mote(id, innNavn1, innType1, innDato1, innSted1, innTid);
                    db.OppdatereMote(mote);
                    db.closeDB();
                    Intent intent1 = new Intent(getActivity().getBaseContext(), MainActivity.class);
                    startActivity(intent1);
                }
            }
        });

        if (bundle != null) {
            try {
                navn.setText(bundle.get("navn8").toString());
                type.setText(bundle.get("type8").toString());
                sted.setText(bundle.get("sted8").toString());
                dato.setText(bundle.get("dato8").toString());
                tid.setText(bundle.getString("tid8"));
                id = bundle.getInt("id");
            } catch (Exception e) {
                Toast.makeText(this.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if (id != 0) {
                List<Person> personerUnerEtmote = db.HenteAllePersonerIMote(id);
                for (Person person : personerUnerEtmote) {
                    personer_view.append(person.getNavn());
                    personer_view.append(", ");
                }
            }
        }

        return v;
    }


    //Validering metoder
    private boolean validerNavn() {
        String navnet = navn.getText().toString().trim();
        if (navnet.isEmpty()) {
            navn.setError("Du må fylle møte navn!");
            return false;
        } else if (navn.length() > 15) {
            navn.setError("Navnet for møtet er langt!");
            return false;
        } else if (!navnet.matches("[a-zA-Z ]+")) {
            navn.setError("Bruk bare bokstaver!");
            return false;
        } else {
            navn.setError(null);
            return true;
        }
    }

    private boolean validerType() {
        String typen = type.getText().toString().trim();
        if (typen.isEmpty()) {
            type.setError("Du må fylle typen!");
            return false;
        } else if (type.length() > 15) {
            type.setError("Navnet på typen er langt!");
            return false;
        } else if (!typen.matches("[a-zA-Z ]+")) {
            type.setError("Bruk bare bokstaver!");
            return false;
        } else {
            type.setError(null);
            return true;
        }
    }

    private boolean validerSted() {
        String stedet = sted.getText().toString().trim();
        if (stedet.isEmpty()) {
            sted.setError("Du må fylle her!");
            return false;
        } else if (sted.length() > 15) {
            sted.setError("Navnet for stedet er langt!");
            return false;
        } else if (!stedet.matches("[a-zA-Z ]+")) {
            sted.setError("Bruk bare bokstaver!");
            return false;
        } else {
            sted.setError(null);
            return true;
        }
    }

    private boolean validerDato() {
        String datoen = dato.getText().toString().trim();
        if (datoen.isEmpty()) {
            dato.setError("Du må fyle dato!");
            return false;
        } else {
            dato.setError(null);
            return true;
        }
    }

    private boolean validerTid() {
        String stedet = tid.getText().toString().trim();
        if (stedet.isEmpty()) {
            tid.setError("Du må fyle tid!");
            return false;
        } else {
            tid.setError(null);
            return true;
        }
    }

}