package com.rh.manage.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Entity
@Table(name = "pointage_fille")
public class PointageFille {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", unique = true)
    // @Column(name = "id", length = 50)
    private String id;
    
    @Column(name = "date_heure_pointage", nullable = false)
    private LocalDateTime dateHeurePointage;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    
    @Column(name = "type_action", length = 3)
    private String typeAction; // "IN", "OUT", "PAU" (pause), "DEB" (début), "FIN" (fin)
    
    @Column(name = "source", length = 50)
    private String source; // "BIOMETRIE", "BADGE", "MANUEL", "SYSTEME"
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "id_pointage", nullable = false, referencedColumnName = "id")
    private Pointage pointage;

    @PrePersist
    public void prePersist() {
        // if (this.id == null || this.id.trim().isEmpty()) {
        //     String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmm"));
        //     this.id = "PTF-" + datePart + "-" + 
        //             Math.abs(new Random().nextInt(100));
        // }
        
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        
        if (this.modifiedAt == null) {
            this.modifiedAt = this.createdAt;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDateHeurePointage() {
        return dateHeurePointage;
    }

    public void setDateHeurePointage(LocalDateTime dateHeurePointage) {
        this.dateHeurePointage = dateHeurePointage;
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

    public String getTypeAction() {
        return typeAction;
    }

    public void setTypeAction(String typeAction) {
        this.typeAction = typeAction;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Pointage getPointage() {
        return pointage;
    }

    public void setPointage(Pointage pointage) {
        this.pointage = pointage;
    }

    // Enums pour les types d'action
    public enum TypeAction {
        ENTREE("IN"),
        SORTIE("OUT"),
        PAUSE_DEBUT("PAU"),
        PAUSE_FIN("FIN"),
        DEBUT_TRAVAIL("DEB"),
        FIN_TRAVAIL("END");
        
        private final String code;
        
        TypeAction(String code) {
            this.code = code;
        }
        
        public String getCode() {
            return code;
        }
        
        public static TypeAction fromCode(String code) {
            for (TypeAction type : TypeAction.values()) {
                if (type.getCode().equals(code)) {
                    return type;
                }
            }
            return null;
        }
    }
    
    // Enums pour les sources
    public enum Source {
        BIOMETRIE("BIOMETRIE"),
        BADGE("BADGE"),
        MANUEL("MANUEL"),
        SYSTEME("SYSTEME"),
        IMPORT("IMPORT");
        
        private final String code;
        
        Source(String code) {
            this.code = code;
        }
        
        public String getCode() {
            return code;
        }
    }
}
