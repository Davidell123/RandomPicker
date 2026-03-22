package com.example.randompicker.model;

public class Idee {
    private String categorie;
    private String nume;

    public Idee(String categorie, String nume) {
        this.categorie = categorie;
        this.nume = nume;
    }

    public String getCategorie() {
        return categorie;
    }

    public String getNume() {
        return nume;
    }
}
