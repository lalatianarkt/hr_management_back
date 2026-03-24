package com.rh.manage.Dto;

import java.util.List;

public class DepartementHierarchiqueDTO {
    private String nomDepartement;
    private List<ManagerHierarchiqueDTO> managers;
    
    // Getters, setters, constructeurs
    public DepartementHierarchiqueDTO() {}
    
    public DepartementHierarchiqueDTO(String nomDepartement, List<ManagerHierarchiqueDTO> managers) {
        this.nomDepartement = nomDepartement;
        this.managers = managers;
    }
    
    public String getNomDepartement() { return nomDepartement; }
    public void setNomDepartement(String nomDepartement) { this.nomDepartement = nomDepartement; }
    public List<ManagerHierarchiqueDTO> getManagers() { return managers; }
    public void setManagers(List<ManagerHierarchiqueDTO> managers) { this.managers = managers; }
}




