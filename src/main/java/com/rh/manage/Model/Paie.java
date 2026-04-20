package com.rh.manage.Model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "paie")
public class Paie {
    
    @Id
    @Column(name = "id", length = 50)
    private String id;
    
    @Column(name = "mode_paiement", length = 150)
    private String modePaiement;
    
    @Column(name = "date_paiement")
    private LocalDate datePaiement;

    @Column(name = "date_cloture")
    private LocalDate dateCloture;
    
    @Column(name = "classification", length = 20)
    private String classification;
    
    @Column(name = "matricule")
    private Integer matricule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_periode", nullable = false, referencedColumnName = "id")
    private PeriodePaie periodePaie;
    
    @Column(name = "nom", length = 255)
    private String nom;
    
    @Column(name = "prenom", length = 255)
    private String prenom;
    
    @Column(name = "fonction", length = 150)
    private String fonction;
    
    @Column(name = "salaire_base", precision = 10, scale = 2)
    private BigDecimal salaireBase;
    
    @Column(name = "num_cnaps")
    private Integer numCnaps;
    
    @Column(name = "anciennete_an_mois_jour", length = 50)
    private String ancienneteAnMoisJour;
    
    @Column(name = "departement", length = 50)
    private String departement;
    
    @Column(name = "conges_pris", precision = 10, scale = 2)
    private BigDecimal congesPris;
    
    @Column(name = "solde_conges", precision = 10, scale = 2)
    private BigDecimal soldeConges;
    
    @Column(name = "statut_cloture")
    private Integer statutCloture;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_info_societe", nullable = false, referencedColumnName = "id")
    private InformationSociete informationSociete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_info_pro", referencedColumnName = "id")
    private InfosProfessionnelles infoPro;
    
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_employe", nullable = false, referencedColumnName = "id")
    private Employe employe;

    // Méthode utilitaire pour mettre à jour la date de modification
    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }
    
    @PrePersist
    public void prePersist() {
        // Générer l'ID si null ou vide
        if (this.id == null || this.id.trim().isEmpty()) {
            String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmssSSS"));
            String random = String.format("%03d", ThreadLocalRandom.current().nextInt(1000));
            this.id = "SM-P-" + datePart + "-" + random;
        }
        
        // Définir la date de création si null
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        
        // Pour les nouvelles entités, modified_at = created_at
        if (this.modifiedAt == null) {
            this.modifiedAt = this.createdAt;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }

    public LocalDate getDatePaiement() {
        return datePaiement;
    }

    public void setDatePaiement(LocalDate datePaiement) {
        this.datePaiement = datePaiement;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public Integer getMatricule() {
        return matricule;
    }

    public void setMatricule(Integer matricule) {
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

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public BigDecimal getSalaireBase() {
        return salaireBase;
    }

    public void setSalaireBase(BigDecimal salaireBase) {
        this.salaireBase = salaireBase;
    }

    public Integer getNumCnaps() {
        return numCnaps;
    }

    public void setNumCnaps(Integer numCnaps) {
        this.numCnaps = numCnaps;
    }

    public String getAncienneteAnMoisJour() {
        return ancienneteAnMoisJour;
    }

    public void setAncienneteAnMoisJour(String ancienneteAnMoisJour) {
        this.ancienneteAnMoisJour = ancienneteAnMoisJour;
    }

    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }

    public BigDecimal getCongesPris() {
        return congesPris;
    }

    public void setCongesPris(BigDecimal congesPris) {
        this.congesPris = congesPris;
    }

    public BigDecimal getSoldeConges() {
        return soldeConges;
    }

    public void setSoldeConges(BigDecimal soldeConges) {
        this.soldeConges = soldeConges;
    }

    public Integer getStatutCloture() {
        return statutCloture;
    }

    public void setStatutCloture(Integer statutCloture) {
        this.statutCloture = statutCloture;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public InformationSociete getInformationSociete() {
        return informationSociete;
    }

    public void setInformationSociete(InformationSociete informationSociete) {
        this.informationSociete = informationSociete;
    }

    public InfosProfessionnelles getInfoPro() {
        return infoPro;
    }

    public void setInfoPro(InfosProfessionnelles infoPro) {
        this.infoPro = infoPro;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public LocalDate getDateCloture() {
        return dateCloture;
    }

    public void setDateCloture(LocalDate dateCloture) {
        this.dateCloture = dateCloture;
    }

    public PeriodePaie getPeriodePaie() {
        return periodePaie;
    }

    public void setPeriodePaie(PeriodePaie periodePaie) {
        this.periodePaie = periodePaie;
    }
}
