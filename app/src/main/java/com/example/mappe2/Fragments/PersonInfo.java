package com.example.mappe2.Fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mappe2.Activity.MainActivity;
import com.example.mappe2.Controller.DatabaseHandler;
import com.example.mappe2.Modul.Person;
import com.example.mappe2.R;
import com.google.android.material.textfield.TextInputEditText;

public class PersonInfo extends Fragment {

    TextInputEditText navn, telfonnr;
    private Button lagre;
    private DatabaseHandler db;
    View v;

    public PersonInfo() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_person_info, container, false);

        navn = v.findViewById(R.id.navn2);
        telfonnr = v.findViewById(R.id.telefonnr2);
        lagre =v.findViewById(R.id.lagre);
        db = new DatabaseHandler(getActivity().getApplicationContext());

        Bundle bundle = this.getArguments();

        if(bundle != null){
            navn.setText(bundle.getString("navn2"));
            telfonnr.setText(bundle.getString("telefonnr2"));
        }
        lagre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String innNavn = navn.getText().toString();
                String innTel = telfonnr.getText().toString();
                Person person = new Person(innNavn, innTel);
                db.createPerson(person);
                Intent intent = new Intent(getActivity().getBaseContext(),
                        MainActivity.class);
                getActivity().startActivity(intent);
            }
        });

        return v;
    }
}