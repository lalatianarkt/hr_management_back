package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.DemandeConge;
import com.rh.manage.Model.Employe;
import com.rh.manage.Model.Manager;
import com.rh.manage.Model.Mouvement;
import com.rh.manage.Model.Notification;
import com.rh.manage.Model.User;
import com.rh.manage.Model.UserRole;
import com.rh.manage.Repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserService userService; 

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private EmployeService employeService;
    
    // Créer une notification
    public Notification createNotification(String message, String expediteur, String destinataire, 
                                          String lien, String referenceType, String referenceId) {
        Notification notification = new Notification(message, expediteur, destinataire, lien, referenceType, referenceId);
        return notificationRepository.save(notification);
    }
    
    // Créer une notification avec objet Notification
    public Notification createNotification(Notification notification) {
        notification.setCreatedAt(LocalDateTime.now());
        notification.setModifiedAt(LocalDateTime.now());
        notification.setEstLu(false);
        return notificationRepository.save(notification);
    }

    @Transactional
    public Notification createNotificationManagerDemandeMouvement(Mouvement mouvement) {
        Notification notification = new Notification();
        Manager manager = managerService.findManagerActuelByDepartement(mouvement.getInfosProActuel().getDepartement().getId());
        userService.findByEmail(manager.getEmploye().getEmail()).ifPresent(userManager -> {
            notification.setIdUtilisateurDestinataire(userManager.getId());
        });
        Employe employeDemandeur = employeService.getById(mouvement.getEmployeDemandeur().getId()).get();
        userService.findByEmail(mouvement.getEmployeDemandeur().getEmail()).ifPresent(userDemandeur -> {
            notification.setIdUtilisateurExpediteur(userDemandeur.getId());
        });
        notification.setMessage("Nouvelle demande de mouvement de " + employeDemandeur.getNom() + " " + employeDemandeur.getPrenom() + " à valider");
        notification.setLien("/mouvements/validation");
        notification.setReferenceId(mouvement.getId());
        notification.setReferenceType("Mouvement");
        return notificationRepository.save(notification);
    }

    @Transactional
    public List<Notification> createNotificationManagerValidationMouvement(Mouvement mouvement, String idUserManager) {
        List<Notification> notifications = new ArrayList<>(); 

        if (mouvement == null || mouvement.getStatut() == null) {
            return notifications;
        }

        if (mouvement.getStatut() == 2 || mouvement.getStatut() == 3) {
            // Notification à l'employé demandeur
            if (mouvement.getEmployeDemandeur() != null 
                    && mouvement.getEmployeDemandeur().getEmail() != null) {
                userService.findByEmail(mouvement.getEmployeDemandeur().getEmail()).ifPresent(userDemandeur -> {
                    Notification notificationEmploye = new Notification();
                    notificationEmploye.setIdUtilisateurDestinataire(userDemandeur.getId());
                    notificationEmploye.setIdUtilisateurExpediteur(idUserManager);
                    if (mouvement.getStatut() == 2) {
                        notificationEmploye.setMessage("Votre demande de mouvement a été validée par votre Manager.");
                    } else {
                        notificationEmploye.setMessage("Votre demande de mouvement a été refusée par votre Manager.");
                    }
                    notificationEmploye.setLien("/mouvement/demande");
                    notificationEmploye.setReferenceId(mouvement.getId());
                    notificationEmploye.setReferenceType("Mouvement");
                    notifications.add(notificationEmploye);
                });
            }

            // Notification aux RH
            userRoleService.getByTypeName("Admin_RH").forEach(ur -> {
                Notification notificationRH = new Notification();
                notificationRH.setIdUtilisateurDestinataire(ur.getUser().getId());
                notificationRH.setIdUtilisateurExpediteur(idUserManager);
                if (mouvement.getStatut() == 2) {
                    notificationRH.setMessage("Une demande de mouvement a été validée par le Manager.");
                } else {
                    notificationRH.setMessage("Une demande de mouvement a été refusée par le Manager.");
                }
                notificationRH.setReferenceId(mouvement.getId());
                notificationRH.setReferenceType("Mouvement");
                notificationRH.setLien("/employees/" + mouvement.getEmployeDemandeur().getId() + "/mouvements");
                notifications.add(notificationRH);
            });
        }

        return notificationRepository.saveAll(notifications);
    }

    @Transactional
    public List<Notification> createNotificationRHValidationMouvement(Mouvement mouvement, String idUserRH) {
        List<Notification> notifications = new ArrayList<>();
        if (mouvement == null || mouvement.getStatut() == null) {
            return notifications;
        }

        boolean sameDepartement = false;
        if (mouvement.getInfosProActuel() != null 
                && mouvement.getInfosProActuel().getDepartement() != null
                && mouvement.getInfosProPropose() != null
                && mouvement.getInfosProPropose().getDepartement() != null) {
            String idActuel = mouvement.getInfosProActuel().getDepartement().getId();
            String idPropose = mouvement.getInfosProPropose().getDepartement().getId();
            if (idActuel != null && idPropose != null && idActuel.equalsIgnoreCase(idPropose)) {
                sameDepartement = true;
            }
        }
        final boolean sameDepartementFinal = sameDepartement;

        if (mouvement.getStatut() == 4) {
            // Notification(s) manager(s)
            if (mouvement.getInfosProActuel() != null && mouvement.getInfosProActuel().getDepartement() != null) {
                Manager managerActuel = managerService.findManagerActuelByDepartement(
                        mouvement.getInfosProActuel().getDepartement().getId()
                );
                if (managerActuel != null) {
                    userService.findByEmail(managerActuel.getEmploye().getEmail()).ifPresent(userManager -> {
                        Notification notificationManager = new Notification();
                        notificationManager.setIdUtilisateurDestinataire(userManager.getId());
                        notificationManager.setIdUtilisateurExpediteur(idUserRH);
                        if (sameDepartementFinal) {
                            notificationManager.setMessage("La demande de mouvement de "
                                    + mouvement.getEmployeDemandeur().getNom() + " "
                                    + mouvement.getEmployeDemandeur().getPrenom()
                                    + " a été validée par le service RH.");
                        } else {
                            notificationManager.setMessage("L'employé "
                                    + mouvement.getEmployeDemandeur().getNom() + " "
                                    + mouvement.getEmployeDemandeur().getPrenom()
                                    + " va changer de département vers "
                                    + mouvement.getInfosProPropose().getDepartement().getNom()
                                    + " à partir du "
                                    + mouvement.getInfosProPropose().getDateDebutAssignationPoste()
                                    + ". Validation RH effectuée.");
                        }
                        notificationManager.setLien("/mouvements/validation");
                        notificationManager.setReferenceId(mouvement.getId());
                        notificationManager.setReferenceType("Mouvement");
                        notifications.add(notificationManager);
                    });
                }
            }

            if (!sameDepartementFinal && mouvement.getInfosProPropose() != null 
                    && mouvement.getInfosProPropose().getDepartement() != null) {
                Manager managerNouveau = managerService.findManagerActuelByDepartement(
                        mouvement.getInfosProPropose().getDepartement().getId()
                );
                if (managerNouveau != null) {
                    userService.findByEmail(managerNouveau.getEmploye().getEmail()).ifPresent(userManager -> {
                        Notification notificationManager = new Notification();
                        notificationManager.setIdUtilisateurDestinataire(userManager.getId());
                        notificationManager.setIdUtilisateurExpediteur(idUserRH);
                        notificationManager.setMessage("L'employé "
                                + mouvement.getEmployeDemandeur().getNom() + " "
                                + mouvement.getEmployeDemandeur().getPrenom()
                                + " va rejoindre votre département ("
                                + mouvement.getInfosProPropose().getDepartement().getNom()
                                + ") à partir du "
                                + mouvement.getInfosProPropose().getDateDebutAssignationPoste()
                                + ". Validation RH effectuée.");
                        notificationManager.setLien("/mouvements/validation");
                        notificationManager.setReferenceId(mouvement.getId());
                        notificationManager.setReferenceType("Mouvement");
                        notifications.add(notificationManager);
                    });
                }
            }

            // Notification employé demandeur
            if (mouvement.getEmployeDemandeur() != null 
                    && mouvement.getEmployeDemandeur().getEmail() != null) {
                userService.findByEmail(mouvement.getEmployeDemandeur().getEmail()).ifPresent(userDemandeur -> {
                    Notification notificationEmploye = new Notification();
                    notificationEmploye.setIdUtilisateurDestinataire(userDemandeur.getId());
                    notificationEmploye.setIdUtilisateurExpediteur(idUserRH);
                    notificationEmploye.setLien("/mouvement/demande");
                    notificationEmploye.setMessage("Votre demande de mouvement a été validée par le service RH.");
                    notificationEmploye.setReferenceId(mouvement.getId());
                    notificationEmploye.setReferenceType("Mouvement");
                    notifications.add(notificationEmploye);
                });
            }
        } else if (mouvement.getStatut() == 5) {
            // Notification manager
            if (mouvement.getInfosProActuel() != null && mouvement.getInfosProActuel().getDepartement() != null) {
                Manager managerActuel = managerService.findManagerActuelByDepartement(
                        mouvement.getInfosProActuel().getDepartement().getId()
                );
                if (managerActuel != null) {
                    userService.findByEmail(managerActuel.getEmploye().getEmail()).ifPresent(userManager -> {
                        Notification notificationManager = new Notification();
                        notificationManager.setIdUtilisateurDestinataire(userManager.getId());
                        notificationManager.setIdUtilisateurExpediteur(idUserRH);
                        notificationManager.setMessage("La demande de mouvement de "
                                + mouvement.getEmployeDemandeur().getNom() + " "
                                + mouvement.getEmployeDemandeur().getPrenom()
                                + " a été refusée par le service RH.");
                        notificationManager.setLien("/mouvements/validation");
                        notificationManager.setReferenceId(mouvement.getId());
                        notificationManager.setReferenceType("Mouvement");
                        notifications.add(notificationManager);
                    });
                }
            }

            // Notification employé demandeur
            if (mouvement.getEmployeDemandeur() != null 
                    && mouvement.getEmployeDemandeur().getEmail() != null) {
                userService.findByEmail(mouvement.getEmployeDemandeur().getEmail()).ifPresent(userDemandeur -> {
                    Notification notificationEmploye = new Notification();
                    notificationEmploye.setIdUtilisateurDestinataire(userDemandeur.getId());
                    notificationEmploye.setIdUtilisateurExpediteur(idUserRH);
                    notificationEmploye.setLien("/mouvement/demande");
                    notificationEmploye.setMessage("Votre demande de mouvement a été refusée par le service RH. Vérifiez le commentaire du RH.");
                    notificationEmploye.setReferenceId(mouvement.getId());
                    notificationEmploye.setReferenceType("Mouvement");
                    notifications.add(notificationEmploye);
                });
            }
        }
        return notificationRepository.saveAll(notifications);
    }

    @Transactional
    public List<Notification> createNotificationValidationConge(DemandeConge demande, String idUserRH) {
        List<Notification> notifications = new ArrayList<>();

        // 1. Notification pour le RH
        Notification notificationRH = new Notification();
        notificationRH.setIdUtilisateurDestinataire(idUserRH);
        notificationRH.setIdUtilisateurExpediteur("système");
        if(demande.getStatut() == 6){
            notificationRH.setMessage("Demande de congé validée avec succès. Un email de confirmation a été envoyé au Manager et  à l'employé");
        }   
        if(demande.getStatut() == 7){
            notificationRH.setMessage("Demande de congé refusée par le service RH. Un email de confirmation a été envoyé au Manager et  à l'employé");
        }   
        notificationRH.setLien("/employees/" + demande.getEmploye().getId() + "/conges");
        notificationRH.setReferenceId(demande.getId());
        notificationRH.setReferenceType("DemandeConge");
        notifications.add(notificationRH);

        // 2. Notification pour l'employé
        userService.findByEmail(demande.getEmploye().getEmail()).ifPresent(userEmploye -> {
            Notification notificationEmploye = new Notification();
            notificationEmploye.setIdUtilisateurDestinataire(userEmploye.getId());
            notificationEmploye.setIdUtilisateurExpediteur(idUserRH);
            if(demande.getStatut() == 6){
                notificationEmploye.setMessage("Votre demande de congé a été validée par votre Manager et a été vérifiée par le service RH. Attestation de congé reçue via email");
            } 
            if(demande.getStatut() == 7){
                notificationEmploye.setMessage("Votre demande de congé a été refusée par le service RH.");
            }
            notificationEmploye.setLien("/conge/demande");
            notificationEmploye.setReferenceId(demande.getId());
            notificationEmploye.setReferenceType("DemandeConge");
            notifications.add(notificationEmploye);
        });

        // 3. Notification pour le manager
        userService.findByEmail(demande.getManager().getEmploye().getEmail()).ifPresent(userManager -> {
            Notification notificationManager = new Notification();
            notificationManager.setIdUtilisateurDestinataire(userManager.getId());
            notificationManager.setIdUtilisateurExpediteur(idUserRH);
            if(demande.getStatut() == 6){
                notificationManager.setMessage("La demande de congé de " 
                    + demande.getEmploye().getNom() + " "
                    + demande.getEmploye().getPrenom()
                    + " a été vérifiée avec succès par le service RH");
            } 
            if(demande.getStatut() == 7){
                notificationManager.setMessage("La demande de congé de " 
                    + demande.getEmploye().getNom() + " "
                    + demande.getEmploye().getPrenom()
                    + " a été refusée par le service RH. Vérifiez le commentaire du RH");
            }
            notificationManager.setLien("/conge/validation");
            notificationManager.setReferenceId(demande.getId());
            notificationManager.setReferenceType("DemandeConge");
            notifications.add(notificationManager);
        });

        return notificationRepository.saveAll(notifications);
    }

    public void createNotificationsDemandeConge(DemandeConge demandeConge, String userIdActuel){
        Notification notifications = new Notification();
        notifications.setMessage("Demande de congé de " + demandeConge.getNbJours() + " jours du " + demandeConge.getDateDebut() + " au " + demandeConge.getDateFin() + " à valider");
        notifications.setReferenceType("DemandeConge");
        notifications.setReferenceId(demandeConge.getId());
        notifications.setIdUtilisateurExpediteur(userIdActuel);
        notifications.setLien("/employees/" + demandeConge.getEmploye().getId() + "/conges");

        List<UserRole> les_user_role = userRoleService.getByTypeName("Admin_RH");
        for(UserRole ur : les_user_role){
            notifications.setIdUtilisateurDestinataire(ur.getUser().getId());
            notificationRepository.save(notifications);
        }
    }
    
    // Créer des notifications en masse
    public List<Notification> createNotifications(List<Notification> notifications) {
        notifications.forEach(notif -> {
            notif.setCreatedAt(LocalDateTime.now());
            notif.setModifiedAt(LocalDateTime.now());
            notif.setEstLu(false);
        });
        return notificationRepository.saveAll(notifications);
    }
    
    // Récupérer toutes les notifications d'un utilisateur
    public List<Notification> getNotificationsByUser(String userId) {
        return notificationRepository.findByIdUtilisateurDestinataireOrderByCreatedAtDesc(userId);
    }
    
    // Récupérer les notifications d'un utilisateur avec pagination
    public Page<Notification> getNotificationsByUser(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return notificationRepository.findByIdUtilisateurDestinataireOrderByCreatedAtDesc(userId, pageable);
    }
    
    // Récupérer les notifications non lues d'un utilisateur
    public List<Notification> getNonLuesByUser(String userId) {
        return notificationRepository.findByIdUtilisateurDestinataireAndEstLuFalseOrderByCreatedAtDesc(userId);
    }
    
    // Compter les notifications non lues
    public long countNonLues(String userId) {
        return notificationRepository.countNonLuesByUtilisateur(userId);
    }
    
    // Marquer une notification comme lue
    public void markAsRead(Long id) {
        notificationRepository.marquerCommeLue(id);
    }
    
    // Marquer toutes les notifications d'un utilisateur comme lues
    public void markAllAsRead(String userId) {
        notificationRepository.marquerToutCommeLu(userId);
    }
    
    // Récupérer une notification par son ID
    public Notification getNotificationById(Long id) {
        return notificationRepository.findById(id).orElse(null);
    }
    
    // Supprimer une notification
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
    
    // Supprimer toutes les notifications d'un utilisateur
    public void deleteAllNotificationsByUser(String userId) {
        notificationRepository.deleteByIdUtilisateurDestinataire(userId);
    }
    
    // Nettoyer les anciennes notifications
    public void cleanOldNotifications(int days) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        notificationRepository.deleteByCreatedAtBefore(cutoffDate);
    }
    
    // Rechercher des notifications
    public Page<Notification> searchNotifications(String userId, Boolean estLu, String referenceType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return notificationRepository.rechercherNotifications(userId, estLu, referenceType, pageable);
    }
    
    // Mettre à jour une notification
    public Notification updateNotification(Long id, Notification notificationDetails) {
        Notification notification = notificationRepository.findById(id).orElse(null);
        if (notification != null) {
            notification.setMessage(notificationDetails.getMessage());
            notification.setLien(notificationDetails.getLien());
            notification.setReferenceType(notificationDetails.getReferenceType());
            notification.setReferenceId(notificationDetails.getReferenceId());
            notification.setModifiedAt(LocalDateTime.now());
            return notificationRepository.save(notification);
        }
        return null;
    }
}
