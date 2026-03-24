package com.rh.manage.Controller;

import com.rh.manage.Model.EmergencyContact;
import com.rh.manage.Service.EmergencyContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/emergency-contact")
// @CrossOrigin(origins = "*")

public class EmergencyContactController {

    @Autowired
    private EmergencyContactService emergencyContactService;

    // ✅ Réponse standardisée
    private ResponseEntity<Map<String, Object>> createResponse(Object data, String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", status == HttpStatus.OK || status == HttpStatus.CREATED);
        response.put("message", message);
        response.put("data", data);
        return new ResponseEntity<>(response, status);
    }

    // ✅ GET - Récupérer tous les contacts
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllContacts() {
        try {
            List<EmergencyContact> contacts = emergencyContactService.getAll();
            return createResponse(contacts, "Contacts récupérés avec succès", HttpStatus.OK);
        } catch (Exception e) {
            return createResponse(null, "Erreur serveur: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ✅ GET - Récupérer un contact par ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getContactById(@PathVariable String id) {
        try {
            Optional<EmergencyContact> contact = emergencyContactService.getById(id);
            
            if (contact.isPresent()) {
                return createResponse(contact.get(), "Contact trouvé", HttpStatus.OK);
            } else {
                return createResponse(null, "Contact non trouvé", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return createResponse(null, "Erreur: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ✅ POST - Créer un nouveau contact
    @PostMapping
    public ResponseEntity<Map<String, Object>> createContact(@RequestBody EmergencyContact contact) {
        try {
            EmergencyContact createdContact = emergencyContactService.create(contact);
            return createResponse(createdContact, "Contact créé avec succès", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return createResponse(null, e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return createResponse(null, "Erreur création: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ✅ PUT - Mettre à jour un contact
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateContact(@PathVariable String id, @RequestBody EmergencyContact contact) {
        try {
            if (!id.equals(contact.getId())) {
                return createResponse(null, "ID incohérent", HttpStatus.BAD_REQUEST);
            }
            EmergencyContact updatedContact = emergencyContactService.update_contact(id, contact);
            return createResponse(updatedContact, "Contact modifié avec succès", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return createResponse(null, e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return createResponse(null, "Erreur modification: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ✅ DELETE - Supprimer un contact
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteContact(@PathVariable String id) {
        try {
            emergencyContactService.deleteById(id);
            return createResponse(null, "Contact supprimé avec succès", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return createResponse(null, e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return createResponse(null, "Erreur suppression: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
