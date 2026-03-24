package com.rh.manage.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Entity
@Table(name = "historique_conge")
public class HistoriqueConge {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "id_employe", length = 50)
    private String idEmploye;

    @Column(name = "id_demande_conge", length = 50)
    private String idDemandeConge;

    @Column(name = "date_debut", length = 50)
    private String dateDebut;

    @Column(name = "date_fin", length = 50)
    private String dateFin;

    @Column(name = "nb_jours", length = 50)
    private String nbJours;

    @Column(name = "type_conge", length = 50)
    private String typeConge;

    // Constructeur par défaut
    public HistoriqueConge() {}

    // Constructeur avec paramètres
    public HistoriqueConge(String id, String idEmploye, String idDemandeConge,
                           String dateDebut, String dateFin, String nbJours, String typeConge) {
        this.id = id;
        this.idEmploye = idEmploye;
        this.idDemandeConge = idDemandeConge;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.nbJours = nbJours;
        this.typeConge = typeConge;
    }

    // Getters et setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(String idEmploye) {
        this.idEmploye = idEmploye;
    }

    public String getIdDemandeConge() {
        return idDemandeConge;
    }

    public void setIdDemandeConge(String idDemandeConge) {
        this.idDemandeConge = idDemandeConge;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getNbJours() {
        return nbJours;
    }

    public void setNbJours(String nbJours) {
        this.nbJours = nbJours;
    }

    public String getTypeConge() {
        return typeConge;
    }

    public void setTypeConge(String typeConge) {
        this.typeConge = typeConge;
    }

    @Override
    public String toString() {
        return "HistoriqueConge{" +
                "id='" + id + '\'' +
                ", idEmploye='" + idEmploye + '\'' +
                ", idDemandeConge='" + idDemandeConge + '\'' +
                ", dateDebut='" + dateDebut + '\'' +
                ", dateFin='" + dateFin + '\'' +
                ", nbJours='" + nbJours + '\'' +
                ", typeConge='" + typeConge + '\'' +
                '}';
    }
}
