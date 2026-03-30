package com.rh.manage.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "TEXT")
    private String message;
    
    @Column(name = "id_utilisateur_expediteur", length = 50)
    private String idUtilisateurExpediteur;
    
    @Column(name = "id_utilisateur_destinataire", length = 50)
    private String idUtilisateurDestinataire;
    
    @Column(length = 50)
    private String lien;
    
    @Column(name = "reference_type", length = 50)
    private String referenceType;
    
    @Column(name = "reference_id", length = 150)
    private String referenceId;
    
    @Column(name = "est_lu")
    private Boolean estLu = false;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    
    // Constructeurs
    public Notification() {}
    
    public Notification(String message, String idUtilisateurExpediteur, String idUtilisateurDestinataire, 
                       String lien, String referenceType, String referenceId) {
        this.message = message;
        this.idUtilisateurExpediteur = idUtilisateurExpediteur;
        this.idUtilisateurDestinataire = idUtilisateurDestinataire;
        this.lien = lien;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.estLu = false;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }
    
    // Getters et Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getIdUtilisateurExpediteur() {
        return idUtilisateurExpediteur;
    }
    
    public void setIdUtilisateurExpediteur(String idUtilisateurExpediteur) {
        this.idUtilisateurExpediteur = idUtilisateurExpediteur;
    }
    
    public String getIdUtilisateurDestinataire() {
        return idUtilisateurDestinataire;
    }
    
    public void setIdUtilisateurDestinataire(String idUtilisateurDestinataire) {
        this.idUtilisateurDestinataire = idUtilisateurDestinataire;
    }
    
    public String getLien() {
        return lien;
    }
    
    public void setLien(String lien) {
        this.lien = lien;
    }
    
    public String getReferenceType() {
        return referenceType;
    }
    
    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }
    
    public String getReferenceId() {
        return referenceId;
    }
    
    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }
    
    public Boolean getEstLu() {
        return estLu;
    }
    
    public void setEstLu(Boolean estLu) {
        this.estLu = estLu;
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
    
    // Méthodes utilitaires
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        modifiedAt = LocalDateTime.now();
        if (estLu == null) {
            estLu = false;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        modifiedAt = LocalDateTime.now();
    }
}