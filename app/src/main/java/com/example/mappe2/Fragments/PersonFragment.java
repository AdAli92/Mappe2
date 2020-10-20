package com.example.mappe2.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mappe2.Adapters.PersonRvAdapter;
import com.example.mappe2.Controller.DatabaseHandler;
import com.example.mappe2.Modul.Person;
import com.example.mappe2.Activity.PersonActivity;
import com.example.mappe2.R;
import com.example.mappe2.RecyclerViewInterface;

import java.util.List;

public class PersonFragment extends Fragment implements RecyclerViewInterface {

    private View view;
    private RecyclerView recyclerView;
    List<Person> personer;
    PersonRvAdapter personRvAdapter;
    Dialog dialog;
    DatabaseHandler db;
    Person person;

    public PersonFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_person, container, false);
        recyclerView = view.findViewById(R.id.rv_person);

        db = new DatabaseHandler(getActivity().getApplicationContext());
        personer =db.HenteAllePersoner();



        personRvAdapter = new PersonRvAdapter(getContext(), personer, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(personRvAdapter);

        return view;
    }

    @Override
    public void onItemClick(int position) {

        Person person = personer.get(position);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

            Intent intent = new Intent(getContext(), PersonActivity.class);
            intent.putExtra("id",person.getPersonId());
            intent.putExtra("navn1", person.getNavn());
            intent.putExtra("telefonnr1", person.getTelefonnr());

            getContext().startActivity(intent);
        }
        else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){

            Bundle bundle = new Bundle();
            bundle.putString("navn2", person.getNavn());
            bundle.putString("telefonnr2", person.getTelefonnr());
            bundle.putInt("id",person.getPersonId());

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            PersonInfo personInfo = new PersonInfo();
            personInfo.setArguments(bundle);

            fragmentTransaction.replace(R.id.frag, personInfo);
            fragmentTransaction.commit();

        }

    }

    @Override
    public void onLongItemClick(final int position) {

        person = personer.get(position);

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
/*
        TextView title = dialog.findViewById(R.id.dialog_title);
        title.setText(person.getNavn());
        
 */

        dialog.show();

        Button btn = dialog.findViewById(R.id.dialog_btn);
        Button dialog_ikke = dialog.findViewById(R.id.dialog_ikke);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                personer.remove(position);
                db.SlettePerson(person.getPersonId());
                personRvAdapter.notifyItemRemoved(position);
                dialog.cancel();
            }
        });
        dialog_ikke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.cancel();
            }
        });

    }
}