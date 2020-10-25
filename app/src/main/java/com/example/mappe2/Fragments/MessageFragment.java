package com.example.mappe2.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mappe2.R;
import com.google.android.material.textfield.TextInputEditText;

public class MessageFragment extends Fragment {

    private TextInputEditText melding;
    private View v;
    private SharedPreferences sp;
    private String smsInnhold;

    public MessageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_message, container, false);

        melding = v.findViewById(R.id.melding1);
        sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        smsInnhold = sp.getString("melding", "Husk! vi har et møte. Takk");
        melding.setText(smsInnhold);

        return v;
    }
}