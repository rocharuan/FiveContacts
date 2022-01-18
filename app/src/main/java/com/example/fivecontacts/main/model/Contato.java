package com.example.fivecontacts.main.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Contato implements Serializable, Comparable {
    String nome;
    String numero;
    Bitmap foto;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    @Override
    public int compareTo(Object o) {
        Contato c2= (Contato)o;
        return this.getNome().compareTo(c2.getNome());
    }
}
