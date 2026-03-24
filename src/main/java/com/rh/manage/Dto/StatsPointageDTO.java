package com.rh.manage.Dto;

import java.util.Map;

public class StatsPointageDTO {
    private double totalHeuresTravaillees; // En heures
    private double totalHeuresSupplementaires;
    private double totalRetards; // En heures
    private double tempsEffectifMoyen; // Heures travaillées - retards
    private Map<String, Double> heuresParDepartement;
    private Map<String, Double> heuresSupParDepartement;
    public double getTotalHeuresTravaillees() {
        return totalHeuresTravaillees;
    }
    public void setTotalHeuresTravaillees(double totalHeuresTravaillees) {
        this.totalHeuresTravaillees = totalHeuresTravaillees;
    }
    public double getTotalHeuresSupplementaires() {
        return totalHeuresSupplementaires;
    }
    public void setTotalHeuresSupplementaires(double totalHeuresSupplementaires) {
        this.totalHeuresSupplementaires = totalHeuresSupplementaires;
    }
    public double getTotalRetards() {
        return totalRetards;
    }
    public void setTotalRetards(double totalRetards) {
        this.totalRetards = totalRetards;
    }
    public double getTempsEffectifMoyen() {
        return tempsEffectifMoyen;
    }
    public void setTempsEffectifMoyen(double tempsEffectifMoyen) {
        this.tempsEffectifMoyen = tempsEffectifMoyen;
    }
    public Map<String, Double> getHeuresParDepartement() {
        return heuresParDepartement;
    }
    public void setHeuresParDepartement(Map<String, Double> heuresParDepartement) {
        this.heuresParDepartement = heuresParDepartement;
    }
    public Map<String, Double> getHeuresSupParDepartement() {
        return heuresSupParDepartement;
    }
    public void setHeuresSupParDepartement(Map<String, Double> heuresSupParDepartement) {
        this.heuresSupParDepartement = heuresSupParDepartement;
    }

    
}
