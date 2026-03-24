package com.rh.manage.Dto;

import java.time.LocalDate;

import com.rh.manage.Model.EtatCivil;

public class EmployeDetailsDTO {
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private String telephone;
    private String email;
    private String adresse;
    private String nomMere;
    private String nomPere;
    private String lieuNaissance;
    private String numCnaps;
    private String cin;
    private Integer nbEnfants;
    private String nomConjoint;
    private String etatCivil;  // String reçue du JSON

    // === MÉTHODE DE CONVERSION IMPORTANTE ===
    public EtatCivil getEtatCivilAsEnum() {
        if (this.etatCivil == null || this.etatCivil.trim().isEmpty()) {
            return EtatCivil.CELIBATAIRE;
        }
        
        try {
            System.out.println("tafiditra ato ++++++++++++++ état civil : " + this.etatCivil);
            // Convertir la string en Enum
            return EtatCivil.valueOf(this.etatCivil.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Si la valeur n'existe pas dans l'Enum, retourner CELIBATAIRE par défaut
            return EtatCivil.CELIBATAIRE;
        }
    }
    
    // Méthode utilitaire pour validation
    public boolean isValidEtatCivil() {
        if (this.etatCivil == null) return false;
        
        String value = this.etatCivil.toUpperCase();
        return value.equals("CELIBATAIRE") || 
               value.equals("MARIE") || 
               value.equals("DIVORCE") || 
               value.equals("VEUF");
    }

    // Getters/Setters existants...
    public String getEtatCivil() {
        return etatCivil;
    }
    
    public void setEtatCivil(String etatCivil) {
        this.etatCivil = etatCivil;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getNomMere() {
        return nomMere;
    }

    public void setNomMere(String nomMere) {
        this.nomMere = nomMere;
    }

    public String getNomPere() {
        return nomPere;
    }

    public void setNomPere(String nomPere) {
        this.nomPere = nomPere;
    }

    public String getLieuNaissance() {
        return lieuNaissance;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public String getNumCnaps() {
        return numCnaps;
    }

    public void setNumCnaps(String numCnaps) {
        this.numCnaps = numCnaps;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public Integer getNbEnfants() {
        return nbEnfants;
    }

    public void setNbEnfants(Integer nbEnfants) {
        this.nbEnfants = nbEnfants;
    }

    public String getNomConjoint() {
        return nomConjoint;
    }

    public void setNomConjoint(String nomConjoint) {
        this.nomConjoint = nomConjoint;
    }
    
    
}