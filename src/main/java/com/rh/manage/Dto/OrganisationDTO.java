package com.rh.manage.Dto;
import java.util.List;

import com.rh.manage.Model.Departement;
import com.rh.manage.Model.Employe;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Model.Manager;

public class OrganisationDTO {
    private Departement departement;
    private List<InfosProfessionnelles> infosProfessionnelles;
    private Manager manager;

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }
    
    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public List<InfosProfessionnelles> getInfosProfessionnelles() {
        return infosProfessionnelles;
    }

    public void setInfosProfessionnelles(List<InfosProfessionnelles> infosProfessionnelles) {
        this.infosProfessionnelles = infosProfessionnelles;
    }
    
    

    
}

