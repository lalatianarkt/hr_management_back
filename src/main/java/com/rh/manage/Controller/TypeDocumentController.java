package com.rh.manage.Controller;

import com.rh.manage.Model.TypeDocument;
import com.rh.manage.Service.TypeDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/type-documents")
public class TypeDocumentController {
    @Autowired
    private TypeDocumentService typeDocumentService;
    
    // GET: Récupérer tous les types de documents
    @GetMapping
    public ResponseEntity<List<TypeDocument>> getAllTypeDocuments() {
        try {
            List<TypeDocument> types = typeDocumentService.getAllTypeDocuments();
            return ResponseEntity.ok(types);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // GET: Récupérer un type par son ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getTypeDocumentById(@PathVariable String id) {
        try {
            Optional<TypeDocument> typeDocument = typeDocumentService.getTypeDocumentById(id);
            
            if (typeDocument.isPresent()) {
                return ResponseEntity.ok(typeDocument.get());
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Type de document non trouvé");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // GET: Rechercher des types par intitulé
    @GetMapping("/search")
    public ResponseEntity<List<TypeDocument>> searchTypeDocuments(@RequestParam String keyword) {
        try {
            List<TypeDocument> types = typeDocumentService.searchTypeDocuments(keyword);
            return ResponseEntity.ok(types);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // POST: Créer un nouveau type de document
    @PostMapping
    public ResponseEntity<?> createTypeDocument(@RequestBody TypeDocument typeDocument) {
        try {
            TypeDocument createdType = typeDocumentService.createTypeDocument(typeDocument);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdType);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // PUT: Mettre à jour un type de document
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTypeDocument(@PathVariable String id, @RequestBody TypeDocument typeDocument) {
        try {
            TypeDocument updatedType = typeDocumentService.updateTypeDocument(id, typeDocument);
            return ResponseEntity.ok(updatedType);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // DELETE: Supprimer un type de document
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTypeDocument(@PathVariable String id) {
        try {
            typeDocumentService.deleteTypeDocument(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Type de document supprimé avec succès");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // GET: Compter le nombre de types de documents
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countTypeDocuments() {
        try {
            long count = typeDocumentService.countTypeDocuments();
            Map<String, Long> response = new HashMap<>();
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // GET: Vérifier si un type existe
    @GetMapping("/exists/{id}")
    public ResponseEntity<Map<String, Boolean>> typeDocumentExists(@PathVariable String id) {
        try {
            boolean exists = typeDocumentService.typeDocumentExists(id);
            Map<String, Boolean> response = new HashMap<>();
            response.put("exists", exists);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
