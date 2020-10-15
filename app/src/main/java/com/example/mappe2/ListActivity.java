package com.example.mappe2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mappe2.Adapters.ListePrsonerAdapter;
import com.example.mappe2.Modul.Person;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = findViewById(R.id.rv_liste_personer);

        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));

        ArrayList<Person> personer = new ArrayList<>();

        personer.add(new Person("person1","34434556", true));
        personer.add(new Person("person2","34434556", false));
        personer.add(new Person("person3","34434556", false));
        personer.add(new Person("person4","34434556", false));
        personer.add(new Person("person5","34434556", true));
        personer.add(new Person("person6","34434556", false));
        personer.add(new Person("person7","34434556", false));
        personer.add(new Person("person5","34434556", true));
        personer.add(new Person("person6","34434556", false));
        personer.add(new Person("person7","34434556", false));

        ListePrsonerAdapter adapter = new ListePrsonerAdapter(getApplicationContext() ,personer);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.liste_meny, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}