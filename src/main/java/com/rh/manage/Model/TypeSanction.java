package com.rh.manage.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "type_sanction")
public class TypeSanction {

    @Id
    @Column(name = "id", length = 50)
    private String id;

    @Column(name = "intitule", length = 150, nullable = false)
    private String intitule;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "modified_at")
    private Timestamp modifiedAt;

    // Constructeur par défaut
    public TypeSanction() {}

    // Constructeur avec paramètres
    public TypeSanction(String id, String intitule, Timestamp createdAt, Timestamp modifiedAt) {
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Timestamp modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @Override
    public String toString() {
        return "TypeSanction{" +
                "id='" + id + '\'' +
                ", intitule='" + intitule + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }
}
