package com.rh.manage.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "document_employe")
public class DocumentEmploye {
    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "nom_fichier", length = 100, nullable = false)
    private String nomFichier;

    @Column(name = "chemin_fichier", length = 150, nullable = false)
    private String cheminFichier;

    @CreationTimestamp
    @Column(name = "date_upload", nullable = false, updatable = false)
    private LocalDateTime dateUpload;

    @UpdateTimestamp
    @Column(name = "date_modified")
    private LocalDateTime dateModified;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "content_type", length = 100)
    private String contentType;

    @Column(name = "description", length = 100)
    private String description;

    @Column(name = "statut")
    private int statut;

    // Relations avec les noms exacts des colonnes de la base
    @ManyToOne
    @JoinColumn(name = "id_type_document") // nullable = true selon votre schéma
    private TypeDocument typeDocument;

    @ManyToOne
    @JoinColumn(name = "id_employe") // nullable = true selon votre schéma
    private Employe employe;

    @PrePersist
    public void prePersist() {
        if (this.id == null || this.id.trim().isEmpty()) {
            this.id = "DOC" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
        if (this.dateUpload == null) {
            this.dateUpload = LocalDateTime.now();
        }
        if (this.dateModified == null) {
            this.dateModified = this.dateModified;
        }
    }
    
    @PreUpdate
    public void preUpdate() {
        this.dateModified = LocalDateTime.now();
    }

    // Constructeurs
    public DocumentEmploye() {}

    public DocumentEmploye(String id, String nomFichier, String cheminFichier) {
        this.id = id;
        this.nomFichier = nomFichier;
        this.cheminFichier = cheminFichier;
    }

    public DocumentEmploye(String id, String nomFichier, String cheminFichier, TypeDocument typeDocument, Employe employe) {
        this.id = id;
        this.nomFichier = nomFichier;
        this.cheminFichier = cheminFichier;
        this.typeDocument = typeDocument;
        this.employe = employe;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNomFichier() { return nomFichier; }
    public void setNomFichier(String nomFichier) { this.nomFichier = nomFichier; }

    public String getCheminFichier() { return cheminFichier; }
    public void setCheminFichier(String cheminFichier) { this.cheminFichier = cheminFichier; }

    public LocalDateTime getDateUpload() { return dateUpload; }
    public void setDateUpload(LocalDateTime dateUpload) { this.dateUpload = dateUpload; }

    public LocalDateTime getDateModified() { return dateModified; }
    public void setDateModified(LocalDateTime dateModified) { this.dateModified = dateModified; }

    public TypeDocument getTypeDocument() { return typeDocument; }
    public void setTypeDocument(TypeDocument typeDocument) { this.typeDocument = typeDocument; }

    public Employe getEmploye() { return employe; }
    public void setEmploye(Employe employe) { this.employe = employe; }

    @Override
    public String toString() {
        return "DocumentEmploye{" +
                "id='" + id + '\'' +
                ", nomFichier='" + nomFichier + '\'' +
                ", cheminFichier='" + cheminFichier + '\'' +
                ", dateUpload=" + dateUpload +
                ", dateModified=" + dateModified +
                '}';
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatut() {
        return statut;
    }

    public void setStatut(int statut) {
        this.statut = statut;
    }

    

    
}

