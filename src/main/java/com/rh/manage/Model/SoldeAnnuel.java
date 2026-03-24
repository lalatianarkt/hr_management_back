package com.rh.manage.Model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(name = "solde_annuel")
public class SoldeAnnuel {

    @Id
    private String id;  // UUID ou autre identifiant externe

    @Column(name = "nb_conge_total")
    private Double nbCongeTotal;

    @Column(name = "nb_conge_restant")
    private Double nbCongeRestant;

    @Column(name = "nb_conge_pris")
    private Double nbCongePris;

    @Column(nullable = false)
    private Integer annee;

    @Column(name = "date_cloture")
    private LocalDate dateCloture;

    @Column(name = "statut_cloture")
    private Integer statutCloture;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "id_employe")
    private String idEmploye;

    // ======== Constructeur ========
    public SoldeAnnuel() {
        this.createdAt = LocalDateTime.now();
    }

    // Méthode utilitaire pour mettre à jour la date de modification
    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }
    
    @PrePersist
    public void prePersist() {
        // Générer l'ID si null ou vide
        if (this.id == null || this.id.trim().isEmpty()) {
            // Format : SOL-YYMMDD-HHMM-XXX (max 20 caractères)
            String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyMMdd-HHmm"));
            
            // 3 chiffres aléatoires
            int random = new Random().nextInt(1000);
            String randomStr = String.format("%03d", random);
            
            this.id = "SOL-" + timestamp + "-" + randomStr;
            // Ex: SOL-251216-1031-527 → 20 caractères
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

    // ======== Getters et Setters ========
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Double getNbCongeTotal() { return nbCongeTotal; }
    public void setNbCongeTotal(Double nbCongeTotal) { this.nbCongeTotal = nbCongeTotal; }

    public Double getNbCongeRestant() { return nbCongeRestant; }
    public void setNbCongeRestant(Double nbCongeRestant) { this.nbCongeRestant = nbCongeRestant; }

    public Double getNbCongePris() { return nbCongePris; }
    public void setNbCongePris(Double nbCongePris) { this.nbCongePris = nbCongePris; }

    public Integer getAnnee() { return annee; }
    public void setAnnee(Integer annee) { this.annee = annee; }

    public LocalDate getDateCloture() { return dateCloture; }
    public void setDateCloture(LocalDate dateCloture) { this.dateCloture = dateCloture; }

    public Integer getStatutCloture() { return statutCloture; }
    public void setStatutCloture(Integer statutCloture) { this.statutCloture = statutCloture; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }

    public String getIdEmploye() { return idEmploye; }
    public void setIdEmploye(String idEmploye) { this.idEmploye = idEmploye; }
}

