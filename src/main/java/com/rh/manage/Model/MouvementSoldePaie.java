package com.rh.manage.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "mouvement_solde_paie")
public class MouvementSoldePaie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "id_mouvement_solde")
    private int idMouvementSolde;
    @Column(name = "id_employe")
    private String idEmploye;

    @Column(name = "date_heure_saisie")
    private LocalDateTime dateHeureSaisie;

    @Column(name = "id_paie", length = 50)
    private String idPaie;

    @Column(name = "nb_conge_a_reporter", precision = 15)
    private Double nbCongeAReporter;

    @Column(name = "nb_conge_dans_paie", precision = 15)
    private Double nbCongeDansPaie;

    @Column(name = "nb_conge_dans_mouvement_solde", precision = 15)
    private Double nbCongeDansMouvementSolde;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Constructeurs
    public MouvementSoldePaie() {}

    @PrePersist
    protected void onCreate() {
        if (this.dateHeureSaisie == null) {
            this.dateHeureSaisie = LocalDateTime.now();
        }
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMouvementSolde() {
        return idMouvementSolde;
    }

    public void setIdMouvementSolde(int idMouvementSolde) {
        this.idMouvementSolde = idMouvementSolde;
    }

    public LocalDateTime getDateHeureSaisie() {
        return dateHeureSaisie;
    }

    public void setDateHeureSaisie(LocalDateTime dateHeureSaisie) {
        this.dateHeureSaisie = dateHeureSaisie;
    }

    public String getIdPaie() {
        return idPaie;
    }

    public void setIdPaie(String idPaie) {
        this.idPaie = idPaie;
    }

    public Double getNbCongeAReporter() {
        return nbCongeAReporter;
    }

    public void setNbCongeAReporter(Double nbCongeAReporter) {
        this.nbCongeAReporter = nbCongeAReporter;
    }

    public Double getNbCongeDansPaie() {
        return nbCongeDansPaie;
    }

    public void setNbCongeDansPaie(Double nbCongeDansPaie) {
        this.nbCongeDansPaie = nbCongeDansPaie;
    }

    public Double getNbCongeDansMouvementSolde() {
        return nbCongeDansMouvementSolde;
    }

    public void setNbCongeDansMouvementSolde(Double nbCongeDansMouvementSolde) {
        this.nbCongeDansMouvementSolde = nbCongeDansMouvementSolde;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(String idEmploye) {
        this.idEmploye = idEmploye;
    }

}
