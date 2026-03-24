package com.rh.manage.Model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.rh.manage.Enum.ModeCalcul;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "rubrique_paie")
public class RubriquePaie {
    
    @Id
    @Column(name = "id", length = 50)
    private String id;
    
    @Column(name = "code", length = 20, nullable = false, unique = true)
    private String code;
    
    @Column(name = "libelle", length = 100, nullable = false)
    private String libelle;
    
    @Column(name = "plafond_mensuel", length = 50)
    private String plafondMensuel;
    
    @Column(name = "est_imposable")
    private Boolean estImposable = false;
    
    @Column(name = "est_soumis_cotisations")
    private Boolean estSoumisCotisations = false;

    @Column(name = "est_deductible_irsa")
    private Boolean estDeductibleIrsa = false;
    
    @Column(name = "compte_comptable")
    private Integer compteComptable;
    
    @Column(name = "ordre")
    private Integer ordre;
    
    @Column(name = "est_actif")
    private Boolean estActif = false;
    
    @Column(name = "plafond_annuel", precision = 10, scale = 2)
    private BigDecimal plafondAnnuel;
    
    @Column(name = "commentaire", columnDefinition = "TEXT")
    private String commentaire;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    
    // Relations avec les autres tables
    @ManyToOne
    @JoinColumn(name = "id_categorie", referencedColumnName = "id")
    private CategorieRub categorie;
    
    @ManyToOne
    @JoinColumn(name = "id_type", referencedColumnName = "id")
    private RubriqueType type;

    @ManyToOne
    @JoinColumn(name = "id_formule", referencedColumnName = "id")
    private Formule formule;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode_calcul", columnDefinition = "mode_calcul_enum")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private ModeCalcul modeCalcul;
    
    // Constructeurs
    public RubriquePaie() {
        // this.createdAt = LocalDateTime.now();
    }
    
    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
        
    public String getPlafondMensuel() { return plafondMensuel; }
    public void setPlafondMensuel(String plafondMensuel) { this.plafondMensuel = plafondMensuel; }
    
    public Boolean getEstImposable() { return estImposable; }
    public void setEstImposable(Boolean estImposable) { this.estImposable = estImposable; }
    
    public Boolean getEstSoumisCotisations() { return estSoumisCotisations; }
    public void setEstSoumisCotisations(Boolean estSoumisCotisations) { this.estSoumisCotisations = estSoumisCotisations; }
    
    public Integer getCompteComptable() { return compteComptable; }
    public void setCompteComptable(Integer compteComptable) { this.compteComptable = compteComptable; }
    
    public Integer getOrdre() { return ordre; }
    public void setOrdre(Integer ordre) { this.ordre = ordre; }
    
    public Boolean getEstActif() { return estActif; }
    public void setEstActif(Boolean estActif) { this.estActif = estActif; }
    
    public BigDecimal getPlafondAnnuel() { return plafondAnnuel; }
    public void setPlafondAnnuel(BigDecimal plafondAnnuel) { this.plafondAnnuel = plafondAnnuel; }
    
    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }
    
    public CategorieRub getCategorie() { return categorie; }
    public void setCategorie(CategorieRub categorie) { this.categorie = categorie; }
    
    public RubriqueType getType() { return type; }
    public void setType(RubriqueType type) { this.type = type; }

    // Méthode de pré-persist
    @PrePersist
    public void prePersist() {
        if (this.id == null || this.id.trim().isEmpty()) {
            // Générer un ID unique avec UUID
            String uuid = UUID.randomUUID().toString();
            this.id = "RP" + uuid.substring(0, 8).toUpperCase();
        }
        System.out.println("ato e++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
        if (this.modifiedAt == null) this.modifiedAt = this.createdAt;
    } 

    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }
    
    // Méthodes utilitaires
    public String getCategorieId() {
        return categorie != null ? categorie.getId() : null;
    }
    
    public String getTypeId() {
        return type != null ? type.getId() : null;
    }
    
    @Override
    public String toString() {
        return "RubriquePaie{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", libelle='" + libelle + '\'' +
                ", estActif=" + estActif +
                '}';
    }

    public Formule getFormule() {
        return formule;
    }

    public void setFormule(Formule formule) {
        this.formule = formule;
    }

    public ModeCalcul getModeCalcul() {
        return modeCalcul;
    }

    public void setModeCalcul(ModeCalcul modeCalcul) {
        this.modeCalcul = modeCalcul;
    }

    public Boolean getEstDeductibleIrsa() {
        return estDeductibleIrsa;
    }

    public void setEstDeductibleIrsa(Boolean estDeductibleIrsa) {
        this.estDeductibleIrsa = estDeductibleIrsa;
    }

    
}
