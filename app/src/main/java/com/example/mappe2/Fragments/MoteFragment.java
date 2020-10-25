package com.example.mappe2.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mappe2.Adapters.MoteRvAdapter;
import com.example.mappe2.Controller.DatabaseHandler;
import com.example.mappe2.Modul.Mote;
import com.example.mappe2.Activity.MoteActivity;
import com.example.mappe2.R;
import com.example.mappe2.RecyclerViewInterface;

import java.util.List;

public class MoteFragment extends Fragment implements RecyclerViewInterface {

    private RecyclerView recyclerView;
    private View view;
    private List<Mote> moter;
    private MoteRvAdapter adapter;
    private Dialog dialog;
    private DatabaseHandler db;
    private Mote mote;

    public MoteFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        db = new DatabaseHandler(getActivity().getApplicationContext());
        view = inflater.inflate(R.layout.fragment_mote, container, false);
        recyclerView = view.findViewById(R.id.rv_mote);
        moter = db.HenteAlleMoter();
        adapter = new MoteRvAdapter(getContext(), moter, this);
        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onItemClick(int position) {
        Mote mote = moter.get(position);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Intent intent = new Intent(getContext(), MoteActivity.class);
            intent.putExtra("id", mote.getMoteId());
            intent.putExtra("navn", mote.getNavn());
            intent.putExtra("type", mote.getType());
            intent.putExtra("sted", mote.getSted());
            intent.putExtra("dato", mote.getDato());
            intent.putExtra("tid", mote.getTid());
            getContext().startActivity(intent);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Bundle bundle = new Bundle();
            bundle.putInt("id", mote.getMoteId());
            bundle.putString("navn8", mote.getNavn());
            bundle.putString("type8", mote.getType());
            bundle.putString("sted8", mote.getSted());
            bundle.putString("dato8", mote.getDato());
            bundle.putString("tid8", mote.getTid());
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            MoteInfo moteInfo = new MoteInfo();
            moteInfo.setArguments(bundle);
            fragmentTransaction.replace(R.id.frag, moteInfo);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onLongItemClick(final int position) {
        mote = moter.get(position);
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        Button btn = dialog.findViewById(R.id.dialog_btn);
        Button dialog_ikke = dialog.findViewById(R.id.dialog_ikke);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moter.remove(position);
                db.SletteMote(mote.getMoteId());
                db.closeDB();
                adapter.notifyItemRemoved(position);
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