package com.rh.manage.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "departement_manager")
public class DepartementManager {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();  // ✅ VALEUR PAR DÉFAUT

    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    // === RELATIONS ===
    @ManyToOne
    @JoinColumn(name = "id_departement")
    private Departement departement;

    @ManyToOne
    @JoinColumn(name = "id_manager")
    private Manager manager;

    // === CONSTRUCTEURS ===

    public DepartementManager() {
        this.createdAt = LocalDateTime.now();  // ✅ INITIALISATION DANS CONSTRUCTEUR
    }

    public DepartementManager(LocalDate dateDebut, Departement departement, Manager manager) {
        this();  // ✅ APPELLE LE CONSTRUCTEUR PAR DÉFAUT
        this.dateDebut = dateDebut;
        this.departement = departement;
        this.manager = manager;
        this.dateFin = null; // Relation actuelle par défaut
    }

    public DepartementManager(LocalDate dateDebut, LocalDate dateFin, Departement departement, Manager manager) {
        this();  // ✅ APPELLE LE CONSTRUCTEUR PAR DÉFAUT
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.departement = departement;
        this.manager = manager;
    }

    // === MÉTHODE PREPERSIST ===
    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = generateCustomId();
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();  // ✅ GARANTIE DANS PREPERSIST
        }
    }

    // === GÉNÉRATION D'IDENTIFIANT ===
    private String generateCustomId() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = String.valueOf((int) (Math.random() * 1000));
        return "DPM-" + timestamp + "-" + random;
    }

    // === GETTERS / SETTERS ===

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }

    public Departement getDepartement() { return departement; }
    public void setDepartement(Departement departement) { this.departement = departement; }

    public Manager getManager() { return manager; }
    public void setManager(Manager manager) { this.manager = manager; }

    // === MÉTHODES UTILES ===

    public boolean isActif() {
        return this.dateFin == null;
    }

    public Long getDureeGestion() {
        if (this.dateDebut == null) {
            return 0L;
        }
        LocalDate fin = this.dateFin != null ? this.dateFin : LocalDate.now();
        return java.time.temporal.ChronoUnit.DAYS.between(this.dateDebut, fin);
    }

    @Override
    public String toString() {
        return "DepartementManager{" +
                "id='" + id + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", createdAt=" + createdAt +  // ✅ AFFICHAGE DE LA DATE DE CRÉATION
                ", departement=" + (departement != null ? departement.getNom() : "null") +
                ", manager=" + (manager != null ? manager.getEmploye().getNom() + " " + manager.getEmploye().getPrenom() : "null") +
                ", actif=" + isActif() +
                '}';
    }
}