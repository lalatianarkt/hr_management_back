package com.rh.manage.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Entity
@Table(name = "type_contrat")
public class TypeContrat {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "intitule", length = 50, nullable = false)
    private String intitule;

    @Column(name = "description", length = 255)
    private String description;

    // Constructeur par défaut
    public TypeContrat() {}

    // Constructeur avec paramètres
    public TypeContrat(String id, String intitule, String description) {
        this.id = id;
        this.intitule = intitule;
        this.description = description;
    }

    // Getters et setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // toString()
    @Override
    public String toString() {
        return "TypeContrat{" +
                "id='" + id + '\'' +
                ", intitule='" + intitule + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
