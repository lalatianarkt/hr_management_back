package com.rh.manage.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "type_mouvement")
public class TypeMouvement {

    @Id
    @Column(length = 50)
    private String id;

    @Column(length = 250, nullable = false)
    private String type;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    // Constructeurs
    public TypeMouvement() {}

    public TypeMouvement(String id, String type, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.type = type;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }
}

