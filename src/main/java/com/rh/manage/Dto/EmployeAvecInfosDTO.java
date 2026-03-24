package com.rh.manage.Dto;

import com.rh.manage.Model.Employe;
import com.rh.manage.Model.InfosProfessionnelles;

public class EmployeAvecInfosDTO {
    private Employe employe;
    private InfosProfessionnelles infosProfessionnelles;
    
    // Constructeurs
    public EmployeAvecInfosDTO() {}
    
    public EmployeAvecInfosDTO(Employe employe, InfosProfessionnelles infosProfessionnelles) {
        this.employe = employe;
        this.infosProfessionnelles = infosProfessionnelles;
    }
    
    // Getters/Setters
    public Employe getEmploye() {
        return employe;
    }
    
    public void setEmploye(Employe employe) {
        this.employe = employe;
    }
    
    public InfosProfessionnelles getInfosProfessionnelles() {
        return infosProfessionnelles;
    }
    
    public void setInfosProfessionnelles(InfosProfessionnelles infosProfessionnelles) {
        this.infosProfessionnelles = infosProfessionnelles;
    }
}
