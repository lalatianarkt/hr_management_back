package com.rh.manage.Dto;

import java.util.List;

import com.rh.manage.Model.Employe;
import com.rh.manage.Model.InfosProfessionnelles;

public class EmployeInfosDTO {
    private Employe employe;
    private List<InfosProfessionnelles> infosProfessionnelles;
    private boolean isManager;
    
    // Constructeurs
    public EmployeInfosDTO() {}
    
    public EmployeInfosDTO(Employe employe, List<InfosProfessionnelles> infosProfessionnelles, Boolean isManager) {
        this.employe = employe;
        this.infosProfessionnelles = infosProfessionnelles;
        this.isManager = isManager;
    }
    
    // Getters et Setters
    public Employe getEmploye() { return employe; }
    public void setEmploye(Employe employe) { this.employe = employe; }
    
    public List<InfosProfessionnelles> getInfosProfessionnelles() { return infosProfessionnelles; }
    public void setInfosProfessionnelles(List<InfosProfessionnelles> infosProfessionnelles) { this.infosProfessionnelles = infosProfessionnelles; }
    public boolean isManager() {return isManager;}
    public void setManager(boolean isManager) {this.isManager = isManager;}
    
} 

