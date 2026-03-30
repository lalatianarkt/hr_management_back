package com.rh.manage.Service;

import com.rh.manage.Model.*;
import com.rh.manage.Repository.MouvementRepository;
import com.rh.manage.Repository.InfosProfessionnellesRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MouvementService {

    private final MouvementRepository mouvementRepository;
    private final InfosProfessionnellesRepository infosProRepository;

    @Autowired
    InfosProfessionnellesService infosProfessionnellesService;

    @Autowired
    ManagerService managerService;

    @Autowired
    PosteService posteService;

    @Autowired
    TypeMouvementService typeMouvementService;

    public MouvementService(MouvementRepository mouvementRepository, 
                          InfosProfessionnellesRepository infosProRepository) {
        this.mouvementRepository = mouvementRepository;
        this.infosProRepository = infosProRepository;
    }

    @Transactional
    public Mouvement creerDemandeMouvement(Mouvement mouvement) {
        try {
            // 1. Vérifier que l'info pro proposée existe
            if (mouvement.getInfosProPropose() == null) {
                throw new RuntimeException("L'info professionnelle proposée est obligatoire");
            } 
            InfosProfessionnelles infosProActuel = mouvement.getInfosProActuel();
            infosProActuel = infosProfessionnellesService.getById(infosProActuel.getId()).get();
            TypeMouvement typeMvt = typeMouvementService.getById(mouvement.getTypeMouvement().getId()).get();
            mouvement.setInfosProActuel(infosProActuel);

            // Retraite ou changement de poste
            if(typeMvt.getType().equalsIgnoreCase("Changement de poste")){
                // 2. Préparer l'info pro proposée
                InfosProfessionnelles infosProProposee = mouvement.getInfosProPropose();

                Poste poste = posteService.getPosteById(infosProProposee.getPoste().getId()).get();
                Manager managerDansDepartementPropose = managerService.findManagerActuelByDepartement(poste.getDepartement().getId());
                infosProProposee.setDateDebauche(null);
                infosProProposee.setManager(managerDansDepartementPropose);
                infosProProposee.setEmploye(infosProActuel.getEmploye());
                infosProProposee.setDateEmbauche(infosProActuel.getDateEmbauche());
                // infosProProposee.setStatut(3); // 3 = PROPOSÉ (pas 0)
                infosProProposee.setCategorieProfessionnelle(mouvement.getInfosProPropose().getCategorieProfessionnelle());
                infosProProposee.setClassification(mouvement.getInfosProPropose().getClassification());
                infosProProposee.setSalaireBase(mouvement.getInfosProPropose().getSalaireBase());
                infosProProposee.setDateDebutAssignationPoste(mouvement.getInfosProPropose().getDateDebutAssignationPoste());
                infosProProposee.setDateFinAssignationPoste(mouvement.getInfosProPropose().getDateFinAssignationPoste());
                infosProProposee.setPoste(mouvement.getInfosProPropose().getPoste());
                infosProProposee.setDepartement(mouvement.getInfosProPropose().getDepartement());

                // 3. Sauvegarder l'info pro proposée
                InfosProfessionnelles savedInfosPro = infosProfessionnellesService.create(infosProProposee);
                mouvement.setInfosProPropose(savedInfosPro);

                // 4. Préparer le mouvement
                // mouvement.setStatut(1); // 1 = EN ATTENTE (pas 0)
                mouvement.setDateValidation(null);
                mouvement.setEmployeValidateur(null);
                if(infosProProposee.getStatut() == 3){
                    mouvement.setStatut(1); //en attente koa 
                }
                if(infosProProposee.getStatut() == 4){
                    mouvement.setStatut(0); //validé avy hatrany
                }
            }

            // Mutation ou renouvellement de contrat
            if(typeMvt.getType().equalsIgnoreCase("Mutation") || 
            typeMvt.getType().equalsIgnoreCase("Renouvellement de contrat")){
                // System.out.println("ato++++++++++");

                // 2. Préparer l'info pro proposée
                InfosProfessionnelles infosProProposee = mouvement.getInfosProPropose();
                // System.out.println("ato");
                // System.out.println("dep : " + infosProProposee.getDepartement().getId());
                
                // Manager managerActuel = infosProfessionnellesService.getManagerActuelByDepartement(infosProProposee.getDepartement().getId());
                // System.out.println("managerId : " + managerActuel.getId());

                // System.out.println("departement amzao : " + infosProProposee.getPoste().getDepartement().getNom());
                Poste poste = posteService.getPosteById(infosProProposee.getPoste().getId()).get();
                Manager managerDansDepartementPropose = managerService.findManagerActuelByDepartement(poste.getDepartement().getId());
                // System.out.println("ato1");
                infosProProposee.setManager(managerDansDepartementPropose);
                // System.out.println("ato2");
                infosProProposee.setEmploye(infosProActuel.getEmploye());
                // System.out.println("ato3");
                infosProProposee.setDateEmbauche(infosProActuel.getDateEmbauche());
                // System.out.println("ato4");
                // infosProProposee.setStatut(3); // 3 = PROPOSÉ (pas 0)
                infosProProposee.setCategorieProfessionnelle(mouvement.getInfosProPropose().getCategorieProfessionnelle());
                infosProProposee.setClassification(mouvement.getInfosProPropose().getClassification());
                infosProProposee.setSalaireBase(mouvement.getInfosProPropose().getSalaireBase());
                infosProProposee.setDateDebutAssignationPoste(mouvement.getInfosProPropose().getDateDebutAssignationPoste());
                infosProProposee.setDateFinAssignationPoste(mouvement.getInfosProPropose().getDateFinAssignationPoste());
                infosProProposee.setPoste(mouvement.getInfosProPropose().getPoste());
                infosProProposee.setDepartement(mouvement.getInfosProPropose().getDepartement());
                // System.out.println("ato5");
                
                // 3. Sauvegarder l'info pro proposée
                InfosProfessionnelles savedInfosPro = infosProfessionnellesService.create(infosProProposee);
                // System.out.println("ato6");
                mouvement.setInfosProPropose(savedInfosPro);

                // 4. Préparer le mouvement
                // mouvement.setStatut(1); // 1 = EN ATTENTE (pas 0)
                // System.out.println("ato7");
                // mouvement.setDateDemande(LocalDate.now());
                mouvement.setDateValidation(null);
                mouvement.setEmployeValidateur(null);
                if(infosProProposee.getStatut() == 3){
                    mouvement.setStatut(1); //en attente koa 
                }
                if(infosProProposee.getStatut() == 4){
                    mouvement.setStatut(0); //en attente koa 
                }
                // System.out.println("ato8");
            }

            if(typeMvt.getType().equalsIgnoreCase("Promotion")){
                // System.out.println("ato++++++++++");
                // System.out.println("infoProActuel : " + infosProActuel.getTypeContrat().getIntitule());

                // 2. Préparer l'info pro proposée
                InfosProfessionnelles infosProProposee = mouvement.getInfosProPropose();
                // System.out.println("ato");
                // System.out.println("dep : " + infosProProposee.getDepartement().getId());
                
                // Manager managerActuel = infosProfessionnellesService.getManagerActuelByDepartement(infosProProposee.getDepartement().getId());
                // System.out.println("managerId : " + managerActuel.getId());

                // System.out.println("departement amzao : " + infosProProposee.getPoste().getDepartement().getNom());
                Poste poste = posteService.getPosteById(infosProProposee.getPoste().getId()).get();
                Manager managerDansDepartementPropose = managerService.findManagerActuelByDepartement(poste.getDepartement().getId());
                // System.out.println("ato1");
                infosProProposee.setManager(managerDansDepartementPropose);
                // System.out.println("ato2");
                infosProProposee.setEmploye(infosProActuel.getEmploye());
                // System.out.println("ato3");
                infosProProposee.setDateEmbauche(infosProActuel.getDateEmbauche());
                // System.out.println("ato4");
                // infosProProposee.setStatut(3); // 3 = PROPOSÉ (pas 0)
                infosProProposee.setCategorieProfessionnelle(mouvement.getInfosProPropose().getCategorieProfessionnelle());
                infosProProposee.setClassification(mouvement.getInfosProPropose().getClassification());
                infosProProposee.setSalaireBase(mouvement.getInfosProPropose().getSalaireBase());
                infosProProposee.setDateDebutAssignationPoste(mouvement.getInfosProPropose().getDateDebutAssignationPoste());
                infosProProposee.setDateFinAssignationPoste(mouvement.getInfosProPropose().getDateFinAssignationPoste());
                infosProProposee.setPoste(mouvement.getInfosProPropose().getPoste());
                infosProProposee.setDepartement(mouvement.getInfosProPropose().getDepartement());
                // System.out.println("ato5");
                
                // 3. Sauvegarder l'info pro proposée
                InfosProfessionnelles savedInfosPro = infosProfessionnellesService.create(infosProProposee);
                // System.out.println("ato6");
                mouvement.setInfosProPropose(savedInfosPro);

                // 4. Préparer le mouvement
                // mouvement.setStatut(1); // 1 = EN ATTENTE (pas 0)
                // System.out.println("ato7");
                // mouvement.setDateDemande(LocalDate.now());
                mouvement.setDateValidation(null);
                mouvement.setEmployeValidateur(null);

                if(infosProProposee.getStatut() == 3){
                    mouvement.setStatut(1); //en attente koa 
                }
                if(infosProProposee.getStatut() == 4){
                    mouvement.setStatut(0); //en attente koa 
                }
            }
            
            
            // 5. Sauvegarder le mouvement
            return mouvementRepository.save(mouvement);

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de la demande: " + e.getMessage(), e);
        }
    }

    /**
     * Valide un mouvement et applique les changements
     */
    @Transactional
    public Mouvement validerMouvement(String idMouvement, Mouvement mouvementUpdated) {
        Mouvement mouvement = mouvementRepository.findById(idMouvement)
            .orElseThrow(() -> new RuntimeException("Mouvement non trouvé"));

        // 1. Archiver l'ancienne info pro (mettre date_fin)
        InfosProfessionnelles ancienneInfoPro = mouvement.getInfosProActuel();
        ancienneInfoPro.setDateFinAssignationPoste(LocalDate.now().minusDays(1));
        ancienneInfoPro.setStatut(1);
        infosProRepository.save(ancienneInfoPro);

        // 2. Activer la nouvelle info pro
        InfosProfessionnelles nouvelleInfoPro = mouvement.getInfosProPropose();
        nouvelleInfoPro.setStatut(0);
        nouvelleInfoPro.setDateDebauche(null); // Devenue l'état actuel
        infosProRepository.save(nouvelleInfoPro);

        // 3. Mettre à jour le mouvement
        mouvement.setStatut(2); // Validé
        mouvement.setEmployeValidateur(mouvementUpdated.getEmployeValidateur());
        mouvement.setDateValidation(LocalDate.now());
        mouvement.setCommentaire(mouvementUpdated.getCommentaire());

        return mouvementRepository.save(mouvement);
    }

    /**
     * Rejette un mouvement
     */
    @Transactional
    public Mouvement rejeterMouvement(String idMouvement, Mouvement mouvementUpdated) {
        Mouvement mouvement = mouvementRepository.findById(idMouvement)
            .orElseThrow(() -> new RuntimeException("Mouvement non trouvé"));

        // 2. Activer la nouvelle info pro
        InfosProfessionnelles nouvelleInfoPro = mouvement.getInfosProPropose();
        nouvelleInfoPro.setStatut(4);
        infosProRepository.save(nouvelleInfoPro);

        // 3. Mettre à jour le mouvement
        mouvement.setStatut(3); // Validé
        // mouvement.setEmployeValidateur(mouvementUpdated.getEmployeValidateur());
        mouvement.setDateValidation(LocalDate.now());
        mouvement.setCommentaire(mouvementUpdated.getCommentaire());
        return mouvementRepository.save(mouvement);
    }

    // Autres méthodes existantes
    public List<Mouvement> findAll() {
        return mouvementRepository.findAll();
    }

    public Optional<Mouvement> findById(String id) {
        return mouvementRepository.findById(id);
    }

    public Mouvement save(Mouvement mouvement) {
        return mouvementRepository.save(mouvement);
    }

    public void deleteById(String id) {
        mouvementRepository.deleteById(id);
    }

    /**
     * Trouve tous les mouvements en attente de validation
     */
    public List<Mouvement> findMouvementsEnAttente() {
        return mouvementRepository.findByStatut(1);
    }

    /**
     * Trouve les mouvements d'un employé
     */
    public List<Mouvement> findMouvementsByEmploye(Employe employe) {
        return mouvementRepository.findByEmployeDemandeur(employe);
    }
}