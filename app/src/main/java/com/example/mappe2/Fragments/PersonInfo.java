package com.example.mappe2.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.mappe2.Activity.MainActivity;
import com.example.mappe2.Controller.DatabaseHandler;
import com.example.mappe2.Modul.Person;
import com.example.mappe2.R;
import com.google.android.material.textfield.TextInputEditText;

public class PersonInfo extends Fragment {
    private TextInputEditText navn, telfonnr;
    private Button lagre, endre;
    private int id;
    private DatabaseHandler db;
    private View v;

    public PersonInfo() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_person_info, container, false);
        navn = v.findViewById(R.id.navn2);
        telfonnr = v.findViewById(R.id.telefonnr2);
        lagre = v.findViewById(R.id.lagre);
        endre = v.findViewById(R.id.endre);
        db = new DatabaseHandler(getActivity().getApplicationContext());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            try {
                navn.setText(bundle.getString("navn2"));
                telfonnr.setText(bundle.getString("telefonnr2"));
                id = bundle.getInt("id");
            } catch (Exception e) {
                Toast.makeText(this.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } else {
            endre.setVisibility(View.INVISIBLE);
        }

        lagre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validerNavn() | !validerTelefonnr()) {
                    return;
                }
                String innNavn = navn.getText().toString();
                String innTel = telfonnr.getText().toString();
                Person person = new Person(innNavn, innTel);
                db.createPerson(person);
                db.closeDB();//lukke databasen
                Intent intent = new Intent(getActivity().getBaseContext(),
                        MainActivity.class);
                getActivity().startActivity(intent);
            }
        });
        endre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validerNavn() | !validerTelefonnr()) {
                    return;
                }
                String innNavn1 = navn.getText().toString();
                String innTel = telfonnr.getText().toString();
                Person person = new Person(id, innNavn1, innTel);
                db.OppdaterePerson(person);
                db.closeDB();//lukke databasen
                Intent intent1 = new Intent(getActivity().getBaseContext(), MainActivity.class);
                startActivity(intent1);
            }
        });
        return v;
    }

    //Validering metoder
    private boolean validerNavn() {
        String navnet = navn.getText().toString().trim();
        if (navnet.isEmpty()) {
            navn.setError("Du må fylle person navn!");
            return false;
        } else if (navn.length() > 15) {
            navn.setError("Navnet for personen er langt!");
            return false;
        } else if (!navnet.matches("[a-zA-Z ]+")) {
            navn.setError("Bruk bare bokstaver!");
            return false;
        } else {
            navn.setError(null);
            return true;
        }
    }

    private boolean validerTelefonnr() {
        String tel = telfonnr.getText().toString().trim();
        if (tel.isEmpty()) {
            telfonnr.setError("Du må skrive telefonnummer!");
            return false;
        } else if (telfonnr.length() != 8) {
            telfonnr.setError("Telefonnummer må være på 8 tall");
            return false;
        } else {
            telfonnr.setError(null);
            return true;
        }
    }
}