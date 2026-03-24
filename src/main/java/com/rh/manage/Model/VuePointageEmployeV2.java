package com.rh.manage.Model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "vue_pointage_employe_v2")
@IdClass(VuePointageEmployeV2.VuePointageEmployeV2Id.class)  // AJOUTER cette annotation
public class VuePointageEmployeV2 {
    
    @Id  // Garder @Id
    @Column(name = "id_employe")
    private String idEmploye;
    
    @Id  // AJOUTER @Id sur datePointage
    @Column(name = "date_pointage")
    private LocalDate datePointage;
    
    @Column(name = "matricule")
    private String matricule;
    
    @Column(name = "nom_complet")
    private String nomComplet;
    
    // Supprimer @Id d'ici car on l'a déplacé
    
    @Column(name = "duree_heure_travaillee_minute")
    private Integer dureeHeureTravailleeMinute;
    
    @Column(name = "duree_retard_minute")
    private Integer dureeRetardMinute;
    
    @Column(name = "duree_heure_supplementaire")
    private Integer dureeHeureSupplementaire;
    
    @Column(name = "id_departement")
    private String idDepartement;
    
    @Column(name = "departement_nom")
    private String departementNom;

    // AJOUTER cette classe interne pour la clé composée
    public static class VuePointageEmployeV2Id implements Serializable {
        private String idEmploye;
        private LocalDate datePointage;

        public VuePointageEmployeV2Id() {}

        public VuePointageEmployeV2Id(String idEmploye, LocalDate datePointage) {
            this.idEmploye = idEmploye;
            this.datePointage = datePointage;
        }

        public String getIdEmploye() {
            return idEmploye;
        }

        public void setIdEmploye(String idEmploye) {
            this.idEmploye = idEmploye;
        }

        public LocalDate getDatePointage() {
            return datePointage;
        }

        public void setDatePointage(LocalDate datePointage) {
            this.datePointage = datePointage;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            VuePointageEmployeV2Id that = (VuePointageEmployeV2Id) o;
            return Objects.equals(idEmploye, that.idEmploye) && 
                   Objects.equals(datePointage, that.datePointage);
        }

        @Override
        public int hashCode() {
            return Objects.hash(idEmploye, datePointage);
        }
    }

    // Constructeurs
    public VuePointageEmployeV2() {
    }

    public VuePointageEmployeV2(String idEmploye, String matricule, String nomComplet, 
                               LocalDate datePointage, Integer dureeHeureTravailleeMinute,
                               Integer dureeRetardMinute, Integer dureeHeureSupplementaire,
                               String idDepartement, String departementNom) {
        this.idEmploye = idEmploye;
        this.matricule = matricule;
        this.nomComplet = nomComplet;
        this.datePointage = datePointage;
        this.dureeHeureTravailleeMinute = dureeHeureTravailleeMinute;
        this.dureeRetardMinute = dureeRetardMinute;
        this.dureeHeureSupplementaire = dureeHeureSupplementaire;
        this.idDepartement = idDepartement;
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

    public LocalDate getDatePointage() {
        return datePointage;
    }

    public void setDatePointage(LocalDate datePointage) {
        this.datePointage = datePointage;
    }

    public Integer getDureeHeureTravailleeMinute() {
        return dureeHeureTravailleeMinute;
    }

    public void setDureeHeureTravailleeMinute(Integer dureeHeureTravailleeMinute) {
        this.dureeHeureTravailleeMinute = dureeHeureTravailleeMinute;
    }

    public Integer getDureeRetardMinute() {
        return dureeRetardMinute;
    }

    public void setDureeRetardMinute(Integer dureeRetardMinute) {
        this.dureeRetardMinute = dureeRetardMinute;
    }

    public Integer getDureeHeureSupplementaire() {
        return dureeHeureSupplementaire;
    }

    public void setDureeHeureSupplementaire(Integer dureeHeureSupplementaire) {
        this.dureeHeureSupplementaire = dureeHeureSupplementaire;
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

    // Méthodes utilitaires
    public Double getDureeHeureTravailleeEnHeures() {
        return dureeHeureTravailleeMinute != null ? dureeHeureTravailleeMinute / 60.0 : 0.0;
    }
    
    public Double getDureeRetardEnHeures() {
        return dureeRetardMinute != null ? dureeRetardMinute / 60.0 : 0.0;
    }
    
    public Double getDureeHeureSupplementaireEnHeures() {
        return dureeHeureSupplementaire != null ? dureeHeureSupplementaire / 60.0 : 0.0;
    }

    // AJOUTER equals et hashCode basés sur la clé composée
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VuePointageEmployeV2 that = (VuePointageEmployeV2) o;
        return Objects.equals(idEmploye, that.idEmploye) && 
               Objects.equals(datePointage, that.datePointage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmploye, datePointage);
    }

    @Override
    public String toString() {
        return "VuePointageEmployeV2{" +
                "idEmploye='" + idEmploye + '\'' +
                ", matricule='" + matricule + '\'' +
                ", nomComplet='" + nomComplet + '\'' +
                ", datePointage=" + datePointage +
                '}';
    }
}