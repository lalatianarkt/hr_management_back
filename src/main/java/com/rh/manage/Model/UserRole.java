package com.rh.manage.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_role")
@IdClass(UserRoleId.class)
public class UserRole {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "userRoles"})
    private User user;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "userRoles"})
    private TypeUser typeUser;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "statut")
    private Integer statut;

    public UserRole() {}

    public UserRole(User user, TypeUser typeUser) {
        this.user = user;
        this.typeUser = typeUser;
    }

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.modifiedAt == null) {
            this.modifiedAt = this.createdAt;
        }
        if (this.statut == null) {
            this.statut = 0; // 0 = actif, 1 = inactif
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

    // Getters et Setters
    public User getUser() { 
        return user; 
    }
    
    public void setUser(User user) { 
        this.user = user; 
    }

    public TypeUser getTypeUser() { 
        return typeUser; 
    }
    
    public void setTypeUser(TypeUser typeUser) { 
        this.typeUser = typeUser; 
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
}