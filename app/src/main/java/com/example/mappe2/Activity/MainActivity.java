package com.example.mappe2.Activity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.mappe2.Adapters.ViewPagerAdapter;
import com.example.mappe2.Fragments.MessageFragment;
import com.example.mappe2.Fragments.MoteFragment;
import com.example.mappe2.Fragments.MoteInfo;
import com.example.mappe2.Fragments.PersonFragment;
import com.example.mappe2.Fragments.PersonInfo;
import com.example.mappe2.Fragments.SettingFragment;
import com.example.mappe2.Fragments.TekstFragment;
import com.example.mappe2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MoteFragment moteFragment;
    private PersonFragment personFragment;
    private SettingFragment settingFragment;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setElevation(0);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark)));
        fab = findViewById(R.id.main_fab);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        //Fragmenter som skal vi sitte i viewpager
        moteFragment = new MoteFragment();
        personFragment = new PersonFragment();
        settingFragment = new SettingFragment();
        //Lage Viewpager som skal ha alle fragmenter.
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(moteFragment, "Møter");
        viewPagerAdapter.addFragment(personFragment, "Kontakter");
        viewPagerAdapter.addFragment(settingFragment, "Prefranser");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            tabLayout.getTabAt(0).setIcon(R.drawable.mote);
            tabLayout.getTabAt(1).setIcon(R.drawable.person);
            tabLayout.getTabAt(2).setIcon(R.drawable.prefranser);
            tabLayout.getTabAt(0).getIcon().setColorFilter(Color.rgb(24, 78, 109), PorterDuff.Mode.SRC_IN);
            tabLayout.getTabAt(1).getIcon().setColorFilter(Color.rgb(24, 78, 109), PorterDuff.Mode.SRC_IN);
            tabLayout.getTabAt(2).getIcon().setColorFilter(Color.rgb(24, 78, 109), PorterDuff.Mode.SRC_IN);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MoteActivity.class);
                startActivity(intent);
            }
        });

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag, new TekstFragment()).addToBackStack(null).commit();
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            lp.gravity = Gravity.BOTTOM;
            fab.setLayoutParams(lp);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        if (tabLayout.getSelectedTabPosition() == 0) {
                            fab.setEnabled(true);
                            fab.setVisibility(View.VISIBLE);
                            fab.setImageResource(R.drawable.add_fore);
                            getSupportFragmentManager().beginTransaction().replace(R.id.frag, new TekstFragment()).addToBackStack(null).commit();
                        }
                        if (tabLayout.getSelectedTabPosition() == 1) {
                            fab.setEnabled(false);
                            fab.setVisibility(View.GONE);
                            getSupportFragmentManager().beginTransaction().replace(R.id.frag, new PersonInfo()).addToBackStack(null).commit();
                        }
                        if (tabLayout.getSelectedTabPosition() == 2) {
                            fab.setVisibility(View.GONE);
                            getSupportFragmentManager().beginTransaction().replace(R.id.frag, new MessageFragment()).addToBackStack(null).commit();
                        }
                    }
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

                        if (tabLayout.getSelectedTabPosition() == 0) {
                            fab.setEnabled(true);
                            fab.setVisibility(View.VISIBLE);
                            fab.setImageResource(R.drawable.add_fore);
                            fab.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getBaseContext(), MoteActivity.class);
                                    startActivity(intent);
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
                        if (tabLayout.getSelectedTabPosition() == 2) {
                            fab.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void Sendsms(View view){


}
}