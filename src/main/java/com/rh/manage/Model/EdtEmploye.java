package com.rh.manage.Model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Entity
@Table(name = "edt_employe")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class EdtEmploye {
    
    @Id
    @Column(name = "id", length = 50)
    private String id;
    
    @Column(name = "date_du_jour", nullable = false)
    private LocalDate dateDuJour;
    
    @Column(name = "heure_debut")
    private LocalTime heureDebut;
    
    @Column(name = "heure_fin")
    private LocalTime heureFin;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    
    @Column(name = "statut")
    private Integer statut;
    
    @Column(name = "is_shift_jour")
    private Boolean isShiftJour;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_horaire", foreignKey = @ForeignKey(name = "fk_edt_horaire"))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private HoraireJournalier horaire;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_employe", foreignKey = @ForeignKey(name = "fk_edt_employe"))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Employe employe;
    
    // Constructeurs
    public EdtEmploye() {
    }
    
    public EdtEmploye(String id, LocalDate dateDuJour, Employe employe) {
        this.id = id;
        this.dateDuJour = dateDuJour;
        this.employe = employe;
        this.createdAt = LocalDateTime.now();
    }
    
    public EdtEmploye(String id, LocalDate dateDuJour, LocalTime heureDebut, LocalTime heureFin, Employe employe) {
        this.id = id;
        this.dateDuJour = dateDuJour;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.employe = employe;
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
            String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
            this.id = "EDT-" + datePart + "-" + 
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
        
        // Définir les valeurs par défaut
        if (this.statut == null) {
            this.statut = 1; // Actif par défaut
        }
        
        if (this.isShiftJour == null) {
            this.isShiftJour = true; // Shift jour par défaut
        }
    }
    
    // Getters et Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public LocalDate getDateDuJour() {
        return dateDuJour;
    }
    
    public void setDateDuJour(LocalDate dateDuJour) {
        this.dateDuJour = dateDuJour;
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
    
    public HoraireJournalier getHoraire() {
        return horaire;
    }
    
    public void setHoraire(HoraireJournalier horaire) {
        this.horaire = horaire;
    }
    
    public Employe getEmploye() {
        return employe;
    }
    
    public void setEmploye(Employe employe) {
        this.employe = employe;
    }
    
    // Méthodes utilitaires
    public boolean estValide() {
        return statut != null && statut == 1;
    }
    
    public boolean estActif() {
        return statut == null || statut >= 0;
    }
    
    public boolean estShiftJour() {
        return isShiftJour != null && isShiftJour;
    }
    
    public long calculerDureeMinutes() {
        if (heureDebut == null || heureFin == null) {
            return 0;
        }
        return java.time.Duration.between(heureDebut, heureFin).toMinutes();
    }
    
    public boolean estDansPlageHoraire(LocalTime heure) {
        if (heureDebut == null || heureFin == null || heure == null) {
            return false;
        }
        return !heure.isBefore(heureDebut) && !heure.isAfter(heureFin);
    }
    
    @Override
    public String toString() {
        return "EdtEmploye{" +
                "id='" + id + '\'' +
                ", dateDuJour=" + dateDuJour +
                ", heureDebut=" + heureDebut +
                ", heureFin=" + heureFin +
                ", statut=" + statut +
                ", isShiftJour=" + isShiftJour +
                ", horaire=" + (horaire != null ? horaire.getId() : "null") +
                ", employe=" + (employe != null ? employe.getId() : "null") +
                '}';
    }
}