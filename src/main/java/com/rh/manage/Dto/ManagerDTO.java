package com.rh.manage.Dto;

import com.rh.manage.Model.Departement;
import com.rh.manage.Model.Manager;

public class ManagerDTO {
    private Manager manager;
    private Departement departement;
    
    public Manager getManager() {
        return manager;
    }
    public void setManager(Manager manager) {
        this.manager = manager;
    }
    public Departement getDepartement() {
        return departement;
    }
    public void setDepartement(Departement departement) {
        this.departement = departement;
    }
    
}
