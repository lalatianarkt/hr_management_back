package com.rh.manage.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "formation")
public class Formation {
    
    @Id
    @Column(name = "id", length = 50)
    private String id;
    
    @Column(name = "nom", length = 150, nullable = false)
    private String nom;
    
    @Column(name = "description", length = 255)
    private String description;
    
    @Column(name = "duree")
    private Integer duree; // Durée en heures
    
    @Column(name = "cout", precision = 10, scale = 2)
    private BigDecimal cout; // Coût de la formation
    
    @Column(name = "prerequis", length = 255)
    private String prerequis;
    
    @Column(name = "objectifs", length = 255)
    private String objectifs;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    
    // Constructeurs
    public Formation() {}
    
    public Formation(String nom, String description, Integer duree, BigDecimal cout) {
        this.nom = nom;
        this.description = description;
        this.duree = duree;
        this.cout = cout;
    }
    
    // Getters et Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getDuree() {
        return duree;
    }
    
    public void setDuree(Integer duree) {
        this.duree = duree;
    }
    
    public BigDecimal getCout() {
        return cout;
    }
    
    public void setCout(BigDecimal cout) {
        this.cout = cout;
    }
    
    public String getPrerequis() {
        return prerequis;
    }
    
    public void setPrerequis(String prerequis) {
        this.prerequis = prerequis;
    }
    
    public String getObjectifs() {
        return objectifs;
    }
    
    public void setObjectifs(String objectifs) {
        this.objectifs = objectifs;
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
    
    // Méthodes de cycle de vie
    @PrePersist
    public void prePersist() {
        if (this.id == null || this.id.trim().isEmpty()) {
            String timestamp = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyMMddHHmmss"));
            this.id = "FORM-" + timestamp;
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
        return "Formation{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", duree=" + duree +
                ", cout=" + cout +
                ", prerequis='" + prerequis + '\'' +
                ", objectifs='" + objectifs + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }
}