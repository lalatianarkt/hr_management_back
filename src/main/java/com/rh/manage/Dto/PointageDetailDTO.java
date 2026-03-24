package com.rh.manage.Dto;

import java.time.LocalDate;

import com.rh.manage.Model.VuePointageEmployeV2;

public class PointageDetailDTO {
    private String idEmploye;
    private String matricule;
    private String nomComplet;
    private LocalDate datePointage;
    private Integer dureeHeureTravaillee;
    private Integer dureeRetard;
    private Integer dureeHeureSup;
    private String idDepartement;
    private String departementNom;

    public PointageDetailDTO() {
    }

    public PointageDetailDTO(VuePointageEmployeV2 entity) {
        this.idEmploye = entity.getIdEmploye();
        this.matricule = entity.getMatricule();
        this.nomComplet = entity.getNomComplet();
        this.datePointage = entity.getDatePointage();
        this.dureeHeureTravaillee = entity.getDureeHeureTravailleeMinute();
        this.dureeRetard = entity.getDureeRetardMinute();
        this.dureeHeureSup = entity.getDureeHeureSupplementaire();
        this.idDepartement = entity.getIdDepartement();
        this.departementNom = entity.getDepartementNom();
    }

    // Getters et Setters
    public String getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(String idEmploye) {
        this.idEmploye = idEmploye;
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

    public LocalDate getDatePointage() {
        return datePointage;
    }

    public void setDatePointage(LocalDate datePointage) {
        this.datePointage = datePointage;
    }

    public Integer getDureeHeureTravaillee() {
        return dureeHeureTravaillee;
    }

    public void setDureeHeureTravaillee(Integer dureeHeureTravaillee) {
        this.dureeHeureTravaillee = dureeHeureTravaillee;
    }

    public Integer getDureeRetard() {
        return dureeRetard;
    }

    public void setDureeRetard(Integer dureeRetard) {
        this.dureeRetard = dureeRetard;
    }

    public Integer getDureeHeureSup() {
        return dureeHeureSup;
    }

    public void setDureeHeureSup(Integer dureeHeureSup) {
        this.dureeHeureSup = dureeHeureSup;
    }

    public String getIdDepartement() {
        return idDepartement;
    }

    public void setIdDepartement(String idDepartement) {
        this.idDepartement = idDepartement;
    }

    public String getDepartementNom() {
        return departementNom;
    }

    public void setDepartementNom(String departementNom) {
        this.departementNom = departementNom;
    }

    // Méthodes utilitaires
    public Double getDureeHeureTravailleeEnHeures() {
        return dureeHeureTravaillee != null ? dureeHeureTravaillee / 60.0 : 0.0;
    }

    public Double getDureeRetardEnHeures() {
        return dureeRetard != null ? dureeRetard / 60.0 : 0.0;
    }

    public Double getDureeHeureSupEnHeures() {
        return dureeHeureSup != null ? dureeHeureSup / 60.0 : 0.0;
    }
}
