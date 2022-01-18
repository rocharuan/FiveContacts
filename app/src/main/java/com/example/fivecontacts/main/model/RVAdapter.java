package com.example.fivecontacts.main.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fivecontacts.R;

import java.util.ArrayList;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView contactName;
        ImageView personPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            contactName = (TextView)itemView.findViewById(R.id.tv_ContactName);
            personPhoto = (ImageView)itemView.findViewById(R.id.iv_contactImage);
        }
    }

    ArrayList<Contato> contatos;

    public RVAdapter(ArrayList<Contato> contatos){
        this.contatos = contatos;
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.contactName.setText(contatos.get(i).nome);
        Bitmap u = contatos.get(i).getFoto();
        if (u != null) {
            personViewHolder.personPhoto.setImageBitmap(u);
        } else {
            personViewHolder.personPhoto.setImageResource(R.drawable.google_contacts);
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}