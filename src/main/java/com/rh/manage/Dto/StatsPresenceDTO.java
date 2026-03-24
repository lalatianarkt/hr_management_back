package com.rh.manage.Dto;

import java.util.Map;

public class StatsPresenceDTO {
    private double tauxPresenceGlobal; // %
    private double tauxRetardGlobal; // %
    private int joursTravailMoyen; // Nombre moyen de jours travaillés/mois
    private double heuresTravailleesMoyennes; // Heures/mois/employé
    private Map<String, Double> tauxPresenceParDepartement;
    private Map<String, Double> tauxRetardParDepartement;
    public double getTauxPresenceGlobal() {
        return tauxPresenceGlobal;
    }
    public void setTauxPresenceGlobal(double tauxPresenceGlobal) {
        this.tauxPresenceGlobal = tauxPresenceGlobal;
    }
    public double getTauxRetardGlobal() {
        return tauxRetardGlobal;
    }
    public void setTauxRetardGlobal(double tauxRetardGlobal) {
        this.tauxRetardGlobal = tauxRetardGlobal;
    }
    public int getJoursTravailMoyen() {
        return joursTravailMoyen;
    }
    public void setJoursTravailMoyen(int joursTravailMoyen) {
        this.joursTravailMoyen = joursTravailMoyen;
    }
    public double getHeuresTravailleesMoyennes() {
        return heuresTravailleesMoyennes;
    }
    public void setHeuresTravailleesMoyennes(double heuresTravailleesMoyennes) {
        this.heuresTravailleesMoyennes = heuresTravailleesMoyennes;
    }
    public Map<String, Double> getTauxPresenceParDepartement() {
        return tauxPresenceParDepartement;
    }
    public void setTauxPresenceParDepartement(Map<String, Double> tauxPresenceParDepartement) {
        this.tauxPresenceParDepartement = tauxPresenceParDepartement;
    }
    public Map<String, Double> getTauxRetardParDepartement() {
        return tauxRetardParDepartement;
    }
    public void setTauxRetardParDepartement(Map<String, Double> tauxRetardParDepartement) {
        this.tauxRetardParDepartement = tauxRetardParDepartement;
    }

    
}


