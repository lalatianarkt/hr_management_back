package com.rh.manage.Model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "demande_conge")
// @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DemandeConge {

    @Id
    private String id;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @Column(name = "date_validation")
    private LocalDate dateValidation;

    @Column(name = "commentaire_annulation")
    private String commentaireAnnulation;

    @Column(name = "date_demande", nullable = false)
    private LocalDate dateDemande;

    @Column(name = "decision_manager")
    private Integer decisionManager;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "commentaire_manager")
    private String commentaireManager;

    @Column
    private String commentaire;

    @Column(name = "autre_motif", nullable = true)
    private String autreMotif;

    @Column(name = "nb_jours", nullable = false)
    private Double nbJours;

    // --- Correction ici : objet Employe ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_employe") // référence la colonne id_employe
    // @JsonManagedReference
    private Employe employe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_type_conge")
    private TypeConge typeConge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_manager")
    private Manager manager;

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public DemandeConge() {}

    @PrePersist
    public void prePersist() {
        if (this.id == null || this.id.trim().isEmpty()) {
            String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmm"));
            this.id = "DEM-CON-" + datePart + "-" + Math.abs(new Random().nextInt(100));
        }
        if (this.createdAt == null) this.createdAt = LocalDateTime.now();
        if (this.modifiedAt == null) this.modifiedAt = this.createdAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

    // --- Getters et Setters ---

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public LocalDate getDateDemande() { return dateDemande; }
    public void setDateDemande(LocalDate dateDemande) { this.dateDemande = dateDemande; }

    public Integer getDecisionManager() { return decisionManager; }
    public void setDecisionManager(Integer decisionManager) { this.decisionManager = decisionManager; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }

    public String getCommentaireManager() { return commentaireManager; }
    public void setCommentaireManager(String commentaireManager) { this.commentaireManager = commentaireManager; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public String getAutreMotif() { return autreMotif; }
    public void setAutreMotif(String autreMotif) { this.autreMotif = autreMotif; }

    public Double getNbJours() { return nbJours; }
    public void setNbJours(Double nbJours) { this.nbJours = nbJours; }

    public Employe getEmploye() { return employe; }
    public void setEmploye(Employe employe) { this.employe = employe; }

    public TypeConge getTypeConge() {
        return typeConge;
    }

    public void setTypeConge(TypeConge typeConge) {
        this.typeConge = typeConge;
    }

    public LocalDate getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(LocalDate dateValidation) {
        this.dateValidation = dateValidation;
    }

    public String getCommentaireAnnulation() {
        return commentaireAnnulation;
    }

    public void setCommentaireAnnulation(String commentaireAnnulation) {
        this.commentaireAnnulation = commentaireAnnulation;
    }

    

    
}
