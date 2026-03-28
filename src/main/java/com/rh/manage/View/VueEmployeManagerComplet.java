package com.rh.manage.View;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vue_employe_manager_complet")
public class VueEmployeManagerComplet {
    
    @Id
    @Column(name = "employe_matricule")
    private String employeMatricule;
    
    @Column(name = "employe_nom")
    private String employeNom;
    
    @Column(name = "employe_prenom")
    private String employePrenom;

    @Column(name = "id_employe")
    private String idEmploye;
    
    @Column(name = "nom_complet")
    private String nomComplet;
    
    @Column(name = "id_poste")
    private String idPoste;
    
    @Column(name = "nom_poste")
    private String nomPoste;
    
    @Column(name = "nom_niveau")
    private String nomNiveau;
    
    @Column(name = "rang")
    private Integer rang;
    
    @Column(name = "departement_nom")
    private String nomDepartement;
    
    @Column(name = "nom_complet_manager")
    private String nomCompletManager;
    
    @Column(name = "nom_poste_manager")
    private String nomPosteManager;
    
    @Column(name = "nom_niveau_manager")
    private String nomNiveauManager;
    
    @Column(name = "rang_poste_manager")
    private Integer rangPosteManager;

    // Constructeurs
    public VueEmployeManagerComplet() {
    }

    // Getters et Setters
    public String getEmployeMatricule() {
        return employeMatricule;
    }

    public void setEmployeMatricule(String employeMatricule) {
        this.employeMatricule = employeMatricule;
    }

    public String getEmployeNom() {
        return employeNom;
    }

    public void setEmployeNom(String employeNom) {
        this.employeNom = employeNom;
    }

    public String getEmployePrenom() {
        return employePrenom;
    }

    public void setEmployePrenom(String employePrenom) {
        this.employePrenom = employePrenom;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public String getIdPoste() {
        return idPoste;
    }

    public void setIdPoste(String idPoste) {
        this.idPoste = idPoste;
    }

    public String getNomPoste() {
        return nomPoste;
    }

    public void setNomPoste(String nomPoste) {
        this.nomPoste = nomPoste;
    }

    public String getNomNiveau() {
        return nomNiveau;
    }

    public void setNomNiveau(String nomNiveau) {
        this.nomNiveau = nomNiveau;
    }

    public Integer getRang() {
        return rang;
    }

    public void setRang(Integer rang) {
        this.rang = rang;
    }

    public String getNomDepartement() {
        return nomDepartement;
    }

    public void setNomDepartement(String nomDepartement) {
        this.nomDepartement = nomDepartement;
    }

    public String getNomCompletManager() {
        return nomCompletManager;
    }

    public void setNomCompletManager(String nomCompletManager) {
        this.nomCompletManager = nomCompletManager;
    }

    public String getNomPosteManager() {
        return nomPosteManager;
    }

    public void setNomPosteManager(String nomPosteManager) {
        this.nomPosteManager = nomPosteManager;
    }

    public String getNomNiveauManager() {
        return nomNiveauManager;
    }

    public void setNomNiveauManager(String nomNiveauManager) {
        this.nomNiveauManager = nomNiveauManager;
    }

    public Integer getRangPosteManager() {
        return rangPosteManager;
    }

    public void setRangPosteManager(Integer rangPosteManager) {
        this.rangPosteManager = rangPosteManager;
    }

    @Override
    public String toString() {
        return "VueEmployeManagerComplet{" +
                "employeMatricule='" + employeMatricule + '\'' +
                ", employeNom='" + employeNom + '\'' +
                ", employePrenom='" + employePrenom + '\'' +
                ", nomComplet='" + nomComplet + '\'' +
                ", idPoste='" + idPoste + '\'' +
                ", nomPoste='" + nomPoste + '\'' +
                ", nomNiveau='" + nomNiveau + '\'' +
                ", rang=" + rang +
                ", nomDepartement='" + nomDepartement + '\'' +
                ", nomCompletManager='" + nomCompletManager + '\'' +
                ", nomPosteManager='" + nomPosteManager + '\'' +
                ", nomNiveauManager='" + nomNiveauManager + '\'' +
                ", rangPosteManager=" + rangPosteManager +
                '}';
    }

    public String getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(String idEmploye) {
        this.idEmploye = idEmploye;
    }
}
