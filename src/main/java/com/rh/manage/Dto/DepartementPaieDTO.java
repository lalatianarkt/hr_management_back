package com.rh.manage.Dto;

import java.time.LocalDate;

import com.rh.manage.Model.Departement;

public class DepartementPaieDTO {
    Departement departement;
    LocalDate date_debut_periode;
    int statut;
    int nbPaieGenere;  
    
    public Departement getDepartement() {
        return departement;
    }
    public void setDepartement(Departement departement) {
        this.departement = departement;
    }
    public LocalDate getDate_debut_periode() {
        return date_debut_periode;
    }
    public void setDate_debut_periode(LocalDate date_debut_periode) {
        this.date_debut_periode = date_debut_periode;
    }
    public int getStatut() {
        return statut;
    }
    public void setStatut(int statut) {
        this.statut = statut;
    }
    public int getNbPaieGenere() {
        return nbPaieGenere;
    }
    public void setNbPaieGenere(int nbPaieGenere) {
        this.nbPaieGenere = nbPaieGenere;
    }
}
