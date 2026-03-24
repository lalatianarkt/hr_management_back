package com.rh.manage.Dto;

public class EmployeeFilterDTO {
    String matricule;
    String nom;
    String prenom;
    String departementId;
    String typeContratId;
    Long statutId;
    private Boolean isManager;

     // Pagination
    private int page = 0;
    private int size = 10;
    private String sortBy = "nom";
    private String direction = "asc";
    
    public int getPage() {
        return page;
    }
    public void setPage(int page) {
        this.page = page;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public String getSortBy() {
        return sortBy;
    }
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
    public String getDirection() {
        return direction;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }
    public String getMatricule() {
        return matricule;
    }
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public String getDepartementId() {
        return departementId;
    }
    public void setDepartementId(String departementId) {
        this.departementId = departementId;
    }
    public String getTypeContratId() {
        return typeContratId;
    }
    public void setTypeContratId(String typeContratId) {
        this.typeContratId = typeContratId;
    }
    public Long getStatutId() {
        return statutId;
    }
    public void setStatutId(Long statutId) {
        this.statutId = statutId;
    }
    public Boolean getIsManager() {
        return isManager;
    }
    public void setIsManager(Boolean isManager) {
        this.isManager = isManager;
    }

    

    
}
