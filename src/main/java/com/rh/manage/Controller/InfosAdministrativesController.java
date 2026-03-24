package com.rh.manage.Controller;

import com.rh.manage.Model.InfosAdministratives;
import com.rh.manage.Service.InfosAdministrativesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/infos-administratives")
// @CrossOrigin(origins = "*")

public class InfosAdministrativesController {

    @Autowired
    private InfosAdministrativesService infosAdministrativesService;

    // ✅ Réponse standardisée
    private ResponseEntity<Map<String, Object>> createResponse(Object data, String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", status == HttpStatus.OK || status == HttpStatus.CREATED);
        response.put("message", message);
        response.put("data", data);
        return new ResponseEntity<>(response, status);
    }

    // ✅ GET - Récupérer toutes les infos administratives
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllInfosAdmin() {
        try {
            List<InfosAdministratives> infosList = infosAdministrativesService.getAll();
            return createResponse(infosList, "Informations administratives récupérées avec succès", HttpStatus.OK);
        } catch (Exception e) {
            return createResponse(null, "Erreur serveur: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ✅ GET - Récupérer une info administrative par ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getInfoAdminById(@PathVariable String id) {
        try {
            Optional<InfosAdministratives> infoAdmin = infosAdministrativesService.getById(id);
            
            if (infoAdmin.isPresent()) {
                return createResponse(infoAdmin.get(), "Information administrative trouvée", HttpStatus.OK);
            } else {
                return createResponse(null, "Information administrative non trouvée avec l'ID: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return createResponse(null, "Erreur: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ✅ GET - Récupérer une info administrative par CIN
    @GetMapping("/cin/{cin}")
    public ResponseEntity<Map<String, Object>> getInfoAdminByCin(@PathVariable String cin) {
        try {
            Optional<InfosAdministratives> infoAdmin = infosAdministrativesService.getByCin(cin);
            
            if (infoAdmin.isPresent()) {
                return createResponse(infoAdmin.get(), "Information administrative trouvée", HttpStatus.OK);
            } else {
                return createResponse(null, "Aucune information administrative trouvée avec le CIN: " + cin, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return createResponse(null, "Erreur: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ✅ GET - Vérifier si un CIN existe
    @GetMapping("/check-cin/{cin}")
    public ResponseEntity<Map<String, Object>> checkCinExists(@PathVariable String cin) {
        try {
            boolean exists = infosAdministrativesService.isCinExists(cin);
            Map<String, Boolean> result = new HashMap<>();
            result.put("exists", exists);
            return createResponse(result, "Vérification CIN effectuée", HttpStatus.OK);
        } catch (Exception e) {
            return createResponse(null, "Erreur vérification CIN: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ✅ POST - Créer une nouvelle info administrative
    @PostMapping
    public ResponseEntity<Map<String, Object>> createInfoAdmin(@RequestBody InfosAdministratives infosAdmin) {
        try {
            InfosAdministratives createdInfo = infosAdministrativesService.create(infosAdmin);
            return createResponse(createdInfo, "Information administrative créée avec succès", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return createResponse(null, e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return createResponse(null, "Erreur création: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ✅ PUT - Mettre à jour une info administrative (méthode update standard)
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateInfoAdmin(@PathVariable String id, @RequestBody InfosAdministratives infosAdmin) {
        try {
            // S'assurer que l'ID dans le path correspond à l'ID dans le body
            if (!id.equals(infosAdmin.getId())) {
                return createResponse(null, "L'ID dans l'URL ne correspond pas à l'ID de l'information administrative", HttpStatus.BAD_REQUEST);
            }
            
            InfosAdministratives updatedInfo = infosAdministrativesService.update(infosAdmin);
            return createResponse(updatedInfo, "Information administrative modifiée avec succès", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return createResponse(null, e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return createResponse(null, "Erreur modification: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ✅ PUT - Mettre à jour une info administrative (méthode update_info_admin personnalisée)
    @PutMapping("/{id}/update-admin")
    public ResponseEntity<Map<String, Object>> updateInfoAdminCustom(@PathVariable String id, @RequestBody InfosAdministratives infosAdmin) {
        try {
            // Forcer l'ID depuis le path pour éviter les incohérences
            infosAdmin.setId(id);
            
            InfosAdministratives updatedInfo = infosAdministrativesService.update_info_admin(infosAdmin, id);
            return createResponse(updatedInfo, "Information administrative modifiée avec succès", HttpStatus.OK);
        } catch (RuntimeException e) {
            return createResponse(null, e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return createResponse(null, "Erreur modification: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ✅ PUT - Mettre à jour sans ID dans le path (version alternative)
    @PutMapping
    public ResponseEntity<Map<String, Object>> updateInfoAdmin(@RequestBody InfosAdministratives infosAdmin) {
        try {
            InfosAdministratives updatedInfo = infosAdministrativesService.update(infosAdmin);
            return createResponse(updatedInfo, "Information administrative modifiée avec succès", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return createResponse(null, e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return createResponse(null, "Erreur modification: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ✅ DELETE - Supprimer une info administrative par ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteInfoAdmin(@PathVariable String id) {
        try {
            infosAdministrativesService.deleteById(id);
            return createResponse(null, "Information administrative supprimée avec succès", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return createResponse(null, e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return createResponse(null, "Erreur suppression: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}