package com.rh.manage.Dto;

import java.time.LocalDate;

public class AlerteDTO {
    private String type; // "absenteeisme", "retard", "conge", "masse_salariale"
    private String titre;
    private String message;
    private String niveau; // "faible", "moyen", "eleve"
    private String actionRecommandee;
    private LocalDate dateDetection;
    public AlerteDTO(String string, String string2, String format, String string3) {
        //TODO Auto-generated constructor stub
    }
    public AlerteDTO() {
        //TODO Auto-generated constructor stub
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTitre() {
        return titre;
    }
    public void setTitre(String titre) {
        this.titre = titre;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getNiveau() {
        return niveau;
    }
    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }
    public String getActionRecommandee() {
        return actionRecommandee;
    }
    public void setActionRecommandee(String actionRecommandee) {
        this.actionRecommandee = actionRecommandee;
    }
    public LocalDate getDateDetection() {
        return dateDetection;
    }
    public void setDateDetection(LocalDate dateDetection) {
        this.dateDetection = dateDetection;
    }

    
}

