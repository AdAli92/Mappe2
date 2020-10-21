package com.example.mappe2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mappe2.Modul.Person;
import com.example.mappe2.R;

import java.util.ArrayList;
import java.util.List;

public class ListePrsonerAdapter extends RecyclerView.Adapter<ListePrsonerAdapter.PersonViewHolder> {
    Context context;
    private List<Person> personer;
    ArrayList<Person> checked = new ArrayList<>();

    public ListePrsonerAdapter(Context context, List<Person> personer) {
        this.context = context;
        this.personer = personer;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.items_liste_personer, null, false);
        PersonViewHolder personViewHolder = new PersonViewHolder(view);
        return personViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        Person person = personer.get(position);
        holder.navn.setText(person.getNavn());
        holder.tel.setText(person.getTelefonnr());
        holder.navn.setTag(person.getPersonId());
        holder.setItemClickListener(new ItemClick() {
            @Override
            public void onItemClick(View v, int position) {
                CheckBox checkBox = (CheckBox) v;
                if (checkBox.isChecked()) {
                    checked.add(personer.get(position));
                } else if (!checkBox.isChecked()) {
                    checked.remove(personer.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return personer.size();
    }

    class PersonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView navn, tel;
        CheckBox checkBox;
        ItemClick itemClick;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            navn = itemView.findViewById(R.id.person2);
            tel = itemView.findViewById(R.id.telefonnr2);
            checkBox = itemView.findViewById(R.id.checkbox);
            checkBox.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClick rv) {
            this.itemClick = rv;
        }

        @Override
        public void onClick(View view) {
            this.itemClick.onItemClick(view, getLayoutPosition());
        }
    }

    public ArrayList<Person> returnChecked() {
        return checked;
    }

    public interface ItemClick {
        void onItemClick(View v, int position);
    }
}
