package com.example.mappe2.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mappe2.Controller.DatabaseHandler;
import com.example.mappe2.Modul.Mote;
import com.example.mappe2.Modul.Person;
import com.example.mappe2.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class MoteActivity extends AppCompatActivity {
    private TextInputEditText navn, type, sted, dato, tid;
    private Button button;
    private Bundle extras;
    private Toolbar toolbar;
    private Calendar calendar;
    private DatabaseHandler db;
    private int id;
    private Mote mote;
    private TextView personer_view;
    private MenuItem lagre, endre, slette;
    private boolean forEndre = false; //Boolean som sjeker om vi kommer til å endre eller legge til

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mote);
        navn = findViewById(R.id.navn1);
        type = findViewById(R.id.type1);
        sted = findViewById(R.id.sted1);
        dato = findViewById(R.id.dato1);
        tid = findViewById(R.id.tid1);
        button = findViewById(R.id.add_personer);
        dato.setShowSoftInputOnFocus(false);
        tid.setShowSoftInputOnFocus(false);
        toolbar = findViewById(R.id.toolbar1);
        personer_view = findViewById(R.id.person_view);
        db = new DatabaseHandler(getApplicationContext());
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.colorPrimaryDark)));

        extras = getIntent().getExtras();
        if (extras != null) {
            forEndre = true;
            try {
                navn.setText(extras.getString("navn"));
                type.setText(extras.getString("type"));
                sted.setText(extras.getString("sted"));
                dato.setText(extras.getString("dato"));
                tid.setText(extras.getString("tid"));
                id = extras.getInt("id");
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            List<Person> personerUnerEtmote = db.HenteAllePersonerIMote(id);
            for (Person person : personerUnerEtmote) {
                personer_view.append(person.getNavn());
                personer_view.append(", ");
            }

        } else {
            forEndre = false;
        }
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
                        MoteActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                        MoteActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int min) {
                        String tiden = hour + ":" + min;
                        tid.setText(tiden);
                    }
                }, hour, minut, DateFormat.is24HourFormat(getApplicationContext()));
                timePickerDialog.show();

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validerNavn() | !validerType() | !validerSted() | !validerDato() | !validerTid()) {
                    return;
                }
                String innNavn = navn.getText().toString();
                String innType = type.getText().toString();
                String innSted = sted.getText().toString();
                String innDato = dato.getText().toString();
                String innTid = tid.getText().toString();

                Intent intent = new Intent(getBaseContext(), ListActivity.class);
                intent.putExtra("navn", innNavn);
                intent.putExtra("type", innType);
                intent.putExtra("sted", innSted);
                intent.putExtra("dato", innDato);
                intent.putExtra("tid", innTid);
                if (id != 0) { //Hvis id!=0 det betyr at vi er her til å endre , og ikke lage ny møte
                    intent.putExtra("id", id);
                }
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.andre_meny, menu);
        lagre = menu.findItem(R.id.meny_lagre);
        endre = menu.findItem(R.id.meny_endre);
        slette = menu.findItem(R.id.meny_slette);
        if (forEndre) {
            lagre.setVisible(false);
            slette.setVisible(true);
        } else {
            endre.setVisible(false);
            lagre.setVisible(false);
            slette.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.meny_lagre:
                return true;
            case R.id.meny_endre:
                String innNavn1 = navn.getText().toString();
                String innType1 = type.getText().toString();
                String innSted1 = sted.getText().toString();
                String innDato1 = dato.getText().toString();
                String innTid = tid.getText().toString();
                if (!validerNavn() | !validerType() | !validerSted() | !validerDato() | !validerTid()) {
                    return false;
                }
                mote = new Mote(id, innNavn1, innType1, innDato1, innSted1, innTid);
                db.OppdatereMote(mote);
                Intent intent1 = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent1);
                db.closeDB();
                finish();
                return true;
            case R.id.meny_slette:
                db.SletteMote(id);
                Intent intent2 = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent2);
                db.closeDB();
                finish();
                return true;
        }
        return false;
    }

    //Validering metoder
    private boolean validerNavn() {
        String navnet = navn.getText().toString().trim();
        if (navnet.isEmpty()) {
            navn.setError(getResources().getString(R.string.fylleNavn));
            return false;
        } else if (navn.length() > 15) {
            navn.setError(getResources().getString(R.string.forlangtmøtenavn));
            return false;
        } else if (!navnet.matches("[a-zA-Z ]+")) {
            navn.setError(getResources().getString(R.string.barebokstav));
            return false;
        } else {
            navn.setError(null);
            return true;
        }
    }

    private boolean validerType() {
        String typen = type.getText().toString().trim();
        if (typen.isEmpty()) {
            type.setError(getResources().getString(R.string.fylleType));
            return false;
        } else if (type.length() > 15) {
            type.setError(getResources().getString(R.string.forlangtType));
            return false;
        } else if (!typen.matches("[a-zA-Z ]+")) {
            type.setError(getResources().getString(R.string.barebokstav));
            return false;
        } else {
            type.setError(null);
            return true;
        }
    }

    private boolean validerSted() {
        String stedet = sted.getText().toString().trim();
        if (stedet.isEmpty()) {
            sted.setError(getResources().getString(R.string.fylleSted));
            return false;
        } else if (sted.length() > 15) {
            sted.setError(getResources().getString(R.string.forlangtStedet));
            return false;
        } else if (!stedet.matches("[a-zA-Z ]+")) {
            sted.setError(getResources().getString(R.string.barebokstav));
            return false;
        } else {
            sted.setError(null);
            return true;
        }
    }

    private boolean validerDato() {
        String datoen = dato.getText().toString().trim();
        if (datoen.isEmpty()) {
            dato.setError(getResources().getString(R.string.fylleDato));
            return false;
        } else {
            dato.setError(null);
            return true;
        }
    }

    private boolean validerTid() {
        String stedet = tid.getText().toString().trim();
        if (stedet.isEmpty()) {
            tid.setError(getResources().getString(R.string.fylleTid));
            return false;
        } else {
            tid.setError(null);
            return true;
        }
    }


}