package com.rh.manage.Dto;

import java.time.LocalDate;

public class StatistiquesAbsenceDTO {
    private Long totalAbsences;
    private Long totalConges;
    private Double tauxAbsence;
    private Double tauxConge;
    private String periode;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    
    public StatistiquesAbsenceDTO(Long totalAbsences, Long totalConges, Double tauxAbsence, 
                                  Double tauxConge, String periode, LocalDate dateDebut, LocalDate dateFin) {
        this.totalAbsences = totalAbsences;
        this.totalConges = totalConges;
        this.tauxAbsence = tauxAbsence;
        this.tauxConge = tauxConge;
        this.periode = periode;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }
    
    public Long getTotalAbsences() {
        return totalAbsences;
    }
    public void setTotalAbsences(Long totalAbsences) {
        this.totalAbsences = totalAbsences;
    }
    public Long getTotalConges() {
        return totalConges;
    }
    public void setTotalConges(Long totalConges) {
        this.totalConges = totalConges;
    }
    public Double getTauxAbsence() {
        return tauxAbsence;
    }
    public void setTauxAbsence(Double tauxAbsence) {
        this.tauxAbsence = tauxAbsence;
    }
    public Double getTauxConge() {
        return tauxConge;
    }
    public void setTauxConge(Double tauxConge) {//-
        this.tauxConge = tauxConge;
    }
    public String getPeriode() {
        return periode;
    }
    public void setPeriode(String periode) {
        this.periode = periode;
    }
    public LocalDate getDateDebut() {
        return dateDebut;
    }
    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }
    public LocalDate getDateFin() {
        return dateFin;
    }
    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    
}
