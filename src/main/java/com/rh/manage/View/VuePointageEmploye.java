package com.rh.manage.View;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vue_pointage_employe")
public class VuePointageEmploye {
    @Id
    @Column(name = "id_employe")
    private String idEmploye;
    
    @Column(name = "matricule")
    private String matricule;
    
    @Column(name = "nom_complet")
    private String nomComplet;

    @Column(name = "date_pointage")
    private LocalDate datePointage;

    @Column(name = "id_departement")
    private String idDepartement;

    @Column(name = "departement_nom")
    private String departementNom;

    public String getDepartementNom() {
        return departementNom;
    }

    public void setDepartementNom(String departementNom) {
        this.departementNom = departementNom;
    }

    // Getters et Setters
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

    @Override
    public String toString() {
        return "VuePointageEmploye{" +
                "idEmploye=" + idEmploye +
                ", matricule='" + matricule + '\'' +
                ", nomComplet='" + nomComplet + '\'' +
                // ", totalHeureTravaillee=" + totalHeureTravaillee +
                // ", totalRetard=" + totalRetard +
                // ", totalHeureSup=" + totalHeureSup +
                '}';
    }

    public String getIdDepartement() {
        return idDepartement;
    }

    public void setIdDepartement(String idDepartement) {
        this.idDepartement = idDepartement;
    }

    public LocalDate getDatePointage() {
        return datePointage;
    }

    public void setDatePointage(LocalDate datePointage) {
        this.datePointage = datePointage;
    }
    
    // Constructeurs
    public VuePointageEmploye() {
    }
    
    @Column(name = "total_heure_travaillee")
    private Integer totalHeureTravaillee; // en minutes
    
    @Column(name = "total_retard")
    private Integer totalRetard; // en minutes
    
    @Column(name = "total_heure_sup")
    private Integer totalHeureSup; // en minutes (ou selon votre unité)

    public VuePointageEmploye(String idEmploye, String matricule, String nomComplet){
                            //  Integer totalHeureTravaillee, Integer totalRetard, Integer totalHeureSup) {
        this.idEmploye = idEmploye;
        this.matricule = matricule;
        this.nomComplet = nomComplet;
        this.totalHeureTravaillee = totalHeureTravaillee;
        this.totalRetard = totalRetard;
        this.totalHeureSup = totalHeureSup;
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
    
    // Méthodes utilitaires pour convertir les minutes en heures
    public Double getTotalHeureTravailleeEnHeures() {
        return totalHeureTravaillee != null ? totalHeureTravaillee / 60.0 : 0.0;
    }
    
    public Double getTotalRetardEnHeures() {
        return totalRetard != null ? totalRetard / 60.0 : 0.0;
    }
    
    public Double getTotalHeureSupEnHeures() {
        return totalHeureSup != null ? totalHeureSup / 60.0 : 0.0;
    }
    
    // Calcul du temps effectif (temps travaillé - retard)
    public Integer getTempsEffectif() {
        int tempsTravaille = totalHeureTravaillee != null ? totalHeureTravaillee : 0;
        int retard = totalRetard != null ? totalRetard : 0;
        return Math.max(0, tempsTravaille - retard);
    }
    
    public Double getTempsEffectifEnHeures() {
        return getTempsEffectif() / 60.0;
    }
    
    // Calcul du pourcentage de retard
    public Double getPourcentageRetard() {
        if (totalHeureTravaillee == null || totalHeureTravaillee == 0) {
            return 0.0;
        }
        int retard = totalRetard != null ? totalRetard : 0;
        return (retard * 100.0) / totalHeureTravaillee;
    }
    
    // Pourcentage d'heures supplémentaires
    public Double getPourcentageHeureSup() {
        if (totalHeureTravaillee == null || totalHeureTravaillee == 0) {
            return 0.0;
        }
        int heureSup = totalHeureSup != null ? totalHeureSup : 0;
        return (heureSup * 100.0) / totalHeureTravaillee;
    }
}
