package com.rh.manage.Model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "mode_paiement")
public class ModePaiement {
    
    @Id
    @Column(name = "id", length = 50)
    private String id;
    
    @Column(name = "nom_banque", length = 255)
    private String nomBanque;
    
    @Column(name = "code_banque", length = 20)
    private String codeBanque;
    
    @Column(name = "code_guichet", length = 20)
    private String codeGuichet;
    
    @Column(name = "numero_compte", length = 50)
    private String numeroCompte;

    @Column(name = "telephone_mobile")
    private String telephoneMobile;
    
    @Column(name = "cle_rib", length = 2)
    private String cleRib;
    
    @Column(name = "titulaire_compte", nullable = false, length = 255)
    private String titulaireCompte;
    
    @Column(name = "domiciliation_agence", length = 255)
    private String domiciliationAgence;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    
    @Column(name = "est_actif")
    private Boolean estActif = true;
    
    @Column(name = "est_par_defaut")
    private Boolean estParDefaut = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_type_paiement", nullable = false)
    private TypePaiement typePaiement;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_employe", nullable = false)
    private Employe employe;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomBanque() {
        return nomBanque;
    }

    public void setNomBanque(String nomBanque) {
        this.nomBanque = nomBanque;
    }

    public String getCodeBanque() {
        return codeBanque;
    }

    public void setCodeBanque(String codeBanque) {
        this.codeBanque = codeBanque;
    }

    public String getCodeGuichet() {
        return codeGuichet;
    }

    public void setCodeGuichet(String codeGuichet) {
        this.codeGuichet = codeGuichet;
    }

    public String getNumeroCompte() {
        return numeroCompte;
    }

    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    public String getCleRib() {
        return cleRib;
    }

    public void setCleRib(String cleRib) {
        this.cleRib = cleRib;
    }

    public String getTitulaireCompte() {
        return titulaireCompte;
    }

    public void setTitulaireCompte(String titulaireCompte) {
        this.titulaireCompte = titulaireCompte;
    }

    public String getDomiciliationAgence() {
        return domiciliationAgence;
    }

    public void setDomiciliationAgence(String domiciliationAgence) {
        this.domiciliationAgence = domiciliationAgence;
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

    public Boolean getEstActif() {
        return estActif;
    }

    public void setEstActif(Boolean estActif) {
        this.estActif = estActif;
    }

    public Boolean getEstParDefaut() {
        return estParDefaut;
    }

    public void setEstParDefaut(Boolean estParDefaut) {
        this.estParDefaut = estParDefaut;
    }

    public TypePaiement getTypePaiement() {
        return typePaiement;
    }

    public void setTypePaiement(TypePaiement typePaiement) {
        this.typePaiement = typePaiement;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public String getTelephoneMobile() {
        return telephoneMobile;
    }

    public void setTelephoneMobile(String telephoneMobile) {
        this.telephoneMobile = telephoneMobile;
    }

    @PrePersist
    protected void onCreate() {
        if (id == null || id.trim().isEmpty()) {
            id = "MP" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
        }
        createdAt = LocalDateTime.now();
        if (estActif == null) estActif = true;
        if (estParDefaut == null) estParDefaut = false;
    }
    
    @PreUpdate
    protected void onUpdate() {
        modifiedAt = LocalDateTime.now();
    }
}
