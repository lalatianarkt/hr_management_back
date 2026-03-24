package com.rh.manage.Controller;

import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Model.Notification;
import com.rh.manage.Service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Integer id) {
        return notificationService.getNotificationParId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        Notification created = notificationService.creerNotification(notification);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notification> updateNotification(@PathVariable Integer id, 
                                                          @RequestBody Notification notification) {
        try {
            Notification updated = notificationService.modifierNotification(id, notification);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Integer id) {
        notificationService.supprimerNotification(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/utilisateur")
    public ResponseEntity<?> getNotificationsByUser(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                        "status", 401,
                        "message", "Utilisateur non authentifié",
                        "error", "UNAUTHENTICATED"
                    ));
        }
        
        String userId = (String) authentication.getPrincipal();
        return ResponseEntity.ok(notificationService.getNotificationsParUtilisateur(userId));
    }

    @GetMapping("/utilisateur/{userId}/non-lues")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable String userId) {
        return ResponseEntity.ok(notificationService.getNotificationsNonLues(userId));
    }

    @GetMapping("/utilisateur/{userId}/non-lues/count")
    public ResponseEntity<Long> countUnreadNotifications(@PathVariable String userId) {
        return ResponseEntity.ok(notificationService.compterNotificationsNonLues(userId));
    }

    @GetMapping("/utilisateur/{userId}/dernieres")
    public ResponseEntity<List<Notification>> getLatestNotifications(
            @PathVariable String userId,
            @RequestParam(defaultValue = "5") int limite) {
        return ResponseEntity.ok(notificationService.getDernieresNotifications(userId, limite));
    }

    @PutMapping("/{id}/lire")
    public ResponseEntity<Notification> markAsRead(@PathVariable Integer id) {
        try {
            Notification notification = notificationService.marquerCommeLue(id);
            return ResponseEntity.ok(notification);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/utilisateur/{userId}/lire-tout")
    public ResponseEntity<Void> markAllAsRead(@PathVariable String userId) {
        notificationService.marquerToutesCommeLues(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<Notification>> getNotificationsByManager(@PathVariable String managerId) {
        return ResponseEntity.ok(notificationService.getNotificationsParManager(managerId));
    }

    @GetMapping("/periode")
    public ResponseEntity<List<Notification>> getNotificationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(notificationService.getNotificationsParPeriode(debut, fin));
    }

    @GetMapping("/recherche")
    public ResponseEntity<List<Notification>> searchNotifications(@RequestParam String motCle) {
        return ResponseEntity.ok(notificationService.rechercherNotifications(motCle));
    }

    @PostMapping("/envoyer")
    public ResponseEntity<Notification> sendSimpleNotification(
            @RequestParam String email,
            @RequestParam String message,
            @RequestParam String managerId,
            @RequestParam String concernedUserId,
            @RequestParam String senderUserId) {
        Notification notification = notificationService.envoyerNotificationSimple(
                email, message, managerId, concernedUserId, senderUserId);
        return new ResponseEntity<>(notification, HttpStatus.CREATED);
    }

    @PostMapping("/envoyer-groupe")
    public ResponseEntity<List<Notification>> sendBulkNotifications(
            @RequestBody List<String> emails,
            @RequestParam String message,
            @RequestParam String managerId,
            @RequestParam String concernedUserId,
            @RequestParam String senderUserId) {
        List<Notification> notifications = notificationService.envoyerNotificationGroupee(
                emails, message, managerId, concernedUserId, senderUserId);
        return new ResponseEntity<>(notifications, HttpStatus.CREATED);
    }

    @GetMapping("/statistiques/total")
    public ResponseEntity<Long> getTotalNotifications() {
        return ResponseEntity.ok(notificationService.getNombreTotalNotifications());
    }

    @GetMapping("/statistiques/utilisateur/{userId}")
    public ResponseEntity<Long> getNotificationsCountByUser(@PathVariable String userId) {
        return ResponseEntity.ok(notificationService.getNombreNotificationsParUtilisateur(userId));
    }

    @GetMapping("/statistiques/statut/{status}")
    public ResponseEntity<Long> getNotificationsCountByStatus(@PathVariable Integer status) {
        return ResponseEntity.ok(notificationService.getNombreNotificationsParStatut(status));
    }

    @DeleteMapping("/nettoyage")
    public ResponseEntity<Void> cleanupOldNotifications(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate avant) {
        notificationService.nettoyerAnciennesNotifications(avant);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recentes")
    public ResponseEntity<List<Notification>> getRecentNotifications() {
        LocalDate debut = LocalDate.now().minusDays(7);
        LocalDate fin = LocalDate.now();
        return ResponseEntity.ok(notificationService.getNotificationsParPeriode(debut, fin));
    }

    @GetMapping("/utilisateurs/non-lues")
    public ResponseEntity<Long> getTotalUnreadForUsers(@RequestParam List<String> users) {
        long total = users.stream()
                .mapToLong(notificationService::compterNotificationsNonLues)
                .sum();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> notificationExists(@PathVariable Integer id) {
        return ResponseEntity.ok(notificationService.getNotificationParId(id).isPresent());
    }
}
