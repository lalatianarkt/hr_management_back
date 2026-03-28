package com.rh.manage.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "regle_gestion_conges")
public class RegleGestionConges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "anciennete_requis", nullable = false)
    private Integer ancienneteRequis;

    @Column(name = "solde_mensuel", nullable = false)
    private Double soldeMensuel;

    @Column(name = "is_week_end_inclus")
    private Boolean isWeekEndInclus;

    @Column(name = "limite_report_annuel")
    private Integer limiteReportAnnuel;

    @Column(name = "duree_annee_report")
    private Integer dureeAnneeReport;

    @Column(name = "statut", nullable = false)
    private Integer statut;

    @Column(name = "prime_anciennete")
    private double primeAnciennete;

    @Column(name = "anciennete_requis_prime")
    private double ancienneteRequisPrime;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "allocation_familiale")
    private double allocationFamiliale;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
        this.statut = 0; // initialisation du statut à 0 lors de l’insertion
    }

    @PreUpdate
    public void onUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getAncienneteRequis() { return ancienneteRequis; }
    public void setAncienneteRequis(Integer ancienneteRequis) { this.ancienneteRequis = ancienneteRequis; }

    public Double getSoldeMensuel() { return soldeMensuel; }
    public void setSoldeMensuel(Double soldeMensuel) { this.soldeMensuel = soldeMensuel; }

    public Boolean getWeekEndInclus() { return isWeekEndInclus; }
    public void setWeekEndInclus(Boolean weekEndInclus) { isWeekEndInclus = weekEndInclus; }

    public Integer getLimiteReportAnnuel() { return limiteReportAnnuel; }
    public void setLimiteReportAnnuel(Integer limiteReportAnnuel) { this.limiteReportAnnuel = limiteReportAnnuel; }

    public Integer getStatut() { return statut; }
    public void setStatut(Integer statut) { this.statut = statut; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }

    public Boolean getIsWeekEndInclus() {
        return isWeekEndInclus;
    }

    public void setIsWeekEndInclus(Boolean isWeekEndInclus) {
        this.isWeekEndInclus = isWeekEndInclus;
    }

    public double getPrimeAnciennete() {
        return primeAnciennete;
    }

    public void setPrimeAnciennete(double primeAnciennete) {
        this.primeAnciennete = primeAnciennete;
    }

    public double getAncienneteRequisPrime() {
        return ancienneteRequisPrime;
    }

    public void setAncienneteRequisPrime(double ancienneteRequisPrime) {
        this.ancienneteRequisPrime = ancienneteRequisPrime;
    }

    public double getAllocationFamiliale() {
        return allocationFamiliale;
    }

    public void setAllocationFamiliale(double allocationFamiliale) {
        this.allocationFamiliale = allocationFamiliale;
    }

    public Integer getDureeAnneeReport() {
        return dureeAnneeReport;
    }

    public void setDureeAnneeReport(Integer dureeAnneeReport) {
        this.dureeAnneeReport = dureeAnneeReport;
    }
    
}




