package com.rh.manage.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "manager")
public class Manager {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    // ✅ STATUT EN INTEGER (comme dans la base de données)
    @Column(name = "statut", nullable = false)
    private Integer statut = 0; // 0 par défaut

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "commentaire")
    private String commentaire;

    // === RELATION AVEC EMPLOYE ===
    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_employe", nullable = false)
    private Employe employe;

    @ManyToOne 
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_departement", nullable = false)
    private Departement departement;

    // === CONSTANTES POUR LES STATUTS ===
    public static class Statut {
        public static final int ACTIF = 0;              // Manager en fonction
        public static final int INACTIF = 1;            // Management terminé
        public static final int EN_COURS_NOMINATION = 2; // En attente de validation
        public static final int SUSPENDU = 3;           // Suspension temporaire
        
        // Méthode utilitaire pour obtenir le libellé
        public static String getLibelle(int statut) {
            switch (statut) {
                case ACTIF: return "Actif";
                case INACTIF: return "Inactif";
                case EN_COURS_NOMINATION: return "En cours de nomination";
                case SUSPENDU: return "Suspendu";
                default: return "Inconnu";
            }
        }
    }

    // === CONSTRUCTEURS ===

    public Manager() {
        // Constructeur par défaut pour JPA
    }

    public Manager(LocalDate dateDebut, Employe employe) {
        this.dateDebut = dateDebut;
        this.employe = employe;
        this.dateFin = null; // Management actuel par défaut
        this.statut = Statut.ACTIF; // ✅ Statut actif par défaut
    }

    public Manager(LocalDate dateDebut, LocalDate dateFin, Employe employe, Integer statut, Departement departement) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.employe = employe;
        this.statut = statut != null ? statut : Statut.ACTIF;
        departement = departement;
    }

    // === MÉTHODE PREPERSIST ===
    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = generateCustomId();
        }
        // ✅ Initialisation du statut si non défini
        if (this.statut == null) {
            this.statut = this.dateFin == null ? Statut.ACTIF : Statut.INACTIF;
        }
    }

    // === GÉNÉRATION D'IDENTIFIANT ===
    private String generateCustomId() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = String.valueOf((int) (Math.random() * 1000));
        return "MGR-" + timestamp + "-" + random;
    }

    // === GETTERS / SETTERS ===

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
        // ✅ Mise à jour automatique du statut
        if (dateFin != null && this.statut == Statut.ACTIF) {
            this.statut = Statut.INACTIF;
        }
    }

    // ✅ GETTER/SETTER POUR STATUT INTEGER
    public Integer getStatut() {
        return statut;
    }

    public void setStatut(Integer statut) {
        this.statut = statut;
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

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    // === MÉTHODES UTILES ===

    public boolean isActif() {
        return this.statut == Statut.ACTIF;
    }

    public String getStatutLibelle() {
        return Statut.getLibelle(this.statut);
    }

    public Long getDureeManagement() {
        if (this.dateDebut == null) {
            return 0L;
        }
        LocalDate fin = this.dateFin != null ? this.dateFin : LocalDate.now();
        return java.time.temporal.ChronoUnit.DAYS.between(this.dateDebut, fin);
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "id='" + id + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", statut=" + statut + " (" + getStatutLibelle() + ")" +
                ", employe=" + (employe != null ? employe.getNom() + " " + employe.getPrenom() : "null") +
                '}';
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    
}