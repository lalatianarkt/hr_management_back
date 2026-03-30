package com.rh.manage.Controller;

import com.rh.manage.Dto.EmployeDTO;
import com.rh.manage.Dto.EmployeInfosDTO;
import com.rh.manage.Dto.EmployeeFilterDTO;
import com.rh.manage.Dto.StatAllInfoDTO;
import com.rh.manage.Model.Employe;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Service.AutomatisationService;
import com.rh.manage.Service.DepartementService;
import com.rh.manage.Service.EmployeService;
import com.rh.manage.Service.InfosProfessionnellesService;
import com.rh.manage.Service.StatAllInfoService;
import com.rh.manage.Service.TokenService;
import com.rh.manage.Service.TokenService.TokenException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/employes")
public class EmployeController {
    @Autowired
    private StatAllInfoService statAllInfoService; 

    @Autowired
    private EmployeService employeService;

    @Autowired
    private TokenService tokenService;

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Employe employe) {
        try {
            Employe updatedEmploye = employeService.update(employe);
            return ResponseEntity.ok(updatedEmploye);    
        } catch (Exception e) {
            System.out.println("Erreur lors de la modification de l'employé: " + e.getMessage());
            e.printStackTrace(); 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la modification de l'employé: " + e.getMessage());
        }
    }

    @GetMapping("/allEmpActives")
    public ResponseEntity<?> getAllEmployeesActived() {
        try {
            return ResponseEntity.ok(employeService.findAllEmployeesActived());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération des employés: " + e.getMessage());
        }
    }

    @PostMapping("/{idEmploye}/archive")
    public ResponseEntity<?> archive(@PathVariable String idEmploye) {
        try {
            return ResponseEntity.ok(employeService.archiver(idEmploye));
        } catch(TokenException e){
            e.printStackTrace();
            return ResponseEntity.status(401).body(e.getMessage());
        }  catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération des employés: " + e.getMessage());
        }
    }


    // @GetMapping("/allEmpWithInfosActisAndInactifs")
    // public ResponseEntity<Map<String, Object>> getMethodName(@RequestParam(defaultValue = "0") int page,
    //     @RequestParam(defaultValue = "10") int size,
    //     @RequestParam(defaultValue = "nom") String sortBy,
    //     @RequestParam(defaultValue = "asc") String direction) {
    //         try {
    //         StatAllInfoDTO stat = statAllInfoService.getAllStatistiques();
    //         Page<EmployeInfosDTO> employesPage = employeService.getAllEmployesWithInfos(
    //             page, size, sortBy, direction
    //         );
            
    //         Map<String, Object> response = new HashMap<>();
    //         response.put("statistiques", stat);
    //         response.put("employes", employesPage.getContent());
    //         response.put("currentPage", employesPage.getNumber());
    //         response.put("totalItems", employesPage.getTotalElements());
    //         response.put("totalPages", employesPage.getTotalPages());
    //         response.put("pageSize", employesPage.getSize());
    //         response.put("hasNext", employesPage.hasNext());
    //         response.put("hasPrevious", employesPage.hasPrevious());
    //         response.put("totalEmployes", employesPage.getTotalElements());
            
    //         return ResponseEntity.ok(response);
    //         } catch (Exception e) {
    //             System.err.println("Erreur lors de la récupération des employés: " + e.getMessage());
    //             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    //         }
    // }

    @GetMapping("/archived")
    public ResponseEntity<?> getAllEmpArchives(@RequestHeader("Authorization") String authHeader) {
        try {
            tokenService.validateToken(authHeader);
            return ResponseEntity.ok(employeService.getAllEmpArchived());
        } catch(TokenException e){
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // @GetMapping("/allEmp")
    // public ResponseEntity<?> getAllEmp() {
    //     try {
            
    //     } catch (Exception e) {
            
    //     }
    // }
    

    @PostMapping("/allEmpWithInfos")
    public ResponseEntity<?> getAllEmployeesWithInfos(
        @RequestBody EmployeeFilterDTO employeeFilterDTO
    ) {
        try {
            Page<EmployeInfosDTO> employePage = employeService.getAllEmployesWithInfosFiltered(
                employeeFilterDTO.getPage(), employeeFilterDTO.getSize(), employeeFilterDTO.getSortBy(), employeeFilterDTO.getDirection(), 
                employeeFilterDTO.getMatricule(), employeeFilterDTO.getNom(), employeeFilterDTO.getPrenom(),
                 employeeFilterDTO.getDepartementId(), employeeFilterDTO.getTypeContratId(), employeeFilterDTO.getStatutId(), employeeFilterDTO.getIsManager()
            );
            
            StatAllInfoDTO stat = statAllInfoService.getAllStatistiques();
            
            Map<String, Object> response = new HashMap<>();
            response.put("employes", employePage.getContent());
            response.put("currentPage", employePage.getNumber());
            response.put("totalItems", employePage.getTotalElements());
            response.put("totalPages", employePage.getTotalPages());
            response.put("hasNext", employePage.hasNext());
            response.put("hasPrevious", employePage.hasPrevious());
            response.put("pageSize", employeeFilterDTO.getSize());
            response.put("statistiques", stat);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @GetMapping
    public ResponseEntity<?> getAllEmployes() {
        try {
            List<Employe> employes = employeService.getAll();
            return ResponseEntity.ok(employes);
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des employés: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeById(@PathVariable String id) {
        try {
            EmployeInfosDTO emp = employeService.getEmployeWithInfosById(id);            
            if (emp != null) {
                return ResponseEntity.ok(emp);
            } else {
                System.out.println("Employé non trouvé: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employé non trouvé");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors de la récupération de l'employé " + id + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération de l'employé");
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getEmployeByEmail(@PathVariable String email) {
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++email " + email);
        Optional<Employe> employeOpt = employeService.getByEmail(email);
        Employe emp = employeOpt.get();
        if(emp != null){
            System.out.println("emp " + emp.getEmail());
        }
        if (employeOpt.isPresent()) {
            return ResponseEntity.ok(employeOpt.get());
        } else {
            return ResponseEntity.status(404).body("Employé non trouvé");
        }
    }

    @PostMapping
    public ResponseEntity<?> saveEmploye(@RequestBody EmployeDTO employeDTO) {
        try {
            System.out.println("employeDTO : " + employeDTO.getRegion().getId());   
            Employe emp = employeService.insertionIntegraleEmploye(employeDTO);
            // automatisationService.sendMailDeBienvenueAutomatique(emp);
            return ResponseEntity.ok("Employé enregistré avec succès");
        } catch (DataAccessException e) {
            System.out.println("erreur : ++++++++++++++++++++++++++++++++++++++++++++++++ : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'enregistrement de l'employé : problème d'accès aux données");
        } catch (IllegalArgumentException e) {
            System.out.println("ato zany ve : " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body("Données employé invalides : " + e.getMessage());
        } 
        // catch(HttpMessageNotReadableException e){
        //     e.printStackTrace();
        //     System.out.println("erreur de conversion : " + e.getMessage());
        //     return ResponseEntity.badRequest()
        //             .body("Erreur de conversion en JSON : " + e.getMessage());
        // }
        
        catch (Exception e) {
            System.out.println("ato ka : " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur inattendue : " + e.getMessage());
        }
    }
    
    @GetMapping("/actifs")
    public ResponseEntity<List<Employe>> getEmployesActifs() {
        try {
            List<Employe> employesActifs = employeService.getEmployesActifs();
            return ResponseEntity.ok(employesActifs);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{employeId}/infos-professionnelles")
    public ResponseEntity<?> getInfosProfessionnellesByEmployeId(@PathVariable String employeId) {
        try {
            List<InfosProfessionnelles> infos = employeService.getInfosProfessionnellesByEmployeId(employeId);
            return ResponseEntity.ok(infos);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{employeId}/derniere-info-professionnelle")
    public ResponseEntity<InfosProfessionnelles> getDerniereInfoProfessionnelleByEmployeId(@PathVariable String employeId) {
        try {
            Optional<InfosProfessionnelles> info = employeService.getDerniereInfoProfessionnelleByEmployeId(employeId);
            
            if (info.isPresent()) {
                return ResponseEntity.ok(info.get());
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @GetMapping("/{employeId}/info-professionnelle")
    public ResponseEntity<?> getInfoProfessionnelleByEmployeId(@PathVariable String employeId) {
        try {
            List<InfosProfessionnelles> info = employeService.getInfoProfessionnelleByEmployeId(employeId);
            if (info.size() != 0 && info.size() > 0) {
                return ResponseEntity.ok(info);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
