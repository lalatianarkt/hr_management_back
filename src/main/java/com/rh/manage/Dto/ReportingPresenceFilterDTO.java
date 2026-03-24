package com.rh.manage.Dto;

public class ReportingPresenceFilterDTO {
    private String departementId;
    private String nomComplet;
    private Integer minConges;
    private Integer maxConges;
    private String periode; // "mois_precedent", "mois_courant", "mois_n_2", "mois_n_3"
    private String triPar;
    private String ordreTri;
    
    // Constructeurs, getters et setters
    public ReportingPresenceFilterDTO() {}
    
    public String getDepartementId() {
        return departementId;
    }

    public void setDepartementId(String departementId) {
        this.departementId = departementId;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public Integer getMinConges() {
        return minConges;
    }

    public void setMinConges(Integer minConges) {
        this.minConges = minConges;
    }

    public Integer getMaxConges() {
        return maxConges;
    }

    public void setMaxConges(Integer maxConges) {
        this.maxConges = maxConges;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public String getTriPar() {
        return triPar;
    }

    public void setTriPar(String triPar) {
        this.triPar = triPar;
    }

    public String getOrdreTri() {
        return ordreTri;
    }

    public void setOrdreTri(String ordreTri) {
        this.ordreTri = ordreTri;
    }
    
    // Méthode utilitaire
    public boolean hasPeriode() {
        return periode != null && !periode.trim().isEmpty();
    }
}