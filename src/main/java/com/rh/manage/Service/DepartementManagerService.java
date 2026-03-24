package com.rh.manage.Service;

import com.rh.manage.Model.DepartementManager;
import com.rh.manage.Dto.ManagerDTO;
import com.rh.manage.Model.Departement;
import com.rh.manage.Model.Manager;
import com.rh.manage.Repository.DepartementManagerRepository;
import com.rh.manage.Repository.DepartementRepository;
import com.rh.manage.Repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
// @Transactional
public class DepartementManagerService {

    // @Autowired
    // private ManagerService managerService;

    @Autowired
    private DepartementManagerRepository departementManagerRepository;
    
    @Autowired
    private DepartementRepository departementRepository;
    
    @Autowired
    private ManagerRepository managerRepository;

    @Transactional
    public void insertManagerWithDepartement(ManagerDTO managerDTO) {
        try {
            // Sauvegarder le manager

            Manager manager = managerRepository.save(managerDTO.getManager());
            // Manager manager = managerService.saveManager(managerDTO.getManager());
            
            // Récupérer le département
            Departement departement = managerDTO.getDepartement();
            
            // Créer la relation département-manager
            DepartementManager depManager = new DepartementManager();
            depManager.setDateDebut(manager.getDateDebut());
            depManager.setDateFin(manager.getDateFin());
            depManager.setDepartement(departement);
            depManager.setManager(manager); // ✅ N'OUBLIEZ PAS CETTE LIGNE !
            
            // Sauvegarder la relation
            save(depManager);
            
        } catch (Exception e) {
            // Log détaillé de l'erreur
            System.err.println("❌ Erreur lors de l'insertion manager-département: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Échec de l'insertion manager-département: " + e.getMessage(), e);
        }
    }

    // CRUD basique
    public DepartementManager save(DepartementManager departementManager) {
        return departementManagerRepository.save(departementManager);
    }

    public List<DepartementManager> findAll() {
        return departementManagerRepository.findAll();
    }

    public DepartementManager findById(String id) {
        return departementManagerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gestion département-manager non trouvée pour l'ID: " + id));
    }

    public DepartementManager update(String id, DepartementManager departementManager) {
        if (!departementManagerRepository.existsById(id)) {
            throw new RuntimeException("Gestion département-manager non trouvée pour l'ID: " + id);
        }
        departementManager.setId(id);
        return departementManagerRepository.save(departementManager);
    }

    public void deleteById(String id) {
        if (!departementManagerRepository.existsById(id)) {
            throw new RuntimeException("Gestion département-manager non trouvée pour l'ID: " + id);
        }
        departementManagerRepository.deleteById(id);
    }

    // Méthodes métier
    public List<DepartementManager> findByDepartementId(String departementId) {
        return departementManagerRepository.findByDepartementId(departementId);
    }

    public List<DepartementManager> findByManagerId(String managerId) {
        return departementManagerRepository.findByManagerId(managerId);
    }

    public DepartementManager findCurrentManagerByDepartementId(String departementId) {
        return departementManagerRepository.findByDepartementIdAndDateFinIsNull(departementId)
                .orElseThrow(() -> new RuntimeException("Aucun manager actuel trouvé pour le département ID: " + departementId));
    }

    public List<DepartementManager> findCurrentDepartementsByManagerId(String managerId) {
        return departementManagerRepository.findByManagerIdAndDateFinIsNull(managerId);
    } 

    public List<DepartementManager> findActiveGestion() {
        return departementManagerRepository.findByDateFinIsNull();
    }

    // Gestion des affectations
    @Transactional
    public DepartementManager assignManagerToDepartement(String managerId, String departementId, LocalDate dateDebut) {
        // Vérifier que le manager et le département existent
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager non trouvé: " + managerId));
        Departement departement = departementRepository.findById(departementId)
                .orElseThrow(() -> new RuntimeException("Département non trouvé: " + departementId));
        
        // Désaffecter l'ancien manager s'il existe
        unassignCurrentManagerFromDepartement(departementId, dateDebut.minusDays(1));
        
        // Créer la nouvelle affectation
        DepartementManager nouvelleAffectation = new DepartementManager();
        nouvelleAffectation.setManager(manager);
        nouvelleAffectation.setDepartement(departement);
        nouvelleAffectation.setDateDebut(dateDebut);
        nouvelleAffectation.setDateFin(null); // Affectation actuelle
        
        return departementManagerRepository.save(nouvelleAffectation);
    }

    @Transactional
    public void unassignManagerFromDepartement(String gestionId, LocalDate dateFin) {
        DepartementManager gestion = findById(gestionId);
        gestion.setDateFin(dateFin);
        departementManagerRepository.save(gestion);
    }

    @Transactional
    public void unassignCurrentManagerFromDepartement(String departementId, LocalDate dateFin) {
        Optional<DepartementManager> gestionActuelle = departementManagerRepository.findByDepartementIdAndDateFinIsNull(departementId);
        if (gestionActuelle.isPresent()) {
            DepartementManager gestion = gestionActuelle.get();
            gestion.setDateFin(dateFin);
            departementManagerRepository.save(gestion);
        }
    }

    // Vérifications
    public boolean isDepartementManaged(String departementId) {
        return departementManagerRepository.existsByDepartementIdAndDateFinIsNull(departementId);
    }

    public boolean isManagerManagingDepartement(String managerId, String departementId) {
        return departementManagerRepository.existsByManagerIdAndDepartementIdAndDateFinIsNull(managerId, departementId);
    }

    public Long countActiveDepartementsByManager(String managerId) {
        return departementManagerRepository.countActiveDepartementsByManager(managerId);
    }

    // Historique
    public List<DepartementManager> getDepartementHistory(String departementId) {
        return departementManagerRepository.findByDepartementIdOrderByDateDebutDesc(departementId);
    }

    public Optional<DepartementManager> findByManagerAndDateFinIsNull(Manager manager){
        return departementManagerRepository.findByManagerAndDateFinIsNull(manager);
    }
    
}