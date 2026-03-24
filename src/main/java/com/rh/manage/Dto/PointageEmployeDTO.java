package com.rh.manage.Dto;

import java.math.BigDecimal;

public class PointageEmployeDTO {
    private String idEmploye;
    private String matricule;
    private String nomComplet;
    private Integer totalHeureTravaillee; // minutes
    private Integer totalRetard; // minutes
    private Integer totalHeureSup; // minutes
    private Double totalHeureTravailleeHeures;
    private Double totalRetardHeures;
    private Double totalHeureSupHeures;
    private Integer tempsEffectif; // minutes
    private Double tempsEffectifHeures;
    private Double pourcentageRetard;
    private Double pourcentageHeureSup;
    
    // Constructeurs
    public PointageEmployeDTO() {}
    
    // Getters et Setters
    public String getIdEmploye() { return idEmploye; }
    public void setIdEmploye(String idEmploye) { this.idEmploye = idEmploye; }
    
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }
    
    public String getNomComplet() { return nomComplet; }
    public void setNomComplet(String nomComplet) { this.nomComplet = nomComplet; }
    
    public Integer getTotalHeureTravaillee() { return totalHeureTravaillee; }
    public void setTotalHeureTravaillee(Integer totalHeureTravaillee) { 
        this.totalHeureTravaillee = totalHeureTravaillee; 
        this.totalHeureTravailleeHeures = totalHeureTravaillee != null ? totalHeureTravaillee / 60.0 : 0.0;
    }
    
    public Integer getTotalRetard() { return totalRetard; }
    public void setTotalRetard(Integer totalRetard) { 
        this.totalRetard = totalRetard; 
        this.totalRetardHeures = totalRetard != null ? totalRetard / 60.0 : 0.0;
    }
    
    public Integer getTotalHeureSup() { return totalHeureSup; }
    public void setTotalHeureSup(Integer totalHeureSup) { 
        this.totalHeureSup = totalHeureSup; 
        this.totalHeureSupHeures = totalHeureSup != null ? totalHeureSup / 60.0 : 0.0;
    }
    
    public Double getTotalHeureTravailleeHeures() { return totalHeureTravailleeHeures; }
    public void setTotalHeureTravailleeHeures(Double totalHeureTravailleeHeures) { 
        this.totalHeureTravailleeHeures = totalHeureTravailleeHeures; 
    }
    
    public Double getTotalRetardHeures() { return totalRetardHeures; }
    public void setTotalRetardHeures(Double totalRetardHeures) { 
        this.totalRetardHeures = totalRetardHeures; 
    }
    
    public Double getTotalHeureSupHeures() { return totalHeureSupHeures; }
    public void setTotalHeureSupHeures(Double totalHeureSupHeures) { 
        this.totalHeureSupHeures = totalHeureSupHeures; 
    }
    
    public Integer getTempsEffectif() { 
        if (tempsEffectif == null) {
            int travaille = totalHeureTravaillee != null ? totalHeureTravaillee : 0;
            int retard = totalRetard != null ? totalRetard : 0;
            tempsEffectif = Math.max(0, travaille - retard);
        }
        return tempsEffectif; 
    }
    
    public void setTempsEffectif(Integer tempsEffectif) { this.tempsEffectif = tempsEffectif; }
    
    public Double getTempsEffectifHeures() { 
        if (tempsEffectifHeures == null) {
            tempsEffectifHeures = getTempsEffectif() / 60.0;
        }
        return tempsEffectifHeures; 
    }
    
    public void setTempsEffectifHeures(Double tempsEffectifHeures) { 
        this.tempsEffectifHeures = tempsEffectifHeures; 
    }
    
    public Double getPourcentageRetard() { 
        if (pourcentageRetard == null) {
            if (totalHeureTravaillee == null || totalHeureTravaillee == 0) {
                pourcentageRetard = 0.0;
            } else {
                int retard = totalRetard != null ? totalRetard : 0;
                pourcentageRetard = (retard * 100.0) / totalHeureTravaillee;
            }
        }
        return pourcentageRetard; 
    }
    
    public void setPourcentageRetard(Double pourcentageRetard) { 
        this.pourcentageRetard = pourcentageRetard; 
    }
    
    public Double getPourcentageHeureSup() { 
        if (pourcentageHeureSup == null) {
            if (totalHeureTravaillee == null || totalHeureTravaillee == 0) {
                pourcentageHeureSup = 0.0;
            } else {
                int heureSup = totalHeureSup != null ? totalHeureSup : 0;
                pourcentageHeureSup = (heureSup * 100.0) / totalHeureTravaillee;
            }
        }
        return pourcentageHeureSup; 
    }
    
    public void setPourcentageHeureSup(Double pourcentageHeureSup) { 
        this.pourcentageHeureSup = pourcentageHeureSup; 
    }
}
