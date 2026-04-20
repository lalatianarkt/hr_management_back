package com.rh.manage.Dto;

public class EvolutionRetardsDTO {
    private String mois;
    private long nombreRetards;
    private double tauxRetard;

    public EvolutionRetardsDTO() {
    }

    public EvolutionRetardsDTO(String mois, long nombreRetards, double tauxRetard) {
        this.mois = mois;
        this.nombreRetards = nombreRetards;
        this.tauxRetard = tauxRetard;
    }

    public String getMois() {
        return mois;
    }

    public void setMois(String mois) {
        this.mois = mois;
    }

    public long getNombreRetards() {
        return nombreRetards;
    }

    public void setNombreRetards(long nombreRetards) {
        this.nombreRetards = nombreRetards;
    }

    public double getTauxRetard() {
        return tauxRetard;
    }

    public void setTauxRetard(double tauxRetard) {
        this.tauxRetard = tauxRetard;
    }
}

