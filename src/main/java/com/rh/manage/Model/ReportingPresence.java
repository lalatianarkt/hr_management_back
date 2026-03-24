package com.rh.manage.Model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reporting_presence")
public class ReportingPresence {
    
    @Id
    @Column(name = "id_employe")
    private String idEmploye;
    
    @Column(name = "matricule")
    private String matricule;
    
    @Column(name = "nom_complet")
    private String nomComplet;
    
    @Column(name = "total_heure_travaillee")
    private Integer totalHeureTravaillee; // en minutes
    
    @Column(name = "total_retard")
    private Integer totalRetard; // en minutes
    
    @Column(name = "total_heure_sup")
    private Integer totalHeureSup; // en minutes
    
    @Column(name = "id_departement")
    private String idDepartement;
    
    @Column(name = "departement_nom")
    private String departementNom;
    
    // Nouveaux attributs de la vue reporting_presence
    @Column(name = "nombre_conges_termines")
    private Integer nombreCongesTermines;
    
    @Column(name = "total_jours_conges_termines")
    private Integer totalJoursCongesTermines;
    
    @Column(name = "date_dernier_conge_termine")
    private LocalDate dateDernierCongeTermine;
    
    @Column(name = "mois_dernier_conge")
    private String moisDernierConge;
    
    // Méthodes utilitaires (héritées de VuePointageEmploye)
    public Double getTotalHeureTravailleeEnHeures() {
        return totalHeureTravaillee != null ? totalHeureTravaillee / 60.0 : 0.0;
    }
    
    public Double getTotalRetardEnHeures() {
        return totalRetard != null ? totalRetard / 60.0 : 0.0;
    }
    
    public Double getTotalHeureSupEnHeures() {
        return totalHeureSup != null ? totalHeureSup / 60.0 : 0.0;
    }
    
    public Integer getTempsEffectif() {
        int tempsTravaille = totalHeureTravaillee != null ? totalHeureTravaillee : 0;
        int retard = totalRetard != null ? totalRetard : 0;
        return Math.max(0, tempsTravaille - retard);
    }
    
    public Double getTempsEffectifEnHeures() {
        return getTempsEffectif() / 60.0;
    }
    
    public Double getPourcentageRetard() {
        if (totalHeureTravaillee == null || totalHeureTravaillee == 0) {
            return 0.0;
        }
        int retard = totalRetard != null ? totalRetard : 0;
        return (retard * 100.0) / totalHeureTravaillee;
    }
    
    public Double getPourcentageHeureSup() {
        if (totalHeureTravaillee == null || totalHeureTravaillee == 0) {
            return 0.0;
        }
        int heureSup = totalHeureSup != null ? totalHeureSup : 0;
        return (heureSup * 100.0) / totalHeureTravaillee;
    }
    
    // Nouvelle méthode : Taux de présence
    public Double getTauxPresence() {
        if (totalHeureTravaillee == null || totalHeureTravaillee == 0) {
            return 0.0;
        }
        return 100.0 - getPourcentageRetard();
    }
    
    // Nouvelle méthode : Jours de congé moyen par congé
    public Double getMoyenneJoursParConge() {
        if (nombreCongesTermines == null || nombreCongesTermines == 0) {
            return 0.0;
        }
        return totalJoursCongesTermines != null ? 
               totalJoursCongesTermines.doubleValue() / nombreCongesTermines : 0.0;
    }
    
    // Nouvelle méthode : Fréquence des congés (jours entre congés)
    public String getFrequenceConges() {
        if (dateDernierCongeTermine == null || nombreCongesTermines == null || nombreCongesTermines == 0) {
            return "Aucun congé";
        }
        
        if (nombreCongesTermines == 1) {
            return "Premier congé";
        }
        
        // Cette logique serait complétée avec plus de données
        return "Dernier: " + dateDernierCongeTermine;
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

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public Integer getTotalHeureTravaillee() {
        return totalHeureTravaillee;
    }

    public void setTotalHeureTravaillee(Integer totalHeureTravaillee) {
        this.totalHeureTravaillee = totalHeureTravaillee;
    }

    public Integer getTotalRetard() {
        return totalRetard;
    }

    public void setTotalRetard(Integer totalRetard) {
        this.totalRetard = totalRetard;
    }

    public Integer getTotalHeureSup() {
        return totalHeureSup;
    }

    public void setTotalHeureSup(Integer totalHeureSup) {
        this.totalHeureSup = totalHeureSup;
    }

    public String getIdDepartement() {
        return idDepartement;
    }

    public void setIdDepartement(String idDepartement) {
        this.idDepartement = idDepartement;
    }

    public String getDepartementNom() {
        return departementNom;
    }

    public void setDepartementNom(String departementNom) {
        this.departementNom = departementNom;
    }

    public Integer getNombreCongesTermines() {
        return nombreCongesTermines;
    }

    public void setNombreCongesTermines(Integer nombreCongesTermines) {
        this.nombreCongesTermines = nombreCongesTermines;
    }

    public Integer getTotalJoursCongesTermines() {
        return totalJoursCongesTermines;
    }

    public void setTotalJoursCongesTermines(Integer totalJoursCongesTermines) {
        this.totalJoursCongesTermines = totalJoursCongesTermines;
    }

    public LocalDate getDateDernierCongeTermine() {
        return dateDernierCongeTermine;
    }

    public void setDateDernierCongeTermine(LocalDate dateDernierCongeTermine) {
        this.dateDernierCongeTermine = dateDernierCongeTermine;
    }

    public String getMoisDernierConge() {
        return moisDernierConge;
    }

    public void setMoisDernierConge(String moisDernierConge) {
        this.moisDernierConge = moisDernierConge;
    }

    
}
