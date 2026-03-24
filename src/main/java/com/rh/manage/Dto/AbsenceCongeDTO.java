package com.rh.manage.Dto;

import java.time.LocalDate;

import com.rh.manage.View.AbsenceCongeView.TypeAbsence;

public class AbsenceCongeDTO {
    public AbsenceCongeDTO(Long id2, LocalDate dateAbsence2, String employeId2, String nomComplet2, String matricule2,
            String departementNom2, String nomPoste2, String idDepartement2, TypeAbsence typeAbsence2) {
    }

    public AbsenceCongeDTO(LocalDate dateAbsence2, String employeId2, String nomComplet2, String matricule2,
            String departementNom2, String nomPoste2, String idDepartement2, TypeAbsence typeAbsence2) {
        //TODO Auto-generated constructor stub
    }

    private Long id;
    private LocalDate dateAbsence;
    private String employeId;
    private String nomComplet;
    private String matricule;
    private String departementNom;
    private String nomPoste;
    private String idDepartement;
    private TypeAbsence typeAbsence;
    
    // Statistiques additionnelles
    private Long totalAbsences;
    private Long totalConges;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public LocalDate getDateAbsence() {
        return dateAbsence;
    }
    public void setDateAbsence(LocalDate dateAbsence) {
        this.dateAbsence = dateAbsence;
    }
    public String getEmployeId() {
        return employeId;
    }
    public void setEmployeId(String employeId) {
        this.employeId = employeId;
    }
    public String getNomComplet() {
        return nomComplet;
    }
    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }
    public String getMatricule() {
        return matricule;
    }
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
    public String getDepartementNom() {
        return departementNom;
    }
    public void setDepartementNom(String departementNom) {
        this.departementNom = departementNom;
    }
    public String getNomPoste() {
        return nomPoste;
    }
    public void setNomPoste(String nomPoste) {
        this.nomPoste = nomPoste;
    }
    public String getIdDepartement() {
        return idDepartement;
    }
    public void setIdDepartement(String idDepartement) {
        this.idDepartement = idDepartement;
    }
    public TypeAbsence getTypeAbsence() {
        return typeAbsence;
    }
    public void setTypeAbsence(TypeAbsence typeAbsence) {
        this.typeAbsence = typeAbsence;
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

    
}




