package com.rh.manage.Dto;

public class StatCongeDTO {
    Long totalDemandes;
    Long demandesEnAttente;
    Long demandesApprouvees;
    Long demandesRefusees;
    Long demandesAnnulees;
    Double tauxApprobation;
    Double joursMoyens;
    String moisPlusActif;

    public Long getTotalDemandes() {
        return totalDemandes;
    }
    public void setTotalDemandes(Long totalDemandes) {
        this.totalDemandes = totalDemandes;
    }
    public Long getDemandesEnAttente() {
        return demandesEnAttente;
    }
    public void setDemandesEnAttente(Long demandesEnAttente) {
        this.demandesEnAttente = demandesEnAttente;
    }
    public Long getDemandesApprouvees() {
        return demandesApprouvees;
    }
    public void setDemandesApprouvees(Long demandesApprouvees) {
        this.demandesApprouvees = demandesApprouvees;
    }
    public Long getDemandesRefusees() {
        return demandesRefusees;
    }
    public void setDemandesRefusees(Long demandesRefusees) {
        this.demandesRefusees = demandesRefusees;
    }
    public Long getDemandesAnnulees() {
        return demandesAnnulees;
    }
    public void setDemandesAnnulees(Long demandesAnnulees) {
        this.demandesAnnulees = demandesAnnulees;
    }
    public Double getTauxApprobation() {
        return tauxApprobation;
    }
    public void setTauxApprobation(Double tauxApprobation) {
        this.tauxApprobation = tauxApprobation;
    }
    public Double getJoursMoyens() {
        return joursMoyens;
    }
    public void setJoursMoyens(Double joursMoyens) {
        this.joursMoyens = joursMoyens;
    }
    public String getMoisPlusActif() {
        return moisPlusActif;
    }
    public void setMoisPlusActif(String moisPlusActif) {
        this.moisPlusActif = moisPlusActif;
    }

    
}
