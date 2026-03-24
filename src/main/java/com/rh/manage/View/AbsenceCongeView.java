package com.rh.manage.View;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@IdClass(AbsenceCongeViewId.class)
@Table(name = "vue_absence_conge")
public class AbsenceCongeView {
    
    @Id
    @Column(name = "date_absence", nullable = false)
    private LocalDate dateAbsence;
    
    @Id
    @Column(name = "employe_id", nullable = false)
    private String employeId;
    
    @Column(name = "nom_complet", nullable = false)
    private String nomComplet;
    
    @Column(name = "matricule", nullable = false)
    private String matricule;
    
    @Column(name = "departement_nom")
    private String departementNom;
    
    @Column(name = "nom_poste")
    private String nomPoste;
    
    @Column(name = "id_departement")
    private String idDepartement;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type_absence", nullable = false)
    private TypeAbsence typeAbsence;

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



    public enum TypeAbsence {
        conge,
        absence
    }
}
