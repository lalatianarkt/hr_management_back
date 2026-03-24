package com.rh.manage.Service;

import com.rh.manage.Model.*;

import java.lang.StackWalker.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh.manage.Repository.EmployeRepository;
import com.rh.manage.Repository.InfosProfessionnellesRepository;

@Service
public class InfosProfessionnellesService {

    @Autowired
    private InfosProfessionnellesRepository infosProfessionnellesRepository;

    // @Autowired
    // private EmployeService employeService; 

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private TypeTempsTravailService typeTempsTravailService;

    @Autowired
    private CategorieProfessionnelleService categorieProfessionnelleService;

    // @Autowired
    // private 

    // @Autowired
    // ManagerService managerService;

    @Autowired
    TokenService tokenService;

    // ✅ Récupérer toutes les InfosProfessionnelles
    public List<InfosProfessionnelles> getAll() {
        return infosProfessionnellesRepository.findAll();
    }

    // ✅ Récupérer une InfosProfessionnelles par son ID
    public Optional<InfosProfessionnelles> getById(String id) {
        return infosProfessionnellesRepository.findById(id);
    }

    public Optional<InfosProfessionnelles> getByIdEmploye(String id) {
        return infosProfessionnellesRepository.findByEmployeId(id);
    }

    // ✅ Ajouter une nouvelle InfosProfessionnelles
    public InfosProfessionnelles create(InfosProfessionnelles infos) {
        return infosProfessionnellesRepository.save(infos);
    } 

    // ✅ Mettre à jour une InfosProfessionnelles existante
    public InfosProfessionnelles update(InfosProfessionnelles infos) throws Exception {
        // if(infos.)
        if(infos.getDateDebauche() != null){
            if(infos.getDateFinAssignationPoste() == null){
                throw new Exception("Veuillez remplir correctement comme la date départ la date fin d\'assignation de poste");
            } 
            if(infos.getDateDebauche() == null){
                throw new Exception("Veuillez remplir correctement comme la date départ la date fin d\'assignation de poste");
            } 
            if(infos.getMotifDepart() == null){
                throw new Exception("Veuillez remplir correctement le motif de départ");
            }
            infos.setStatut(1);
        }
        if (infos.getId() == null || !infosProfessionnellesRepository.existsById(infos.getId())) {
            throw new IllegalArgumentException("L'ID de l'InfosProfessionnelles est invalide ou inexistant");
        } 
        // if(infos.getCategorieProfessionnelle() ==){

        // }
        return infosProfessionnellesRepository.save(infos);
    }

    // ✅ Supprimer une InfosProfessionnelles par ID
    public void deleteById(String id) {
        if (!infosProfessionnellesRepository.existsById(id)) {
            throw new IllegalArgumentException("L'ID de l'InfosProfessionnelles est inexistant");
        }
        infosProfessionnellesRepository.deleteById(id);
    } 

    public List<InfosProfessionnelles> findByManagerAndDateFinIsNull(Manager manager){
       return infosProfessionnellesRepository.findByManagerAndDateFinAssignationPosteIsNull(manager);
    }

    public InfosProfessionnelles getDernierInfosParProEmploye(String idEmploye){
        return infosProfessionnellesRepository.findLatestByEmployeId(idEmploye).get();
    }

    public InfosProfessionnelles findInfosProfessionnellesByIdEmploye(String idEmploye){
        return infosProfessionnellesRepository.findCurrentByEmployeId(idEmploye);
    } 

    public InfosProfessionnelles findLastByEmployeId(String idEmploye){
        return infosProfessionnellesRepository.findLastByEmployeId(idEmploye);
    }

    public Optional<List<InfosProfessionnelles>> findByDepartement(String idDep){
        return infosProfessionnellesRepository.findByDepartementIdAndStatut(idDep, 0);
    }

    public List<InfosProfessionnelles> findAllEmpActiveByIdDepartement(String idDepartement) {
        return infosProfessionnellesRepository.findByDepartementAndStatut(idDepartement, 0);
    }

         
    // Récupérer le département actuel du manager
    public InfosProfessionnelles getDepartementActuel(Manager manager) {
        try {
            if (manager == null || manager.getEmploye() == null) {
                System.err.println("[ERREUR] Le manager ou son employé est null.");
                return null;
            }

            // Vérifier si l'employé existe
            Optional<Employe> optEmploye = employeRepository.findById(manager.getEmploye().getId());
            if (!optEmploye.isPresent()) {
                System.err.println("[ERREUR] Aucun employé trouvé avec l'ID : " + manager.getEmploye().getId());
                return null;
            }

            Employe emp = optEmploye.get();

            // Vérifier si les infos professionnelles existent
            Optional<InfosProfessionnelles> optInfos = getByIdEmploye(emp.getId());
            if (!optInfos.isPresent()) {
                System.err.println("[ERREUR] Aucune information professionnelle trouvée pour l'employé : " + emp.getId());
                return null;
            }

            return optInfos.get();

        } catch (Exception e) {
            System.err.println("[EXCEPTION] Erreur lors de la récupération du département actuel : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    } 

    public InfosProfessionnelles getInfosProEmploye(String idEmp){
        return infosProfessionnellesRepository.findActiveByEmployeId(idEmp).get();
    } 

    // Récupérer tous les employés actifs (statut = 0)
    public List<Employe> getEmployesActifs() {
        return employeRepository.findByStatut(0);
    }
    
    // Récupérer un employé actif par ID
    public Optional<Employe> getEmployeActifById(String id) {
        return employeRepository.findByIdAndStatut(id, 0);
    }
    
    // Récupérer les infos professionnelles avec statut = 0 d'un employé
    public List<InfosProfessionnelles> getInfosProfessionnellesByEmployeId(String employeId) {
        return infosProfessionnellesRepository.findByEmployeIdAndStatut(employeId);
    }

    public List<InfosProfessionnelles> getAllInfosProParTypeContrat(String typeContrat){
        return infosProfessionnellesRepository.findByTypeContratIntitule(typeContrat);
    }
    
    // Récupérer la dernière info professionnelle avec statut = 0 d'un employé
    public Optional<InfosProfessionnelles> getDerniereInfoProfessionnelleByEmployeId(String employeId) {
        List<InfosProfessionnelles> infos = infosProfessionnellesRepository.findLatestByEmployeIdAndStatut(employeId);
        return infos.isEmpty() ? Optional.empty() : Optional.of(infos.get(0));
    }
    
    // Récupérer une info professionnelle avec statut = 0 d'un employé
    public List<InfosProfessionnelles> getInfoProfessionnelleByEmployeId(String employeId) {
        return infosProfessionnellesRepository.findByEmployeIdAndStatut(employeId);
    } 

    public List<InfosProfessionnelles> getAllInfoProActif(){
        return infosProfessionnellesRepository.findByStatut(0);
    }

    public InfosProfessionnelles findInfosProActifByMatricule(String matricule){
        return infosProfessionnellesRepository.findByMatriculeAndStatut(matricule, 0).get();
    }

    public Optional<InfosProfessionnelles> findByMatricule(String matricule){
        return infosProfessionnellesRepository.findByMatriculeAndStatut(matricule, 0);
    }

    public boolean isMatriculeExists(String matricule){
        return infosProfessionnellesRepository.existsByMatricule(matricule);
    }

    // Récupérer les infos pro des non-managers
    public List<InfosProfessionnelles> getInfoProForNonManagers() {
        return infosProfessionnellesRepository.findInfoProForNonManagers();
    }
    
    // Vérifier si un employé a des infos professionnelles avec statut = 0
    // public boolean employeAvecInfosProfessionnelles(String employeId) {
    //     InfosProfessionnelles infos = infosProfessionnellesRepository.findByEmployeIdAndStatut(employeId, 0);
    //     return !infos.isEmpty();
    // }

    public InfosProfessionnelles update_info_pro(InfosProfessionnelles infosPro, String id) {
        try {
            InfosProfessionnelles infoPro = getById(id)
                .orElseThrow(() -> new RuntimeException("InfoPro non trouvé avec l'ID: " + id));

            
            infoPro.setDateDebutAssignationPoste(infosPro.getDateDebutAssignationPoste());
            infoPro.setDateFinAssignationPoste(infosPro.getDateFinAssignationPoste());
            infoPro.setMatricule(infosPro.getMatricule());
            infoPro.setDateEmbauche(infosPro.getDateEmbauche());
            infoPro.setDateDebauche(infosPro.getDateDebauche());
            infoPro.setMotifDepart(infosPro.getMotifDepart());
            infoPro.setSalaireBase(infosPro.getSalaireBase());
            infoPro.setId(id);
            System.out.println("matricule : " + infosPro.getMatricule());
            System.out.println("tafiditra ato ah nefa ++++++++++++++++++++++++++++++++++++++");
            return infosProfessionnellesRepository.save(infoPro);
            // return infoPro_updated;
            
        } catch (RuntimeException e) {
            // Relancer les exceptions métier (employé non trouvé)
            throw e;
        } catch (Exception e) {
            System.out.println("erreur : " + e.getMessage());
            e.printStackTrace();
            // Logger l'erreur technique
            System.err.println("Erreur technique lors de la modification de l'info admin " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur technique lors de la modification de l'info admin", e);
        }
    }

    public List<InfosProfessionnelles> getAllEmployeesByDepartement(Manager manager){
        return infosProfessionnellesRepository.findByManagerAndStatutZero(manager);
    } 

    
}
