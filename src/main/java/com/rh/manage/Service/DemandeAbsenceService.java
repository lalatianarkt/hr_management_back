package com.rh.manage.Service;

import com.rh.manage.Model.DemandeAbsence;
import com.rh.manage.Model.Employe;
import com.rh.manage.Repository.DemandeAbsenceRepository;
import com.rh.manage.Repository.EmployeRepository;
import com.rh.manage.Repository.TokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DemandeAbsenceService {

    @Autowired
    TypeDemandeService typeDemandeService;

    @Autowired
    EmployeService employeService;

    @Autowired
    TokenService tokenService;

    @Autowired
    DemandeAbsenceRepository demandeAbsenceRepository;
    
    // Constantes pour les statuts
    public static final int STATUT_EN_ATTENTE = 0;
    public static final int STATUT_APPROUVEE = 1;
    public static final int STATUT_REJETEE = 2;
    public static final int STATUT_ANNULEE = 3;
    
    /**
     * Créer une nouvelle demande d'absence
     */
    // @Transactional
    public DemandeAbsence createDemande(DemandeAbsence demande) {
        try {
            // Valider la demande
            // validateDemande(demande);

            System.out.println("tafiditra ato eeeeeeeeeeee+++++++++++++++");
            
            // Définir le statut
            demande.setStatut(STATUT_EN_ATTENTE);
            
            // Sauvegarder
            DemandeAbsence savedDemande = demandeAbsenceRepository.save(demande);
            
            return savedDemande;
            
        } catch (Exception e) {
            // Logger l'erreur
            System.out.println("Erreur lors de la création de la demande: " + e.getMessage());
            e.printStackTrace();
            
            // Relancer l'exception
            throw e;
        }
    }
    
    /**
     * Mettre à jour une demande d'absence
     */
    @Transactional
    public DemandeAbsence updateDemande(String id, DemandeAbsence demandeDetails) {
        try{
            // log.info("Mise à jour de la demande d'absence ID: {}", id);
            System.out.println("ato anaty update e+++++++++++++++++");
            DemandeAbsence demande = getDemandeById(id);

            System.out.println("azo ity : " + demande.getId());
            
            // Validation
           
            System.out.println("ato+++++++++++");
            
            // Vérifier les chevauchements (exclure la demande actuelle)
            // checkOverlappingDemandesForUpdate(id, demandeDetails);
            
            // Mettre à jour les champs
            demande.setDateHeureAbsenceDebut(demandeDetails.getDateHeureAbsenceDebut());
            demande.setDateHeureAbsenceFin(demandeDetails.getDateHeureAbsenceFin());
            demande.setTypeDemande(demandeDetails.getTypeDemande());
            // demande.setEmploye(deman);

            System.out.println("demande eeeeeeeeeee");
            // demande.setModifiedAt(LocalDateTime.now());
            
            // Réinitialiser le statut si les dates changent
            if (!demande.getDateHeureAbsenceDebut().equals(demandeDetails.getDateHeureAbsenceDebut()) ||
                !demande.getDateHeureAbsenceFin().equals(demandeDetails.getDateHeureAbsenceFin())) {
                demande.setStatut(STATUT_EN_ATTENTE);
                // notificationService.notifyManagerDemandeUpdated(demande);
            }
             validateDemande(demande);
            demandeAbsenceRepository.save(demande);
        } catch(Exception e){
            e.printStackTrace();
            System.out.println("error : " + e.getMessage());
        }
        return demandeDetails;
    }
    
    /**
     * Approuver une demande d'absence
     */
    @Transactional
    public DemandeAbsence approveDemande(String id, String approverId) {
        // log.info("Approbation de la demande d'absence ID: {} par: {}", id, approverId);
        
        DemandeAbsence demande = getDemandeById(id);
        
        // Vérifier que la demande est en attente
        if (!demande.isEnAttente()) {
            throw new IllegalStateException("Seules les demandes en attente peuvent être approuvées");
        }
        
        // Mettre à jour le statut
        demande.setStatut(STATUT_APPROUVEE);
        demande.setModifiedAt(LocalDateTime.now());
        
        DemandeAbsence updated = demandeAbsenceRepository.save(demande);
        
        // Notifier l'employé
        // notificationService.notifyEmployeeDemandeApprouved(updated);
        
        // log.info("Demande d'absence ID: {} approuvée", id);
        return updated;
    }
    
    /**
     * Rejeter une demande d'absence
     */
    @Transactional
    public DemandeAbsence rejectDemande(String id, String rejecterId, String raison) {
        // log.info("Rejet de la demande d'absence ID: {} par: {}", id, rejecterId);
        
        DemandeAbsence demande = getDemandeById(id);
        
        // Vérifier que la demande est en attente
        if (!demande.isEnAttente()) {
            throw new IllegalStateException("Seules les demandes en attente peuvent être rejetées");
        }
        
        // Mettre à jour le statut
        demande.setStatut(STATUT_REJETEE);
        demande.setModifiedAt(LocalDateTime.now());
        
        DemandeAbsence updated = demandeAbsenceRepository.save(demande);
        
        // Notifier l'employé avec la raison
        // notificationService.notifyEmployeeDemandeRejected(updated, raison);
        
        // log.info("Demande d'absence ID: {} rejetée. Raison: {}", id, raison);
        return updated;
    }
    
    /**
     * Annuler une demande d'absence
     */
    @Transactional
    public DemandeAbsence cancelDemande(String id, String employeeId) {
        // log.info("Annulation de la demande d'absence ID: {} par l'employé: {}", id, employeeId);
        
        DemandeAbsence demande = getDemandeById(id);
        
        // Vérifier que l'employé est bien le propriétaire
        if (!demande.getEmploye().getId().equals(employeeId)) {
            throw new SecurityException("Seul le propriétaire de la demande peut l'annuler");
        }
        
        // Vérifier que la demande n'est pas déjà traitée
        if (demande.isApprouvee() || demande.isRejetee()) {
            throw new IllegalStateException("Les demandes déjà traitées ne peuvent pas être annulées");
        }
        
        // Mettre à jour le statut
        demande.setStatut(STATUT_ANNULEE);
        demande.setModifiedAt(LocalDateTime.now());
        
        DemandeAbsence updated = demandeAbsenceRepository.save(demande);
        
        // Notifier le manager
        // notificationService.notifyManagerDemandeCancelled(updated);
        
        // log.info("Demande d'absence ID: {} annulée", id);
        return updated;
    }
    
    /**
     * Obtenir une demande par son ID
     */
    public DemandeAbsence getDemandeById(String id) {
        return demandeAbsenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande d'absence non trouvée avec ID: " + id));
    }
    
    /**
     * Obtenir toutes les demandes d'un employé
     */
    public List<DemandeAbsence> getDemandesByEmploye(String employeId) {
        return demandeAbsenceRepository.findByEmployeId(employeId);
    }
    
    /**
     * Obtenir les demandes en attente d'un employé
     */
    public List<DemandeAbsence> getPendingDemandesByEmploye(String employeId) {
        return demandeAbsenceRepository.findByEmployeIdAndStatut(employeId, STATUT_EN_ATTENTE);
    }
    
    /**
     * Obtenir toutes les demandes en attente
     */
    public List<DemandeAbsence> getAllPendingDemandes() {
        return demandeAbsenceRepository.findPendingDemandes();
    }
    
    /**
     * Obtenir les demandes pour approbation par un manager
     */
    // public List<DemandeAbsence> getDemandesForManager(String managerId) {
    //     return demandeAbsenceRepository.findByManagerId(managerId);
    // }
    
    /**
     * Valider une période d'absence pour un employé
     */
    public boolean isPeriodAvailable(String employeId, LocalDateTime debut, LocalDateTime fin) {
        List<DemandeAbsence> overlapping = demandeAbsenceRepository.findOverlappingAbsences(employeId, debut, fin);
        return overlapping.isEmpty();
    }
    
    /**
     * Valider les données de la demande
     */
    private void validateDemande(DemandeAbsence demande) {
        if (demande.getDateHeureAbsenceDebut() == null || demande.getDateHeureAbsenceFin() == null) {
            throw new IllegalArgumentException("Les dates de début et fin sont obligatoires");
        }
        
        if (demande.getDateHeureAbsenceDebut().isAfter(demande.getDateHeureAbsenceFin())) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin");
        }
        
        if (demande.getDateHeureAbsenceDebut().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La date de début ne peut pas être dans le passé");
        }
        
        if (demande.getEmploye() == null || demande.getEmploye().getId() == null) {
            throw new IllegalArgumentException("L'employé est obligatoire");
        }
        
        if (demande.getTypeDemande() == null || demande.getTypeDemande().getId() == null) {
            throw new IllegalArgumentException("Le type de demande est obligatoire");
        }
        
        employeService.getById(demande.getEmploye().getId())
            .orElseThrow(() -> new RuntimeException("Employé non trouvé: " + demande.getEmploye().getId()));
        
        typeDemandeService.getTypeDemandeById(demande.getTypeDemande().getId());
    }
    
    /**
     * Vérifier les chevauchements de demandes
     */
    // private void checkOverlappingDemandes(DemandeAbsence demande) {
    //     List<DemandeAbsence> overlapping = demandeAbsenceRepository.findOverlappingAbsences(
    //             demande.getEmploye().getId(),
    //             demande.getDateHeureAbsenceDebut(),
    //             demande.getDateHeureAbsenceFin());
        
    //     if (!overlapping.isEmpty()) {
    //         throw new IllegalStateException("Chevauchement avec une autre demande d'absence existante");
    //     }
    // }
    
    /**
     * Vérifier les chevauchements pour la mise à jour
     */
    private void checkOverlappingDemandesForUpdate(String demandeId, DemandeAbsence demandeDetails) {
        List<DemandeAbsence> overlapping = demandeAbsenceRepository.findOverlappingAbsencesExcludingCurrent(
                demandeId,
                demandeDetails.getEmploye().getId(),
                demandeDetails.getDateHeureAbsenceDebut(),
                demandeDetails.getDateHeureAbsenceFin());
        
        if (!overlapping.isEmpty()) {
            throw new IllegalStateException("Chevauchement avec une autre demande d'absence existante");
        }
    }
    
    /**
     * Obtenir les statistiques des demandes
     */
    // public DemandeAbsenceStats getStats(int mois, int annee) {
    //     List<Object[]> stats = demandeAbsenceRepository.getStatsByMonth(mois, annee);
        
    //     DemandeAbsenceStats statsDto = new DemandeAbsenceStats();
    //     statsDto.setMois(mois);
    //     statsDto.setAnnee(annee);
        
    //     for (Object[] stat : stats) {
    //         Integer count = ((Number) stat[0]).intValue();
    //         Integer statut = (Integer) stat[1];
            
    //         switch (statut) {
    //             case STATUT_EN_ATTENTE:
    //                 statsDto.setEnAttente(count);
    //                 break;
    //             case STATUT_APPROUVEE:
    //                 statsDto.setApprouvees(count);
    //                 break;
    //             case STATUT_REJETEE:
    //                 statsDto.setRejetees(count);
    //                 break;
    //             case STATUT_ANNULEE:
    //                 statsDto.setAnnulees(count);
    //                 break;
    //         }
    //     }
        
    //     return statsDto;
    // }
    
    // /**
    //  * DTO pour les statistiques
    //  */
    // @Data
    // @Builder
    // public static class DemandeAbsenceStats {
    //     private int mois;
    //     private int annee;
    //     private int enAttente;
    //     private int approuvees;
    //     private int rejetees;
    //     private int annulees;
        
    //     public int getTotal() {
    //         return enAttente + approuvees + rejetees + annulees;
    //     }
    // }
}