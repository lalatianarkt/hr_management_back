package com.rh.manage.Model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "categorie_rub")
public class CategorieRub {
    
    @Id
    @Column(name = "id", length = 50)
    private String id;
    
    @Column(name = "libelle", length = 150, nullable = false)
    private String libelle;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    
    // Constructeurs
    public CategorieRub() {
        this.createdAt = LocalDateTime.now();
    }
    
    public CategorieRub(String id, String libelle) {
        this.id = id;
        this.libelle = libelle;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }
    
    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }
    
    // Méthode de pré-persist
    @PrePersist
    @PreUpdate
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        this.modifiedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "CategorieRub{" +
                "id='" + id + '\'' +
                ", libelle='" + libelle + '\'' +
                '}';
    }
}
