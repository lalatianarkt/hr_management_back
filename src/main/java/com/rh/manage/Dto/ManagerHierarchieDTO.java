package com.rh.manage.Dto;

import java.util.List;

public class ManagerHierarchieDTO {
    private EmployeAvecInfosDTO manager;          // ✅ Le manager
    private List<EmployeAvecInfosDTO> employes;   // ✅ Ses employés directs
    
    // Constructeurs
    public ManagerHierarchieDTO() {}

    public EmployeAvecInfosDTO getManager() {
        return manager;
    }

    public void setManager(EmployeAvecInfosDTO manager) {
        this.manager = manager;
    }

    public List<EmployeAvecInfosDTO> getEmployes() {
        return employes;
    }

    public void setEmployes(List<EmployeAvecInfosDTO> employes) {
        this.employes = employes;
    }
    
    
    
    
}