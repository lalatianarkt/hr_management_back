
package com.rh.manage.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.layout.Document;
import com.rh.manage.Model.DocumentEmploye;
import com.rh.manage.Service.DocumentEmployeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/documents")
public class DocumentEmployeController {
    @Autowired
    private DocumentEmployeService documentEmployeService;
    
    // Afficher un document dans le navigateur (PDF viewer)
    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewDocument(@PathVariable String id) {
        System.out.println("=== DEBUG viewDocument ===");
        System.out.println("Document ID: " + id);
        
        try {
            DocumentEmploye document = documentEmployeService.getById(id);
            System.out.println("Document trouvé: " + document.getNomFichier());
            
            byte[] fileContent = documentEmployeService.getFileContent(id);
            System.out.println("Taille contenu: " + fileContent.length + " bytes");
            
            // Déterminer le Content-Type
            String contentType = document.getContentType();
            if (contentType == null || contentType.isEmpty()) {
                contentType = "application/pdf";
            }
            
            // Construire les headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentLength(fileContent.length);
            
            // IMPORTANT: Headers pour permettre l'embedding
            headers.setContentDisposition(ContentDisposition.inline()
                .filename(document.getNomFichier())
                .build());
            
            // Headers CORS importants pour l'embedding
            headers.set("X-Content-Type-Options", "nosniff");
            headers.set("Access-Control-Allow-Origin", "*"); // Ou votre domaine spécifique
            headers.set("Access-Control-Allow-Methods", "GET");
            headers.set("Access-Control-Allow-Headers", "Content-Type");
            headers.set("Access-Control-Expose-Headers", "Content-Disposition");
            
            // Headers de cache
            headers.setCacheControl(CacheControl.noCache());
            headers.setPragma("no-cache");
            headers.setExpires(0L);
            
            System.out.println("Headers envoyés pour le PDF");
            
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    } 
    // Télécharger un document (attachment)
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable String id) {
        try {
            DocumentEmploye document = documentEmployeService.getById(id);
            byte[] fileContent = documentEmployeService.getFileContent(id);
            
            String contentType = document.getContentType();
            if (contentType == null || contentType.isEmpty()) {
                contentType = "application/octet-stream";
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentLength(fileContent.length);
            headers.setContentDisposition(
                ContentDisposition.attachment()
                    .filename(document.getNomFichier())
                    .build()
            );
            
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
            
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    // Uploader un document
    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("employeId") String employeId,
            @RequestParam("typeDocumentId") String typeDocumentId,
            @RequestParam(value = "description", required = false) String description) {
        
        try {
            DocumentEmploye document = documentEmployeService.uploadDocument(
                file, employeId, typeDocumentId, description
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Document uploadé avec succès");
            response.put("document", document);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IOException e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", "Erreur d'upload: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (RuntimeException e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    
    // Récupérer tous les documents d'un employé
    @GetMapping("/employe/{employeId}")
    public ResponseEntity<List<DocumentEmploye>> getDocumentsByEmploye(@PathVariable String employeId) {
        try {
            List<DocumentEmploye> documents = documentEmployeService.getDocumentsByEmploye(employeId);
            return ResponseEntity.ok(documents);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id, @RequestBody DocumentEmploye documentEmploye) {
        try {
            if (!id.equals(documentEmploye.getId())) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "L'ID dans l'URL ne correspond pas à l'ID du document"));
            }
            
            documentEmployeService.delete(id, documentEmploye);
            
            return ResponseEntity.ok()
                .body(Map.of("message", "Document supprimé avec succès"));
                
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Erreur inattendue: " + e.getMessage()));
        }
    }
    
    // Supprimer un document
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable String id) {
        try {
            documentEmployeService.deleteDocument(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Document supprimé avec succès");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Erreur lors de la suppression du fichier");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}