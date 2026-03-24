package com.rh.manage.Model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "email_destinateur", length = 50)
    private String recipientEmail;
    
    @Column(name = "date_envoi")
    private LocalDate sentDate;

    @Column(name = "titre", length = 500)
    private String title;

    @Column(columnDefinition = "TEXT", name = "message")
    private String message;
    
    @Column(name = "id_manager", nullable = false)
    private String managerId;
    
    @Column(name = "id_utilisateur_conserne", nullable = false)
    private String concernedUserId;
    
    @Column(name = "id_utilisateur_destinateur", nullable = false)
    private String senderUserId;
    
    @Column(name = "statut")
    private Integer status;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    
    // Constructeurs
    public Notification() {}
    
    public Notification(String recipientEmail, LocalDate sentDate, String message, 
                       String managerId, String concernedUserId, 
                       String senderUserId, Integer status) {
        this.recipientEmail = recipientEmail;
        this.sentDate = sentDate;
        this.message = message;
        this.managerId = managerId;
        this.concernedUserId = concernedUserId;
        this.senderUserId = senderUserId;
        this.status = status;
    }
    
    // Getters et Setters
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getRecipientEmail() {
        return recipientEmail;
    }
    
    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }
    
    public LocalDate getSentDate() {
        return sentDate;
    }
    
    public void setSentDate(LocalDate sentDate) {
        this.sentDate = sentDate;
    }


    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getManagerId() {
        return managerId;
    }
    
    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }
    
    public String getConcernedUserId() {
        return concernedUserId;
    }
    
    public void setConcernedUserId(String concernedUserId) {
        this.concernedUserId = concernedUserId;
    }
    
    public String getSenderUserId() {
        return senderUserId;
    }
    
    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }
    
    public Integer getStatus() {
        return status;
    }
    
    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }
    
    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}