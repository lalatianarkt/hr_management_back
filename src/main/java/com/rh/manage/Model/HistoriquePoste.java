package com.rh.manage.Model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "historique_poste")
public class HistoriquePoste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "date_du_jour")
    private LocalDate dateDuJour;

    // === RELATIONS AU LIEU DE STRINGS ===
    
    @ManyToOne
    @JoinColumn(name = "id_employe")
    private Employe employe;

    @ManyToOne
    @JoinColumn(name = "id_poste")
    private Poste poste;

    // === CONSTRUCTEURS ===

    public HistoriquePoste() {}

    public HistoriquePoste(Employe employe, Poste poste, LocalDate dateDebut, LocalDate dateFin) {
        this.employe = employe;
        this.poste = poste;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.dateDuJour = LocalDate.now();
    }

    public HistoriquePoste(Employe employe, Poste poste, LocalDate dateDebut, LocalDate dateFin, LocalDate dateDuJour) {
        this.employe = employe;
        this.poste = poste;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.dateDuJour = dateDuJour;
    }

    // === GETTERS / SETTERS ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public LocalDate getDateDuJour() {
        return dateDuJour;
    }

    public void setDateDuJour(LocalDate dateDuJour) {
        this.dateDuJour = dateDuJour;
    }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

    public Poste getPoste() {
        return poste;
    }

    public void setPoste(Poste poste) {
        this.poste = poste;
    }

    @Override
    public String toString() {
        return "HistoriquePoste{" +
                "id=" + id +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", dateDuJour=" + dateDuJour +
                ", employe=" + (employe != null ? employe.getNom() + " " + employe.getPrenom() : "null") +
                ", poste=" + (poste != null ? poste.getNom() : "null") +
                '}';
    }
}