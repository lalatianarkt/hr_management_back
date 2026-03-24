package com.rh.manage.Dto;

import com.rh.manage.Model.Manager;

public class ManagerUpdateRequest {
    private Manager manager;
    private String nouveauDepartementId;

    public Manager getManager() {
        return manager;
    }
    public void setManager(Manager manager) {
        this.manager = manager;
    }
    public String getNouveauDepartementId() {
        return nouveauDepartementId;
    }
    public void setNouveauDepartementId(String nouveauDepartementId) {
        this.nouveauDepartementId = nouveauDepartementId;
    }
}
