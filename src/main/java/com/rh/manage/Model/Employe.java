package com.rh.manage.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rh.manage.converter.EtatCivilConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "employe")
public class Employe {
    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "statut", nullable = false)
    private int statut;

    @Column(name = "nom", length = 100, nullable = false)
    private String nom;

    @Column(name = "prenom", length = 250, nullable = false)
    private String prenom;

    @Column(name = "date_naissance", nullable = false)
    private LocalDate dateNaissance;

    @Column(name = "lieu_naissance", length = 250, nullable = false)
    private String lieuNaissance;

    @Column(name = "telephone", length = 12, nullable = false)
    private String telephone;

    @Column(name = "email", length = 100, nullable = false)
    private String email;

    @Column(name = "adresse", length = 255, nullable = false)
    private String adresse;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "nom_mere", length = 255)
    private String nomMere;

    @Column(name = "nom_pere", length = 255)
    private String nomPere;

    @Column(name = "nb_enfants")
    private Integer nbEnfants;

    @Column(name = "nom_conjoint", length = 255)
    private String nomConjoint;

    // ✅ CORRECT pour VARCHAR en base
    @Enumerated(EnumType.STRING)  // Convertit l'enum Java en String
    @Column(name = "etat_civil", nullable = false, length = 20)
    private EtatCivil etatCivil = EtatCivil.CELIBATAIRE; 

    @Column(name = "cin", nullable = false, unique = true, length = 12)
    private String cin;

    @Column(name = "num_cnaps", length = 50)
    private String numCnaps;

    @Column(name = "num_ostie", length = 50)
    private String numOstie;

    @Column(name = "commentaire")
    private String commentaire;

    @Column(name = "code_postal", nullable = false)
    private int codePostal = 0;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name= "id_region", referencedColumnName = "id", nullable = false)
    private Region region;

    // === Relations ===
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_emergency_contact", referencedColumnName = "id", nullable = false)
    private EmergencyContact emergencyContact;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_sexe", referencedColumnName = "id", nullable = false)
    private Sexe sexe;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_nationalite", referencedColumnName = "id", nullable = false)
    private Nationalite nationalite;    

    // === Constructeurs ===
    public Employe() {
        // Constructeur par défaut pour JPA
    }

    // === Méthode PrePersist pour initialiser ID et createdAt ===
    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = generateCustomId(LocalDate.now());
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        // Validation métier : si marié, doit avoir un nom de conjoint
        if (this.etatCivil == EtatCivil.MARIE && (this.nomConjoint == null || this.nomConjoint.trim().isEmpty())) {
            throw new IllegalStateException("Un employé marié doit avoir un nom de conjoint");
        }
        // Validation : si pas marié, pas de nom de conjoint
        if (this.etatCivil != EtatCivil.MARIE && this.nomConjoint != null) {
            this.nomConjoint = null;
        }
    }

    public Employe(String id, int statut, String nom, String prenom, LocalDate dateNaissance, String lieuNaissance,
            String telephone, String email, String adresse, LocalDateTime createdAt, LocalDateTime modifiedAt,
            String nomMere, String nomPere, Integer nbEnfants, String nomConjoint, EtatCivil etatCivil, String cin,
            String numCnaps, String numOstie, int codePostal, Region region, EmergencyContact emergencyContact,
            Sexe sexe, Nationalite nationalite) {
        this.id = id;
        this.statut = statut;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.lieuNaissance = lieuNaissance;
        this.telephone = telephone;
        this.email = email;
        this.adresse = adresse;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.nomMere = nomMere;
        this.nomPere = nomPere;
        this.nbEnfants = nbEnfants;
        this.nomConjoint = nomConjoint;
        this.etatCivil = etatCivil;
        this.cin = cin;
        this.numCnaps = numCnaps;
        this.numOstie = numOstie;
        this.codePostal = codePostal;
        this.region = region;
        this.emergencyContact = emergencyContact;
        this.sexe = sexe;
        this.nationalite = nationalite;
    }

    // ✅ Génération d'un ID du type EMP-20251029-ABC123
    private String generateCustomId(LocalDate date) {
        String datePart = date != null
                ? date.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                : LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "EMP-" + datePart + "-" + randomPart;
    }

    // === Méthodes utilitaires ===
    public int getAge() {
        if (dateNaissance == null) {
            return 0;
        }
        return LocalDate.now().getYear() - dateNaissance.getYear();
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    // Validation métier pour la cohérence des données
    public boolean isDonneesValides() {
        // Vérification de l'état civil
        if (etatCivil == EtatCivil.MARIE) {
            return nomConjoint != null && !nomConjoint.trim().isEmpty();
        } else {
            return nomConjoint == null || nomConjoint.trim().isEmpty();
        }
    }

    // === Getters / Setters ===
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate dateNaissance) { this.dateNaissance = dateNaissance; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }

    public String getNomMere() { return nomMere; }
    public void setNomMere(String nomMere) { this.nomMere = nomMere; }

    public String getNomPere() { return nomPere; }
    public void setNomPere(String nomPere) { this.nomPere = nomPere; }

    public Integer getNbEnfants() { return nbEnfants; }
    public void setNbEnfants(Integer nbEnfants) { 
        this.nbEnfants = nbEnfants; 
    }

    public String getNomConjoint() { return nomConjoint; }
    public void setNomConjoint(String nomConjoint) { 
        this.nomConjoint = nomConjoint;
        // Mettre à jour l'état civil si nécessaire
        if (nomConjoint != null && !nomConjoint.trim().isEmpty() && this.etatCivil != EtatCivil.MARIE) {
            this.etatCivil = EtatCivil.MARIE;
        }
    }

    public EtatCivil getEtatCivil() { return etatCivil; }
    public void setEtatCivil(EtatCivil etatCivil) { 
        this.etatCivil = etatCivil;
        // Si l'état civil n'est plus "MARIE", supprimer le nom du conjoint
        if (etatCivil != EtatCivil.MARIE) {
            this.nomConjoint = null;
        }
    }

    public String getCin() { return cin; }
    public void setCin(String cin) { this.cin = cin; }

    public String getNumCnaps() { return numCnaps; }
    public void setNumCnaps(String numCnaps) { this.numCnaps = numCnaps; }

    public EmergencyContact getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(EmergencyContact emergencyContact) { this.emergencyContact = emergencyContact; }

    public Sexe getSexe() { return sexe; }
    public void setSexe(Sexe sexe) { this.sexe = sexe; }

    public Nationalite getNationalite() { return nationalite; }
    public void setNationalite(Nationalite nationalite) { this.nationalite = nationalite; }

    public String getLieuNaissance() { return lieuNaissance; }
    public void setLieuNaissance(String lieuNaissance) { this.lieuNaissance = lieuNaissance; }

    public int getStatut() { return statut; }
    public void setStatut(int statut) { this.statut = statut; }

    public String getNumOstie() {
        return numOstie;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public void setNumOstie(String numOstie) {
        this.numOstie = numOstie;
    }

    public int getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
    public String toString() {
        return "Employe{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", dateNaissance=" + dateNaissance +
                ", lieuNaissance='" + lieuNaissance + '\'' +
                ", email='" + email + '\'' +
                ", cin=" + cin +
                ", etatCivil=" + etatCivil +
                ", createdAt=" + createdAt +
                '}';
    }
}