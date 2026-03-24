package com.rh.manage.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "jour_travail")
public class JourTravail {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nom_jour", length = 50)
    private String nomJour;
    
    @Column(name = "code_jour")
    private Integer codeJour;
    
    @Column(name = "statut", nullable = false)
    private Integer statut;
    
    @Column(name = "est_weekend")
    private Integer estWeekend;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    
    // Constructeurs
    public JourTravail() {
        this.createdAt = LocalDateTime.now();
    }
    
    public JourTravail(String nomJour, Integer codeJour, Integer statut, Integer estWeekend) {
        this();
        this.nomJour = nomJour;
        this.codeJour = codeJour;
        this.statut = statut;
        this.estWeekend = estWeekend;
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNomJour() {
        return nomJour;
    }
    
    public void setNomJour(String nomJour) {
        this.nomJour = nomJour;
    }
    
    public Integer getCodeJour() {
        return codeJour;
    }
    
    public void setCodeJour(Integer codeJour) {
        this.codeJour = codeJour;
    }
    
    public Integer getStatut() {
        return statut;
    }
    
    public void setStatut(Integer statut) {
        this.statut = statut;
    }
    
    public Integer getEstWeekend() {
        return estWeekend;
    }
    
    public void setEstWeekend(Integer estWeekend) {
        this.estWeekend = estWeekend;
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
    
    // Méthode de pré-persist
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    
    // Méthode de pré-update
    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "JourTravail{" +
                "id=" + id +
                ", nomJour='" + nomJour + '\'' +
                ", codeJour=" + codeJour +
                ", statut=" + statut +
                ", estWeekend=" + estWeekend +
                '}';
    }
}
