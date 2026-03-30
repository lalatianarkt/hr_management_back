package com.rh.manage.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.rh.manage.Model.Notification;
import com.rh.manage.Service.NotificationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    // Créer une notification
    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        try {
            Notification created = notificationService.createNotification(notification);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Créer une notification avec paramètres simples
    @PostMapping("/simple")
    public ResponseEntity<Notification> createSimpleNotification(
            @RequestParam String message,
            @RequestParam(required = false) String expediteur,
            @RequestParam String destinataire,
            @RequestParam(required = false) String lien,
            @RequestParam(required = false) String referenceType,
            @RequestParam(required = false) String referenceId) {
        try {
            Notification notification = notificationService.createNotification(message, expediteur, destinataire, lien, referenceType, referenceId);
            return new ResponseEntity<>(notification, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Créer plusieurs notifications
    @PostMapping("/batch")
    public ResponseEntity<List<Notification>> createNotifications(@RequestBody List<Notification> notifications) {
        try {
            List<Notification> created = notificationService.createNotifications(notifications);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Récupérer toutes les notifications d'un utilisateur
    @GetMapping("/utilisateur")
    public ResponseEntity<List<Notification>> getNotificationsByUser(Authentication authentication) {
        try {
            String userId = authentication.getPrincipal().toString();
            List<Notification> notifications = notificationService.getNotificationsByUser(userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Récupérer les notifications d'un utilisateur avec pagination
    @GetMapping("/utilisateur/paginated")
    public ResponseEntity<Page<Notification>> getNotificationsByUserPaginated(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            String userId = authentication.getPrincipal().toString();
            Page<Notification> notifications = notificationService.getNotificationsByUser(userId, page, size);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Récupérer les notifications non lues d'un utilisateur
    @GetMapping("/utilisateur/non-lues")
    public ResponseEntity<List<Notification>> getNonLuesByUser(Authentication authentication) {
        try {
            String userId = authentication.getPrincipal().toString();
            List<Notification> notifications = notificationService.getNonLuesByUser(userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Compter les notifications non lues
    @GetMapping("/utilisateur/count-non-lues")
    public ResponseEntity<Map<String, Long>> countNonLues(Authentication authentication) {
        try {
            String userId = authentication.getPrincipal().toString();
            long count = notificationService.countNonLues(userId);
            Map<String, Long> response = new HashMap<>();
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Récupérer une notification par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {
        try {
            Notification notification = notificationService.getNotificationById(id);
            if (notification != null) {
                return ResponseEntity.ok(notification);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Marquer une notification comme lue
    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        try {
            notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Marquer toutes les notifications d'un utilisateur comme lues
    @PutMapping("/utilisateur/read-all")
    public ResponseEntity<Void> markAllAsRead(Authentication authentication) {
        try {
            String userId = authentication.getPrincipal().toString();
            notificationService.markAllAsRead(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Mettre à jour une notification
    @PutMapping("/{id}")
    public ResponseEntity<Notification> updateNotification(@PathVariable Long id, @RequestBody Notification notification) {
        try {
            Notification updated = notificationService.updateNotification(id, notification);
            if (updated != null) {
                return ResponseEntity.ok(updated);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Supprimer une notification
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        try {
            notificationService.deleteNotification(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Supprimer toutes les notifications d'un utilisateur
    @DeleteMapping("/utilisateur")
    public ResponseEntity<Void> deleteAllNotificationsByUser(Authentication authentication) {
        try {
            String userId = authentication.getPrincipal().toString();
            notificationService.deleteAllNotificationsByUser(userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Nettoyer les anciennes notifications
    @DeleteMapping("/clean")
    public ResponseEntity<Void> cleanOldNotifications(@RequestParam(defaultValue = "30") int days) {
        try {
            notificationService.cleanOldNotifications(days);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Rechercher des notifications
    @GetMapping("/search")
    public ResponseEntity<Page<Notification>> searchNotifications(
            Authentication authentication,
            @RequestParam(required = false) Boolean estLu,
            @RequestParam(required = false) String referenceType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            String userId = authentication.getPrincipal().toString();
            Page<Notification> notifications = notificationService.searchNotifications(userId, estLu, referenceType, page, size);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
