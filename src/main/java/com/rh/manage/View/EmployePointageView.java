package com.rh.manage.View;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.time.LocalDate;

/**
 * Vue pour afficher les employés pour le pointage
 * Cette entité est en lecture seule (Immutable)
 */
@Entity
@Immutable
@Table(name = "v_employe_pointage")
public class EmployePointageView {
    
    @Id
    private String id;
    
    private String nom;
    
    private String prenom;
    
    private String matricule;
    
    private String nomComplet;
    
    private String affichageComplet;
    
    private String departement;
    
    private String poste;
    
    private Integer employeStatut;
    
    private Integer infoProStatut;
    
    private LocalDate dateNaissance;
    
    private String telephone;
    
    private String email;
    
    private LocalDate dateEmbauche;
    
    private Double salaireBase;
    
    // Constructeurs
    public EmployePointageView() {
    }
    
    public EmployePointageView(String id, String nom, String prenom, String matricule) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;
    }
    
    // Getters et Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
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
    
    public String getAffichageComplet() {
        return affichageComplet;
    }
    
    public void setAffichageComplet(String affichageComplet) {
        this.affichageComplet = affichageComplet;
    }
    
    public String getDepartement() {
        return departement;
    }
    
    public void setDepartement(String departement) {
        this.departement = departement;
    }
    
    public String getPoste() {
        return poste;
    }
    
    public void setPoste(String poste) {
        this.poste = poste;
    }
    
    public Integer getEmployeStatut() {
        return employeStatut;
    }
    
    public void setEmployeStatut(Integer employeStatut) {
        this.employeStatut = employeStatut;
    }
    
    public Integer getInfoProStatut() {
        return infoProStatut;
    }
    
    public void setInfoProStatut(Integer infoProStatut) {
        this.infoProStatut = infoProStatut;
    }
    
    public LocalDate getDateNaissance() {
        return dateNaissance;
    }
    
    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public LocalDate getDateEmbauche() {
        return dateEmbauche;
    }
    
    public void setDateEmbauche(LocalDate dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }
    
    public Double getSalaireBase() {
        return salaireBase;
    }
    
    public void setSalaireBase(Double salaireBase) {
        this.salaireBase = salaireBase;
    }
    
    // Méthodes utilitaires
    public boolean isActif() {
        return employeStatut != null && employeStatut == 0 
               && infoProStatut != null && infoProStatut == 0;
    }
    
    public String getInitiales() {
        if (nom == null || prenom == null) return "";
        return (nom.charAt(0) + "" + prenom.charAt(0)).toUpperCase();
    }
    
    public Integer getAge() {
        if (dateNaissance == null) return null;
        return java.time.Period.between(dateNaissance, LocalDate.now()).getYears();
    }
    
    public Integer getAnciennete() {
        if (dateEmbauche == null) return null;
        return java.time.Period.between(dateEmbauche, LocalDate.now()).getYears();
    }
    
    @Override
    public String toString() {
        return "EmployePointageView{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", matricule='" + matricule + '\'' +
                ", departement='" + departement + '\'' +
                ", poste='" + poste + '\'' +
                ", actif=" + isActif() +
                '}';
    }
}
