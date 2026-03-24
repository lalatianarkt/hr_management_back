package com.rh.manage.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "competences")
public class Competence {
    
    @Id
    @Column(name = "id", length = 50)
    private String id;
    
    @Column(name = "nom", length = 255, nullable = false)
    private String nom;
    
    @Column(name = "description", length = 255)
    private String description;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categorie", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Categorie categorie;
    
    // Constructeurs
    public Competence() {}
    
    public Competence(String id, String nom, String description, Categorie categorie) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.categorie = categorie;
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
    
    public Categorie getCategorie() {
        return categorie;
    }
    
    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
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
            this.id = "COMP-" + datePart + "-" + 
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

    @Override
    public String toString() {
        return "Competence{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", categorie=" + (categorie != null ? categorie.getNom() : "null") +
                '}';
    }
}