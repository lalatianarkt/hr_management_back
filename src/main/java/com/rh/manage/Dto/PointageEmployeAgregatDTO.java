package com.rh.manage.Dto;

public class PointageEmployeAgregatDTO {
    private String idEmploye;
    private String matricule;
    private String nomComplet;
    private String idDepartement;
    private String departementNom;
    private Integer totalHeureTravaillee;
    private Integer totalRetard;
    private Integer totalHeureSup;

    public PointageEmployeAgregatDTO() {
    }

    public PointageEmployeAgregatDTO(String idEmploye, String matricule, String nomComplet,
                                     String idDepartement, String departementNom,
                                     Long totalHeureTravaillee, Long totalRetard, Long totalHeureSup) {
        this.idEmploye = idEmploye;
        this.matricule = matricule;
        this.nomComplet = nomComplet;
        this.idDepartement = idDepartement;
        this.departementNom = departementNom;
        this.totalHeureTravaillee = totalHeureTravaillee != null ? totalHeureTravaillee.intValue() : 0;
        this.totalRetard = totalRetard != null ? totalRetard.intValue() : 0;
        this.totalHeureSup = totalHeureSup != null ? totalHeureSup.intValue() : 0;
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

    // Méthodes utilitaires
    public Double getTotalHeureTravailleeEnHeures() {
        return totalHeureTravaillee != null ? totalHeureTravaillee / 60.0 : 0.0;
    }

    public Double getTotalRetardEnHeures() {
        return totalRetard != null ? totalRetard / 60.0 : 0.0;
    }

    public Double getTotalHeureSupEnHeures() {
        return totalHeureSup != null ? totalHeureSup / 60.0 : 0.0;
    }

    public Integer getTempsEffectif() {
        int tempsTravaille = totalHeureTravaillee != null ? totalHeureTravaillee : 0;
        int retard = totalRetard != null ? totalRetard : 0;
        return Math.max(0, tempsTravaille - retard);
    }

    public Double getTempsEffectifEnHeures() {
        return getTempsEffectif() / 60.0;
    }

    public Double getPourcentageRetard() {
        if (totalHeureTravaillee == null || totalHeureTravaillee == 0) {
            return 0.0;
        }
        int retard = totalRetard != null ? totalRetard : 0;
        return (retard * 100.0) / totalHeureTravaillee;
    }

    public Double getPourcentageHeureSup() {
        if (totalHeureTravaillee == null || totalHeureTravaillee == 0) {
            return 0.0;
        }
        int heureSup = totalHeureSup != null ? totalHeureSup : 0;
        return (heureSup * 100.0) / totalHeureTravaillee;
    }
}
