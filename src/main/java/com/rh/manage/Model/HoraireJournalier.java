package com.rh.manage.Model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.time.LocalDateTime;

@Entity
@Table(name = "horaire_journalier")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HoraireJournalier {
    
    @Id
    @Column(name = "id", length = 50)
    private String id;
    
    @Column(name = "heure_debut", nullable = false)
    private LocalTime heureDebut;
    
    @Column(name = "heure_fin", nullable = false)
    private LocalTime heureFin;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    
    @Column(name = "duree_pause_minutes")
    private Integer dureePauseMinutes;
    
    @Column(name = "duree_journaliere_attendue")
    private Integer dureeJournaliereAttendue;
    
    @Column(name = "statut")
    private Integer statut;
    
    @Column(name = "is_shift_jour")
    private Boolean isShiftJour;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jour", foreignKey = @ForeignKey(name = "fk_horaire_jour"))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private JourTravail jourTravail;
    
    // Constructeurs
    public HoraireJournalier() {
    }
    
    public HoraireJournalier(String id, LocalTime heureDebut, LocalTime heureFin) {
        this.id = id;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
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
            String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmm"));
            this.id = "HOR-" + datePart + "-" + 
                    Math.abs(new Random().nextInt(100));
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
    
    // Getters et Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public LocalTime getHeureDebut() {
        return heureDebut;
    }
    
    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }
    
    public LocalTime getHeureFin() {
        return heureFin;
    }
    
    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
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
    
    public Integer getDureePauseMinutes() {
        return dureePauseMinutes;
    }
    
    public void setDureePauseMinutes(Integer dureePauseMinutes) {
        this.dureePauseMinutes = dureePauseMinutes;
    }
    
    public Integer getDureeJournaliereAttendue() {
        return dureeJournaliereAttendue;
    }
    
    public void setDureeJournaliereAttendue(Integer dureeJournaliereAttendue) {
        this.dureeJournaliereAttendue = dureeJournaliereAttendue;
    }
    
    public Integer getStatut() {
        return statut;
    }
    
    public void setStatut(Integer statut) {
        this.statut = statut;
    }
    
    public Boolean getIsShiftJour() {
        return isShiftJour;
    }
    
    public void setIsShiftJour(Boolean isShiftJour) {
        this.isShiftJour = isShiftJour;
    }
    
    public JourTravail getJourTravail() {
        return jourTravail;
    }
    
    public void setJourTravail(JourTravail jourTravail) {
        this.jourTravail = jourTravail;
    }
    
    /**
     * Calcule la durée de présence en minutes (heure_fin - heure_debut)
     */
    public long calculerDureePresenceMinutes() {
        if (heureDebut == null || heureFin == null) {
            return 0;
        }
        return java.time.Duration.between(heureDebut, heureFin).toMinutes();
    }
    
    /**
     * Calcule la durée effective de travail en minutes (présence - pause)
     */
    public long calculerDureeTravailEffectiveMinutes() {
        long presence = calculerDureePresenceMinutes();
        Integer pause = dureePauseMinutes != null ? dureePauseMinutes : 0;
        return presence - pause;
    }
    
    /**
     * Calcule la durée de travail attendue en minutes
     */
    public long calculerDureeAttendueMinutes() {
        if (dureeJournaliereAttendue != null) {
            return dureeJournaliereAttendue;
        }
        return calculerDureePresenceMinutes();
    }
    
    /**
     * Vérifie si l'horaire est validé (statut = 1)
     */
    public boolean estValide() {
        return statut != null && statut == 1;
    }
    
    /**
     * Vérifie si l'horaire est actif (non supprimé/annulé)
     */
    public boolean estActif() {
        return statut == null || statut >= 0;
    }
    
    /**
     * Vérifie si c'est un shift de jour
     */
    public boolean estShiftJour() {
        return isShiftJour != null && isShiftJour;
    }
    
    /**
     * Calcule le retard en minutes si l'heure d'arrivée est après l'heure de début
     */
    public long calculerRetardMinutes(LocalTime heureArrivee) {
        if (heureDebut == null || heureArrivee == null) {
            return 0;
        }
        if (heureArrivee.isAfter(heureDebut)) {
            return java.time.Duration.between(heureDebut, heureArrivee).toMinutes();
        }
        return 0;
    }
    
    /**
     * Calcule les heures supplémentaires si l'heure de départ est après l'heure de fin
     */
    public long calculerHeuresSupplementairesMinutes(LocalTime heureDepart) {
        if (heureFin == null || heureDepart == null) {
            return 0;
        }
        if (heureDepart.isAfter(heureFin)) {
            return java.time.Duration.between(heureFin, heureDepart).toMinutes();
        }
        return 0;
    }
    
    /**
     * Vérifie si une heure donnée est dans la plage horaire
     */
    public boolean estDansPlageHoraire(LocalTime heure) {
        if (heureDebut == null || heureFin == null || heure == null) {
            return false;
        }
        return !heure.isBefore(heureDebut) && !heure.isAfter(heureFin);
    }
    
    @Override
    public String toString() {
        return "HoraireJournalier{" +
                "id='" + id + '\'' +
                ", heureDebut=" + heureDebut +
                ", heureFin=" + heureFin +
                ", dureePauseMinutes=" + dureePauseMinutes +
                ", dureeJournaliereAttendue=" + dureeJournaliereAttendue +
                ", statut=" + statut +
                ", isShiftJour=" + isShiftJour +
                ", jourTravail=" + (jourTravail != null ? jourTravail.getId() : "null") +
                '}';
    }
    
    // Méthode pour formater l'horaire en String
    public String getHoraireFormate() {
        if (heureDebut == null || heureFin == null) {
            return "Non défini";
        }
        return heureDebut + " - " + heureFin;
    }
}