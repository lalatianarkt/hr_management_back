package com.rh.manage.Dto;

import java.util.Map;

public class StatsEffectifDTO {
    private int totalEmployes;
    private int employesActifs;
    private Map<String, Integer> parDepartement; // Département → Nombre
    private Map<String, Integer> parPoste; // Poste → Nombre
    private int nouveauxEmployesMois; // Employés embauchés ce mois
    public int getTotalEmployes() {
        return totalEmployes;
    }
    public void setTotalEmployes(int totalEmployes) {
        this.totalEmployes = totalEmployes;
    }
    public int getEmployesActifs() {
        return employesActifs;
    }
    public void setEmployesActifs(int employesActifs) {
        this.employesActifs = employesActifs;
    }
    public Map<String, Integer> getParDepartement() {
        return parDepartement;
    }
    public void setParDepartement(Map<String, Integer> parDepartement) {
        this.parDepartement = parDepartement;
    }
    public Map<String, Integer> getParPoste() {
        return parPoste;
    }
    public void setParPoste(Map<String, Integer> parPoste) {
        this.parPoste = parPoste;
    }
    public int getNouveauxEmployesMois() {
        return nouveauxEmployesMois;
    }
    public void setNouveauxEmployesMois(int nouveauxEmployesMois) {
        this.nouveauxEmployesMois = nouveauxEmployesMois;
    }

    
}

