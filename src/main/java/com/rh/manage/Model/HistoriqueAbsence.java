package com.rh.manage.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Entity
@Table(name = "historique_absence")
public class HistoriqueAbsence {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "id_employe", length = 50)
    private String idEmploye;

    @Column(name = "duree_heures", length = 50)
    private String dureeHeures;

    @Column(name = "motif", length = 255)
    private String motif;

    @Column(name = "justifiee", length = 1)
    private String justifiee;

    @Column(name = "remarque", length = 255)
    private String remarque;

    @Column(name = "date_absence", length = 50)
    private String dateAbsence;

    // Constructeur par défaut
    public HistoriqueAbsence() {}

    // Constructeur avec paramètres
    public HistoriqueAbsence(String id, String idEmploye, String dureeHeures,
                             String motif, String justifiee, String remarque, String dateAbsence) {
        this.id = id;
        this.idEmploye = idEmploye;
        this.dureeHeures = dureeHeures;
        this.motif = motif;
        this.justifiee = justifiee;
        this.remarque = remarque;
        this.dateAbsence = dateAbsence;
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

    public String getDureeHeures() {
        return dureeHeures;
    }

    public void setDureeHeures(String dureeHeures) {
        this.dureeHeures = dureeHeures;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getJustifiee() {
        return justifiee;
    }

    public void setJustifiee(String justifiee) {
        this.justifiee = justifiee;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public String getDateAbsence() {
        return dateAbsence;
    }

    public void setDateAbsence(String dateAbsence) {
        this.dateAbsence = dateAbsence;
    }

    @Override
    public String toString() {
        return "HistoriqueAbsence{" +
                "id='" + id + '\'' +
                ", idEmploye='" + idEmploye + '\'' +
                ", dureeHeures='" + dureeHeures + '\'' +
                ", motif='" + motif + '\'' +
                ", justifiee='" + justifiee + '\'' +
                ", remarque='" + remarque + '\'' +
                ", dateAbsence='" + dateAbsence + '\'' +
                '}';
    }
}
