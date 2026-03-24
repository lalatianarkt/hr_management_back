package com.rh.manage.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "type_departement")
public class TypeDepartement {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "intitule", length = 150, nullable = false)
    private String intitule;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "modified_at")
    private Date modifiedAt;

    // Constructeur par défaut
    public TypeDepartement() {}

    // Constructeur avec paramètres
    public TypeDepartement(String id, String intitule, Date createdAt, Date modifiedAt) {
        this.id = id;
        this.intitule = intitule;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        return "TypeDepartement{" +
                "id='" + id + '\'' +
                ", intitule='" + intitule + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }
}
