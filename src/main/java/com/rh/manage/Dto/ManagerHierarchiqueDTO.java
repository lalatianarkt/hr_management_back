package com.rh.manage.Dto;

import java.util.ArrayList;
import java.util.List;

public class ManagerHierarchiqueDTO {
    private String nomComplet;
    private String matricule;
    private String nomPoste;
    private String nomNiveau;
    private Integer rang;
    private List<EmployeHierarchiqueDTO> subordonnes;
    private List<EmployeCompactDTO> subordonnesCompacts;
    private Integer nombreSubordonnes;
    
    // Constructeur par défaut
    public ManagerHierarchiqueDTO() {}
    
    // Constructeur UNIQUE avec tous les paramètres
    public ManagerHierarchiqueDTO(String nomComplet, String matricule, String nomPoste, 
                                String nomNiveau, Integer rang, 
                                List<EmployeHierarchiqueDTO> subordonnes,
                                List<EmployeCompactDTO> subordonnesCompacts) {
        this.nomComplet = nomComplet;
        this.matricule = matricule;
        this.nomPoste = nomPoste;
        this.nomNiveau = nomNiveau;
        this.rang = rang;
        this.subordonnes = subordonnes;
        this.subordonnesCompacts = subordonnesCompacts;
        this.nombreSubordonnes = 0;
        
        if (subordonnes != null) {
            this.nombreSubordonnes += subordonnes.size();
        }
        if (subordonnesCompacts != null) {
            this.nombreSubordonnes += subordonnesCompacts.size();
        }
    }
    
    // Méthodes statiques de création pour plus de clarté
    public static ManagerHierarchiqueDTO createWithSubordonnes(
            String nomComplet, String matricule, String nomPoste, 
            String nomNiveau, Integer rang, 
            List<EmployeHierarchiqueDTO> subordonnes) {
        ManagerHierarchiqueDTO dto = new ManagerHierarchiqueDTO();
        dto.nomComplet = nomComplet;
        dto.matricule = matricule;
        dto.nomPoste = nomPoste;
        dto.nomNiveau = nomNiveau;
        dto.rang = rang;
        dto.subordonnes = subordonnes;
        dto.nombreSubordonnes = subordonnes != null ? subordonnes.size() : 0;
        return dto;
    }
    
    public static ManagerHierarchiqueDTO createWithSubordonnesCompacts(
            String nomComplet, String matricule, String nomPoste, 
            String nomNiveau, Integer rang, 
            List<EmployeCompactDTO> subordonnesCompacts) {
        ManagerHierarchiqueDTO dto = new ManagerHierarchiqueDTO();
        dto.nomComplet = nomComplet;
        dto.matricule = matricule;
        dto.nomPoste = nomPoste;
        dto.nomNiveau = nomNiveau;
        dto.rang = rang;
        dto.subordonnesCompacts = subordonnesCompacts;
        dto.nombreSubordonnes = subordonnesCompacts != null ? subordonnesCompacts.size() : 0;
        return dto;
    }
    
    // Getters et setters
    public String getNomComplet() { return nomComplet; }
    public void setNomComplet(String nomComplet) { this.nomComplet = nomComplet; }
    
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }
    
    public String getNomPoste() { return nomPoste; }
    public void setNomPoste(String nomPoste) { this.nomPoste = nomPoste; }
    
    public String getNomNiveau() { return nomNiveau; }
    public void setNomNiveau(String nomNiveau) { this.nomNiveau = nomNiveau; }
    
    public Integer getRang() { return rang; }
    public void setRang(Integer rang) { this.rang = rang; }
    
    public List<EmployeHierarchiqueDTO> getSubordonnes() { return subordonnes; }
    public void setSubordonnes(List<EmployeHierarchiqueDTO> subordonnes) { 
        this.subordonnes = subordonnes; 
        updateNombreSubordonnes();
    }
    
    public List<EmployeCompactDTO> getSubordonnesCompacts() { return subordonnesCompacts; }
    public void setSubordonnesCompacts(List<EmployeCompactDTO> subordonnesCompacts) { 
        this.subordonnesCompacts = subordonnesCompacts;
        updateNombreSubordonnes();
    }
    
    public Integer getNombreSubordonnes() { 
        if (nombreSubordonnes == null) {
            updateNombreSubordonnes();
        }
        return nombreSubordonnes; 
    }
    
    public void setNombreSubordonnes(Integer nombreSubordonnes) { 
        this.nombreSubordonnes = nombreSubordonnes; 
    }
    
    private void updateNombreSubordonnes() {
        int total = 0;
        if (subordonnes != null) {
            total += subordonnes.size();
        }
        if (subordonnesCompacts != null) {
            total += subordonnesCompacts.size();
        }
        this.nombreSubordonnes = total;
    }
    
    // Méthode utilitaire pour obtenir les subordonnés dans le format approprié
    public List<?> getSubordonnesPourAffichage() {
        if (subordonnesCompacts != null && !subordonnesCompacts.isEmpty()) {
            return subordonnesCompacts;
        }
        return subordonnes != null ? subordonnes : new ArrayList<>();
    }
}