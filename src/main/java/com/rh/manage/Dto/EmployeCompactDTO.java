package com.rh.manage.Dto;

public class EmployeCompactDTO {
    private String matricule;
    private String nomComplet;
    private String nomAbrege; // Format: J. Tsilavinay
    private String nomPoste;
    private String nomNiveau;
    private Integer rang;
    private String nomDepartement;
    
    // Constructeurs, getters, setters...
    public EmployeCompactDTO() {}
    
    public EmployeCompactDTO(String matricule, String nomComplet, String nomAbrege, 
                           String nomPoste, String nomNiveau, Integer rang, String nomDepartement) {
        this.matricule = matricule;
        this.nomComplet = nomComplet;
        this.nomAbrege = nomAbrege;
        this.nomPoste = nomPoste;
        this.nomNiveau = nomNiveau;
        this.rang = rang;
        this.nomDepartement = nomDepartement;
    }

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

    public String getNomAbrege() {
        return nomAbrege;
    }

    public void setNomAbrege(String nomAbrege) {
        this.nomAbrege = nomAbrege;
    }

    public String getNomPoste() {
        return nomPoste;
    }

    public void setNomPoste(String nomPoste) {
        this.nomPoste = nomPoste;
    }

    public String getNomNiveau() {
        return nomNiveau;
    }

    public void setNomNiveau(String nomNiveau) {
        this.nomNiveau = nomNiveau;
    }

    public Integer getRang() {
        return rang;
    }

    public void setRang(Integer rang) {
        this.rang = rang;
    }

    public String getNomDepartement() {
        return nomDepartement;
    }

    public void setNomDepartement(String nomDepartement) {
        this.nomDepartement = nomDepartement;
    }
    
    
}

