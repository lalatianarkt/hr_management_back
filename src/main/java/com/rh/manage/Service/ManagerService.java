package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.BooleanNode;
import com.rh.manage.Model.Departement;
import com.rh.manage.Model.DepartementManager;
import com.rh.manage.Model.Employe;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Model.Manager;
import com.rh.manage.Model.Token;
import com.rh.manage.Model.TypeUser;
import com.rh.manage.Model.User;
// import com.rh.manage.Model.ManagerEmploye;
import com.rh.manage.Repository.DepartementManagerRepository;
import com.rh.manage.Repository.DepartementRepository;
import com.rh.manage.Repository.InfosProfessionnellesRepository;
import com.rh.manage.Repository.ManagerRepository;
import com.rh.manage.Repository.TypeUserRepository;
import com.rh.manage.Repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ManagerService {
    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    DepartementManagerRepository departementManagerRepository;

    @Autowired 
    InfosProfessionnellesService infosProfessionnellesService;

    @Autowired 
    DepartementManagerService departementManagerService;

    @Autowired
    DepartementRepository departementRepository;

    @Autowired
    InfosProfessionnellesRepository infosProfessionnellesRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TypeUserRepository typeUserRepository;

    @Autowired
    private UserRoleService userRoleService;

    public Boolean isManager(Employe employe){
        if(managerRepository.findByEmployeId(employe.getId()).isPresent()){
            return true;
        }
        return false;
    }

    @Transactional
    public Manager affecter(Manager manager, String userId) throws Exception {
        System.out.println("dateDebut : " + manager.getDateDebut());
        System.out.println("commentaite : " + manager.getCommentaire());
        System.out.println("ndriiii : ");
        List<InfosProfessionnelles> les_infos_pro = infosProfessionnellesService.findAllEmpActiveByIdDepartement
        (manager.getDepartement().getId());
        Optional<User> userActuel = userRepository.findById(userId);
        TypeUser typeUserManager =  typeUserRepository.findByType("Manager");
        if(findManagerActuelByDepartement(manager.getDepartement().getId()) == null){
            Manager managerInserted = managerRepository.save(manager);
            List<InfosProfessionnelles> les_infos_to_update = new ArrayList<>();
            if(les_infos_pro.size() > 0){
                for (InfosProfessionnelles infosProfessionnelles : les_infos_pro) {
                    if(!infosProfessionnelles.getEmploye().getId().equalsIgnoreCase(manager.getEmploye().getId())){
                        infosProfessionnelles.setManager(managerInserted);
                        les_infos_to_update.add(infosProfessionnelles);
                    } 
                }
                infosProfessionnellesRepository.saveAll(les_infos_to_update);
            }
            if(userActuel.isPresent()){
                if(!userRoleService.hasRole(userActuel.get().getId(), "Manager")){
                    userRoleService.assignRole(userActuel.get().getId(), typeUserManager);
                }
            }
        } else{
            throw new Exception("Un manager existe déja pour ce département");
        }
        return manager;
    }

    public List<InfosProfessionnelles> findAllInfosParManager(String userId) {
        User managerUser = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!userRoleService.hasRole(managerUser.getId(), "Manager")) {
            throw new RuntimeException("Accès réservé aux managers");
        }

        Manager manager = getManagerByEmploye(managerUser.getEmploye().getId());
        return infosProfessionnellesService.findAllEmpActiveByIdDepartement(
            manager.getDepartement().getId()
        );
    }


    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    public Optional<Manager> getManagerById(String id) {
        return managerRepository.findById(id);
    }

    public Manager saveManager(Manager manager) {
        return managerRepository.save(manager);
    }

    public void deleteManager(String id) {
        managerRepository.deleteById(id);
    }

    public Manager getManagerByToken(String token){
        // 1. Vérifier que le token n'est pas null ou vide
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token manquant");
        } 

        // 2. Récupérer et valider le token
        Token gottenToken = tokenService.getTokenByToken(token);
        System.out.println("hitany ve le token ? : " + gottenToken.getTokenGenere());
        if (gottenToken == null) {
            throw new RuntimeException("Token invalide ou introuvable");
        }
        
        // 3. Vérifier que le token est actif
        if (gottenToken.getIsActive() != null && gottenToken.getIsActive() == 0) {
            throw new RuntimeException("Token désactivé");
        }
        
        // 4. Vérifier l'expiration du token
        if (gottenToken.getExpiresAt() != null && 
            LocalDateTime.now().isAfter(gottenToken.getExpiresAt())) {
            throw new RuntimeException("Token expiré");
        } 

        // 5. Récupérer l'utilisateur
        User managerActuel = gottenToken.getUser();
        System.out.println("iza no tomplé token ary e, le user : " + managerActuel.getEmploye().getNom());
        if (managerActuel == null) {
            throw new RuntimeException("Utilisateur non trouvé pour ce token");
        }
        
        // 6. Vérifier que c'est bien un manager (optionnel mais recommandé)
        if (!userRoleService.hasRole(managerActuel.getId(), "Manager")) {
            throw new RuntimeException("Accès réservé aux managers");
        }
        System.out.println("tena manager ve izy e ? " + userRoleService.hasRole(managerActuel.getId(), "Manager"));
        
        // 7. Vérifier que l'utilisateur a un employé associé
        if (managerActuel.getEmploye() == null) {
            throw new RuntimeException("Aucun employé associé à cet utilisateur");
        }
        
        return getManagerByEmploye(managerActuel.getEmploye().getId());
    }

    public Map<String, Object> archiverManager(String id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Manager non trouvé"));
            
            manager.setStatut(1);
            manager.setDateFin(LocalDate.now()); // ✅ DATE DE FIN DANS MANAGER
            manager.setModifiedAt(LocalDateTime.now());
            managerRepository.save(manager);
            result.put("success", true);
            result.put("message", "Manager archivé avec succès");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Erreur lors de l'archivage: " + e.getMessage());
        }
        return result;
    }

    public Map<String, Object> reactiverManager(String id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Manager non trouvé"));
            
            manager.setStatut(0); // 0 = actif
            manager.setModifiedAt(LocalDateTime.now()); 
            manager.setDateFin(null);
            managerRepository.save(manager);
            
            result.put("success", true);
            result.put("message", "Manager réactivé avec succès");
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Erreur lors de la réactivation: " + e.getMessage());
        }
        return result;
    }

    @Transactional
    public Map<String, Object> updateStatutManager(String managerId, Integer nouveauStatut) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            int updated = managerRepository.updateStatut(
                managerId, 
                nouveauStatut, 
                LocalDateTime.now()
            );
            
            if (updated > 0) {
                result.put("success", true);
                result.put("message", "Statut du manager mis à jour avec succès");
                result.put("managerId", managerId);
                result.put("nouveauStatut", nouveauStatut);
                result.put("modifiedAt", LocalDateTime.now());
            } else {
                result.put("success", false);
                result.put("message", "Échec de la mise à jour du statut");
            }
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Erreur lors de la mise à jour: " + e.getMessage());
        }
        
        return result;
    }

    @Transactional
    public Map<String, Object> affecterManagerADepartement(String managerId, String departementId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Optional<Manager> managerOpt = managerRepository.findById(managerId);
            if (managerOpt.isEmpty()) {
                result.put("success", false);
                result.put("message", "Manager non trouvé");
                return result;
            }

            Manager manager = managerOpt.get();
            
            Optional<Departement> departementOpt = departementRepository.findById(departementId);
            if (departementOpt.isEmpty()) {
                result.put("success", false);
                result.put("message", "Département non trouvé");
                return result;
            }
            
            int employesAffectes = 0;
            result.put("success", true);
            result.put("message", String.format(
                "✅ Manager %s %s affecté à %d employé(s) du département %s",
                manager.getEmploye().getPrenom(),
                manager.getEmploye().getNom(),
                employesAffectes,
                departementOpt.get().getNom()
            ));
            result.put("employesAffectes", employesAffectes);
            result.put("manager", manager.getEmploye().getPrenom() + " " + manager.getEmploye().getNom());
            result.put("departement", departementOpt.get().getNom());

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Erreur lors de l'affectation du manager: " + e.getMessage());
        }
        
        return result;
    }

    @Transactional
    public Map<String, Object> removeManagerFromEmployees(String managerId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            Optional<Manager> managerOpt = managerRepository.findById(managerId);
            if (managerOpt.isEmpty()) {
                result.put("success", false);
                result.put("message", "Manager non trouvé");
                return result;
            }

            Manager manager = managerOpt.get();
            List<InfosProfessionnelles> employesAvant = managerRepository.findEmployesByManager(managerId);
            int nombreEmployes = employesAvant.size();

            if (nombreEmployes == 0) {
                result.put("success", true);
                result.put("message", "Aucun employé n'était assigné à ce manager");
                result.put("employesAffectes", 0);
                return result;
            }

            int employesModifies = managerRepository.removeManagerFromEmployees(managerId);

            List<InfosProfessionnelles> employesApres = managerRepository.findEmployesByManager(managerId);
            int employesRestants = employesApres.size();

            if (employesRestants == 0) {
                result.put("success", true);
                result.put("message", String.format(
                    "✅ Manager retiré de %d employé(s) avec succès", 
                    employesModifies
                ));
                result.put("employesAffectes", employesModifies);
                result.put("managerNom", manager.getEmploye().getPrenom() + " " + manager.getEmploye().getNom());
            } else {
                result.put("success", false);
                result.put("message", String.format(
                    "❌ Erreur: %d employé(s) n'ont pas pu être mis à jour", 
                    employesRestants
                ));
                result.put("employesAffectes", employesModifies);
                result.put("employesRestants", employesRestants);
            }

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Erreur lors du retrait du manager des employés: " + e.getMessage());
        }
        
        return result;
    }

    public Map<String, Object> getEmployesByManager(String managerId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            List<InfosProfessionnelles> infosPro = managerRepository.findEmployesByManager(managerId);
            
            List<Map<String, Object>> employes = infosPro.stream()
                .map(ip -> {
                    Map<String, Object> emp = new HashMap<>();
                    emp.put("id", ip.getEmploye().getId());
                    emp.put("nom", ip.getEmploye().getNom());
                    emp.put("prenom", ip.getEmploye().getPrenom());
                    // emp.put("matricule", ip.getEmploye().getMatricule());
                    emp.put("email", ip.getEmploye().getEmail());
                    return emp;
                })
                .collect(Collectors.toList());
            
            result.put("success", true);
            result.put("employes", employes);
            result.put("nombreEmployes", employes.size());
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Erreur lors de la récupération des employés: " + e.getMessage());
        }
        
        return result;
    }

    @Transactional
    public Manager updateManagerWithDepartment(String managerId, Manager updatedManager, String nouveauDepartementId) {
        // 1. Récupérer le manager existant
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager non trouvé"));

        List<InfosProfessionnelles> employesActuels = 
            infosProfessionnellesService.findByManagerAndDateFinIsNull(manager);

        manager.setDateDebut(updatedManager.getDateDebut());
        manager.setDateFin(updatedManager.getDateFin());
        manager.setStatut(updatedManager.getStatut());

        if (nouveauDepartementId != null && !nouveauDepartementId.isEmpty()) {
            updateManagerDepartment(manager, nouveauDepartementId);
            removeManagerFromEmployees(employesActuels);
        }

        return managerRepository.save(manager);
    }

    private void removeManagerFromEmployees(List<InfosProfessionnelles> employesActuels) {
        for (InfosProfessionnelles infoPro : employesActuels) {
            infoPro.setManager(null); 
            infosProfessionnellesService.create(infoPro);
        }
        System.out.println("✅ Manager enlevé de " + employesActuels.size() + " employés");
    }

    private void updateManagerDepartment(Manager manager, String nouveauDepartementId) {
        Optional<DepartementManager> currentRelation = 

            departementManagerService.findByManagerAndDateFinIsNull(manager);
        
        currentRelation.ifPresent(relation -> {
            relation.setDateFin(LocalDate.now());
            departementManagerService.save(relation);
        });

        Departement nouvelleRelation = new Departement();
        nouvelleRelation.setId(nouveauDepartementId);

        DepartementManager newDepartementManager = new DepartementManager();
        newDepartementManager.setManager(manager);
        newDepartementManager.setDepartement(nouvelleRelation);
        newDepartementManager.setDateDebut(LocalDate.now());
        departementManagerService.save(newDepartementManager);
    }

    public List<Manager> getManagersArchives() {
        return managerRepository.findByStatut(Manager.Statut.INACTIF);
    }

    public List<Manager> getManagerActif(){
        return managerRepository.findByStatut(0);
    }
    
    public List<Manager> getManagersActifs() {
        return managerRepository.findByDateFinIsNull();
    }

    public Manager findManagerActuelByDepartement(String idDepartement) {
        return managerRepository.findManagerActuelByDepartement(idDepartement)
                                .orElse(null);
    }

    public Manager getManagerByEmploye(String idEmploye){
        return managerRepository.findByEmployeId(idEmploye).get();
    }

    public Manager archiver(Manager manager){
        Manager managerWithInfo = getManagerById(manager.getId()).get();
        managerWithInfo.setCommentaire(manager.getCommentaire());
        // managerWithInfo.setDateDebut(manager.getDateDebut());
        managerWithInfo.setDateFin(manager.getDateFin());
        Manager managerUpdated = saveManager(managerWithInfo);
        return managerUpdated;
    }

    
    

}

