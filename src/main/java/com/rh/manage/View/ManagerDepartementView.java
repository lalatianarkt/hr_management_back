package com.rh.manage.View;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "manager_departement")
public class ManagerDepartementView {
    @Id
    private String idManager;
    private String nomManager;
    private String prenomManager;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String idEmploye;
    private int statutManager;
    private String idDepartement;
    private String nomDepartement;
    private String descriptionDepartement;
    private int statutDepartement;

    // ===== Constructeur vide =====
    public ManagerDepartementView() {
    }

    // ===== Constructeur complet =====
    public ManagerDepartementView(String idManager, String nomManager, String prenomManager,
                              LocalDate dateDebut, LocalDate dateFin, LocalDateTime createdAt, LocalDateTime modifiedAt,
                              String idEmploye, int statutManager,
                              String idDepartement, String nomDepartement, String descriptionDepartement, int statutDepartement) {
        this.idManager = idManager;
        this.nomManager = nomManager;
        this.prenomManager = prenomManager;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.idEmploye = idEmploye;
        this.statutManager = statutManager;
        this.idDepartement = idDepartement;
        this.nomDepartement = nomDepartement;
        this.descriptionDepartement = descriptionDepartement;
        this.statutDepartement = statutDepartement;
    }

    // ===== Getters & Setters =====
    public String getIdManager() {
        return idManager;
    }

    public void setIdManager(String idManager) {
        this.idManager = idManager;
    }

    public String getNomManager() {
        return nomManager;
    }

    public void setNomManager(String nomManager) {
        this.nomManager = nomManager;
    }

    public String getPrenomManager() {
        return prenomManager;
    }

    public void setPrenomManager(String prenomManager) {
        this.prenomManager = prenomManager;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(String idEmploye) {
        this.idEmploye = idEmploye;
    }

    public int getStatutManager() {
        return statutManager;
    }

    public void setStatutManager(int statutManager) {
        this.statutManager = statutManager;
    }

    public String getIdDepartement() {
        return idDepartement;
    }

    public void setIdDepartement(String idDepartement) {
        this.idDepartement = idDepartement;
    }

    public String getNomDepartement() {
        return nomDepartement;
    }

    public void setNomDepartement(String nomDepartement) {
        this.nomDepartement = nomDepartement;
    }

    public String getDescriptionDepartement() {
        return descriptionDepartement;
    }

    public void setDescriptionDepartement(String descriptionDepartement) {
        this.descriptionDepartement = descriptionDepartement;
    }

    public int getStatutDepartement() {
        return statutDepartement;
    }

    public void setStatutDepartement(int statutDepartement) {
        this.statutDepartement = statutDepartement;
    }

    @Override
    public String toString() {
        return "ManagerDepartement{" +
                "idManager='" + idManager + '\'' +
                ", nomManager='" + nomManager + '\'' +
                ", prenomManager='" + prenomManager + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                ", idEmploye='" + idEmploye + '\'' +
                ", statutManager=" + statutManager +
                ", idDepartement='" + idDepartement + '\'' +
                ", nomDepartement='" + nomDepartement + '\'' +
                ", descriptionDepartement='" + descriptionDepartement + '\'' +
                ", statutDepartement=" + statutDepartement +
                '}';
    }
}

