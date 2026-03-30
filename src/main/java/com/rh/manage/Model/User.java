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
    private Integer statut = 0;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UserRole> userRoles;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_employe", referencedColumnName = "id", nullable = false)
    private Employe employe;
    
    // Constructeurs
    public User() {
    }
    
    public User(String id, String email, String password, Employe employe, LocalDateTime createdAt, Integer statut) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.employe = employe;
        this.createdAt = createdAt;
        this.statut = statut;
    }
    
    // Getters et Setters
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
    
    public List<UserRole> getUserRoles() {
        return userRoles;
    }
    
    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
    
    public Employe getEmploye() {
        return employe;
    }
    
    public void setEmploye(Employe employe) {
        this.employe = employe;
    }
    
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.id == null) {
            this.id = "USR-" + UUID.randomUUID().toString();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", statut=" + statut +
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