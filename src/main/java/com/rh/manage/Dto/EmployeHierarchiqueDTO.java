package com.rh.manage.Dto;

public class EmployeHierarchiqueDTO {
    private String matricule;
    private String nomComplet;
    private String nomPoste;
    private String nomNiveau;
    private Integer rang;
    private String nomDepartement;
    private String nomManager;
    private Integer ecartHierarchique;
    
    // Getters, setters, constructeurs
    public EmployeHierarchiqueDTO() {}
    
    public EmployeHierarchiqueDTO(String matricule, String nomComplet, String nomPoste, 
                                  String nomNiveau, Integer rang, String nomDepartement, 
                                  String nomManager, Integer ecartHierarchique) {
        this.matricule = matricule;
        this.nomComplet = nomComplet;
        this.nomPoste = nomPoste;
        this.nomNiveau = nomNiveau;
        this.rang = rang;
        this.nomDepartement = nomDepartement;
        this.nomManager = nomManager;
        this.ecartHierarchique = ecartHierarchique;
    }
    
    // Getters et setters...
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }
    public String getNomComplet() { return nomComplet; }
    public void setNomComplet(String nomComplet) { this.nomComplet = nomComplet; }
    public String getNomPoste() { return nomPoste; }
    public void setNomPoste(String nomPoste) { this.nomPoste = nomPoste; }
    public String getNomNiveau() { return nomNiveau; }
    public void setNomNiveau(String nomNiveau) { this.nomNiveau = nomNiveau; }
    public Integer getRang() { return rang; }
    public void setRang(Integer rang) { this.rang = rang; }
    public String getNomDepartement() { return nomDepartement; }
    public void setNomDepartement(String nomDepartement) { this.nomDepartement = nomDepartement; }
    public String getNomManager() { return nomManager; }
    public void setNomManager(String nomManager) { this.nomManager = nomManager; }
    public Integer getEcartHierarchique() { return ecartHierarchique; }
    public void setEcartHierarchique(Integer ecartHierarchique) { this.ecartHierarchique = ecartHierarchique; }
}
