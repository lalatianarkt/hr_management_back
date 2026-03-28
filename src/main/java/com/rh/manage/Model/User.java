package com.rh.manage.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id", length = 50)
    private String id;
    
    @Column(name = "email", length = 150, unique = true, nullable = false)
    private String email;
    
    @Column(name = "password", length = 150, nullable = false)
    private String password;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    
    @Column(name = "statut")
    private Integer statut = 0; // 0 = en attente, 1 = actif
    
    @Transient
    private TypeUser typeUser;

    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    private List<UserRole> userRoles;
    
    // 🔗 Relation One-to-One avec Employe
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_employe", referencedColumnName = "id", nullable = false, unique = true)
    private Employe employe;
    
    // --- Constructeurs ---
    public User() {
    }
    
    public User(String id, String email, String password, TypeUser typeUser, Employe employe, LocalDateTime createdAt, Integer statut) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.typeUser = typeUser;
        this.employe = employe;
        this.createdAt = createdAt;
        this.statut = statut;
    }

    // --- Getters et Setters ---
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public TypeUser getTypeUser() {
        if (typeUser != null) {
            return typeUser;
        }
        if (userRoles != null) {
            for (UserRole role : userRoles) {
                if (role != null && role.getTypeUser() != null) {
                    return role.getTypeUser();
                }
            }
        }
        return null;
    }

    public void setTypeUser(TypeUser typeUser) {
        this.typeUser = typeUser;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

     // --- Callbacks automatiques ---
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.id == null) {
            // Génération automatique d'un ID unique
            this.id = "USR-" + UUID.randomUUID().toString();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

    // --- Méthodes utilitaires ---
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", statut=" + statut +
                ", typeUser=" + (getTypeUser() != null ? getTypeUser().getType() : "null") +
                ", employe=" + (employe != null ? employe.getNom() : "null") +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
