package com.rh.manage.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "infos_professionnelles")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class InfosProfessionnelles {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "date_debauche")
    private LocalDate dateDebauche;

    @Column(name = "date_embauche", nullable = false)
    private LocalDate dateEmbauche;

    @Column(name= "date_debut_assignation_poste")
    private LocalDate dateDebutAssignationPoste;

    @Column(name= "date_fin_assignation_poste")
    private LocalDate dateFinAssignationPoste;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt; 

    @Column(name = "statut", nullable = false)
    private int statut;

    @Column(name = "salaire_base")
    private double salaireBase;

    @Column(name = "matricule")
    private String matricule;

    @Column(name = "motif_depart")
    private String motifDepart;

    @Column(name = "classification")
    private String classification;

    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_categorie")
    private CategorieProfessionnelle categorieProfessionnelle;

    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_temps_travail")
    TypeTempsTravail typeTempsTravail;

    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_type_entree")
    private TypeEntree typeEntree;

    // === RELATIONS ===
    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_manager")
    private Manager manager;

    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_poste", nullable = false)
    private Poste poste;

    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_type_contrat", nullable = false)
    private TypeContrat typeContrat;

    // ✅ CORRECTION : PAS DE CASCADE pour ManyToOne
    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_employe", nullable = false)
    private Employe employe;

    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_departement")
    private Departement departement;

    // === CONSTRUCTEURS ===

    public InfosProfessionnelles() {
        // Constructeur par défaut pour JPA
    }

    

    // public InfosProfessionnelles(LocalDate dateEmbauche, Employe employe, Poste poste, TypeContrat typeContrat, 
    // Manager manager, LocalDate date_debauche, int statut, Departement departement) {
    //     this.dateEmbauche = dateEmbauche;
    //     this.employe = employe;
    //     this.poste = poste;
    //     this.typeContrat = typeContrat;
    //     this.departement = departement;
    //     // this.manager = manager;
    //     this.dateDebauche = date_debauche;
    //     this.statut = statut;
    // }

    // === MÉTHODE PREPERSIST ===
    
    public InfosProfessionnelles(String id, LocalDate dateDebauche, LocalDate dateEmbauche,
            LocalDate dateDebutAssignationPoste, LocalDate dateFinAssignationPoste, LocalDateTime createdAt,
            LocalDateTime modifiedAt, int statut, double salaireBase, String matricule, String motifDepart,
            String classification, CategorieProfessionnelle categorieProfessionnelle, TypeTempsTravail typeTempsTravail,
            TypeEntree typeEntree, Manager manager, Poste poste, TypeContrat typeContrat, Employe employe,
            Departement departement) {
        this.id = id;
        this.dateDebauche = dateDebauche;
        this.dateEmbauche = dateEmbauche;
        this.dateDebutAssignationPoste = dateDebutAssignationPoste;
        this.dateFinAssignationPoste = dateFinAssignationPoste;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.statut = statut;
        this.salaireBase = salaireBase;
        this.matricule = matricule;
        this.motifDepart = motifDepart;
        this.classification = classification;
        this.categorieProfessionnelle = categorieProfessionnelle;
        this.typeTempsTravail = typeTempsTravail;
        this.typeEntree = typeEntree;
        this.manager = manager;
        this.poste = poste;
        this.typeContrat = typeContrat;
        this.employe = employe;
        this.departement = departement;
    }

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            String datePart = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
            String shortUuid = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
            this.id = "IP-" + datePart + "-" + shortUuid;
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate(){
        this.modifiedAt = LocalDateTime.now();
    }

    // === GETTERS / SETTERS ===
    // (inchangés) 
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDate getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(LocalDate dateEmbauche) { this.dateEmbauche = dateEmbauche; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }

    public Manager getManager() { return manager; }
    public void setManager(Manager manager) { this.manager = manager; }

    public Poste getPoste() { return poste; }
    public void setPoste(Poste poste) { this.poste = poste; }

    public TypeContrat getTypeContrat() { return typeContrat; }
    public void setTypeContrat(TypeContrat typeContrat) { this.typeContrat = typeContrat; }

    public Employe getEmploye() { return employe; }
    public void setEmploye(Employe employe) { this.employe = employe; }

    @Override
    public String toString() {
        return "InfosProfessionnelles{" +
                "id='" + id + '\'' +
                ", dateEmbauche=" + dateEmbauche +
                ", employe=" + (employe != null ? employe.getId() : "null") +
                '}';
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

    public LocalDate getDateDebauche() {
        return dateDebauche;
    }

    public void setDateDebauche(LocalDate dateDebauche) {
        this.dateDebauche = dateDebauche;
    }

    public LocalDate getDateDebutAssignationPoste() {
        return dateDebutAssignationPoste;
    }

    public void setDateDebutAssignationPoste(LocalDate dateDebutAssignationPoste) {
        this.dateDebutAssignationPoste = dateDebutAssignationPoste;
    }

    public LocalDate getDateFinAssignationPoste() {
        return dateFinAssignationPoste;
    }

    public void setDateFinAssignationPoste(LocalDate dateFinAssignationPoste) {
        this.dateFinAssignationPoste = dateFinAssignationPoste;
    }

    public double getSalaireBase() {
        return salaireBase;
    }

    public void setSalaireBase(double salaire_base) {
        this.salaireBase = salaire_base;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getMotifDepart() {
        return motifDepart;
    }

    public void setMotifDepart(String motifDepart) {
        this.motifDepart = motifDepart;
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public TypeEntree getTypeEntree() {
        return typeEntree;
    }

    public void setTypeEntree(TypeEntree typeEntree) {
        this.typeEntree = typeEntree;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public CategorieProfessionnelle getCategorieProfessionnelle() {
        return categorieProfessionnelle;
    }

    public void setCategorieProfessionnelle(CategorieProfessionnelle categorieProfessionnelle) {
        this.categorieProfessionnelle = categorieProfessionnelle;
    }

    public TypeTempsTravail getTypeTempsTravail() {
        return typeTempsTravail;
    }

    public void setTypeTempsTravail(TypeTempsTravail typeTempsTravail) {
        this.typeTempsTravail = typeTempsTravail;
    }

    

    

    
} 

