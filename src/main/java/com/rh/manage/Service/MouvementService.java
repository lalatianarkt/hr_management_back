package com.rh.manage.Service;

import com.rh.manage.Model.*;
import com.rh.manage.Repository.MouvementRepository;

import com.rh.manage.Service.AutomatisationService;
import com.rh.manage.Service.EmployeService;
import io.jsonwebtoken.Claims;

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

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    EmployeService employeService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    AutomatisationService automatisationService;

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
                infosProProposee.setMatricule(infosProActuel.getMatricule());
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
                infosProProposee.setMatricule(infosProActuel.getMatricule());
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
                infosProProposee.setMatricule(infosProActuel.getMatricule());
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
            
            notificationService.createNotificationManagerDemandeMouvement(mouvement);
            // 5. Sauvegarder le mouvement
            return mouvementRepository.save(mouvement);

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de la demande: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Mouvement validerMouvementByManager(String idMouvement, Mouvement mouvementUpdated, Claims claims) {
        String idEmploye = claims.get("idEmploye", String.class);
        String idUser = claims.getSubject().toString();
        Employe employe = employeService.getById(idEmploye).orElseThrow(() -> new RuntimeException("Employé validateur non trouvé"));
        Mouvement mouvement = mouvementRepository.findById(idMouvement)
            .orElseThrow(() -> new RuntimeException("Mouvement non trouvé"));

        // 3. Mettre à jour le mouvement
        mouvement.setStatut(2); // Validé
        mouvement.setEmployeValidateur(employe);
        mouvement.setCommentaireManager(mouvementUpdated.getCommentaireManager());
        mouvement.setDateValidation(LocalDate.now());
        notificationService.createNotificationManagerValidationMouvement(mouvement, idUser);
        return mouvementRepository.save(mouvement);
    }

    @Transactional
    public Mouvement refuserMouvementByManager(String idMouvement, Mouvement mouvementUpdated, Claims claims) {
        String idEmploye = claims.get("idEmploye", String.class);
        String idUser = claims.getSubject().toString();
        Employe employe = employeService.getById(idEmploye).orElseThrow(() -> new RuntimeException("Employé validateur non trouvé"));
        Mouvement mouvement = mouvementRepository.findById(idMouvement)
            .orElseThrow(() -> new RuntimeException("Mouvement non trouvé"));

        // 3. Mettre à jour le mouvement
        mouvement.setStatut(3); // Validé
        mouvement.setEmployeValidateur(employe);
        mouvement.setCommentaireManager(mouvementUpdated.getCommentaireManager());
        mouvement.setDateValidation(LocalDate.now());
        notificationService.createNotificationManagerValidationMouvement(mouvement, idUser);
        return mouvementRepository.save(mouvement);
    }

    /**
     * Valide un mouvement et applique les changements, côté RH
     */
    @Transactional
    public Mouvement validerMouvement(String idMouvement, Mouvement mouvementUpdated, Claims claims) {
        String idEmploye = claims.get("idEmploye", String.class);
        String idUser = claims.getSubject().toString();
        Employe employe = employeService.getById(idEmploye)
            .orElseThrow(() -> new RuntimeException("Employé validateur non trouvé"));

        // Récupérer d'abord les valeurs envoyées par le frontend
        double salaireBaseFront = 0;
        LocalDate dateDebutFront = null;
        LocalDate dateFinFront = null;
        if (mouvementUpdated != null && mouvementUpdated.getInfosProPropose() != null) {
            salaireBaseFront = mouvementUpdated.getInfosProPropose().getSalaireBase();
            dateDebutFront = mouvementUpdated.getInfosProPropose().getDateDebutAssignationPoste();
            dateFinFront = mouvementUpdated.getInfosProPropose().getDateFinAssignationPoste();
        }

        Mouvement mouvement = mouvementRepository.findById(idMouvement)
            .orElseThrow(() -> new RuntimeException("Mouvement non trouvé"));

        InfosProfessionnelles actuel = infosProfessionnellesService
            .getById(mouvement.getInfosProActuel().getId())
            .orElseThrow(() -> new RuntimeException("Info professionnelle actuelle non trouvée"));
        actuel.setDateFinAssignationPoste(LocalDate.now());
        actuel.setStatut(1);

        InfosProfessionnelles propose = infosProfessionnellesService
            .getById(mouvement.getInfosProPropose().getId())
            .orElseThrow(() -> new RuntimeException("Info professionnelle proposée non trouvée"));
        propose.setStatut(0);
        propose.setDateEmbauche(actuel.getDateEmbauche());
        propose.setCategorieProfessionnelle(actuel.getCategorieProfessionnelle());
        propose.setClassification(actuel.getClassification());
        propose.setDepartement(actuel.getDepartement());
        propose.setTypeContrat(actuel.getTypeContrat());
        propose.setMatricule(actuel.getMatricule());
        propose.setTypeEntree(actuel.getTypeEntree());
        propose.setEmploye(actuel.getEmploye());
        propose.setTypeTempsTravail(actuel.getTypeTempsTravail());

        // Appliquer les valeurs du frontend
        if (salaireBaseFront != 0) {
            propose.setSalaireBase(salaireBaseFront);
        } else {
            propose.setSalaireBase(actuel.getSalaireBase());
        }
        if (dateDebutFront != null) {
            propose.setDateDebutAssignationPoste(dateDebutFront);
        }
        if (dateFinFront != null) {
            propose.setDateFinAssignationPoste(dateFinFront);
        }

        Manager managerActuel = managerService.findManagerActuelByDepartement(propose.getDepartement().getId());
        propose.setManager(managerActuel);

        infosProRepository.save(propose);
        infosProRepository.save(actuel);

        // 3. Mettre à jour le mouvement
        mouvement.setStatut(4); // Validé
        mouvement.setEmployeValidateur(employe);
        mouvement.setDateValidation(LocalDate.now());
        mouvement.setCommentaire(mouvementUpdated.getCommentaire());

        notificationService.createNotificationRHValidationMouvement(mouvement, idUser);
        automatisationService.sendEmailNotificationDemandeMvt(mouvement);
        return mouvementRepository.save(mouvement);
    }


    /**
     * Rejette un mouvement, côté RH
     */
    @Transactional
    public Mouvement rejeterMouvement(String idMouvement, Mouvement mouvementUpdated, Claims claims) {
        String idEmploye = claims.get("idEmploye", String.class);
        String idUser = claims.getSubject().toString();
        Employe employe = employeService.getById(idEmploye).orElseThrow(() -> new RuntimeException("Employé validateur non trouvé"));
        Mouvement mouvement = mouvementRepository.findById(idMouvement)
            .orElseThrow(() -> new RuntimeException("Mouvement non trouvé"));

        // 2. Activer la nouvelle info pro
        InfosProfessionnelles nouvelleInfoPro = mouvement.getInfosProPropose();
        nouvelleInfoPro.setStatut(4);
        infosProRepository.save(nouvelleInfoPro);

        // 3. Mettre à jour le mouvement
        mouvement.setStatut(5); // Validé
        mouvement.setEmployeValidateur(employe);
        mouvement.setDateValidation(LocalDate.now());
        mouvement.setCommentaire(mouvementUpdated.getCommentaire());
        notificationService.createNotificationRHValidationMouvement(mouvement, idUser);
        automatisationService.sendEmailNotificationDemandeMvt(mouvement);
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

    /**
     * Récupère tous les mouvements dont le statut n'est pas égal à 6
     */
    public List<Mouvement> getMouvement() {
        return mouvementRepository.getMouvement();
    }
    
    /**
     * Récupère tous les mouvements pour les statuts 1 à 7
     */
    public List<Mouvement> getMouvementsStatuts1a7() {
        return mouvementRepository.findByStatutIn(List.of(1, 2, 3, 4, 5, 6, 7));
    }

    public List<Mouvement> findByManager(Claims claims) {
        String idEmploye = claims.get("idEmploye", String.class);
        Manager manager = managerService.getManagerByEmploye(idEmploye);
        List<Mouvement> les_mvts_en_attente = getMouvementsStatuts1a7();
        List<Mouvement> result = new java.util.ArrayList<>();
        for (Mouvement mouvement : les_mvts_en_attente) {
            if (mouvement == null || mouvement.getEmployeDemandeur() == null || manager == null || manager.getDepartement() == null) {
                continue;
            }
            List<UserRole> userRoles = userRoleService.getByEmployeId(mouvement.getEmployeDemandeur().getId());
            boolean hasManagerRole = userRoles != null && userRoles.stream()
                .anyMatch(ur -> ur.getTypeUser() != null && "Manager".equalsIgnoreCase(ur.getTypeUser().getType()));
            boolean hasEmployeRole = userRoles != null && userRoles.stream()
                .anyMatch(ur -> ur.getTypeUser() != null && "Employe".equalsIgnoreCase(ur.getTypeUser().getType()));
            if(hasManagerRole || hasEmployeRole){
                if(infosProfessionnellesService.existsInDepartement(manager.getDepartement().getId(), mouvement.getEmployeConcerne().getId())) {
                    result.add(mouvement);
                }
            }
        }
        return result;
    }
}
