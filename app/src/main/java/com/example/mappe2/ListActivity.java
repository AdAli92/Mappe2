package com.example.mappe2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mappe2.Adapters.ListePrsonerAdapter;
import com.example.mappe2.Controller.DatabaseHandler;
import com.example.mappe2.Fragments.MoteFragment;
import com.example.mappe2.Modul.Mote;
import com.example.mappe2.Modul.Person;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private DatabaseHandler db;
    private List<Person> personer;
    private ArrayList<Person> checked;
    private MenuItem lagre;
    private ListePrsonerAdapter adapter;
    private Bundle extras;
    private Mote mote;
    private int id;
    private boolean finnes;  //Pass på å ikke gå til hjemmesiden  ,hvis man legger samme person til møte.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);
        LageView(); // metoden som lage adapter , og sette Recycleview på den.
        extras = getIntent().getExtras();
        if (extras != null) {
            mote = new Mote(extras.getString("navn"), extras.getString("type"),
                    extras.getString("sted"), extras.getString("dato"), extras.getString("tid"));
            id = extras.getInt("id");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.liste_meny, menu);
        lagre = menu.findItem(R.id.meny_lagre);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.meny_lagre: {
                Toast.makeText(this, "Hei from list", Toast.LENGTH_SHORT).show();
                //Array som ble returnet fra adapter med checked true value.
                checked = adapter.returnChecked();

                int[] presoner_ider = new int[checked.size()];
                for (int i = 0; i < presoner_ider.length; i++) {
                    presoner_ider[i] = checked.get(i).getPersonId();
                }

                if (id == 0) { //Lage et nytt møte.
                    long mote_id = db.LageMote(mote, presoner_ider);
                    Log.d("Tag Name", "Mote_id" + mote_id);
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                } else if (id != 0) { //Endre et møte.
                    List<Person> personerIMote = db.HenteAllePersonerIMote(id);


                    //Sjekke hvis person ligger i et møte allerede.
                    for (int i = 0; i < presoner_ider.length; i++) { //Todo sjekke om det er riktig alltid denne algoritmen
                        for (int j = 0; j < personerIMote.size(); j++) {
                            Log.d("personer", "den sjekker");
                            Log.d("personer", presoner_ider[i] + "==" + personerIMote.get(j).getPersonId());
                            if (presoner_ider[i] == personerIMote.get(j).getPersonId()) {
                                finnes = true;
                                break;
                            } else {
                                finnes = false;

                            }
                        }
                        if (!finnes) {

                            db.createMotePerson(id, presoner_ider[i]);

                        } else {
                            Toast.makeText(this, "Du kan ikke legge til samme person til møte", Toast.LENGTH_LONG).show();
                        }
                    }

                }
                if (!finnes) {
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        }
        return super.onOptionsItemSelected(item);
    }

    public void LageView() {
        recyclerView = findViewById(R.id.rv_liste_personer);
        toolbar = findViewById(R.id.toolbar2);
        db = new DatabaseHandler(getApplicationContext());
        checked = new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
        personer = db.HenteAllePersoner();
        adapter = new ListePrsonerAdapter(getApplicationContext(), personer);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);
    }
}