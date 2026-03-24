package com.rh.manage.Dto;

import java.time.LocalDate;

public class ReportingPresenceDTO {
    private String idEmploye;
    private String matricule;
    private String nomComplet;
    private Integer totalHeureTravaillee;
    private Integer totalRetard;
    private Integer totalHeureSup;
    private String idDepartement;
    private String departementNom;
    private Integer nombreCongesTermines;
    private Integer totalJoursCongesTermines;
    private LocalDate dateDernierCongeTermine;
    private String moisDernierConge;
    
    // Champs calculés
    private Double totalHeureTravailleeEnHeures;
    private Double totalRetardEnHeures;
    private Double totalHeureSupEnHeures;
    private Double tempsEffectifEnHeures;
    private Double pourcentageRetard;
    private Double pourcentageHeureSup;
    private Double tauxPresence;
    private Double moyenneJoursParConge;
    private String frequenceConges;
    
    // Constructeurs
    public ReportingPresenceDTO() {}
    
    public ReportingPresenceDTO(String idEmploye, String matricule, String nomComplet,
                               Integer totalHeureTravaillee, Integer totalRetard, Integer totalHeureSup,
                               String idDepartement, String departementNom,
                               Integer nombreCongesTermines, Integer totalJoursCongesTermines,
                               LocalDate dateDernierCongeTermine, String moisDernierConge) {
        this.idEmploye = idEmploye;
        this.matricule = matricule;
        this.nomComplet = nomComplet;
        this.totalHeureTravaillee = totalHeureTravaillee;
        this.totalRetard = totalRetard;
        this.totalHeureSup = totalHeureSup;
        this.idDepartement = idDepartement;
        this.departementNom = departementNom;
        this.nombreCongesTermines = nombreCongesTermines;
        this.totalJoursCongesTermines = totalJoursCongesTermines;
        this.dateDernierCongeTermine = dateDernierCongeTermine;
        this.moisDernierConge = moisDernierConge;
    }

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

    public Integer getTotalHeureTravaillee() {
        return totalHeureTravaillee;
    }

    public void setTotalHeureTravaillee(Integer totalHeureTravaillee) {
        this.totalHeureTravaillee = totalHeureTravaillee;
    }

    public Integer getTotalRetard() {
        return totalRetard;
    }

    public void setTotalRetard(Integer totalRetard) {
        this.totalRetard = totalRetard;
    }

    public Integer getTotalHeureSup() {
        return totalHeureSup;
    }

    public void setTotalHeureSup(Integer totalHeureSup) {
        this.totalHeureSup = totalHeureSup;
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

    public Integer getNombreCongesTermines() {
        return nombreCongesTermines;
    }

    public void setNombreCongesTermines(Integer nombreCongesTermines) {
        this.nombreCongesTermines = nombreCongesTermines;
    }

    public Integer getTotalJoursCongesTermines() {
        return totalJoursCongesTermines;
    }

    public void setTotalJoursCongesTermines(Integer totalJoursCongesTermines) {
        this.totalJoursCongesTermines = totalJoursCongesTermines;
    }

    public LocalDate getDateDernierCongeTermine() {
        return dateDernierCongeTermine;
    }

    public void setDateDernierCongeTermine(LocalDate dateDernierCongeTermine) {
        this.dateDernierCongeTermine = dateDernierCongeTermine;
    }

    public String getMoisDernierConge() {
        return moisDernierConge;
    }

    public void setMoisDernierConge(String moisDernierConge) {
        this.moisDernierConge = moisDernierConge;
    }

    public Double getTotalHeureTravailleeEnHeures() {
        return totalHeureTravailleeEnHeures;
    }

    public void setTotalHeureTravailleeEnHeures(Double totalHeureTravailleeEnHeures) {
        this.totalHeureTravailleeEnHeures = totalHeureTravailleeEnHeures;
    }

    public Double getTotalRetardEnHeures() {
        return totalRetardEnHeures;
    }

    public void setTotalRetardEnHeures(Double totalRetardEnHeures) {
        this.totalRetardEnHeures = totalRetardEnHeures;
    }

    public Double getTotalHeureSupEnHeures() {
        return totalHeureSupEnHeures;
    }

    public void setTotalHeureSupEnHeures(Double totalHeureSupEnHeures) {
        this.totalHeureSupEnHeures = totalHeureSupEnHeures;
    }

    public Double getTempsEffectifEnHeures() {
        return tempsEffectifEnHeures;
    }

    public void setTempsEffectifEnHeures(Double tempsEffectifEnHeures) {
        this.tempsEffectifEnHeures = tempsEffectifEnHeures;
    }

    public Double getPourcentageRetard() {
        return pourcentageRetard;
    }

    public void setPourcentageRetard(Double pourcentageRetard) {
        this.pourcentageRetard = pourcentageRetard;
    }

    public Double getPourcentageHeureSup() {
        return pourcentageHeureSup;
    }

    public void setPourcentageHeureSup(Double pourcentageHeureSup) {
        this.pourcentageHeureSup = pourcentageHeureSup;
    }

    public Double getTauxPresence() {
        return tauxPresence;
    }

    public void setTauxPresence(Double tauxPresence) {
        this.tauxPresence = tauxPresence;
    }

    public Double getMoyenneJoursParConge() {
        return moyenneJoursParConge;
    }

    public void setMoyenneJoursParConge(Double moyenneJoursParConge) {
        this.moyenneJoursParConge = moyenneJoursParConge;
    }

    public String getFrequenceConges() {
        return frequenceConges;
    }

    public void setFrequenceConges(String frequenceConges) {
        this.frequenceConges = frequenceConges;
    }

    
}