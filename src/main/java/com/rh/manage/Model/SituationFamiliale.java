package com.rh.manage.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "situation_familiale")
public class SituationFamiliale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "type", length = 50, nullable = false)
    private String type;

    // @Column(name = "created_at", nullable = false)
    // private Timestamp createdAt;

    // @Column(name = "modified_at")
    // private Timestamp modifiedAt;

    // Constructeur par défaut
    public SituationFamiliale() {}

    // Constructeur avec paramètres
    public SituationFamiliale(Integer id, String type) {
        this.id = id;
        this.type = type;
        // this.createdAt = createdAt;
        // this.modifiedAt = modifiedAt;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // public Timestamp getCreatedAt() {
    //     return createdAt;
    // }

    // public void setCreatedAt(Timestamp createdAt) {
    //     this.createdAt = createdAt;
    // }

    // public Timestamp getModifiedAt() {
    //     return modifiedAt;
    // }

    // public void setModifiedAt(Timestamp modifiedAt) {
    //     this.modifiedAt = modifiedAt;
    // }

    @Override
    public String toString() {
        return "SituationFamiliale{" +
                "id=" + id +
                ", type='" + type + '\'' +
                // ", createdAt=" + createdAt +
                // ", modifiedAt=" + modifiedAt +
                '}';
    }
}
