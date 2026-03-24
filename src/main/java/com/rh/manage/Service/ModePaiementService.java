package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.Employe;
import com.rh.manage.Model.ModePaiement;
import com.rh.manage.Model.TypePaiement;
import com.rh.manage.Repository.EmployeRepository;
import com.rh.manage.Repository.ModePaiementRepository;
import com.rh.manage.Repository.TypePaiementRepository;
import com.rh.manage.Service.UserService.ResourceNotFoundException;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ModePaiementService {
    @Autowired
    private ModePaiementRepository modePaiementRepository;
    @Autowired
    private TypePaiementRepository typePaiementRepository;
    @Autowired
    private EmployeRepository employeRepository;
    
    public ModePaiement createModePaiement(ModePaiement modePaiement)throws Exception {
        // Vérifier si l'employé existe
        Employe employe = employeRepository.findById(modePaiement.getEmploye().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé"));
        
        // Vérifier si le type de paiement existe
        TypePaiement typePaiement = typePaiementRepository.findById(modePaiement.getTypePaiement().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Type de paiement non trouvé"));
        
        // Vérifier si le numéro de compte existe déjà pour cet employé
        if (modePaiementRepository.existsByNumeroCompteAndEmployeId(
                modePaiement.getNumeroCompte(), employe.getId())) {
            throw new Exception("Ce numéro de compte existe déjà pour cet employé");
        }
        
        // Générer l'ID si nécessaire
        if (modePaiement.getId() == null || modePaiement.getId().isEmpty()) {
            modePaiement.setId(UUID.randomUUID().toString());
        }
        
        // Gérer le mode par défaut
        if (Boolean.TRUE.equals(modePaiement.getEstParDefaut())) {
            handleDefaultMode(employe.getId(), null);
        }
        
        modePaiement.setEmploye(employe);
        modePaiement.setTypePaiement(typePaiement);
        
        return modePaiementRepository.save(modePaiement);
    }

    public String calculerCleRIB(String codeBanque, String codeGuichet, String numeroCompte) {
        // Validation des entrées
        if (codeBanque == null || codeGuichet == null || numeroCompte == null) {
            throw new IllegalArgumentException("Les codes banque, guichet et numéro de compte sont requis");
        }
        
        // Nettoyer les entrées (garder seulement les chiffres)
        String codeBanqueClean = codeBanque.replaceAll("[^0-9]", "");
        String codeGuichetClean = codeGuichet.replaceAll("[^0-9]", "");
        String numeroCompteClean = numeroCompte.replaceAll("[^0-9]", "");
        
        // Vérifier les longueurs
        if (codeBanqueClean.length() != 5) {
            throw new IllegalArgumentException("Le code banque doit contenir 5 chiffres");
        }
        if (codeGuichetClean.length() != 5) {
            throw new IllegalArgumentException("Le code guichet doit contenir 5 chiffres");
        }
        if (numeroCompteClean.length() != 11) {
            throw new IllegalArgumentException("Le numéro de compte doit contenir 11 chiffres");
        }
        
        // Concaténer pour former le RIB
        String rib = codeBanqueClean + codeGuichetClean + numeroCompteClean;
        
        // Remplacer les lettres par des chiffres (au cas où)
        rib = remplacerLettres(rib);
        
        try {
            // Calcul modulo 97
            BigInteger ribNumber = new BigInteger(rib);
            BigInteger quatreVingtDixSept = BigInteger.valueOf(97);
            BigInteger reste = ribNumber.mod(quatreVingtDixSept);
            
            // Clé = 97 - reste
            int cle = 97 - reste.intValue();
            
            // Retourner sur 2 chiffres (avec zéro devant si nécessaire)
            return String.format("%02d", cle);
            
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le RIB contient des caractères non valides", e);
        }
    }
    
    /**
     * Remplace les lettres par des chiffres selon la norme bancaire
     * A=1, B=2, ..., I=9, J=1, K=2, ... (cyclique)
     */
    private String remplacerLettres(String input) {
        StringBuilder result = new StringBuilder();
        
        for (char c : input.toCharArray()) {
            if (Character.isLetter(c)) {
                // Conversion lettre -> chiffre (A=1, B=2, ..., I=9, J=1, ...)
                int val = (Character.toUpperCase(c) - 'A') % 9 + 1;
                result.append(val);
            } else {
                result.append(c);
            }
        }
        
        return result.toString();
    }
    
    /**
     * Vérifie si une clé RIB est valide pour un RIB donné
     */
    public boolean verifierCleRIB(String codeBanque, String codeGuichet, 
                                   String numeroCompte, String cleSaisie) {
        try {
            String cleCalculee = calculerCleRIB(codeBanque, codeGuichet, numeroCompte);
            return cleCalculee.equals(cleSaisie);
        } catch (Exception e) {
            return false;
        }
    }

    
    public ModePaiement updateModePaiement(String id, ModePaiement modePaiementDetails) {
        ModePaiement modePaiement = modePaiementRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Mode de paiement non trouvé avec l'id : " + id));
        
        // Vérifier si le type de paiement existe (si modifié)
        if (modePaiementDetails.getTypePaiement() != null && 
            !modePaiement.getTypePaiement().getId().equals(modePaiementDetails.getTypePaiement().getId())) {
            TypePaiement typePaiement = typePaiementRepository.findById(modePaiementDetails.getTypePaiement().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Type de paiement non trouvé"));
            modePaiement.setTypePaiement(typePaiement);
        }
        
        // Gérer le mode par défaut
        if (Boolean.TRUE.equals(modePaiementDetails.getEstParDefaut()) && 
            !Boolean.TRUE.equals(modePaiement.getEstParDefaut())) {
            handleDefaultMode(modePaiement.getEmploye().getId(), id);
        }
        
        // Mise à jour des champs
        modePaiement.setNomBanque(modePaiementDetails.getNomBanque());
        modePaiement.setCodeBanque(modePaiementDetails.getCodeBanque());
        modePaiement.setCodeGuichet(modePaiementDetails.getCodeGuichet());
        modePaiement.setNumeroCompte(modePaiementDetails.getNumeroCompte());
        modePaiement.setCleRib(modePaiementDetails.getCleRib());
        modePaiement.setTitulaireCompte(modePaiementDetails.getTitulaireCompte());
        modePaiement.setDomiciliationAgence(modePaiementDetails.getDomiciliationAgence());
        modePaiement.setEstActif(modePaiementDetails.getEstActif());
        modePaiement.setEstParDefaut(modePaiementDetails.getEstParDefaut());
        
        return modePaiementRepository.save(modePaiement);
    }
    
    private void handleDefaultMode(String employeId, String excludeId) {
        if (modePaiementRepository.existsOtherDefaultMode(employeId, excludeId)) {
            modePaiementRepository.removeDefaultFromOtherModes(employeId, excludeId);
        }
    }
    
    @Transactional(readOnly = true)
    public ModePaiement getModePaiementById(String id) {
        return modePaiementRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Mode de paiement non trouvé avec l'id : " + id));
    }
    
    @Transactional(readOnly = true)
    public List<ModePaiement> getModesPaiementByEmploye(String employeId) {
        if (!employeRepository.existsById(employeId)) {
            throw new ResourceNotFoundException("Employé non trouvé avec l'id : " + employeId);
        }
        return modePaiementRepository.findByEmployeId(employeId);
    }
    
    @Transactional(readOnly = true)
    public List<ModePaiement> getActiveModesPaiementByEmploye(String employeId) {
        return modePaiementRepository.findActiveModesByEmploye(employeId);
    }
    
    @Transactional(readOnly = true)
    public ModePaiement getDefaultModePaiementByEmploye(String employeId) {
        return modePaiementRepository.findByEmployeIdAndEstParDefautTrue(employeId)
            .orElseThrow(() -> new ResourceNotFoundException("Aucun mode de paiement par défaut trouvé pour cet employé"));
    }
    
    public ModePaiement setAsDefaultMode(String id) throws Exception{
        ModePaiement modePaiement = getModePaiementById(id);
        
        if (!Boolean.TRUE.equals(modePaiement.getEstActif())) {
            throw new Exception("Impossible de définir un mode de paiement inactif comme défaut");
        }
        
        handleDefaultMode(modePaiement.getEmploye().getId(), id);
        modePaiement.setEstParDefaut(true);
        
        return modePaiementRepository.save(modePaiement);
    }
    
    public void deleteModePaiement(String id) throws Exception {
        ModePaiement modePaiement = getModePaiementById(id);
        
        if (Boolean.TRUE.equals(modePaiement.getEstParDefaut())) {
            throw new Exception("Impossible de supprimer le mode de paiement par défaut");
        }
        
        modePaiementRepository.delete(modePaiement);
    }
    
    public ModePaiement toggleActif(String id) {
        ModePaiement modePaiement = getModePaiementById(id);
        modePaiement.setEstActif(!modePaiement.getEstActif());
        
        // Si on désactive le mode par défaut, on doit enlever le statut par défaut
        if (!Boolean.TRUE.equals(modePaiement.getEstActif()) && 
            Boolean.TRUE.equals(modePaiement.getEstParDefaut())) {
            modePaiement.setEstParDefaut(false);
        }
        
        return modePaiementRepository.save(modePaiement);
    }
}
