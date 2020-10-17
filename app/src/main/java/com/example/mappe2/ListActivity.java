package com.example.mappe2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private DatabaseHandler db;
    List<Person> personer;
    ArrayList<Person>checked;
    MenuItem lagre;
    ListePrsonerAdapter adapter;
    private Bundle extras;
    Mote mote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView = findViewById(R.id.rv_liste_personer);

        toolbar = findViewById(R.id.toolbar2);
        db =  new DatabaseHandler(getApplicationContext());
        checked = new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));

        personer = db.HenteAllePersoner();



        adapter = new ListePrsonerAdapter(getApplicationContext() ,personer);

        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);

        extras = getIntent().getExtras();
        if(extras != null){
            mote = new Mote(extras.getString("navn"),extras.getString("type"),
                    extras.getString("sted"),extras.getString("dato"));


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
            case  R.id.meny_lagre :{
                Toast.makeText(this, "Hei from list", Toast.LENGTH_SHORT).show();

                checked= adapter.returnChecked();
                int [] presoner_ider = new int [checked.size()];
             for(int i = 0; i<presoner_ider.length; i++){
                 presoner_ider[i]=checked.get(i).getPersonId();
             }
            long mote_id   = db.LageMote(mote,presoner_ider);
                Log.d("Tag Name", mote_id +"Dette er id møte");
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();



            }

            }
        return super.onOptionsItemSelected(item);

    }
}