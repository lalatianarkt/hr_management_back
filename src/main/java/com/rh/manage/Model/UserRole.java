package com.rh.manage.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.io.Serializable;

@Entity
@Table(name = "user_role")
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")  // Important: spécifie que ce champ correspond à la propriété userId dans UserRoleId
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "userRoles"})
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("typeUserId")  // Important: spécifie que ce champ correspond à la propriété typeUserId dans UserRoleId
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
        this.id = new UserRoleId(user.getId(), typeUser.getId());
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
            this.statut = 0;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

    // Getters et Setters
    public UserRoleId getId() { 
        return id; 
    }
    
    public void setId(UserRoleId id) { 
        this.id = id; 
    }

    public User getUser() { 
        return user; 
    }
    
    public void setUser(User user) { 
        this.user = user; 
        if (this.id == null && user != null && typeUser != null) {
            this.id = new UserRoleId(user.getId(), typeUser.getId());
        }
    }

    public TypeUser getTypeUser() { 
        return typeUser; 
    }
    
    public void setTypeUser(TypeUser typeUser) { 
        this.typeUser = typeUser; 
        if (this.id == null && user != null && typeUser != null) {
            this.id = new UserRoleId(user.getId(), typeUser.getId());
        }
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