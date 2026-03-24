package com.rh.manage.Model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "competence_employe")
public class CompetenceEmploye {
    
    @Id
    @Column(name = "id", length = 50)
    private String id;
    
    @Column(name = "niveau", nullable = false)
    private Integer niveau;
    
    @Column(name = "date_acquisition", nullable = false)
    private LocalDate dateAcquisition;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_competence", referencedColumnName = "id")
    private Competence competence;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_employe", referencedColumnName = "id")
    private Employe employe;
    
    // Constructeurs
    public CompetenceEmploye() {}
    
    public CompetenceEmploye(String id, Integer niveau, LocalDate dateAcquisition, Competence competence, Employe employe) {
        this.id = id;
        this.niveau = niveau;
        this.dateAcquisition = dateAcquisition;
        this.competence = competence;
        this.employe = employe;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters et Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public Integer getNiveau() {
        return niveau;
    }
    
    public void setNiveau(Integer niveau) {
        this.niveau = niveau;
    }
    
    public LocalDate getDateAcquisition() {
        return dateAcquisition;
    }
    
    public void setDateAcquisition(LocalDate dateAcquisition) {
        this.dateAcquisition = dateAcquisition;
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
    
    public Competence getCompetence() {
        return competence;
    }
    
    public void setCompetence(Competence competence) {
        this.competence = competence;
    }
    
    public Employe getEmploye() {
        return employe;
    }
    
    public void setEmploye(Employe employe) {
        this.employe = employe;
    }
    
    // Méthode utilitaire pour mettre à jour la date de modification
    // @PreUpdate
    // public void preUpdate() {
    //     this.modifiedAt = LocalDateTime.now();
    // } 

    @PrePersist
    public void prePersist() {
        // Générer l'ID si null ou vide
        if (this.id == null || this.id.trim().isEmpty()) {
            String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmm"));
            this.id = "COMPEMP-" + datePart + "-" + 
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
    
    // Méthode utilitaire pour valider le niveau
    public boolean isNiveauValide() {
        return niveau != null && niveau >= 1 && niveau <= 5;
    }
    
    @Override
    public String toString() {
        return "CompetenceEmploye{" +
                "id='" + id + '\'' +
                ", niveau=" + niveau +
                ", dateAcquisition=" + dateAcquisition +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", competence=" + (competence != null ? competence.getNom() : "null") +
                ", employe=" + (employe != null ? employe.getNom() + " " + employe.getPrenom() : "null") +
                '}';
    }
}