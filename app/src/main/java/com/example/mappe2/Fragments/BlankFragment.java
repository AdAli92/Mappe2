package com.example.mappe2.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mappe2.R;
import com.google.android.material.textfield.TextInputEditText;

public class BlankFragment extends Fragment {

    private TextInputEditText melding;
    View v ;

    public BlankFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_blank, container, false);

        melding = v.findViewById(R.id.melding);



        return v;
    }
}