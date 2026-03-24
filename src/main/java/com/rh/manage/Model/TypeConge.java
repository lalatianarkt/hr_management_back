package com.rh.manage.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "type_conge")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class TypeConge {

    @Id
    private String id;

    @Column(name = "intitule", nullable = false, length = 50)
    private String intitule;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "obligatoire_doc")
    private Boolean obligatoireDoc;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "is_cumulable")
    private Boolean isCumulable;

    // Getters et Setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIntitule() { return intitule; }
    public void setIntitule(String intitule) { this.intitule = intitule; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getObligatoireDoc() { return obligatoireDoc; }
    public void setObligatoireDoc(Boolean obligatoireDoc) { this.obligatoireDoc = obligatoireDoc; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }

    public Boolean getIsCumulable() { return isCumulable; }
    public void setIsCumulable(Boolean isCumulable) { this.isCumulable = isCumulable; }
}

