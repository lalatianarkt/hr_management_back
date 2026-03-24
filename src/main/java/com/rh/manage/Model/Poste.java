package com.rh.manage.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "poste")
public class Poste {
    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "nom", length = 100, nullable = false)
    private String nom;

    @Column(name = "description", length = 255)
    private String description;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @ManyToOne
    @JoinColumn(name = "id_departement")
    private Departement departement;

    @ManyToOne
    @JoinColumn(name = "id_niveau")
    private NiveauHierarchique niveauHierarchique;

    // Constructeurs
    public Poste() {}

    public Poste(String id, String nom, String description, Departement departement) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.departement = departement;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public NiveauHierarchique getNiveauHierarchique() {
        return niveauHierarchique;
    }

    public void setNiveauHierarchique(NiveauHierarchique niveauHierarchique) {
        this.niveauHierarchique = niveauHierarchique;
    }

    
}