package com.example.mappe2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mappe2.Adapters.MoteRvAdapter;
import com.example.mappe2.Adapters.ViewPagerAdapter;
import com.example.mappe2.Fragments.MoteFragment;
import com.example.mappe2.Fragments.MoteInfo;
import com.example.mappe2.Fragments.PersonFragment;
import com.example.mappe2.Fragments.PersonInfo;
import com.example.mappe2.Fragments.PrefranserFragment;
import com.example.mappe2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import com.example.mappe2.Controller.DatabaseHandler;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MoteFragment moteFragment;
    private PersonFragment personFragment;
    private PrefranserFragment prefranserFragment;
    private FloatingActionButton fab;
    DatabaseHandler db;
    MoteRvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHandler(getApplicationContext());
        //Add komentar


        db.closeDB();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setElevation(0);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));

        fab = findViewById(R.id.main_fab);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        moteFragment = new MoteFragment();
        personFragment = new PersonFragment();
        prefranserFragment = new PrefranserFragment();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(moteFragment, "Møter");
        viewPagerAdapter.addFragment(personFragment, "Personer");
        viewPagerAdapter.addFragment(prefranserFragment, "Prefranser");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            tabLayout.getTabAt(0).setIcon(R.drawable.mote);
            tabLayout.getTabAt(1).setIcon(R.drawable.person);
            tabLayout.getTabAt(2).setIcon(R.drawable.prefranser);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MoteActivity.class);
                startActivity(intent);
                finish();
            }
        });


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fab.setEnabled(false);
            fab.setVisibility(View.GONE);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    fab.setEnabled(false);
                    fab.setVisibility(View.GONE);
                    if (tabLayout.getSelectedTabPosition() == 0) {

                        getSupportFragmentManager().beginTransaction().replace(R.id.frag, new MoteInfo()).addToBackStack(null).commit();
                    } else {
                        getSupportFragmentManager().beginTransaction().replace(R.id.frag, new PersonInfo()).addToBackStack(null).commit();
                    }
                }
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                    if(tabLayout.getSelectedTabPosition() == 0) {
                        fab.setEnabled(true);
                        fab.setVisibility(View.VISIBLE);
                        fab.setImageResource(R.drawable.addmote);
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getBaseContext(), MoteActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                    if (tabLayout.getSelectedTabPosition() == 1) {
                        fab.setEnabled(true);
                        fab.setVisibility(View.VISIBLE);
                        fab.setImageResource(R.drawable.addperson);
                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getBaseContext(), PersonActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                    if(tabLayout.getSelectedTabPosition() == 2){
                        fab.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_meny, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.soke).getActionView();
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                /*
                ArrayList<Mote> mote = db.getMote(query);
                adapter.setMoteList(mote);
                adapter.notifyDataSetChanged();*/
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               /* ArrayList<Mote> mote = db.getMote(newText);
                adapter.setMoteList(mote);
                adapter.notifyDataSetChanged();*/
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
               /* List<Mote> mote = db.HenteAlleMoter();
                adapter.setMoteList(mote);
                adapter.notifyDataSetChanged();*/
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}