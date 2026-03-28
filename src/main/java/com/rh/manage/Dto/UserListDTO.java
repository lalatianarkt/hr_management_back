package com.rh.manage.Dto;

public class UserListDTO {
    private String idUser;
    private String nom;
    private String prenom;
    private String typeUser;
    private String departement;
    private Integer statut;

    public UserListDTO() {
    }

    public UserListDTO(String idUser, String nom, String prenom, String typeUser, String departement, Integer statut) {
        this.idUser = idUser;
        this.nom = nom;
        this.prenom = prenom;
        this.typeUser = typeUser;
        this.departement = departement;
        this.statut = statut;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
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

    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public Integer getStatut() {
        return statut;
    }

    public void setStatut(Integer statut) {
        this.statut = statut;
    }
}
