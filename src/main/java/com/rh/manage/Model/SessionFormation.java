package com.rh.manage.Model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "session_formation")
public class SessionFormation {
    
    @Id
    @Column(name = "id", length = 50)
    private String id;
    
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;
    
    @Column(name = "date_fin", nullable = false, length = 50)
    private String dateFin;
    
    @Column(name = "lieu", length = 255)
    private String lieu;
    
    @Column(name = "statut", nullable = false)
    private Integer statut;
    
    @Column(name = "place_max")
    private Integer placeMax;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    // Relations ManyToOne (pour les jointures seulement)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_formation", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Formation formation;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_formateur", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Formateur formateur;
    
    // Constructeurs
    public SessionFormation() {
    }
    
    public SessionFormation(String id, LocalDate dateDebut, String dateFin, String lieu, 
                          Integer statut, Integer placeMax, LocalDateTime createdAt, 
                          LocalDateTime modifiedAt) {
        this.id = id;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.lieu = lieu;
        this.statut = statut;
        this.placeMax = placeMax;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
    
    // Getters et Setters
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
    
    public String getDateFin() {
        return dateFin;
    }
    
    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }
    
    public String getLieu() {
        return lieu;
    }
    
    public void setLieu(String lieu) {
        this.lieu = lieu;
    }
    
    public Integer getStatut() {
        return statut;
    }
    
    public void setStatut(Integer statut) {
        this.statut = statut;
    }
    
    public Integer getPlaceMax() {
        return placeMax;
    }
    
    public void setPlaceMax(Integer placeMax) {
        this.placeMax = placeMax;
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

    
    public Formation getFormation() {
        return formation;
    }
    
    public void setFormation(Formation formation) {
        this.formation = formation;
    }
    
    public Formateur getFormateur() {
        return formateur;
    }
    
    public void setFormateur(Formateur formateur) {
        this.formateur = formateur;
    }
    
    // Méthodes utilitaires pour le statut
    public boolean isPlanifiee() {
        return statut != null && statut == 1;
    }
    
    public boolean isEnCours() {
        return statut != null && statut == 2;
    }
    
    public boolean isTerminee() {
        return statut != null && statut == 3;
    }
    
    public boolean isAnnulee() {
        return statut != null && statut == 4;
    }

    // Méthodes de cycle de vie
    @PrePersist
    public void prePersist() {
        if (this.id == null || this.id.trim().isEmpty()) {
            String timestamp = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyMMddHHmmss"));
            this.id = "SESS-" + timestamp;
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        // modifiedAt reste null à la création
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now(); // Se remplit seulement à la modification
    }
    
    @Override
    public String toString() {
        return "SessionFormation{" +
                "id='" + id + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin='" + dateFin + '\'' +
                ", lieu='" + lieu + '\'' +
                ", statut=" + statut +
                ", placeMax=" + placeMax +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt;
    }
}