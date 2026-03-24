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

    @Column(name = "id_mouvement_solde", length = 50, unique = true)
    private String idMouvementSolde;

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

    public MouvementSoldePaie(String idPaie, Double nbCongeAReporter, Double nbCongeDansPaie, Double nbCongeDansMouvementSolde) {
        this.idMouvementSolde = generateId();
        this.dateHeureSaisie = LocalDateTime.now();
        this.idPaie = idPaie;
        this.nbCongeAReporter = nbCongeAReporter;
        this.nbCongeDansPaie = nbCongeDansPaie;
        this.nbCongeDansMouvementSolde = nbCongeDansMouvementSolde;
    }

    // Méthode pour générer un ID unique
    private String generateId() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String shortUuid = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "MS-PAIE-" + datePart + "-" + shortUuid;
    }

    @PrePersist
    protected void onCreate() {
        if (this.idMouvementSolde == null) {
            this.idMouvementSolde = generateId();
        }
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

    public String getIdMouvementSolde() {
        return idMouvementSolde;
    }

    public void setIdMouvementSolde(String idMouvementSolde) {
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
}
