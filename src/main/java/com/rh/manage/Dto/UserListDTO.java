package com.rh.manage.Dto;

import java.util.List;

public class UserListDTO {
    private String idUser;
    private String matricule;
    private String nom;
    private String prenom;
    private String typeUser;
    private String departement;
    private Integer statut;
    private List<Integer> roleIds;
    private List<String> roleTypes;

    public UserListDTO() {
    }

    public UserListDTO(String idUser, String matricule, String nom, String prenom, String typeUser, String departement, Integer statut, List<Integer> roleIds, List<String> roleTypes) {
        this.idUser = idUser;
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        this.typeUser = typeUser;
        this.departement = departement;
        this.statut = statut;
        this.roleIds = roleIds;
        this.roleTypes = roleTypes;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
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

    public List<Integer> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Integer> roleIds) {
        this.roleIds = roleIds;
    }

    public List<String> getRoleTypes() {
        return roleTypes;
    }

    public void setRoleTypes(List<String> roleTypes) {
        this.roleTypes = roleTypes;
    }
}
