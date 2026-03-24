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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "demande_absence")
public class DemandeAbsence {
    
    @Id
    @Column(name = "id", length = 50)
    private String id;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    
    @Column(name = "statut")
    private Integer statut;
    
    @Column(name = "date_heure_absence_debut", nullable = false)
    private LocalDateTime dateHeureAbsenceDebut;
    
    @Column(name = "date_heure_absence_fin", nullable = false)
    private LocalDateTime dateHeureAbsenceFin;

    @Column(name = "commentaire")
    private String commentaire;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_employe", nullable = false)
    private Employe employe;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_type_demande", nullable = false)
    private TypeDemande typeDemande;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getStatut() {
        return statut;
    }

    public void setStatut(Integer statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateHeureAbsenceDebut() {
        return dateHeureAbsenceDebut;
    }

    public void setDateHeureAbsenceDebut(LocalDateTime dateHeureAbsenceDebut) {
        this.dateHeureAbsenceDebut = dateHeureAbsenceDebut;
    }

    public LocalDateTime getDateHeureAbsenceFin() {
        return dateHeureAbsenceFin;
    }

    public void setDateHeureAbsenceFin(LocalDateTime dateHeureAbsenceFin) {
        this.dateHeureAbsenceFin = dateHeureAbsenceFin;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public TypeDemande getTypeDemande() {
        return typeDemande;
    }

    public void setTypeDemande(TypeDemande typeDemande) {
        this.typeDemande = typeDemande;
    }

    // Méthodes utilitaires
    public boolean isEnAttente() {
        return statut != null && statut == 0;
    }
    
    public boolean isApprouvee() {
        return statut != null && statut == 1;
    }
    
    public boolean isRejetee() {
        return statut != null && statut == 2;
    }
    
    public long getDureeEnJours() {
        if (dateHeureAbsenceDebut == null || dateHeureAbsenceFin == null) {
            return 0;
        }
        return java.time.Duration.between(dateHeureAbsenceDebut, dateHeureAbsenceFin).toDays();
    }
    
    public long getDureeEnHeures() {
        if (dateHeureAbsenceDebut == null || dateHeureAbsenceFin == null) {
            return 0;
        }
        return java.time.Duration.between(dateHeureAbsenceDebut, dateHeureAbsenceFin).toHours();
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    @PrePersist
    public void prePersist() {
        if (this.id == null || this.id.isEmpty()) {
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String rand = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
            this.id = "DM-ABS-" + date + "-" + rand;
        }

        if (this.statut == null) {
            this.statut = 0; // en attente
        }

        if(this.createdAt == null) this.createdAt = LocalDateTime.now();
        if(this.modifiedAt == null) this.modifiedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }
}