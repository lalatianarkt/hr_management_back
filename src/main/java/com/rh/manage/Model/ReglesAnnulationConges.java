package com.rh.manage.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "regles_annulation_conges")
public class ReglesAnnulationConges {

    @Id
    private String id; // UUID ou identifiant externe

    @Column(name = "delai_min_jours")
    private Integer delaiMinJours;

    @Column(name = "duree_max_jours")
    private Integer dureeMaxJours;

    @Column(name = "besoin_validation_manager")
    private Boolean besoinValidationManager;

    @Column(name = "besoin_validation_rh")
    private Boolean besoinValidationRH;

    @Column(name = "actif")
    private Boolean actif;

    // ======== Constructeurs ========
    public ReglesAnnulationConges() {}

    // ======== Getters et Setters ========
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Integer getDelaiMinJours() { return delaiMinJours; }
    public void setDelaiMinJours(Integer delaiMinJours) { this.delaiMinJours = delaiMinJours; }

    public Integer getDureeMaxJours() { return dureeMaxJours; }
    public void setDureeMaxJours(Integer dureeMaxJours) { this.dureeMaxJours = dureeMaxJours; }

    public Boolean getBesoinValidationManager() { return besoinValidationManager; }
    public void setBesoinValidationManager(Boolean besoinValidationManager) { this.besoinValidationManager = besoinValidationManager; }

    public Boolean getBesoinValidationRH() { return besoinValidationRH; }
    public void setBesoinValidationRH(Boolean besoinValidationRH) { this.besoinValidationRH = besoinValidationRH; }

    public Boolean getActif() { return actif; }
    public void setActif(Boolean actif) { this.actif = actif; }
}

