package com.example.mappe2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.android.material.textfield.TextInputEditText;

public class MoteActivity extends AppCompatActivity {

    private TextInputEditText navn, type, sted, dato;
    private Button button;
    private Bundle extras;
    private Toolbar toolbar;
    private Calendar calendar;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mote);

        navn = findViewById(R.id.navn1);
        type = findViewById(R.id.type1);
        sted = findViewById(R.id.sted1);
        dato = findViewById(R.id.dato1);
        button = findViewById(R.id.add_personer);
        dato.setShowSoftInputOnFocus(false);
        toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));

        extras = getIntent().getExtras();
        if(extras != null){
            navn.setText(extras.getString("navn"));
            type.setText(extras.getString("type"));
            sted.setText(extras.getString("sted"));
            dato.setText(extras.getString("dato"));
        }

        calendar = Calendar.getInstance();
        final int aar = calendar.get(Calendar.YEAR);
        final int moneder = calendar.get(Calendar.MONTH);
        final int dag = calendar.get(Calendar.DAY_OF_MONTH);

        dato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MoteActivity.this, new DatePickerDialog.OnDateSetListener() {
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ListActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.andre_meny, menu);
        MenuItem lagre = menu.findItem(R.id.meny_lagre);
        MenuItem endre = menu.findItem(R.id.meny_endre);
        MenuItem slette = menu.findItem(R.id.meny_slette);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.meny_lagre:
                return true;
            case R.id.meny_endre:
                return true;
            case R.id.meny_slette:
                return true;
        }
        return false;
    }

    private void disableFelt(){
        navn.setEnabled(false);
        type.setEnabled(false);
        sted.setEnabled(false);
        dato.setEnabled(false);
    }

    private void enableFelt(){
        navn.setEnabled(true);
        type.setEnabled(true);
        sted.setEnabled(true);
        dato.setEnabled(true);
    }

    private void clearFelt(){
        navn.setText("");
        type.setText("");
        sted.setText("");
        dato.setText("");
    }

}