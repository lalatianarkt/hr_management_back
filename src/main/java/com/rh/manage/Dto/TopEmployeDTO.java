package com.rh.manage.Dto;

public class TopEmployeDTO {
    private String matricule;
    private String nomComplet;
    private String departement;
    private String poste;
    private double valeur; // Nombre d'absences, heures travaillées, etc.
    private String indicateur; // "absences", "heures_travaillees", "retards", "heures_sup"
    private int rang;
    public String getMatricule() {
        return matricule;
    }
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
    public String getNomComplet() {
        return nomComplet;
    }
    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }
    public String getDepartement() {
        return departement;
    }
    public void setDepartement(String departement) {
        this.departement = departement;
    }
    public String getPoste() {
        return poste;
    }
    public void setPoste(String poste) {
        this.poste = poste;
    }
    public double getValeur() {
        return valeur;
    }
    public void setValeur(double valeur) {
        this.valeur = valeur;
    }
    public String getIndicateur() {
        return indicateur;
    }
    public void setIndicateur(String indicateur) {
        this.indicateur = indicateur;
    }
    public int getRang() {
        return rang;
    }
    public void setRang(int rang) {
        this.rang = rang;
    }

    
}

