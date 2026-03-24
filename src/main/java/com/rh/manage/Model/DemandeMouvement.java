// package com.rh.manage.Model;

// import jakarta.persistence.*;
// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.time.format.DateTimeFormatter;
// import java.util.UUID;

// @Entity
// @Table(name = "demande_mouvement")
// public class DemandeMouvement {

//     @Id
//     @Column(length = 50)
//     private String id;

//     @Column(length = 255)
//     private String motif;

//     @Column(nullable = false)
//     private Integer statut;  // 0 = en attente, 1 = approuvée, 2 = refusée

//     @Column(name = "date_demande", nullable = false)
//     private LocalDate dateDemande;

//     @Column(name = "date_validation")
//     private LocalDate dateValidation;

//     @Column(length = 255)
//     private String commentaire;

//     // --- NEW TIMESTAMPS ---

//     @Column(name = "created_at", nullable = false)
//     private LocalDateTime createdAt;

//     @Column(name = "modified_at")
//     private LocalDateTime modifiedAt;

//     // --- RELATIONS ---

//     @ManyToOne
//     @JoinColumn(name = "id_employe_demandeur")
//     private Employe employeDemandeur;

//     @ManyToOne
//     @JoinColumn(name = "id_type_mouvement")
//     private TypeMouvement typeMouvement;

//     // -------------------------------------------------------
//     // PrePersist : au moment de la création
//     // -------------------------------------------------------
//     @PrePersist
//     public void prePersist() {

//         // Génération ID automatique
//         if (this.id == null || this.id.isEmpty()) {
//             String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//             String rand = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
//             this.id = "DM-" + date + "-" + rand;
//         }

//         if (this.dateDemande == null) {
//             this.dateDemande = LocalDate.now();
//         }

//         if (this.statut == null) {
//             this.statut = 0; // en attente
//         }

//         this.createdAt = LocalDateTime.now();
//     }

//     // -------------------------------------------------------
//     // PreUpdate : à chaque mise à jour
//     // -------------------------------------------------------
//     @PreUpdate
//     public void preUpdate() {
//         this.modifiedAt = LocalDateTime.now();
//     }

//     // --- Getters & Setters ---

//     public String getId() { return id; }
//     public void setId(String id) { this.id = id; }

//     public String getMotif() { return motif; }
//     public void setMotif(String motif) { this.motif = motif; }

//     public Integer getStatut() { return statut; }
//     public void setStatut(Integer statut) { this.statut = statut; }

//     public LocalDate getDateDemande() { return dateDemande; }
//     public void setDateDemande(LocalDate dateDemande) { this.dateDemande = dateDemande; }

//     public LocalDate getDateValidation() { return dateValidation; }
//     public void setDateValidation(LocalDate dateValidation) { this.dateValidation = dateValidation; }

//     public String getCommentaire() { return commentaire; }
//     public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

//     public LocalDateTime getCreatedAt() { return createdAt; }
//     public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

//     public LocalDateTime getModifiedAt() { return modifiedAt; }
//     public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }

//     public Employe getEmployeDemandeur() { return employeDemandeur; }
//     public void setEmployeDemandeur(Employe employeDemandeur) { this.employeDemandeur = employeDemandeur; }

//     public TypeMouvement getTypeMouvement() { return typeMouvement; }
//     public void setTypeMouvement(TypeMouvement typeMouvement) { this.typeMouvement = typeMouvement; }
// }
