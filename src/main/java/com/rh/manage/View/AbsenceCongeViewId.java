package com.rh.manage.View;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class AbsenceCongeViewId implements Serializable {
    
    private LocalDate dateAbsence;
    private String employeId;
    
    // Constructeur par défaut (OBLIGATOIRE pour JPA)
    public AbsenceCongeViewId() {
    }
    
    // Constructeur avec paramètres
    public AbsenceCongeViewId(LocalDate dateAbsence, String employeId) {
        this.dateAbsence = dateAbsence;
        this.employeId = employeId;
    }
    
    // Getters et Setters
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
    
    // Méthodes equals et hashCode (OBLIGATOIRES pour JPA)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        AbsenceCongeViewId that = (AbsenceCongeViewId) o;
        
        if (!Objects.equals(dateAbsence, that.dateAbsence)) return false;
        return Objects.equals(employeId, that.employeId);
    }
    
    @Override
    public int hashCode() {
        int result = dateAbsence != null ? dateAbsence.hashCode() : 0;
        result = 31 * result + (employeId != null ? employeId.hashCode() : 0);
        return result;
    }
    
    // Méthode toString (optionnelle mais utile pour le débogage)
    @Override
    public String toString() {
        return "AbsenceCongeViewId{" +
                "dateAbsence=" + dateAbsence +
                ", employeId='" + employeId + '\'' +
                '}';
    }
}