package com.rh.manage.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Model.Notification;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    // Trouver les notifications par destinataire
    List<Notification> findByIdUtilisateurDestinataireOrderByCreatedAtDesc(String idUtilisateurDestinataire);
    
    Page<Notification> findByIdUtilisateurDestinataireOrderByCreatedAtDesc(String idUtilisateurDestinataire, Pageable pageable);
    
    // Trouver les notifications non lues par destinataire
    List<Notification> findByIdUtilisateurDestinataireAndEstLuFalseOrderByCreatedAtDesc(String idUtilisateurDestinataire);
    
    // Compter les notifications non lues
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.idUtilisateurDestinataire = :userId AND n.estLu = false")
    long countNonLuesByUtilisateur(@Param("userId") String userId);
    
    // Marquer comme lue
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.estLu = true, n.modifiedAt = CURRENT_TIMESTAMP WHERE n.id = :id")
    void marquerCommeLue(@Param("id") Long id);
    
    // Marquer toutes les notifications d'un utilisateur comme lues
    @Modifying
    @Transactional
    @Query("UPDATE Notification n SET n.estLu = true, n.modifiedAt = CURRENT_TIMESTAMP WHERE n.idUtilisateurDestinataire = :userId AND n.estLu = false")
    void marquerToutCommeLu(@Param("userId") String userId);
    
    // Supprimer les notifications plus anciennes qu'une date
    @Modifying
    @Transactional
    @Query("DELETE FROM Notification n WHERE n.createdAt < :date")
    void deleteByCreatedAtBefore(@Param("date") LocalDateTime date);
    
    // Supprimer les notifications d'un utilisateur
    @Modifying
    @Transactional
    void deleteByIdUtilisateurDestinataire(String idUtilisateurDestinataire);
    
    // Trouver par référence
    List<Notification> findByReferenceTypeAndReferenceId(String referenceType, String referenceId);
    
    // Trouver par expéditeur
    List<Notification> findByIdUtilisateurExpediteurOrderByCreatedAtDesc(String idUtilisateurExpediteur);
    
    // Recherche avancée
    @Query("SELECT n FROM Notification n WHERE " +
           "(:userId IS NULL OR n.idUtilisateurDestinataire = :userId) AND " +
           "(:estLu IS NULL OR n.estLu = :estLu) AND " +
           "(:referenceType IS NULL OR n.referenceType = :referenceType) " +
           "ORDER BY n.createdAt DESC")
    Page<Notification> rechercherNotifications(@Param("userId") String userId,
                                               @Param("estLu") Boolean estLu,
                                               @Param("referenceType") String referenceType,
                                               Pageable pageable);
}