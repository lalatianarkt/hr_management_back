package com.rh.manage.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Entity
@Table(name = "paie_fille")
public class PaieFille {

    private static final String PREFIX = "FO-";
    private static int counter = 0;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "paie_fille_seq")
    @SequenceGenerator(
        name = "paie_fille_seq",
        sequenceName = "paie_fille_id_seq",
        allocationSize = 1
    )
    Long id;
    
    @Column(name = "taux", precision = 10, scale = 2)
    private BigDecimal taux;
    
    @Column(name = "montant", precision = 10, scale = 2)
    private BigDecimal montant;
    
    @Column(name = "base", precision = 12, scale = 2)
    private BigDecimal base;
    
    @Column(name = "nombre", precision = 10, scale = 2)
    private BigDecimal nombre;
    
    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_paie", nullable = false, foreignKey = @ForeignKey(name = "FK_paie_fille_paie"))
    private Paie paie;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_rubrique", nullable = false, foreignKey = @ForeignKey(name = "FK_paie_fille_rubrique"))
    private RubriquePaie rubrique;

    @PrePersist
    protected void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        
        // if (this.id == null || this.id.isEmpty()) {
        //     // Format: FO-0012, FO-0013, etc.
        //     counter++; // En production, il faudrait un mécanisme thread-safe
        //     String formattedNumber = String.format("%04d", counter);
        //     this.id = formattedNumber;
        // }
    }

    @PostPersist
    @PostLoad
    private void generateFormattedId() {
        if (id != 0) {
            // Formater pour avoir exactement 4 chiffres, même pour 10000+
            // Exemples: 1 → "0001", 123 → "0123", 9999 → "9999", 10000 → "0000"
            // On utilise le modulo 10000 pour garder toujours 4 chiffres
            long formattedNumber = id % 10000;
            this.id = formattedNumber;
        }
    }

    @PreUpdate
    protected void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTaux() {
        return taux;
    }

    public void setTaux(BigDecimal taux) {
        this.taux = taux;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public BigDecimal getBase() {
        return base;
    }

    public void setBase(BigDecimal base) {
        this.base = base;
    }

    public BigDecimal getNombre() {
        return nombre;
    }

    public void setNombre(BigDecimal nombre) {
        this.nombre = nombre;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Paie getPaie() {
        return paie;
    }

    public void setPaie(Paie paie) {
        this.paie = paie;
    }

    public RubriquePaie getRubrique() {
        return rubrique;
    }

    public void setRubrique(RubriquePaie rubrique) {
        this.rubrique = rubrique;
    }

    
}
