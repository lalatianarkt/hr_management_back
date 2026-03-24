package com.rh.manage.Model;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "reglement_horaire_interieur")
public class ReglementHoraireInterieur {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "heure_mat_entree")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime heureMatEntree;
    
    @Column(name = "heure_aprem_sortie")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime heureApremSortie;
    
    @Column(name = "heure_normale_journaliere", precision = 10, scale = 2)
    private BigDecimal heureNormaleJournaliere;
    
    @Column(name = "heure_normale_semaine")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime heureNormaleSemaine;
    
    @Column(name = "heure_normale_mois")
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime heureNormaleMois;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    
    @Column(name = "duree_normale_pause_minutes", precision = 10, scale = 2)
    private BigDecimal dureeNormalePauseMinutes;

    @Column(name = "statut")
    private int statut;
    
    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

    // Constructeurs
    public ReglementHoraireInterieur() {
        this.createdAt = LocalDateTime.now();
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getHeureMatEntree() {
        return heureMatEntree;
    }

    public void setHeureMatEntree(LocalTime heureMatEntree) {
        this.heureMatEntree = heureMatEntree;
    }

    public LocalTime getHeureApremSortie() {
        return heureApremSortie;
    }

    public void setHeureApremSortie(LocalTime heureApremSortie) {
        this.heureApremSortie = heureApremSortie;
    }

    public BigDecimal getHeureNormaleJournaliere() {
        return heureNormaleJournaliere;
    }

    public void setHeureNormaleJournaliere(BigDecimal heureNormaleJournaliere) {
        this.heureNormaleJournaliere = heureNormaleJournaliere;
    }

    public LocalTime getHeureNormaleSemaine() {
        return heureNormaleSemaine;
    }

    public void setHeureNormaleSemaine(LocalTime heureNormaleSemaine) {
        this.heureNormaleSemaine = heureNormaleSemaine;
    }

    public LocalTime getHeureNormaleMois() {
        return heureNormaleMois;
    }

    public void setHeureNormaleMois(LocalTime heureNormaleMois) {
        this.heureNormaleMois = heureNormaleMois;
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

    public BigDecimal getDureeNormalePauseMinutes() {
        return dureeNormalePauseMinutes;
    }

    public void setDureeNormalePauseMinutes(BigDecimal dureeNormalePauseMinutes) {
        this.dureeNormalePauseMinutes = dureeNormalePauseMinutes;
    }

    // Méthode de pré-persist
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        modifiedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "ReglementHoraireInterieur{" +
                "id=" + id +
                ", heureMatEntree=" + heureMatEntree +
                ", heureApremSortie=" + heureApremSortie +
                ", heureNormaleJournaliere=" + heureNormaleJournaliere +
                ", heureNormaleSemaine=" + heureNormaleSemaine +
                ", heureNormaleMois=" + heureNormaleMois +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", dureeNormalePauseMinutes=" + dureeNormalePauseMinutes +
                '}';
    }
}
