package com.rh.manage.Service;

import com.rh.manage.Dto.AttestationCongeData;
import com.rh.manage.Model.DemandeConge;
import com.rh.manage.Model.DepartementManager;
import com.rh.manage.Model.Employe;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Model.Manager;
import com.rh.manage.Model.MouvementSolde;
import com.rh.manage.Model.Notification;
import com.rh.manage.Model.ReglesAnnulationConges;
import com.rh.manage.Model.Token;
import com.rh.manage.Model.TypeConge;
import com.rh.manage.Model.TypeEnumConge;
import com.rh.manage.Model.User;
import com.rh.manage.Model.UserRole;
import com.rh.manage.Repository.DemandeCongeRepository;

import com.rh.manage.Service.AutomatisationService;
import com.rh.manage.Service.EmployeService;
import io.jsonwebtoken.Claims;

import org.bouncycastle.jcajce.provider.asymmetric.dsa.DSASigner.detDSA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.lang.StackWalker.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DemandeCongeService {
    @Autowired
    TokenService tokenService;

    @Autowired
    ManagerService managerService;

    @Autowired
    InfosProfessionnellesService infosProfessionnellesService;

    @Autowired
    MouvementSoldeService mouvementSoldeService;

    @Autowired
    ReglesAnnulationCongesService reglesAnnulationCongesService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EmployeService employeService;

    @Autowired
    AutomatisationService automatisationService;

    @Autowired
    private AttestationCongeDataService attestationCongeDataService;

    private static final String[] NOMS_MOIS = {
        "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
        "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
    };

    private final DemandeCongeRepository repository;

    public DemandeCongeService(DemandeCongeRepository repository) {
        this.repository = repository;
    } 

    Optional<DemandeConge> findByDate(String employeId, LocalDate date){
        return repository.findByEmployeIdAndDateDebut(employeId, date);
    }

    public List<DemandeConge> getDemandesByEmployeAndPeriode(String employeId, LocalDate dateDebut, LocalDate dateFin) {
        return repository.findByEmployeIdAndPeriode(employeId, dateDebut, dateFin);
    }

    public List<DemandeConge> getDemandeCongesParManagerByUserId(String userId) {
        User managerUser = userService.getById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        List<UserRole> userRoles = userRoleService.getByUserId(userId);
        if(userRoles == null || userRoles.isEmpty()) {
            throw new RuntimeException("Aucun rôle trouvé pour cet utilisateur");
        } 

        boolean isManager = userRoles.stream()
            .anyMatch(userRole -> "Manager".equals(userRole.getTypeUser().getType())); 
        
        if(!isManager) {
            throw new RuntimeException("Accès réservé aux managers");
        }

        System.out.println("tafiditra ato ah ee++++++++++++++++++++++++++++++++++++");
        System.out.println("Manager User ID: " + managerUser.getId());
        System.out.println("Manager Emp ID: " + managerUser.getEmploye().getId());

        Manager manager = managerService.getManagerByEmploye(managerUser.getEmploye().getId());
        System.out.println("id Manager: " + manager.getId());
        List<DemandeConge> les_demandes = repository.findByManagerId(manager.getId());
        System.out.println("demande siez : " + les_demandes.size());
        for (DemandeConge demandeConge : les_demandes) {
            System.out.println("dem : " + demandeConge.getCommentaireManager());
        }
        
        return repository.findByManagerId(manager.getId());
    }

    public List<DemandeConge> findTodayDemandeCongeActif() {
        LocalDate today = LocalDate.now();
        return repository.findTodayDemandeCongeActif(today, 1);
    }

    // Filtrage avec pagination - Version CORRIGÉE
    public Page<DemandeConge> filtrerDemandes(
            Integer statut, 
            String idEmploye, 
            String typeConge,
            LocalDate dateDebut, 
            LocalDate dateFin,
            String periode,
            Pageable pageable) {
        
        LocalDate dateDemandeMin = calculerDateLimite(periode);
        
        return repository.filtrerDemandes(
            idEmploye, 
            typeConge, 
            dateDebut, 
            dateFin, 
            dateDemandeMin,
            pageable
        );
    }

    // Filtrage sans pagination (pour compatibilité)
    public List<DemandeConge> filtrerDemandes(
            String idEmploye, 
            String typeConge,
            LocalDate dateDebut, 
            LocalDate dateFin,
            String periode) {
        
        LocalDate dateDemandeMin = calculerDateLimite(periode);
        
        return repository.filtrerDemandes(
            idEmploye, 
            typeConge, 
            dateDebut, 
            dateFin, 
            dateDemandeMin
        );
    }

     // Calcul de la date limite selon la période
    private LocalDate calculerDateLimite(String periode) {
        if (periode == null || periode.equals("personnalise")) {
            return null;
        }
        
        switch(periode) {
            case "7jours":
                return LocalDate.now().minusDays(7);
            case "30jours":
                return LocalDate.now().minusDays(30);
            case "90jours":
                return LocalDate.now().minusDays(90);
            default:
                return LocalDate.now().minusDays(30);
        }
    }

    @Transactional
    // validation côté manager
    public DemandeConge validerDemande(DemandeConge demandeConge, Claims claims) throws Exception {
        try {
            String userId = claims.getSubject();
            if (demandeConge.getCommentaireManager().isEmpty()) {
                throw new Exception("Veuillez assigner un motif pour la validation de cette demande");
            }
            // demandeConge.setDecisionManager(1); // statut validé
            demandeConge.setStatut(1);
            demandeConge.setDateValidation(LocalDate.now());
            DemandeConge demandeCongeUpdated = update(demandeConge.getId(), demandeConge);
            notificationService.createNotificationsDemandeConge(demandeCongeUpdated, userId);
            return demandeCongeUpdated;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la validation de la demande : " + e.getMessage(), e);
        }
    }

    public MouvementSolde insertNouveauMvtAvecDemande(DemandeConge demande, int idMouvement){
        MouvementSolde ancienMouvement = mouvementSoldeService.getMouvementById(idMouvement).get();
        MouvementSolde nouveauMouvement = new MouvementSolde();
        nouveauMouvement.setNbCongeRestant(ancienMouvement.getNbCongeRestant() - demande.getNbJours());
        nouveauMouvement.setNbCongePris(ancienMouvement.getNbCongePris() + demande.getNbJours());
        nouveauMouvement.setNbCongeTotal(ancienMouvement.getNbCongeTotal());
        nouveauMouvement.setMois(ancienMouvement.getMois());
        nouveauMouvement.setAnnee(ancienMouvement.getAnnee());
        nouveauMouvement.setCommentaire(demande.getCommentaire());
        // nouveauMouvement.setStatut(1); //accordé
        nouveauMouvement.setTypeMouvement(TypeEnumConge.PRISE);
        nouveauMouvement.setEmploye(ancienMouvement.getEmploye());
        // nouveauMouvement.set
        return mouvementSoldeService.saveMouvement(nouveauMouvement);
    }

    @Transactional
    public DemandeConge validerDemandeRH(DemandeConge demande, Claims claims) {
        try {
            String userId = claims.getSubject();
            Optional<DemandeConge> opt = findById(demande.getId());

            if (opt.isEmpty()) {
                throw new RuntimeException("Demande de conge introuvable pour l'id : " + demande.getId());
            }

            DemandeConge demandeConge = opt.get();
            demandeConge.setCommentaire(demande.getCommentaire());
            demandeConge.setStatut(6);
            demandeConge.setDateValidation(LocalDate.now());
            DemandeConge demandeUpdated = update(demande.getId(), demandeConge);
            notificationService.createNotificationValidationConge(demandeUpdated, userId);
            AttestationCongeData attestation = attestationCongeDataService.createCongeValidation(demandeUpdated);
            automatisationService.sendEmailNotificationDemandeConge(attestation);
            // Mise à jour
            return demandeUpdated;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la validation de la demande : " + e.getMessage(), e);
        }
    }

    @Transactional
    public DemandeConge refuserDemandeRH(DemandeConge demande, Claims claims) {
        try {
            String userId = claims.getSubject();
            Optional<DemandeConge> opt = findById(demande.getId());

            if (opt.isEmpty()) {
                throw new RuntimeException("Demande de conge introuvable pour l'id : " + demande.getId());
            }

            DemandeConge demandeConge = opt.get();
            demandeConge.setCommentaire(demande.getCommentaire());
            demandeConge.setStatut(7);
            demandeConge.setDateValidation(LocalDate.now());

            DemandeConge demandeUpdated = update(demande.getId(), demandeConge);
            notificationService.createNotificationValidationConge(demandeUpdated, userId);
            AttestationCongeData attestation = attestationCongeDataService.createCongeValidation(demandeUpdated);
            automatisationService.sendEmailNotificationDemandeConge(attestation);
            return demandeUpdated;

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du refus RH de la demande : " + e.getMessage(), e);
        }
    }
    
    @Transactional
    public DemandeConge enregistrer(DemandeConge demandeConge, Claims claims) throws Exception{
        String idEmploye = claims.get("idEmploye", String.class);
        String userId = claims.getSubject(); 
        demandeConge.setDecisionManager(0);
        demandeConge.setStatut(0); 
        if(!repository.existeChevauchementConge(idEmploye, demandeConge.getDateDebut(),
                                                demandeConge.getDateFin())
        ){
            Notification notifications = new Notification();
            notifications.setMessage("Nouvelle demande de congé de " + demandeConge.getNbJours() + " jours du " + demandeConge.getDateDebut() + " au " + demandeConge.getDateFin());
            notifications.setReferenceType("DemandeConge");
            notifications.setReferenceId(demandeConge.getId());
            notifications.setIdUtilisateurExpediteur(userId);
            

            InfosProfessionnelles infosPro = infosProfessionnellesService.getDerniereInfoProfessionnelleByEmployeId
            (idEmploye).get();
            if(infosPro.getManager() == null){
                throw new Exception("Un manager devrait être assigné à ce département");
            } else {
                demandeConge.setManager(infosPro.getManager());
            }
            User userManager = userService.findByEmployeId(infosPro.getManager().getEmploye().getId()).get();
            notifications.setIdUtilisateurDestinataire(userManager.getId());
            Employe employeToAssign = new Employe();
            employeToAssign.setId(demandeConge.getEmploye().getId());
            demandeConge.setEmploye(employeToAssign);
            DemandeConge demandeCongeInserted = repository.save(demandeConge);
            notifications.setReferenceId(demandeCongeInserted.getId());
            notifications.setLien("/conge/validation");
            notificationService.createNotification(notifications);
            return demandeCongeInserted;
        } else{
            throw new Exception("erreur : Cette période : " + demandeConge.getDateDebut() + " et " + demandeConge.getDateFin() +
             " chevauchent avec votre période de congé déja existante");
        } 
    } 

    public DemandeConge annulerDemande(String id, DemandeConge demandeEnvoye) throws Exception{
        DemandeConge demande = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Demande introuvable"
            ));

        ReglesAnnulationConges regles = reglesAnnulationCongesService
                .getActives()
                .get(0);

        int nbJourMin = regles.getDelaiMinJours();

        // Calcul du nombre de jours entre aujourd’hui et la date de début
        long joursAvantDebut = ChronoUnit.DAYS
                .between(LocalDate.now(), demande.getDateDebut());

        if (joursAvantDebut < nbJourMin) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Le délai minimum requis pour annuler est de " + nbJourMin + " jours"
            );
        }

        // demande.setDecisionManager(3); // annulé par le demandeur
        demande.setStatut(3);
        if(demandeEnvoye.getCommentaireAnnulation() == null){
            throw new Exception("Le motif d'annulation ne doit pas être null");
        }
        demande.setCommentaireAnnulation(demandeEnvoye.getCommentaireAnnulation());
        return repository.save(demande);
    }

    public Optional<List<DemandeConge>> getDemandesParEmploye(String idEmploye){
        if(repository.findByEmployeId(idEmploye).isPresent()){
            return repository.findByEmployeId(idEmploye);
        } else {
            return null;
        }
    }

    public DemandeConge refuserDemande(String idDemande) {
        try {
            // Vérifier si la demande existe
            Optional<DemandeConge> opt = findById(idDemande);

            if (opt.isEmpty()) {
                throw new RuntimeException("Demande de conge introuvable pour l'id : " + idDemande);
            }

            DemandeConge demandeConge = opt.get();
            demandeConge.setDecisionManager(2); // statut refusé
            demandeConge.setDateValidation(LocalDate.now());

            // demandeConge.setStatut(2);  // statut refusé

            // Mise à jour
            return update(idDemande, demandeConge);

        } catch (RuntimeException e) {
            // Erreurs métier (ID inexistant)
            throw e;

        } catch (Exception e) {
            // Erreurs techniques (database, null pointer, etc.)
            throw new RuntimeException("Erreur lors de la validation de la demande : " + e.getMessage(), e);
        }
    }

    public DemandeConge update(String id, DemandeConge demandeConge) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Gestion demande de congé non trouvée pour l'ID: " + id);
        }
        demandeConge.setId(id);
        return repository.save(demandeConge);
    }


    public DemandeConge updateFromExisting(String id, DemandeConge incoming) {
        DemandeConge existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gestion demande de congé non trouvée pour l'ID: " + id));

        if (incoming.getDateDebut() != null) existing.setDateDebut(incoming.getDateDebut());
        if (incoming.getDateFin() != null) existing.setDateFin(incoming.getDateFin());
        if (incoming.getDateDemande() != null) existing.setDateDemande(incoming.getDateDemande());
        if (incoming.getDateValidation() != null) existing.setDateValidation(incoming.getDateValidation());
        if (incoming.getCommentaireAnnulation() != null) existing.setCommentaireAnnulation(incoming.getCommentaireAnnulation());
        if (incoming.getDecisionManager() != null) existing.setDecisionManager(incoming.getDecisionManager());
        if (incoming.getCommentaireManager() != null) existing.setCommentaireManager(incoming.getCommentaireManager());
        if (incoming.getCommentaire() != null) existing.setCommentaire(incoming.getCommentaire());
        if (incoming.getAutreMotif() != null) existing.setAutreMotif(incoming.getAutreMotif());
        if (incoming.getNbJours() != null) existing.setNbJours(incoming.getNbJours());
        if (incoming.getEmploye() != null) existing.setEmploye(incoming.getEmploye());
        if (incoming.getTypeConge() != null) existing.setTypeConge(incoming.getTypeConge());
        if (incoming.getManager() != null) existing.setManager(incoming.getManager());

        return repository.save(existing);
    }

    // Enregistrer demande
    public DemandeConge save(DemandeConge demande) {
        Manager manager = managerService.getManagerByEmploye(demande.getManager().getId());
        demande.setManager(manager);
        demande.setDecisionManager(0);
        return repository.save(demande);
    }

    public List<DemandeConge> findAllDemandeValidatedByManager(){
        return repository.findByDecisionManager(1);
    }

    public List<DemandeConge> findAll() {
        return repository.findAll();
    }

    public Optional<DemandeConge> findById(String id) {
        return repository.findById(id);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    // Nouvelles méthodes pour la pagination
    public Page<DemandeConge> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // Méthodes pour les statistiques
    public long count() {
        return repository.count();
    }

    public Long getTotalDemandesAnneeEnCours() {
        return repository.countTotalDemandesAnneeEnCours();
    }
    
    // Ou pour une année spécifique
    public Long getTotalDemandesParAnnee(int annee) {
        return repository.countTotalDemandesParAnnee(annee);
    } 

    public Double getMoyenneJoursDemandeesParAnneeEnCours() {
        return repository.calculerMoyenneJoursCongesAnneeEnCours();
    }

    public List<DemandeConge> getEmployeTopDemande(){
        return repository.findDemandesDesTop5Employes();
    }

    public List<DemandeConge> getDemandesRecentest(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("dateDemande").descending());
        return repository.findDemandesRecentest(pageable);
    }

    /**
     * Retourne le mois le plus actif (avec le plus de demandes) pour l'année en cours
     * Format: "Mars 2024"
     */
    public String getMoisPlusActif() {
        try { 
            List<Object[]> resultats = repository.findMoisAvecPlusDeDemandes();
            
            if (resultats == null || resultats.isEmpty()) {
                return "Aucune demande cette année";
            }
            
            // Premier élément = mois avec le plus de demandes
            Object[] premierResultat = resultats.get(0);
            Integer moisNumero = ((Number) premierResultat[0]).intValue();
            Long nombreDemandes = ((Number) premierResultat[1]).longValue();
            
            int anneeEnCours = LocalDate.now().getYear();
            
            // Vérifier si le mois est valide
            if (moisNumero < 1 || moisNumero > 12) {
                return "Données invalides";
            }
            
            return NOMS_MOIS[moisNumero - 1] + " " + anneeEnCours;
            
        } catch (Exception e) {
            return "Erreur calcul";
        }
    }

    public List<DemandeConge> getDemandeParTypeConge(TypeConge typeConge){
        return repository.findByTypeCongeId(typeConge.getId());
    }
    
    public Long countDemandeCongeParType(TypeConge typeConge){
        return repository.countByTypeCongeId(typeConge.getId());
    }
    
    /**
     * Retourne le mois avec le plus de jours de congé demandés
     */
    public String getMoisPicConges() {
        try {
            List<Object[]> resultats = repository.findMoisAvecPlusDeJoursConges();
            
            if (resultats == null || resultats.isEmpty()) {
                return "Aucune demande cette année";
            }
            
            Object[] premierResultat = resultats.get(0);
            Integer moisNumero = ((Number) premierResultat[0]).intValue();
            Double totalJours = ((Number) premierResultat[1]).doubleValue();
            
            int anneeEnCours = LocalDate.now().getYear();
            
            if (moisNumero < 1 || moisNumero > 12) {
                return "Données invalides";
            }
            
            return NOMS_MOIS[moisNumero - 1] + " " + anneeEnCours;
            
        } catch (Exception e) {
            return "Erreur calcul";
        }
    }
    
    /**
     * Version complète avec détails
     */
    public Map<String, Object> getMoisPlusActifAvecDetails() {
        Map<String, Object> resultat = new HashMap<>();
        
        try {
            List<Object[]> resultatsDemandes = repository.findMoisAvecPlusDeDemandes();
            List<Object[]> resultatsJours = repository.findMoisAvecPlusDeJoursConges();
            
            if ((resultatsDemandes == null || resultatsDemandes.isEmpty()) && 
                (resultatsJours == null || resultatsJours.isEmpty())) {
                resultat.put("moisPlusActif", "Aucune demande");
                resultat.put("nombreDemandes", 0);
                resultat.put("totalJours", 0);
                return resultat;
            }
            
            int anneeEnCours = LocalDate.now().getYear();
            
            // Traiter le mois avec plus de demandes
            if (resultatsDemandes != null && !resultatsDemandes.isEmpty()) {
                Object[] topDemandes = resultatsDemandes.get(0);
                Integer moisDemandes = ((Number) topDemandes[0]).intValue();
                Long nbDemandes = ((Number) topDemandes[1]).longValue();
                
                resultat.put("moisPlusActif", NOMS_MOIS[moisDemandes - 1] + " " + anneeEnCours);
                resultat.put("nombreDemandes", nbDemandes);
                resultat.put("moisNumero", moisDemandes);
            }
            
            // Traiter le mois avec plus de jours
            if (resultatsJours != null && !resultatsJours.isEmpty()) {
                Object[] topJours = resultatsJours.get(0);
                Integer moisJours = ((Number) topJours[0]).intValue();
                Double totalJours = ((Number) topJours[1]).doubleValue();
                
                resultat.put("moisPicJours", NOMS_MOIS[moisJours - 1] + " " + anneeEnCours);
                resultat.put("totalJours", Math.round(totalJours));
            }
            
            return resultat;
            
        } catch (Exception e) {
            resultat.put("moisPlusActif", "Erreur de calcul");
            resultat.put("erreur", e.getMessage());
            return resultat;
        }
    }
    
    /**
     * Retourne les 3 mois les plus actifs
     */
    public List<String> getTop3MoisActifs() {
        List<String> topMois = new ArrayList<>();
        
        try {
            List<Object[]> resultats = repository.findMoisAvecPlusDeDemandes();
            
            if (resultats == null || resultats.isEmpty()) {
                topMois.add("Aucune demande");
                return topMois;
            }
            
            int anneeEnCours = LocalDate.now().getYear();
            int limit = Math.min(3, resultats.size());
            
            for (int i = 0; i < limit; i++) {
                Object[] resultat = resultats.get(i);
                Integer moisNumero = ((Number) resultat[0]).intValue();
                Long nombreDemandes = ((Number) resultat[1]).longValue();
                
                if (moisNumero >= 1 && moisNumero <= 12) {
                    String moisNom = NOMS_MOIS[moisNumero - 1];
                    topMois.add(moisNom + " " + anneeEnCours + " (" + nombreDemandes + " demandes)");
                }
            }
            
        } catch (Exception e) {
            topMois.add("Erreur calcul");
        }
        
        return topMois;
    }
    
    /**
     * Vérifie si le mois en cours est le plus actif
     */
    public boolean isMoisEnCoursPlusActif() {
        try {
            List<Object[]> resultats = repository.findMoisAvecPlusDeDemandes();
            
            if (resultats == null || resultats.isEmpty()) {
                return false;
            }
            
            Object[] topResultat = resultats.get(0);
            Integer topMois = ((Number) topResultat[0]).intValue();
            int moisEnCours = LocalDate.now().getMonthValue();
            
            return topMois == moisEnCours;
            
        } catch (Exception e) {
            return false;
        }
    }
}
