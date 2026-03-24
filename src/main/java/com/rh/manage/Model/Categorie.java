package com.rh.manage.Model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "categorie")
public class Categorie {
    
    @Id
    @Column(name = "id", length = 50)
    private String id;
    
    @Column(name = "nom", length = 150, nullable = false)
    private String nom;
    
    @Column(name = "description", length = 250)
    private String description;
    
    @Column(name = "couleur", length = 7)
    private String couleur;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    
    // Constructeurs
    public Categorie() {}
    
    public Categorie(String id, String nom, String description, String couleur) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.couleur = couleur;
        this.createdAt = LocalDateTime.now();
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
    
    public String getCouleur() {
        return couleur;
    }
    
    public void setCouleur(String couleur) {
        this.couleur = couleur;
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
    
    // Méthode utilitaire pour mettre à jour la date de modification
    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "Categorie{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", couleur='" + couleur + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }
}