package com.rh.manage.Service;

import com.rh.manage.Model.DocumentEmploye;
import com.rh.manage.Model.Employe;
import com.rh.manage.Model.TypeDocument;
import com.rh.manage.Repository.DocumentEmployeRepository;
import com.rh.manage.Repository.EmployeRepository;
import com.rh.manage.Repository.TypeDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class DocumentEmployeService {
    
    @Autowired
    private DocumentEmployeRepository documentRepository;
    
    @Autowired
    private EmployeRepository employeRepository;
    
    @Autowired
    private TypeDocumentRepository typeDocumentRepository;
    
    @Value("${file.upload-dir:./uploads}")
    private String uploadDir;

    
    
    // Uploader un document
    public DocumentEmploye uploadDocument(MultipartFile file, String employeId, 
                                         String typeDocumentId, String description) throws IOException {
        
        // Vérifier l'employé
        Employe employe = employeRepository.findById(employeId)
            .orElseThrow(() -> new RuntimeException("Employé non trouvé"));
        
        // Vérifier le type de document
        TypeDocument typeDocument = typeDocumentRepository.findById(typeDocumentId)
            .orElseThrow(() -> new RuntimeException("Type de document non trouvé"));
        
        // Vérifier le fichier
        if (file.isEmpty()) {
            throw new RuntimeException("Le fichier est vide");
        }
        
        // Vérifier la taille (max 10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new RuntimeException("Le fichier est trop volumineux (max 10MB)");
        }
        
        // Créer le répertoire d'upload s'il n'existe pas
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Générer un nom de fichier unique
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
        Path targetLocation = uploadPath.resolve(uniqueFilename);
        
        // Sauvegarder le fichier
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        
        // Créer et sauvegarder le document
        DocumentEmploye document = new DocumentEmploye();
        document.setNomFichier(originalFilename);
        document.setCheminFichier(targetLocation.toString());
        document.setContentType(file.getContentType());
        document.setFileSize(file.getSize());
        document.setDescription(description);
        document.setEmploye(employe);
        document.setTypeDocument(typeDocument);
        
        return documentRepository.save(document);
    }

    public DocumentEmploye delete(String documentId, DocumentEmploye documentEmploye){
        documentEmploye.setId(documentId);
        return documentRepository.save(documentEmploye);
    }
    
    // Récupérer un document pour affichage (view)
    public Resource viewDocument(String documentId) throws MalformedURLException {
        DocumentEmploye document = documentRepository.findById(documentId)
            .orElseThrow(() -> new RuntimeException("Document non trouvé"));
        
        Path filePath = Paths.get(document.getCheminFichier());
        Resource resource = new UrlResource(filePath.toUri());
        
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("Le fichier n'existe pas ou n'est pas accessible: " + filePath);
        }
    }
    
    // Récupérer le contenu du fichier en bytes (pour l'affichage)
    public byte[] getFileContent(String documentId) throws IOException {
        DocumentEmploye document = documentRepository.findById(documentId)
            .orElseThrow(() -> new RuntimeException("Document non trouvé"));
        
        Path filePath = Paths.get(document.getCheminFichier());
        return Files.readAllBytes(filePath);
    }
    
    // Récupérer un document par ID
    public DocumentEmploye getById(String id) {
        return documentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Document non trouvé avec l'ID: " + id));
    }
    
    // Récupérer tous les documents d'un employé
    public List<DocumentEmploye> getDocumentsByEmploye(String employeId) {
        return documentRepository.findByEmployeIdAndStatutOrderByDateUploadDesc(employeId, 0);
    }
    
    // Supprimer un document
    public void deleteDocument(String documentId) throws IOException {
        DocumentEmploye document = documentRepository.findById(documentId)
            .orElseThrow(() -> new RuntimeException("Document non trouvé"));
        
        // Supprimer le fichier physique
        Path filePath = Paths.get(document.getCheminFichier());
        Files.deleteIfExists(filePath);
        
        // Supprimer l'entrée en base
        documentRepository.delete(document);
    }
    
    // Mettre à jour la description
    public DocumentEmploye updateDescription(String documentId, String description) {
        DocumentEmploye document = documentRepository.findById(documentId)
            .orElseThrow(() -> new RuntimeException("Document non trouvé"));
        
        document.setDescription(description);
        return documentRepository.save(document);
    }
}