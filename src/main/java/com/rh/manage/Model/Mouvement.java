package com.rh.manage.Model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "mouvement")
public class Mouvement {
    @Id
    @Column(length = 50)
    private String id;

    @Column(length = 255)
    private String motif;

    @Column(nullable = false)
    private Integer statut;

    @Column(name = "date_demande", nullable = false)
    private LocalDate dateDemande;

    @Column(name = "date_validation")
    private LocalDate dateValidation;

    @Column(length = 255)
    private String commentaire;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    // NOUVELLES RELATIONS SELON LA STRUCTURE
    @ManyToOne
    @JoinColumn(name = "id_employe_demandeur")
    private Employe employeDemandeur;

    // NOUVELLES RELATIONS SELON LA STRUCTURE
    @ManyToOne
    @JoinColumn(name = "id_employe_concerne")
    private Employe employeConcerne;

    public Employe getEmployeConcerne() {
        return employeConcerne;
    }

    public void setEmployeConcerne(Employe employeConcerne) {
        this.employeConcerne = employeConcerne;
    }

    @ManyToOne
    @JoinColumn(name = "id_infos_pro_actuel", nullable = false)
    private InfosProfessionnelles infosProActuel;

    @ManyToOne
    @JoinColumn(name = "id_infos_pro_propose")
    private InfosProfessionnelles infosProPropose;

    @ManyToOne
    @JoinColumn(name = "id_employe_validateur")
    private Employe employeValidateur;

    @ManyToOne
    @JoinColumn(name = "id_type_mouvement")
    private TypeMouvement typeMouvement;

    // Constructeur
    public Mouvement() {
        
        // this.createdAt = LocalDateTime.now();
        // this.statut = 1; // Par défaut "en attente"
        // this.dateDemande = LocalDate.now();
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public Integer getStatut() { return statut; }
    public void setStatut(Integer statut) { this.statut = statut; }

    public LocalDate getDateDemande() { return dateDemande; }
    public void setDateDemande(LocalDate dateDemande) { this.dateDemande = dateDemande; }

    public LocalDate getDateValidation() { return dateValidation; }
    public void setDateValidation(LocalDate dateValidation) { this.dateValidation = dateValidation; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }

    @PreUpdate
    public void preUpdate() { 
        this.modifiedAt = LocalDateTime.now(); 
    }

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
            String shortUuid = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            this.id = "MVT-" + datePart + "-" + shortUuid;
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    // NOUVEAUX GETTERS/SETTERS
    public Employe getEmployeDemandeur() { return employeDemandeur; }
    public void setEmployeDemandeur(Employe employeDemandeur) { this.employeDemandeur = employeDemandeur; }

    public InfosProfessionnelles getInfosProActuel() { return infosProActuel; }
    public void setInfosProActuel(InfosProfessionnelles infosProActuel) { this.infosProActuel = infosProActuel; }

    public InfosProfessionnelles getInfosProPropose() { return infosProPropose; }
    public void setInfosProPropose(InfosProfessionnelles infosProPropose) { this.infosProPropose = infosProPropose; }

    public Employe getEmployeValidateur() { return employeValidateur; }
    public void setEmployeValidateur(Employe employeValidateur) { this.employeValidateur = employeValidateur; }

    public TypeMouvement getTypeMouvement() { return typeMouvement; }
    public void setTypeMouvement(TypeMouvement typeMouvement) { this.typeMouvement = typeMouvement; }

    // Méthode utilitaire pour vérifier si le mouvement est validé
    public boolean estValide() {
        return this.statut != null && this.statut == 2;
    }

    // Méthode utilitaire pour vérifier si le mouvement est en attente
    public boolean estEnAttente() {
        return this.statut != null && this.statut == 1;
    }

    // Méthode utilitaire pour vérifier si le mouvement est appliqué
    public boolean estApplique() {
        return this.statut != null && this.statut == 3;
    }
}