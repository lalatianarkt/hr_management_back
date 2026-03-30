package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.Notification;
import com.rh.manage.Repository.NotificationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
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