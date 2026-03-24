package com.rh.manage.Repository;

import com.rh.manage.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    
    // Recherche par utilisateur concerné
    List<Notification> findByConcernedUserId(String concernedUserId);
    
    // Recherche par email destinataire
    List<Notification> findByRecipientEmail(String recipientEmail);
    
    // Recherche par manager
    List<Notification> findByManagerId(String managerId);
    
    // Recherche par expéditeur
    List<Notification> findBySenderUserId(String senderUserId);
    
    // Recherche par statut
    List<Notification> findByStatus(Integer status);
    
    // Recherche par date d'envoi
    List<Notification> findBySentDate(LocalDate sentDate);
    
    // Recherche par période
    List<Notification> findBySentDateBetween(LocalDate startDate, LocalDate endDate);
    
    // Recherche combinée : utilisateur concerné + statut
    List<Notification> findByConcernedUserIdAndStatus(String concernedUserId, Integer status);
    
    // Recherche combinée : manager + statut
    List<Notification> findByManagerIdAndStatus(String managerId, Integer status);
    
    // Requête personnalisée JPQL
    @Query("SELECT n FROM Notification n WHERE n.managerId = :managerId AND n.status = :status ORDER BY n.sentDate DESC")
    List<Notification> findNotificationsByManagerAndStatus(@Param("managerId") String managerId, 
                                                          @Param("status") Integer status);
    
    // Mise à jour du statut
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.status = :status WHERE n.id = :id")
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);
    
    // Compter les notifications non lues pour un utilisateur (status = 0)
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.concernedUserId = :userId AND n.status = 0")
    long countUnreadNotifications(@Param("userId") String userId);
    
    // Supprimer les anciennes notifications
    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.sentDate < :date")
    int deleteOldNotifications(@Param("date") LocalDate date);
    
    // Dernières notifications pour un utilisateur
    List<Notification> findTop10ByConcernedUserIdOrderBySentDateDescCreatedAtDesc(String concernedUserId);
    
    // Recherche par mot-clé dans le message
    @Query("SELECT n FROM Notification n WHERE LOWER(n.message) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Notification> searchByKeyword(@Param("keyword") String keyword);


    // Recherche par mot-clé dans le message
    @Query("SELECT n FROM Notification n WHERE LOWER(n.message) LIKE LOWER(CONCAT('%', :motCle, '%'))")
    List<Notification> rechercherParMotCle(@Param("motCle") String motCle);
}