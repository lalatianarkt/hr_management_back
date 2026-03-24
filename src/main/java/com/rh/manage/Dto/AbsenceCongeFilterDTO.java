package com.rh.manage.Dto;

import java.time.LocalDate;

import com.rh.manage.View.AbsenceCongeView.TypeAbsence;

public class AbsenceCongeFilterDTO {
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String employeId;
    private String departementId;
    private String matricule;
    private TypeAbsence typeAbsence;
    private String nomComplet;

    public AbsenceCongeFilterDTO(String employeId2, String departementId2, LocalDate dateDebut2, LocalDate dateFin2) {
        //TODO Auto-generated constructor stub
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
    public String getEmployeId() {
        return employeId;
    }
    public void setEmployeId(String employeId) {
        this.employeId = employeId;
    }
    public String getDepartementId() {
        return departementId;
    }
    public void setDepartementId(String departementId) {
        this.departementId = departementId;
    }
    public String getMatricule() {
        return matricule;
    }
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
    public TypeAbsence getTypeAbsence() {
        return typeAbsence;
    }
    public void setTypeAbsence(TypeAbsence typeAbsence) {
        this.typeAbsence = typeAbsence;
    }
    public String getNomComplet() {
        return nomComplet;
    }
    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    
}
