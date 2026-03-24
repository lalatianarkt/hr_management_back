package com.rh.manage.Model;

import jakarta.persistence.*;
import java.time.LocalDate;

import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "vue_statistiques_employe_demandes")
@Immutable // Important : une vue est en lecture seule
public class VueStatistiquesEmployeDemandes {
    
    @Id
    @Column(name = "employe_id")
    private String employeId;
    
    @Column(name = "nom")
    private String nom;
    
    @Column(name = "prenom")
    private String prenom;
    
    @Column(name = "matricule")
    private String matricule;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "nombre_demandes")
    private Long nombreDemandes;
    
    @Column(name = "total_jours_demandes")
    private Double totalJoursDemandes;
    
    @Column(name = "moyenne_jours_demande")
    private Double moyenneJoursDemande;
    
    @Column(name = "derniere_demande")
    private LocalDate derniereDemande;
    
    @Column(name = "premiere_demande")
    private LocalDate premiereDemande;
    
    @Column(name = "demandes_en_attente")
    private Long demandesEnAttente;
    
    @Column(name = "demandes_approuvees")
    private Long demandesApprouvees;
    
    @Column(name = "demandes_refusees")
    private Long demandesRefusees;
    
    @Column(name = "demandes_annulees")
    private Long demandesAnnulees;
    
    @Column(name = "taux_approbation")
    private Double tauxApprobation;
    
    // Constructeurs
    public VueStatistiquesEmployeDemandes() {}
    
    public VueStatistiquesEmployeDemandes(String employeId, String nom, String prenom, String matricule, 
                                         String email, Long nombreDemandes, Double totalJoursDemandes, 
                                         Double moyenneJoursDemande, LocalDate derniereDemande, 
                                         LocalDate premiereDemande, Long demandesEnAttente, 
                                         Long demandesApprouvees, Long demandesRefusees, 
                                         Long demandesAnnulees, Double tauxApprobation) {
        this.employeId = employeId;
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;
        this.email = email;
        this.nombreDemandes = nombreDemandes;
        this.totalJoursDemandes = totalJoursDemandes;
        this.moyenneJoursDemande = moyenneJoursDemande;
        this.derniereDemande = derniereDemande;
        this.premiereDemande = premiereDemande;
        this.demandesEnAttente = demandesEnAttente;
        this.demandesApprouvees = demandesApprouvees;
        this.demandesRefusees = demandesRefusees;
        this.demandesAnnulees = demandesAnnulees;
        this.tauxApprobation = tauxApprobation;
    }
    
    // Getters et Setters
    public String getEmployeId() { return employeId; }
    public void setEmployeId(String employeId) { this.employeId = employeId; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Long getNombreDemandes() { return nombreDemandes; }
    public void setNombreDemandes(Long nombreDemandes) { this.nombreDemandes = nombreDemandes; }
    
    public Double getTotalJoursDemandes() { return totalJoursDemandes; }
    public void setTotalJoursDemandes(Double totalJoursDemandes) { this.totalJoursDemandes = totalJoursDemandes; }
    
    public Double getMoyenneJoursDemande() { return moyenneJoursDemande; }
    public void setMoyenneJoursDemande(Double moyenneJoursDemande) { this.moyenneJoursDemande = moyenneJoursDemande; }
    
    public LocalDate getDerniereDemande() { return derniereDemande; }
    public void setDerniereDemande(LocalDate derniereDemande) { this.derniereDemande = derniereDemande; }
    
    public LocalDate getPremiereDemande() { return premiereDemande; }
    public void setPremiereDemande(LocalDate premiereDemande) { this.premiereDemande = premiereDemande; }
    
    public Long getDemandesEnAttente() { return demandesEnAttente; }
    public void setDemandesEnAttente(Long demandesEnAttente) { this.demandesEnAttente = demandesEnAttente; }
    
    public Long getDemandesApprouvees() { return demandesApprouvees; }
    public void setDemandesApprouvees(Long demandesApprouvees) { this.demandesApprouvees = demandesApprouvees; }
    
    public Long getDemandesRefusees() { return demandesRefusees; }
    public void setDemandesRefusees(Long demandesRefusees) { this.demandesRefusees = demandesRefusees; }
    
    public Long getDemandesAnnulees() { return demandesAnnulees; }
    public void setDemandesAnnulees(Long demandesAnnulees) { this.demandesAnnulees = demandesAnnulees; }
    
    public Double getTauxApprobation() { return tauxApprobation; }
    public void setTauxApprobation(Double tauxApprobation) { this.tauxApprobation = tauxApprobation; }
    
    // Méthodes utilitaires
    public String getNomComplet() {
        return prenom + " " + nom;
    }
    
    public Long getDemandesTraitees() {
        return demandesApprouvees + demandesRefusees + demandesAnnulees;
    }
    
    public Double getTotalJoursArrondi() {
        return totalJoursDemandes != null ? Math.round(totalJoursDemandes) : 0.0;
    }
    
    public Double getMoyenneJoursArrondie() {
        return moyenneJoursDemande != null ? Math.round(moyenneJoursDemande * 10.0) / 10.0 : 0.0;
    }
    
    public Double getTauxApprobationArrondi() {
        return tauxApprobation != null ? Math.round(tauxApprobation * 10.0) / 10.0 : 0.0;
    }
}