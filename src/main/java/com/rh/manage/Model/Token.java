package com.rh.manage.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "token_genere", length = 255, nullable = false)
    private String tokenGenere;

    @Column(name = "type", length = 150)
    private String type;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "is_active")
    private Integer isActive;

    // Relation avec la table Users
    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    // --- Constructeurs ---
    public Token() {}

    public Token(String tokenGenere, String type, LocalDateTime createdAt, LocalDateTime expiresAt, Integer isActive, User user) {
        this.tokenGenere = tokenGenere;
        this.type = type;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.isActive = isActive;
        this.user = user;
    }

    // --- Getters et Setters ---
    public int getId() {
        return id;
    }

    public String getTokenGenere() {
        return tokenGenere;
    }

    public void setTokenGenere(String tokenGenere) {
        this.tokenGenere = tokenGenere;
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

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
