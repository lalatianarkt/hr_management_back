package com.rh.manage.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mouvement_solde")
public class MouvementSolde {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nb_conge_total", nullable = false)
    private Double nbCongeTotal;

    @Column(name = "nb_conge_restant", nullable = false)
    private Double nbCongeRestant;

    @Column(name = "nb_conge_pris", nullable = false)
    private Double nbCongePris;

    @Column(nullable = false)
    private Integer annee;

    @Column(nullable = false)
    private Integer mois;

    @Column(nullable = false)
    private Integer statut;

    @Column(length = 255)
    private String commentaire;

    @Enumerated(EnumType.STRING)
    @Convert(converter = TypeEnumCongeConverter.class)
    @Column(name = "type_mouvement", nullable = false)
    private TypeEnumConge typeMouvement;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @ManyToOne
    @JoinColumn(name = "id_employe", nullable = false)
    // private String idEmploye;
    private Employe employe;

    // ======== Constructeur ========
    public MouvementSolde() {
        this.createdAt = LocalDateTime.now();
    }

    // ======== Getters et Setters ========
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Double getNbCongeTotal() { return nbCongeTotal; }
    public void setNbCongeTotal(Double nbCongeTotal) { this.nbCongeTotal = nbCongeTotal; }

    public Double getNbCongeRestant() { return nbCongeRestant; }
    public void setNbCongeRestant(Double nbCongeRestant) { this.nbCongeRestant = nbCongeRestant; }

    public Double getNbCongePris() { return nbCongePris; }
    public void setNbCongePris(Double nbCongePris) { this.nbCongePris = nbCongePris; }

    public Integer getAnnee() { return annee; }
    public void setAnnee(Integer annee) { this.annee = annee; }

    public Integer getMois() { return mois; }
    public void setMois(Integer mois) { this.mois = mois; }

    public Integer getStatut() { return statut; }
    public void setStatut(Integer statut) { this.statut = statut; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public TypeEnumConge getTypeMouvement() { return typeMouvement; }
    public void setTypeMouvement(TypeEnumConge typeMouvement) { this.typeMouvement = typeMouvement; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }

    public Employe getEmploye() {
        return employe;
    }

    public void setEmploye(Employe employe) {
        this.employe = employe;
    }

}


