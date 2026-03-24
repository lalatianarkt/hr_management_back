package com.rh.manage.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "type_user")
public class TypeUser {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "type", length = 50, nullable = false, unique = true)
    private String type;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    
    // --- Constructeurs ---
    public TypeUser() {
        // Constructeur par défaut requis par JPA
    }

    public TypeUser(String type, LocalDateTime createdAt) {
        this.type = type;
        this.createdAt = createdAt;
    }

    // --- Getters et Setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    // --- Méthodes de cycle de vie ---
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

    // --- Méthodes utilitaires ---
    @Override
    public String toString() {
        return "TypeUser{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TypeUser)) return false;
        TypeUser typeUser = (TypeUser) o;
        return id == typeUser.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
