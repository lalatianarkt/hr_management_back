package com.rh.manage.Dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.rh.manage.Model.Employe;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Model.TypeMouvement;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class MouvementData {
    private String id;
    private String motif;
    private Integer statut;
    private LocalDate dateDemande;
    private LocalDate dateValidation;
    private String commentaire;
    private Employe employeDemandeur;
    private Employe employeConcerne;
    private InfosProfessionnelles infosProActuel;
    private InfosProfessionnelles infosProPropose;
    private Employe employeValidateur;
    private TypeMouvement typeMouvement;

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public String getMotif() {return motif;}
    public void setMotif(String motif) {this.motif = motif;}
    public Integer getStatut() {return statut;}
    public void setStatut(Integer statut) {this.statut = statut;}
    public LocalDate getDateDemande() {return dateDemande;}
    public void setDateDemande(LocalDate dateDemande) {this.dateDemande = dateDemande;}
    public LocalDate getDateValidation() {return dateValidation;}
    public void setDateValidation(LocalDate dateValidation) {this.dateValidation = dateValidation;}
    public String getCommentaire() {return commentaire;}
    public void setCommentaire(String commentaire) {this.commentaire = commentaire;}
    public Employe getEmployeDemandeur() {return employeDemandeur;}
    public void setEmployeDemandeur(Employe employeDemandeur) {this.employeDemandeur = employeDemandeur;}
    public Employe getEmployeConcerne() {return employeConcerne;}
    public void setEmployeConcerne(Employe employeConcerne) {this.employeConcerne = employeConcerne;}
    public InfosProfessionnelles getInfosProActuel() {return infosProActuel;}
    public void setInfosProActuel(InfosProfessionnelles infosProActuel) {this.infosProActuel = infosProActuel;}
    public InfosProfessionnelles getInfosProPropose() {return infosProPropose;}
    public void setInfosProPropose(InfosProfessionnelles infosProPropose) {this.infosProPropose = infosProPropose;}
    public Employe getEmployeValidateur() {return employeValidateur;}
    public void setEmployeValidateur(Employe employeValidateur) {this.employeValidateur = employeValidateur;}
    public TypeMouvement getTypeMouvement() {return typeMouvement;}
    public void setTypeMouvement(TypeMouvement typeMouvement) {this.typeMouvement = typeMouvement;}
    
}
