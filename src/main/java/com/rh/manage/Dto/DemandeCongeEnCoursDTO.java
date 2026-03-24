package com.rh.manage.Dto;

import java.time.LocalDate;

public class DemandeCongeEnCoursDTO {
    private String employeNom;
    private String matricule;
    private String departement;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private int dureeJours;
    private String statut; // "en_attente", "valide_manager"
    private LocalDate dateDemande;
    public String getEmployeNom() {
        return employeNom;
    }
    public void setEmployeNom(String employeNom) {
        this.employeNom = employeNom;
    }
    public String getMatricule() {
        return matricule;
    }
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
    public String getDepartement() {
        return departement;
    }
    public void setDepartement(String departement) {
        this.departement = departement;
    }
    public LocalDate getDateDebut() {
        return dateDebut;
    }
    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }
    public LocalDate getDateFin() {
        return dateFin;
    }
    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }
    public int getDureeJours() {
        return dureeJours;
    }
    public void setDureeJours(int dureeJours) {
        this.dureeJours = dureeJours;
    }
    public String getStatut() {
        return statut;
    }
    public void setStatut(String statut) {
        this.statut = statut;
    }
    public LocalDate getDateDemande() {
        return dateDemande;
    }
    public void setDateDemande(LocalDate dateDemande) {
        this.dateDemande = dateDemande;
    }

    
}


