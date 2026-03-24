package com.rh.manage.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Query;
import jakarta.persistence.Table;

@Entity
@Table(name = "formule")
public class Formule {
    
    private static final String PREFIX = "FO-";
    private static int counter = 0;
    
    @Id
    @Column(name = "id", length = 50)
    private String id;
    
    @Column(name = "base", length = 50)
    private String base;
    
    @Column(name = "montant_fixe", precision = 10, scale = 2)
    private BigDecimal montantFixe;
    
    @Column(name = "nombre", precision = 10, scale = 2)
    private BigDecimal nombre;
    
    @Column(name = "taux", precision = 10, scale = 2)
    private BigDecimal taux;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    // Constructeurs
    public Formule() {}

    public Formule(String base, BigDecimal montantFixe, BigDecimal nombre, BigDecimal taux) {
        this.base = base;
        this.montantFixe = montantFixe;
        this.nombre = nombre;
        this.taux = taux;
    }
    
    @PrePersist
    protected void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        
        // if (this.id == null || this.id.isEmpty()) {
        //     // Le compteur sera géré par la base de données
        //     this.id = PREFIX + "0000"; // Valeur temporaire
        // }
    }

    @PreUpdate
    protected void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getBase() { return base; }
    public void setBase(String base) { this.base = base; }
    
    public BigDecimal getMontantFixe() { return montantFixe; }
    public void setMontantFixe(BigDecimal montantFixe) { this.montantFixe = montantFixe; }
    
    public BigDecimal getNombre() { return nombre; }
    public void setNombre(BigDecimal nombre) { this.nombre = nombre; }
    
    public BigDecimal getTaux() { return taux; }
    public void setTaux(BigDecimal taux) { this.taux = taux; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }
}