package com.rh.manage.Service;

import com.rh.manage.Model.Notification;
import com.rh.manage.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    // === Opérations CRUD de base ===
    
    public Notification creerNotification(Notification notification) {
        if (notification.getSentDate() == null) {
            notification.setSentDate(LocalDate.now());
        }
        if (notification.getStatus() == null) {
            notification.setStatus(0); // 0 = non lu par défaut
        }
        return notificationRepository.save(notification);
    }
    
    public Optional<Notification> getNotificationParId(Integer id) {
        return notificationRepository.findById(id);
    }
    
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
    
    public Notification modifierNotification(Integer id, Notification notificationDetails) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notification non trouvée avec l'id: " + id));
        
        // Mise à jour des champs
        notification.setRecipientEmail(notificationDetails.getRecipientEmail());
        notification.setMessage(notificationDetails.getMessage());
        notification.setManagerId(notificationDetails.getManagerId());
        notification.setConcernedUserId(notificationDetails.getConcernedUserId());
        notification.setSenderUserId(notificationDetails.getSenderUserId());
        notification.setStatus(notificationDetails.getStatus());
        
        return notificationRepository.save(notification);
    }
    
    public void supprimerNotification(Integer id) {
        notificationRepository.deleteById(id);
    }
    
    // === Méthodes métier ===
    
    public List<Notification> getNotificationsParUtilisateur(String idUtilisateur) {
        return notificationRepository.findByConcernedUserId(idUtilisateur);
    }
    
    public List<Notification> getNotificationsNonLues(String idUtilisateur) {
        return notificationRepository.findByConcernedUserIdAndStatus(idUtilisateur, 0);
    }
    
    public long compterNotificationsNonLues(String idUtilisateur) {
        return notificationRepository.countUnreadNotifications(idUtilisateur);
    }
    
    public Notification marquerCommeLue(Integer id) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Notification non trouvée"));
        notification.setStatus(1); // 1 = lu
        return notificationRepository.save(notification);
    }
    
    public void marquerToutesCommeLues(String idUtilisateur) {
        List<Notification> notificationsNonLues = notificationRepository
            .findByConcernedUserIdAndStatus(idUtilisateur, 0);
        notificationsNonLues.forEach(n -> n.setStatus(1));
        notificationRepository.saveAll(notificationsNonLues);
    }
    
    public List<Notification> getNotificationsParPeriode(LocalDate debut, LocalDate fin) {
        return notificationRepository.findBySentDateBetween(debut, fin);
    }
    
    public List<Notification> getNotificationsParManager(String idManager) {
        return notificationRepository.findByManagerId(idManager);
    }
    
    public List<Notification> getDernieresNotifications(String idUtilisateur, int limite) {
        return notificationRepository.findTop10ByConcernedUserIdOrderBySentDateDescCreatedAtDesc(idUtilisateur)
                .stream().limit(limite).toList();
    }
    
    public List<Notification> rechercherNotifications(String motCle) {
        return notificationRepository.rechercherParMotCle(motCle);
    }
    
    public void nettoyerAnciennesNotifications(LocalDate dateLimite) {
        notificationRepository.deleteOldNotifications(dateLimite);
    }
    
    // === Méthodes utilitaires pour envoi rapide ===
    
    public Notification envoyerNotificationSimple(String email, String message, 
                                                String managerId, String concernedUserId, 
                                                String senderUserId) {
        Notification notification = new Notification();
        notification.setRecipientEmail(email);
        notification.setMessage(message);
        notification.setManagerId(managerId);
        notification.setConcernedUserId(concernedUserId);
        notification.setSenderUserId(senderUserId);
        notification.setSentDate(LocalDate.now());
        notification.setStatus(0);
        
        return notificationRepository.save(notification);
    }
    
    public List<Notification> envoyerNotificationGroupee(List<String> emails, String message,
                                                       String managerId, String concernedUserId,
                                                       String senderUserId) {
        List<Notification> notifications = emails.stream().map(email -> {
            Notification notif = new Notification();
            notif.setRecipientEmail(email);
            notif.setMessage(message);
            notif.setManagerId(managerId);
            notif.setConcernedUserId(concernedUserId);
            notif.setSenderUserId(senderUserId);
            notif.setSentDate(LocalDate.now());
            notif.setStatus(0);
            return notif;
        }).toList();
        
        return notificationRepository.saveAll(notifications);
    }
    
    // === Statistiques ===
    
    public long getNombreTotalNotifications() {
        return notificationRepository.count();
    }
    
    public long getNombreNotificationsParUtilisateur(String idUtilisateur) {
        return notificationRepository.findByConcernedUserId(idUtilisateur).size();
    }
    
    public long getNombreNotificationsParStatut(Integer statut) {
        return notificationRepository.findByStatus(statut).size();
    }
    
}