package com.rh.manage.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "calendrier_ferie")
public class CalendrierFerie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "date_ferie", nullable = false)
    private LocalDate dateFerie;
    
    @Column(name = "libelle", length = 50)
    private String libelle;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    
    @Column(name = "est_actif")
    private Boolean estActif;
    
    // Constructeurs
    public CalendrierFerie() {}
    
    public CalendrierFerie(LocalDate dateFerie, String libelle, LocalDateTime createdAt, Boolean estActif) {
        this.dateFerie = dateFerie;
        this.libelle = libelle;
        this.createdAt = createdAt;
        this.estActif = estActif;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDate getDateFerie() {
        return dateFerie;
    }
    
    public void setDateFerie(LocalDate dateFerie) {
        this.dateFerie = dateFerie;
    }
    
    public String getLibelle() {
        return libelle;
    }
    
    public void setLibelle(String libelle) {
        this.libelle = libelle;
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
    
    public Boolean getEstActif() {
        return estActif;
    }
    
    public void setEstActif(Boolean estActif) {
        this.estActif = estActif;
    }
}
