package com.rh.manage.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "nationalite")
public class Nationalite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nationalite", nullable = false, length = 150)
    private String nationalite;

    public Nationalite() {
    }
    
    public Nationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    // --- Getters et Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }
}
