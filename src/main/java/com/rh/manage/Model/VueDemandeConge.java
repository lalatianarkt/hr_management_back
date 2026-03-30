package com.rh.manage.Model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vue_demande_conge")
public class VueDemandeConge {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    
    @Column(name = "date_debut")
    private LocalDate dateDebut;
    
    @Column(name = "date_fin")
    private LocalDate dateFin;
    
    @Column(name = "nb_jours")
    private Integer nbJours;
    
    @Column(name = "decision_manager_libelle")
    private String decisionManagerLibelle;
    
    @Column(name = "date_demande")
    private LocalDate dateDemande;
    
    @Column(name = "date_validation")
    private LocalDate dateValidation;
    
    @Column(name = "commentaire_manager", length = 500)
    private String commentaireManager;
    
    @Column(name = "commentaire", length = 500)
    private String commentaire;
    
    @Column(name = "commentaire_annulation", length = 500)
    private String commentaireAnnulation;
    
    @Column(name = "nom_complet_employe")
    private String nomCompletEmploye;

    @Column(name = "statut")
    private Integer statut;

    @Column(name = "nom_employe")
    private String nomEmploye;
    
    @Column(name = "prenom_employe")
    private String prenomEmploye;
    
    @Column(name = "id_employe")
    private String idEmploye;
    
    @Column(name = "matricule")
    private String matricule;
    
    @Column(name = "nom_manager")
    private String nomManager;
    
    @Column(name = "prenom_manager")
    private String prenomManager;
    
    @Column(name = "nom_complet_manager")
    private String nomCompletManager;
    
    @Column(name = "id_manager")
    private String idManager;
    
    @Column(name = "nom_departement")
    private String nomDepartement;
    
    @Column(name = "id_departement")
    private String idDepartement;
    
    // Constructeurs
    public VueDemandeConge() {
    }
    
    // Getters et Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public LocalDate getDateDebut() {
        return dateDebut;
    }
    
    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }
    
    public LocalDate getDateFin() {
        return dateFin;
    }
    
    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }
    
    public Integer getNbJours() {
        return nbJours;
    }
    
    public void setNbJours(Integer nbJours) {
        this.nbJours = nbJours;
    }
    
    public String getDecisionManagerLibelle() {
        return decisionManagerLibelle;
    }
    
    public void setDecisionManagerLibelle(String decisionManagerLibelle) {
        this.decisionManagerLibelle = decisionManagerLibelle;
    }
    
    public LocalDate getDateDemande() {
        return dateDemande;
    }
    
    public void setDateDemande(LocalDate dateDemande) {
        this.dateDemande = dateDemande;
    }
    
    public LocalDate getDateValidation() {
        return dateValidation;
    }
    
    public void setDateValidation(LocalDate dateValidation) {
        this.dateValidation = dateValidation;
    }
    
    public String getCommentaireManager() {
        return commentaireManager;
    }
    
    public void setCommentaireManager(String commentaireManager) {
        this.commentaireManager = commentaireManager;
    }
    
    public String getCommentaire() {
        return commentaire;
    }
    
    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
    
    public String getCommentaireAnnulation() {
        return commentaireAnnulation;
    }
    
    public void setCommentaireAnnulation(String commentaireAnnulation) {
        this.commentaireAnnulation = commentaireAnnulation;
    }
    
    public String getNomCompletEmploye() {
        return nomCompletEmploye;
    }
    
    public void setNomCompletEmploye(String nomCompletEmploye) {
        this.nomCompletEmploye = nomCompletEmploye;
    }
    
    public String getNomEmploye() {
        return nomEmploye;
    }
    
    public void setNomEmploye(String nomEmploye) {
        this.nomEmploye = nomEmploye;
    }
    
    public String getPrenomEmploye() {
        return prenomEmploye;
    }
    
    public void setPrenomEmploye(String prenomEmploye) {
        this.prenomEmploye = prenomEmploye;
    }
    
    public String getIdEmploye() {
        return idEmploye;
    }
    
    public void setIdEmploye(String idEmploye) {
        this.idEmploye = idEmploye;
    }
    
    public String getMatricule() {
        return matricule;
    }
    
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }
    
    public String getNomManager() {
        return nomManager;
    }
    
    public void setNomManager(String nomManager) {
        this.nomManager = nomManager;
    }
    
    public String getPrenomManager() {
        return prenomManager;
    }
    
    public void setPrenomManager(String prenomManager) {
        this.prenomManager = prenomManager;
    }
    
    public String getNomCompletManager() {
        return nomCompletManager;
    }
    
    public void setNomCompletManager(String nomCompletManager) {
        this.nomCompletManager = nomCompletManager;
    }
    
    public String getIdManager() {
        return idManager;
    }
    
    public void setIdManager(String idManager) {
        this.idManager = idManager;
    }
    
    public String getNomDepartement() {
        return nomDepartement;
    }
    
    public void setNomDepartement(String nomDepartement) {
        this.nomDepartement = nomDepartement;
    }
    
    public String getIdDepartement() {
        return idDepartement;
    }
    
    public void setIdDepartement(String idDepartement) {
        this.idDepartement = idDepartement;
    }
    
    @Override
    public String toString() {
        return "VueDemandeConge{" +
                "id=" + id +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", nbJours=" + nbJours +
                ", decisionManagerLibelle='" + decisionManagerLibelle + '\'' +
                ", nomCompletEmploye='" + nomCompletEmploye + '\'' +
                ", matricule='" + matricule + '\'' +
                ", nomDepartement='" + nomDepartement + '\'' +
                '}';
    }

    public Integer getStatut() {
        return statut;
    }

    public void setStatut(Integer statut) {
        this.statut = statut;
    }
}