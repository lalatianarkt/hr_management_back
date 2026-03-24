package com.rh.manage.Model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "base_irsa")
public class BaseIrsa {
    
    @Id
    @Column(name = "id", length = 50)
    private String id;
    
    @Column(name = "tranche_min", precision = 10, scale = 2)
    private BigDecimal trancheMin;
    
    @Column(name = "tranche_max", precision = 10, scale = 2)
    private BigDecimal trancheMax;
    
    @Column(name = "taux", precision = 10, scale = 2)
    private BigDecimal taux;
    
    @Column(name = "num_tranche")
    private Integer numTranche;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    
    // Constructeurs
    public BaseIrsa() {
        // Constructeur par défaut
    }
    
    public BaseIrsa(BigDecimal trancheMin, BigDecimal trancheMax, BigDecimal taux, Integer numTranche) {
        this.trancheMin = trancheMin;
        this.trancheMax = trancheMax;
        this.taux = taux;
        this.numTranche = numTranche;
    }
    
    // Méthodes de pré-persist et pré-update
    @PrePersist
    public void prePersist() {
        if (this.id == null || this.id.trim().isEmpty()) {
            this.id = "IRSA" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.modifiedAt == null) {
            this.modifiedAt = this.createdAt;
        }
    }
    
    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }
    
    // Getters et Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public BigDecimal getTrancheMin() {
        return trancheMin;
    }
    
    public void setTrancheMin(BigDecimal trancheMin) {
        this.trancheMin = trancheMin;
    }
    
    public BigDecimal getTrancheMax() {
        return trancheMax;
    }
    
    public void setTrancheMax(BigDecimal trancheMax) {
        this.trancheMax = trancheMax;
    }
    
    public BigDecimal getTaux() {
        return taux;
    }
    
    public void setTaux(BigDecimal taux) {
        this.taux = taux;
    }
    
    public Integer getNumTranche() {
        return numTranche;
    }
    
    public void setNumTranche(Integer numTranche) {
        this.numTranche = numTranche;
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
    
    // Méthode utilitaire pour vérifier si un montant est dans cette tranche
    public boolean estDansTranche(BigDecimal montant) {
        if (montant == null) return false;
        
        boolean superieurOuEgalMin = trancheMin == null || montant.compareTo(trancheMin) >= 0;
        boolean inferieurMax = trancheMax == null || montant.compareTo(trancheMax) < 0;
        
        return superieurOuEgalMin && inferieurMax;
    }
    
    // Calculer l'impôt pour un montant dans cette tranche
    public BigDecimal calculerImpotTranche(BigDecimal montant) {
        if (!estDansTranche(montant) || taux == null) {
            return BigDecimal.ZERO;
        }
        
        // Pour la dernière tranche (trancheMax = null)
        if (trancheMax == null) {
            BigDecimal baseImposable = montant.subtract(trancheMin != null ? trancheMin : BigDecimal.ZERO);
            return baseImposable.multiply(taux).divide(new BigDecimal("100"));
        }
        
        // Pour les tranches intermédiaires
        BigDecimal baseTranche = trancheMax.subtract(trancheMin != null ? trancheMin : BigDecimal.ZERO);
        return baseTranche.multiply(taux).divide(new BigDecimal("100"));
    }
    
    @Override
    public String toString() {
        return "BaseIrsa{" +
                "id='" + id + '\'' +
                ", trancheMin=" + trancheMin +
                ", trancheMax=" + trancheMax +
                ", taux=" + taux +
                ", numTranche=" + numTranche +
                '}';
    }
}
