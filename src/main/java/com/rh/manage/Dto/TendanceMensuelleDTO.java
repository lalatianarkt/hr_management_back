package com.rh.manage.Dto;

public class TendanceMensuelleDTO {
    private String mois; // Format: "Jan-2024"
    private int effectif;
    private double tauxPresence;
    private double tauxAbsenteeisme;
    private double heuresTravaillees;
    private double congesPris;
    private double retards;
    private double variationEffectif; // % vs mois précédent
    private double variationPresence; // % vs mois précédent
    public String getMois() {
        return mois;
    }
    public void setMois(String mois) {
        this.mois = mois;
    }
    public int getEffectif() {
        return effectif;
    }
    public void setEffectif(int effectif) {
        this.effectif = effectif;
    }
    public double getTauxPresence() {
        return tauxPresence;
    }
    public void setTauxPresence(double tauxPresence) {
        this.tauxPresence = tauxPresence;
    }
    public double getTauxAbsenteeisme() {
        return tauxAbsenteeisme;
    }
    public void setTauxAbsenteeisme(double tauxAbsenteeisme) {
        this.tauxAbsenteeisme = tauxAbsenteeisme;
    }
    public double getHeuresTravaillees() {
        return heuresTravaillees;
    }
    public void setHeuresTravaillees(double heuresTravaillees) {
        this.heuresTravaillees = heuresTravaillees;
    }
    public double getCongesPris() {
        return congesPris;
    }
    public void setCongesPris(double congesPris) {
        this.congesPris = congesPris;
    }
    public double getRetards() {
        return retards;
    }
    public void setRetards(double retards) {
        this.retards = retards;
    }
    public double getVariationEffectif() {
        return variationEffectif;
    }
    public void setVariationEffectif(double variationEffectif) {
        this.variationEffectif = variationEffectif;
    }
    public double getVariationPresence() {
        return variationPresence;
    }
    public void setVariationPresence(double variationPresence) {
        this.variationPresence = variationPresence;
    }

    
}

