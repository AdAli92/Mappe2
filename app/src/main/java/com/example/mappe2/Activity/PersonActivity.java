package com.example.mappe2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mappe2.Activity.MainActivity;
import com.example.mappe2.Controller.DatabaseHandler;
import com.example.mappe2.Modul.Person;
import com.example.mappe2.R;
import com.google.android.material.textfield.TextInputEditText;

public class PersonActivity extends AppCompatActivity {

    TextInputEditText navn, telfonnr;
    Toolbar toolbar;
    private Bundle bundle;
    private DatabaseHandler db;
    private boolean forEndre = false ;
    MenuItem lagre,endre,slette;
    private int id ;
    Person person;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        navn = findViewById(R.id.navn2);
        telfonnr = findViewById(R.id.telefonnr2);

        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        db =  new DatabaseHandler(getApplicationContext());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));

        bundle = getIntent().getExtras();

        if(bundle != null){
            forEndre = true;
            navn.setText(bundle.getString("navn1"));
            telfonnr.setText(bundle.getString("telefonnr1"));
            id=bundle.getInt("id");

        }
        else{
            forEndre = false;
        }
//fgbfb
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.andre_meny, menu);
        lagre = menu.findItem(R.id.meny_lagre);
        endre = menu.findItem(R.id.meny_endre);
        slette = menu.findItem(R.id.meny_slette);
        if(forEndre){
            lagre.setVisible(false);
            slette.setVisible(true);
        }else{
            endre.setVisible(false);
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( ! validerNavn() | ! validerTelefonnr()){
            return false;
        }
        switch (item.getItemId()) {
            case R.id.meny_lagre:
                String innNavn = navn.getText().toString();
                String innTelfon = telfonnr.getText().toString();
               person = new Person(innNavn,innTelfon,R.drawable.person);

             db.createPerson(person);
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.meny_endre:
                String innNavn1 = navn.getText().toString();
                String innTel = telfonnr.getText().toString();


                person = new Person(id,innNavn1,innTel);

                db.OppdaterePerson(person);
                Intent intent1 = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent1);
                return true;
            case R.id.meny_slette:
                db.SlettePerson(id);
                Intent intent2 = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent2);
                finish();
                return true;
        }
        return false;
    }


    private  boolean validerNavn(){
        String navnet = navn.getText().toString().trim();

        if(navnet.isEmpty()){
            navn.setError("Du må fylle person navn!");
            return false;
        }
        else if (navn.length() > 15){
            navn.setError("Navnet for personen er langt!");
            return false;
        }
        else if(!navnet.matches("[a-zA-Z ]+"))
        {
            navn.setError("Bruk bare bokstaver!");
            return false;
        }
        else {
            navn.setError(null);
            return true;
        }
    }

    private  boolean validerTelefonnr(){
        String tel = telfonnr.getText().toString().trim();

        if(tel.isEmpty()){
            telfonnr.setError("Du må skrive telefonnummer!");
            return false;
        }
        else if(telfonnr.length() != 8){
            telfonnr.setError("Telefonnummer må være på 8 tall");
            return false;
        }
       /* else if(!tel.matches("/^[0-9]{1,8}$/"))
        {
            telfonnr.setError("Bruk bare tall!");
            return false;
        }*/
        else {
            telfonnr.setError(null);
            return true;
        }
    }

}