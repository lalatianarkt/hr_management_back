// src/main/java/com/rh/manage/Model/PreparationExportEmploye.java
package com.rh.manage.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "preparation_export_employe")
public class PreparationExportEmploye {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Informations personnelles de l'employé
    @Column(name = "is_matricule")
    private boolean isMatricule = true;

    @Column(name = "is_nom")
    private boolean isNom = true;

    @Column(name = "is_prenom")
    private boolean isPrenom = true;

    @Column(name = "is_date_naissance")
    private boolean isDateNaissance = false;

    @Column(name = "is_email")
    private boolean isEmail = false;

    @Column(name = "is_cin")
    private boolean isCin = false;

    @Column(name = "is_lieu_naissance")
    private boolean isLieuNaissance = false;

    @Column(name = "is_telephone")
    private boolean isTelephone = false;

    @Column(name = "is_code_postal")
    private boolean isCodePostal = false;

    @Column(name = "is_adresse")
    private boolean isAdresse = false;

    @Column(name = "is_num_cnaps")
    private boolean isNumCnaps = false;

    @Column(name = "is_num_ostie")
    private boolean isNumOstie = false;

    @Column(name = "is_nom_complet_mere")
    private boolean isNomCompletMere = false;

    @Column(name = "is_nom_complet_pere")
    private boolean isNomCompletPere = false;

    @Column(name = "is_nb_enfants")
    private boolean isNbEnfants = false;

    @Column(name = "is_nom_conjoint")
    private boolean isNomConjoint = false;

    @Column(name = "is_mode_paiement_nom_banque")
    private boolean isModePaiementNomBanque = false;

    @Column(name = "is_mode_paiement_code_banque")
    private boolean isModePaiementCodeBanque = false;

    @Column(name = "is_mode_paiement_code_guichet")
    private boolean isModePaiementCodeGuichet = false;

    // Informations professionnelles
    @Column(name = "is_info_pro_date_embauche")
    private boolean isInfoProDateEmbauche = true;

    @Column(name = "is_info_pro_salaire_base")
    private boolean isInfoProSalaireBase = true;

    @Column(name = "is_info_pro_classification")
    private boolean isInfoProClassification = false;

    @Column(name = "is_info_pro_periodicite_paiement")
    private boolean isInfoProPeriodicitePaiement = false;

    @Column(name = "is_info_pro_categorie")
    private boolean isInfoProCategorie = false;

    @Column(name = "is_info_pro_poste")
    private boolean isInfoProPoste = true;

    @Column(name = "is_info_pro_departement")
    private boolean isInfoProDepartement = true;

    // Autres informations
    @Column(name = "is_situation_familiale")
    private boolean isSituationFamiliale = false;

    @Column(name = "is_nationalite")
    private boolean isNationalite = false;

    @Column(name = "is_sexe")
    private boolean isSexe = false;

    @Column(name = "is_emergency_contact_telephone")
    private boolean isEmergencyContactTelephone = false;

    @Column(name = "is_emergency_contact_nom")
    private boolean isEmergencyContactNom = false;

    @Column(name = "is_emergency_contact_email")
    private boolean isEmergencyContactEmail = false;

    // Dates
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    // Constructeurs
    public PreparationExportEmploye() {}

    // Getters et Setters (à générer)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public boolean isMatricule() { return isMatricule; }
    public void setMatricule(boolean matricule) { isMatricule = matricule; }

    public boolean isNom() { return isNom; }
    public void setNom(boolean nom) { isNom = nom; }

    public boolean isPrenom() { return isPrenom; }
    public void setPrenom(boolean prenom) { isPrenom = prenom; }

    public boolean isDateNaissance() { return isDateNaissance; }
    public void setDateNaissance(boolean dateNaissance) { isDateNaissance = dateNaissance; }

    public boolean isEmail() { return isEmail; }
    public void setEmail(boolean email) { isEmail = email; }

    public boolean isCin() { return isCin; }
    public void setCin(boolean cin) { isCin = cin; }

    public boolean isLieuNaissance() { return isLieuNaissance; }
    public void setLieuNaissance(boolean lieuNaissance) { isLieuNaissance = lieuNaissance; }

    public boolean isTelephone() { return isTelephone; }
    public void setTelephone(boolean telephone) { isTelephone = telephone; }

    public boolean isCodePostal() { return isCodePostal; }
    public void setCodePostal(boolean codePostal) { isCodePostal = codePostal; }

    public boolean isAdresse() { return isAdresse; }
    public void setAdresse(boolean adresse) { isAdresse = adresse; }

    public boolean isNumCnaps() { return isNumCnaps; }
    public void setNumCnaps(boolean numCnaps) { isNumCnaps = numCnaps; }

    public boolean isNumOstie() { return isNumOstie; }
    public void setNumOstie(boolean numOstie) { isNumOstie = numOstie; }

    public boolean isNomCompletMere() { return isNomCompletMere; }
    public void setNomCompletMere(boolean nomCompletMere) { isNomCompletMere = nomCompletMere; }

    public boolean isNomCompletPere() { return isNomCompletPere; }
    public void setNomCompletPere(boolean nomCompletPere) { isNomCompletPere = nomCompletPere; }

    public boolean isNbEnfants() { return isNbEnfants; }
    public void setNbEnfants(boolean nbEnfants) { isNbEnfants = nbEnfants; }

    public boolean isNomConjoint() { return isNomConjoint; }
    public void setNomConjoint(boolean nomConjoint) { isNomConjoint = nomConjoint; }

    public boolean isModePaiementNomBanque() { return isModePaiementNomBanque; }
    public void setModePaiementNomBanque(boolean modePaiementNomBanque) { isModePaiementNomBanque = modePaiementNomBanque; }

    public boolean isModePaiementCodeBanque() { return isModePaiementCodeBanque; }
    public void setModePaiementCodeBanque(boolean modePaiementCodeBanque) { isModePaiementCodeBanque = modePaiementCodeBanque; }

    public boolean isModePaiementCodeGuichet() { return isModePaiementCodeGuichet; }
    public void setModePaiementCodeGuichet(boolean modePaiementCodeGuichet) { isModePaiementCodeGuichet = modePaiementCodeGuichet; }

    public boolean isInfoProDateEmbauche() { return isInfoProDateEmbauche; }
    public void setInfoProDateEmbauche(boolean infoProDateEmbauche) { isInfoProDateEmbauche = infoProDateEmbauche; }

    public boolean isInfoProSalaireBase() { return isInfoProSalaireBase; }
    public void setInfoProSalaireBase(boolean infoProSalaireBase) { isInfoProSalaireBase = infoProSalaireBase; }

    public boolean isInfoProClassification() { return isInfoProClassification; }
    public void setInfoProClassification(boolean infoProClassification) { isInfoProClassification = infoProClassification; }

    public boolean isInfoProPeriodicitePaiement() { return isInfoProPeriodicitePaiement; }
    public void setInfoProPeriodicitePaiement(boolean infoProPeriodicitePaiement) { isInfoProPeriodicitePaiement = infoProPeriodicitePaiement; }

    public boolean isInfoProCategorie() { return isInfoProCategorie; }
    public void setInfoProCategorie(boolean infoProCategorie) { isInfoProCategorie = infoProCategorie; }

    public boolean isInfoProPoste() { return isInfoProPoste; }
    public void setInfoProPoste(boolean infoProPoste) { isInfoProPoste = infoProPoste; }

    public boolean isInfoProDepartement() { return isInfoProDepartement; }
    public void setInfoProDepartement(boolean infoProDepartement) { isInfoProDepartement = infoProDepartement; }

    public boolean isSituationFamiliale() { return isSituationFamiliale; }
    public void setSituationFamiliale(boolean situationFamiliale) { isSituationFamiliale = situationFamiliale; }

    public boolean isNationalite() { return isNationalite; }
    public void setNationalite(boolean nationalite) { isNationalite = nationalite; }

    public boolean isSexe() { return isSexe; }
    public void setSexe(boolean sexe) { isSexe = sexe; }

    public boolean isEmergencyContactTelephone() { return isEmergencyContactTelephone; }
    public void setEmergencyContactTelephone(boolean emergencyContactTelephone) { isEmergencyContactTelephone = emergencyContactTelephone; }

    public boolean isEmergencyContactNom() { return isEmergencyContactNom; }
    public void setEmergencyContactNom(boolean emergencyContactNom) { isEmergencyContactNom = emergencyContactNom; }

    public boolean isEmergencyContactEmail() { return isEmergencyContactEmail; }
    public void setEmergencyContactEmail(boolean emergencyContactEmail) { isEmergencyContactEmail = emergencyContactEmail; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }
}