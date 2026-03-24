// src/main/java/com/rh/manage/service/PreparationExportEmployeService.java
package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.PreparationExportEmploye;
import com.rh.manage.Repository.PreparationExportEmployeRepository;

@Service
public class PreparationExportEmployeService {

    @Autowired
    private PreparationExportEmployeRepository repository;

    /**
     * Récupérer la configuration d'export active
     */
    public PreparationExportEmploye getActiveConfiguration() {
        return repository.findActiveConfiguration()
                .orElseGet(this::getDefaultConfiguration);
    }

    /**
     * Créer ou mettre à jour la configuration d'export
     */
    @Transactional
    public PreparationExportEmploye saveConfiguration(PreparationExportEmploye entity) {
        if (repository.existsConfiguration()) {
            // Mettre à jour la configuration existante
            PreparationExportEmploye existing = repository.findActiveConfiguration().get();
            copyProperties(entity, existing);
            return repository.save(existing);
        } else {
            // Créer une nouvelle configuration
            return repository.save(entity);
        }
    }

    /**
     * Réinitialiser la configuration aux valeurs par défaut
     */
    @Transactional
    public PreparationExportEmploye resetToDefault() {
        if (repository.existsConfiguration()) {
            repository.deleteAll();
        }
        return getDefaultConfiguration();
    }

    /**
     * Configuration par défaut
     */
    private PreparationExportEmploye getDefaultConfiguration() {
        PreparationExportEmploye config = new PreparationExportEmploye();
        
        // Colonnes activées par défaut
        config.setMatricule(true);
        config.setNom(true);
        config.setPrenom(true);
        config.setInfoProDateEmbauche(true);
        config.setInfoProSalaireBase(true);
        config.setInfoProPoste(true);
        config.setInfoProDepartement(true);
        
        // Toutes les autres colonnes sont false par défaut
        return config;
    }

    /**
     * Copier les propriétés d'une entité vers une autre
     */
    private void copyProperties(PreparationExportEmploye source, PreparationExportEmploye target) {
        // Informations personnelles
        target.setMatricule(source.isMatricule());
        target.setNom(source.isNom());
        target.setPrenom(source.isPrenom());
        target.setDateNaissance(source.isDateNaissance());
        target.setEmail(source.isEmail());
        target.setCin(source.isCin());
        target.setLieuNaissance(source.isLieuNaissance());
        target.setTelephone(source.isTelephone());
        target.setCodePostal(source.isCodePostal());
        target.setAdresse(source.isAdresse());
        target.setNumCnaps(source.isNumCnaps());
        target.setNumOstie(source.isNumOstie());
        target.setNomCompletMere(source.isNomCompletMere());
        target.setNomCompletPere(source.isNomCompletPere());
        target.setNbEnfants(source.isNbEnfants());
        target.setNomConjoint(source.isNomConjoint());
        target.setModePaiementNomBanque(source.isModePaiementNomBanque());
        target.setModePaiementCodeBanque(source.isModePaiementCodeBanque());
        target.setModePaiementCodeGuichet(source.isModePaiementCodeGuichet());

        // Informations professionnelles
        target.setInfoProDateEmbauche(source.isInfoProDateEmbauche());
        target.setInfoProSalaireBase(source.isInfoProSalaireBase());
        target.setInfoProClassification(source.isInfoProClassification());
        target.setInfoProPeriodicitePaiement(source.isInfoProPeriodicitePaiement());
        target.setInfoProCategorie(source.isInfoProCategorie());
        target.setInfoProPoste(source.isInfoProPoste());
        target.setInfoProDepartement(source.isInfoProDepartement());

        // Autres informations
        target.setSituationFamiliale(source.isSituationFamiliale());
        target.setNationalite(source.isNationalite());
        target.setSexe(source.isSexe());
        target.setEmergencyContactTelephone(source.isEmergencyContactTelephone());
        target.setEmergencyContactNom(source.isEmergencyContactNom());
        target.setEmergencyContactEmail(source.isEmergencyContactEmail());
    }
}