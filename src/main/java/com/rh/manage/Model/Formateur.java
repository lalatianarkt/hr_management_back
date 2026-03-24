package com.rh.manage.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "formateur")
public class Formateur {
    
    @Id
    @Column(name = "id", length = 50)
    private String id;
    
    @Column(name = "nom", nullable = false, length = 255)
    private String nom;
    
    @Column(name = "email", length = 255)
    private String email;
    
    @Column(name = "telephone", length = 20)
    private String telephone;
    
    @Column(name = "specialite", length = 255)
    private String specialite;
    
    @Column(name = "type")
    private Integer type;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    
    @Column(name = "id_employe", length = 50)
    private String idEmploye;
    
    // Constructeurs
    public Formateur() {
    }
    
    public Formateur(String id, String nom, String email, String telephone, 
                    String specialite, Integer type, LocalDateTime createdAt, 
                    LocalDateTime modifiedAt, String idEmploye) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
        this.specialite = specialite;
        this.type = type;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.idEmploye = idEmploye;
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public String getSpecialite() {
        return specialite;
    }
    
    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }
    
    public Integer getType() {
        return type;
    }
    
    public void setType(Integer type) {
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
    
    public String getIdEmploye() {
        return idEmploye;
    }
    
    public void setIdEmploye(String idEmploye) {
        this.idEmploye = idEmploye;
    }
    
    @Override
    public String toString() {
        return "Formateur{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", specialite='" + specialite + '\'' +
                ", type=" + type +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", idEmploye='" + idEmploye + '\'' +
                '}';
    }
}