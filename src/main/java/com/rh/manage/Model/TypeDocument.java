package com.rh.manage.Model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "type_document")
public class TypeDocument {
    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "intitule", length = 50, nullable = false)
    private String intitule;

    // Relation OneToMany avec DocumentEmploye
    // @OneToMany(mappedBy = "typeDocument", cascade = CascadeType.ALL)
    // private List<DocumentEmploye> documents;

    // Constructeurs
    public TypeDocument() {}

    public TypeDocument(String id, String intitule) {
        this.id = id;
        this.intitule = intitule;
    }

    // Getters et Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIntitule() { return intitule; }
    public void setIntitule(String intitule) { this.intitule = intitule; }

    // public List<DocumentEmploye> getDocuments() { return documents; }
    // public void setDocuments(List<DocumentEmploye> documents) { this.documents = documents; }

    // Méthode utilitaire pour ajouter un document
    public void addDocument(DocumentEmploye document) {
        // this.documents.add(document);
        document.setTypeDocument(this);
    }

    public void removeDocument(DocumentEmploye document) {
        // this.documents.remove(document);
        document.setTypeDocument(null);
    }
}