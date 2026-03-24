package com.rh.manage.Dto;

import java.util.Map;

public class StatsAbsencesCongesDTO {
    private int totalAbsencesMois; // Absences non justifiées
    private int totalCongesPrisMois; // Congés pris
    private int totalCongesEnAttente; // Demandes en attente
    private double tauxAbsenteeisme; // (Absences / Effectif) * 100
    private double tauxUtilisationConges; // (Congés pris / Congés acquis) * 100
    private Map<String, Integer> absencesParDepartement;
    private Map<String, Integer> congesParDepartement;
    public int getTotalAbsencesMois() {
        return totalAbsencesMois;
    }
    public void setTotalAbsencesMois(int totalAbsencesMois) {
        this.totalAbsencesMois = totalAbsencesMois;
    }
    public int getTotalCongesPrisMois() {
        return totalCongesPrisMois;
    }
    public void setTotalCongesPrisMois(int totalCongesPrisMois) {
        this.totalCongesPrisMois = totalCongesPrisMois;
    }
    public int getTotalCongesEnAttente() {
        return totalCongesEnAttente;
    }
    public void setTotalCongesEnAttente(int totalCongesEnAttente) {
        this.totalCongesEnAttente = totalCongesEnAttente;
    }
    public double getTauxAbsenteeisme() {
        return tauxAbsenteeisme;
    }
    public void setTauxAbsenteeisme(double tauxAbsenteeisme) {
        this.tauxAbsenteeisme = tauxAbsenteeisme;
    }
    public double getTauxUtilisationConges() {
        return tauxUtilisationConges;
    }
    public void setTauxUtilisationConges(double tauxUtilisationConges) {
        this.tauxUtilisationConges = tauxUtilisationConges;
    }
    public Map<String, Integer> getAbsencesParDepartement() {
        return absencesParDepartement;
    }
    public void setAbsencesParDepartement(Map<String, Integer> absencesParDepartement) {
        this.absencesParDepartement = absencesParDepartement;
    }
    public Map<String, Integer> getCongesParDepartement() {
        return congesParDepartement;
    }
    public void setCongesParDepartement(Map<String, Integer> congesParDepartement) {
        this.congesParDepartement = congesParDepartement;
    }

    
}

