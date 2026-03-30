package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Dto.EmployeDTO;
import com.rh.manage.Dto.EmployeDetailsDTO;
import com.rh.manage.Dto.EmployeInfosDTO;
import com.rh.manage.Enum.ModeCalcul;
import com.rh.manage.Model.Departement;
import com.rh.manage.Model.EmergencyContact;
import com.rh.manage.Model.Employe;
import com.rh.manage.Model.EtatCivil;
import com.rh.manage.Model.InfosAdministratives;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Model.Manager;
import com.rh.manage.Model.ModePaiement;
import com.rh.manage.Model.Nationalite;
import com.rh.manage.Model.Poste;
import com.rh.manage.Model.PosteEmploye;
import com.rh.manage.Model.Region;
import com.rh.manage.Model.Sexe;
import com.rh.manage.Repository.EmployeRepository;
import com.rh.manage.Repository.InfosProfessionnellesRepository;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeService {

    @Autowired
    private InfosProfessionnellesRepository infosProfessionnellesRepository;

    @Autowired
    InfosProfessionnellesService infosProfessionnellesService;

    @Autowired
    EmployeRepository employeRepository;

    @Autowired
    InfosAdministrativesService infosAdministrativesService;

    @Autowired 
    EmergencyContactService emergencyContactService;

    @Autowired
    NationaliteService nationaliteService;

    @Autowired
    SexeService sexeService;

    @Autowired
    ManagerService managerService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private ModePaiementService modePaiementService;

    // ✅ Récupérer tous les employés
    public List<Employe> getAll() {
        return employeRepository.findAll();
    }

    // ✅ Récupérer par ID
    public Optional<Employe> getById(String id) {
        return employeRepository.findById(id);
    }

    // ✅ Récupérer par email
    public Optional<Employe> getByEmail(String email) {
        return employeRepository.findByEmail(email);
    }

    // ✅ Créer un employé
    public Employe create(Employe employe) {
        return employeRepository.save(employe);
    }

    // ✅ Mettre à jour un employé
    public Employe update(Employe employe) {
        System.out.println("employeInfoNationalite : " + employe.getNationalite().getId());
        Employe employeInfo = employeRepository.findById(employe.getId()).get();
        if(employe.getEmergencyContact() == null){
            System.out.println("ato aloha e++++++++++++++");
            employe.setEmergencyContact(emergencyContactService.getById(employeInfo.getEmergencyContact().getId()).get()); 
        } 
        if(employe.getNationalite() != null){
            employe.setNationalite(nationaliteService.getNationaliteById(employe.getNationalite().getId()).get());
        }
        if(employe.getEtatCivil() != null){
            if (employe.getEtatCivil() == EtatCivil.CELIBATAIRE) {
                employe.setEtatCivil(EtatCivil.CELIBATAIRE);

            } else if (employe.getEtatCivil() == EtatCivil.MARIE) {
                employe.setEtatCivil(EtatCivil.MARIE);

            } else if (employe.getEtatCivil() == EtatCivil.DIVORCE) {
                employe.setEtatCivil(EtatCivil.DIVORCE);

            } else if (employe.getEtatCivil() == EtatCivil.VEUF) {
                employe.setEtatCivil(EtatCivil.VEUF);
            }
        }
        if(employe.getSexe() != null){
            employe.setSexe(sexeService.getSexeById(employe.getSexe().getId()));
        }
        if(employe.getRegion() != null){
            employe.setRegion(regionService.findById(employe.getRegion().getId()).get());
        }
        if(employe.getNom() == null){
            employe.setNom(employeInfo.getNom());
            employe.setPrenom(employeInfo.getPrenom());
            employe.setCin(employeInfo.getCin());
            employe.setCodePostal(employeInfo.getCodePostal());
            employe.setDateNaissance(employeInfo.getDateNaissance());
            employe.setEmail(employeInfo.getEmail());
            employe.setEtatCivil(employeInfo.getEtatCivil());
            employe.setLieuNaissance(employeInfo.getLieuNaissance());
            employe.setNationalite(employeInfo.getNationalite());
            employe.setNbEnfants(employeInfo.getNbEnfants());
            employe.setNomConjoint(employeInfo.getNomConjoint());
            employe.setNomMere(employeInfo.getNomMere());
            employe.setNomPere(employeInfo.getNomPere());
            employe.setNumCnaps(employeInfo.getNumCnaps());
            employe.setNumOstie(employeInfo.getNumOstie());
            employe.setRegion(employeInfo.getRegion());
            employe.setAdresse(employeInfo.getAdresse());
            employe.setSexe(employeInfo.getSexe());
            employe.setStatut(employeInfo.getStatut());
            employe.setTelephone(employeInfo.getTelephone());
            
        }
        return employeRepository.save(employe);
    }  

    // ✅ Supprimer un employé
    public void delete(String id) {
        employeRepository.deleteById(id);

    }

    public List<Employe> findAllEmployeesActived(){
        return employeRepository.findAllEmployeesByStatusSorted(0);
    }

    @Transactional
    public Employe archiver(String idEmploye){
        Employe employe = employeRepository.findById(idEmploye).get();
        employe.setStatut(1);
        // InfosProfessionnelles infoProActuel = infosProfessionnellesService.getDerniereInfoProfessionnelleByEmployeId(idEmploye).get();
        // infoProActuel.setStatut(1);
        // infosProfessionnellesRepository.save(infoProActuel);
        Employe employeUpdated = employeRepository.save(employe);
        return employeUpdated;
    }

    public Long countTotalEmployeInactif(){
        return (long) employeRepository.findByStatut(1).size();
    }

    public Long countTotalEmployes() {
        return (long) employeRepository.findByStatut(0).size();
    }

    public List<Employe> getAllEmpArchived(){
        return employeRepository.findByStatut(1);
    }

    public Page<EmployeInfosDTO> getAllEmployesWithInfosFiltered(
        int page, 
        int size, 
        String sortBy, 
        String direction,
        String matricule,
        String nom,
        String prenom,
        String departementId,
        String typeContratId,
        Long statutId,
        Boolean isManager) {
    
    List<Employe> allEmployes;
    
    List<String> managerEmployeIds = managerService.getManagerActif().stream()
            .map(Manager::getEmploye)  
            .filter(Objects::nonNull)
            .map(Employe::getId)        
            .collect(Collectors.toList());
    
            for (String id : managerEmployeIds) {
                System.out.println("++++++++++++++++++++++++++++idEmp : " + id);
            }
    System.out.println("sizeoffff : " + managerEmployeIds.size());
    
    if (hasAnyFilter(matricule, nom, prenom, departementId, statutId, typeContratId)) {
        allEmployes = employeRepository.findWithFiltersWithoutPagination(
            matricule, nom, prenom, departementId, typeContratId, statutId
        );
    } else {
        allEmployes = employeRepository.findAllActiveEmployeesWithoutPagination();
    }
    
    // Convertir tous les employés en DTO
    List<EmployeInfosDTO> allDtos = allEmployes.stream()
        .map(employe -> {
            List<InfosProfessionnelles> infosList = 
                infosProfessionnellesService.getInfoProfessionnelleByEmployeId(employe.getId());
            
            InfosProfessionnelles infosActives = infosList.stream()
                .filter(info -> info.getStatut() == 0)
                .findFirst()
                .orElse(null);
            
            // Déterminer si l'employé est manager
            boolean isManagerFlag = false;
            
            if (infosActives != null) {
                // Condition 1: L'employé a un manager dans ses infos pro (c'est un manager)
                boolean hasManagerInInfos = infosActives.getManager() == null;
                
                // Condition 2: L'ID de l'employé (String) est dans la liste des managers (table Manager)
                boolean isInManagerList = managerEmployeIds.contains(infosActives.getEmploye().getId());
                
                // Un employé est manager si l'une des deux conditions est vraie
                isManagerFlag = hasManagerInInfos && isInManagerList;
            }
            
            return new EmployeInfosDTO(employe, 
                                    infosActives != null ? List.of(infosActives) : new ArrayList<>(), 
                                    isManagerFlag);
        })
        .collect(Collectors.toList());
    
    // Filtrer par manager si demandé
    if (isManager != null) {
        allDtos = allDtos.stream()
            .filter(dto -> dto.isManager() == isManager)
            .collect(Collectors.toList());
    }
    
    // Trier
    Comparator<EmployeInfosDTO> comparator = getComparator(sortBy, direction);
    if (comparator != null) {
        allDtos.sort(comparator);
    }
    
    // Appliquer la pagination
    int start = Math.min(page * size, allDtos.size());
    int end = Math.min(start + size, allDtos.size());
    List<EmployeInfosDTO> pagedDtos = allDtos.subList(start, end);
    
    // Créer la page avec les bonnes métadonnées
    Sort sort = Sort.by(sortBy);
    sort = direction.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();
    Pageable pageable = PageRequest.of(page, size, sort);
    
    return new PageImpl<>(pagedDtos, pageable, allDtos.size());
}

    // Méthode utilitaire pour le tri
    private Comparator<EmployeInfosDTO> getComparator(String sortBy, String direction) {
        Comparator<EmployeInfosDTO> comparator = null;
        
        switch (sortBy) {
            case "nom":
                comparator = Comparator.comparing(dto -> dto.getEmploye().getNom());
                break;
            case "prenom":
                comparator = Comparator.comparing(dto -> dto.getEmploye().getPrenom());
                break;
            case "matricule":
                comparator = Comparator.comparing(dto -> {
                    if (dto.getInfosProfessionnelles() != null && !dto.getInfosProfessionnelles().isEmpty()) {
                        InfosProfessionnelles info = dto.getInfosProfessionnelles().get(0);
                        return info.getMatricule() != null ? info.getMatricule() : "";
                    }
                    return "";
                });
                break;
            default:
                comparator = Comparator.comparing(dto -> dto.getEmploye().getNom());
        }
        
        if (direction.equalsIgnoreCase("desc")) {
            comparator = comparator.reversed();
        }
        
        return comparator;
    }

    private boolean hasAnyFilter(String matricule, String nom, String prenom, 
                            String departementId, Long statutId, String typeContratId) {
    
        System.out.println("=== DEBUG hasAnyFilter ===");
        System.out.println("typeContratId: " + typeContratId);
        System.out.println("typeContratId is null? " + (typeContratId == null));
        System.out.println("typeContratId is empty? " + (typeContratId != null && typeContratId.trim().isEmpty()));
        System.out.println("Should filter by typeContrat? " + (typeContratId != null && !typeContratId.trim().isEmpty()));
        
        boolean hasFilter = (matricule != null && !matricule.trim().isEmpty()) ||
            (nom != null && !nom.trim().isEmpty()) ||
            (prenom != null && !prenom.trim().isEmpty()) ||
            departementId != null ||
            statutId != null ||
            (typeContratId != null && !typeContratId.trim().isEmpty());
        
        System.out.println("hasAnyFilter result: " + hasFilter);
        return hasFilter;
    }

    // Version paginée avec paramètres
    // public Page<EmployeInfosDTO> getAllEmployesWithInfos(
    //     int page, 
    //     int size, 
    //     String sortBy, 
    //     String direction
    // ) {
    //     // Créer l'objet Pageable
    //     Sort sort = Sort.by(sortBy);
        
    //     if (direction.equalsIgnoreCase("desc")) {
    //         sort = sort.descending();
    //     } else {
    //         sort = sort.ascending();
    //     }
        
    //     Pageable pageable = PageRequest.of(page, size, sort);
        
    //     // Récupérer la page d'employés
    //     Page<Employe> employePage = employeRepository.findAllActiveEmployeesSorted(pageable);
        
    //     // Transformer chaque employé en DTO
    //     List<EmployeInfosDTO> dtos = employePage.getContent().stream()
    //         .map(employe -> {
    //             List<InfosProfessionnelles> infos = 
    //                 infosProfessionnellesService.getInfoProfessionnelleByEmployeId(employe.getId());
    //             return new EmployeInfosDTO(employe, infos);
    //         })
    //         .collect(Collectors.toList());
        
    //     // Retourner une Page de DTOs
    //     return new PageImpl<>(dtos, pageable, employePage.getTotalElements());
    // }

    public List<EmployeInfosDTO> getAllEmployesWithInfos() {

        List<Employe> employes = employeRepository.findAllEmployeesByStatusSorted(0);
        // System.out.println("long : " + employes.size());

        List<EmployeInfosDTO> result = new ArrayList<>();

        for (Employe employe : employes) {

            List<InfosProfessionnelles> optInfos =
                    infosProfessionnellesService.getInfoProfessionnelleByEmployeId(employe.getId());

            // InfosProfessionnelles infosPro = optInfos.orElse(null);

            // if (infosPro == null) {
            //     // System.out.println("⚠️ Pas d'infos pro pour : " + employe.getId());
            // } else {
            //     // System.out.println("infosPro : " + infosPro.getEmploye().getId());
            // }

            EmployeInfosDTO dto = new EmployeInfosDTO(employe, optInfos, false);
            result.add(dto);
        }

        return result;
    }

    public EmployeInfosDTO getEmployeWithInfosById(String id) {
        Optional<Employe> employeOpt = employeRepository.findByIdAndStatut(id, 0);
        if (employeOpt.isPresent()) {
            Employe employe = employeOpt.get();
            List<InfosProfessionnelles> infosProOpt = infosProfessionnellesRepository.findByEmployeIdAndStatut(id);
            
            EmployeInfosDTO dto = new EmployeInfosDTO();
            dto.setEmploye(employe);
            dto.setInfosProfessionnelles(infosProOpt);
            
            return dto;
        }
        return null;
    }

    public void validation_formulaire_insertion(EmployeDTO employeDTO) throws Exception {
        StringBuilder errors = new StringBuilder();

        // if(employeDTO.getInfosAdministratives() != null){
        //     InfosAdministratives infosAdministratives = employeDTO.getInfosAdministratives();

        //     if(infosAdministratives.getCin().length() != 12){
        //         errors.append("Le nombre de chiffres dans cin doit contenir obligatoirement 12 chiffres. \n");
        //     }
        // }
        
        // Validation de l'employé
        if (employeDTO.getEmploye() != null) {
            // EmployeDetailsDTO employe = employeDTO.getEmployeDetailsDTO();
            Employe employe = employeDTO.getEmploye();

            if(employeDTO.getNationalite() == null){
                errors.append("La nationalité de l'employé est obligatoire. Veuillez remplir ce champ");
            }
            
            if (employe.getNom() == null || employe.getNom().trim().isEmpty()) {
                errors.append("Le nom de l'employé est obligatoire.\n");
            } else if (employe.getNom().length() > 100) {
                errors.append("Le nom de l'employé ne doit pas dépasser 100 caractères.\n");
            } 
            
            if (employe.getPrenom() == null || employe.getPrenom().trim().isEmpty()) {
                errors.append("Le prénom de l'employé est obligatoire.\n");
            } else if (employe.getPrenom().length() > 250) {
                errors.append("Le prénom de l'employé ne doit pas dépasser 250 caractères.\n");
            }
            
            if (employe.getDateNaissance() == null) {
                errors.append("La date de naissance est obligatoire.\n");
            } else if (employe.getDateNaissance().isAfter(java.time.LocalDate.now())) {
                errors.append("La date de naissance ne peut pas être dans le futur.\n");
            }
            
            if (employe.getTelephone() == null || employe.getTelephone().trim().isEmpty()) {
                errors.append("Le téléphone de l'employé est obligatoire.\n");
            } else if (employe.getTelephone().length() > 12) {
                errors.append("Le téléphone de l'employé ne doit pas dépasser 12 caractères.\n");
            }
            
            if (employe.getEmail() == null || employe.getEmail().trim().isEmpty()) {
                errors.append("L'email de l'employé est obligatoire.\n");
            } else if (employe.getEmail().length() > 100) {
                errors.append("L'email de l'employé ne doit pas dépasser 100 caractères.\n");
            } else if (!isValidEmail(employe.getEmail())) {
                errors.append("L'email de l'employé n'est pas valide.\n");
            }
            
            if (employe.getAdresse() == null || employe.getAdresse().trim().isEmpty()) {
                errors.append("L'adresse de l'employé est obligatoire.\n");
            } else if (employe.getAdresse().length() > 255) {
                errors.append("L'adresse de l'employé ne doit pas dépasser 255 caractères.\n");
            }
            
            if (employe.getLieuNaissance() == null || employe.getLieuNaissance().trim().isEmpty()) {
                errors.append("Le lieu de naissance est obligatoire.\n");
            }

            if (employeDTO.getEmploye().getCin() == null || employeDTO.getEmploye().getCin().trim().isEmpty() || 
            employeDTO.getEmploye().getCin().length() < 12 || employeDTO.getEmploye().getCin().length() > 12) {
            errors.append("Le CIN est obligatoire. Veuillez vérifier la longueur du CIN peut ne pas être dans le norme. \n");
            }
            
            if (employeDTO.getEmploye().getNbEnfants() < 0) {
                errors.append("Le nombre d'enfants ne peut pas être négatif.\n");
            }

            if(employeDTO.getEmploye().getEtatCivil() == null){
                errors.append("La situation familiale est obligatoire.\n");
            }
        } else {
            errors.append("Les informations de l'employé sont obligatoires.\n");
        }
        
        // Validation du contact d'urgence
        if (employeDTO.getEmergencyContact() != null) {
            EmergencyContact emergencyContact = employeDTO.getEmergencyContact();
            
            if (emergencyContact.getNom() == null || emergencyContact.getNom().trim().isEmpty()) {
                errors.append("Le nom du contact d'urgence est obligatoire.\n");
            } else if (emergencyContact.getNom().length() > 250) {
                errors.append("Le nom du contact d'urgence ne doit pas dépasser 250 caractères.\n");
            }
            
            if (emergencyContact.getContact() == null || emergencyContact.getContact().trim().isEmpty()) {
                errors.append("Le contact d'urgence est obligatoire.\n");
            } else if (emergencyContact.getContact().length() > 12 && emergencyContact.getContact().length() < 0) {
                errors.append("Le contact d'urgence doit contenir exactement 12 caractères.\n");
            } else if (!emergencyContact.getContact().matches("\\d{10}")) {
                errors.append("Le contact d'urgence doit contenir uniquement des chiffres.\n");
            }
            
            if (emergencyContact.getEmail() != null && !emergencyContact.getEmail().trim().isEmpty()) {
                if (emergencyContact.getEmail().length() > 100) {
                    errors.append("L'email du contact d'urgence ne doit pas dépasser 100 caractères.\n");
                } else if (!isValidEmail(emergencyContact.getEmail())) {
                    errors.append("L'email du contact d'urgence n'est pas valide.\n");
                }
            }
            
            if (emergencyContact.getAdresse() != null && emergencyContact.getAdresse().length() > 255) {
                errors.append("L'adresse du contact d'urgence ne doit pas dépasser 255 caractères.\n");
            }
        } else {
            errors.append("Le contact d'urgence est obligatoire.\n");
        }
        
        // Validation des informations professionnelles
        if (employeDTO.getInfosProfessionnelles().size() != 0) {
            List<InfosProfessionnelles> InfosProfessionnelles = employeDTO.getInfosProfessionnelles();
            for (InfosProfessionnelles infosPro : InfosProfessionnelles) {
                if (infosProfessionnellesService.isMatriculeExists(infosPro.getMatricule())){
                    errors.append("Cette matricule est déja attribué à un employé");
                }

                if (infosPro.getDateEmbauche() == null) {
                    errors.append("La date d'embauche est obligatoire.\n");
                } else if (infosPro.getDateEmbauche().isAfter(java.time.LocalDate.now())) {
                    errors.append("La date d'embauche ne peut pas être dans le futur.\n");
                }
                
                if (infosPro.getPoste() == null || infosPro.getPoste().getId() == null) {
                    errors.append("Le poste est obligatoire.\n");
                }
                
                if (infosPro.getTypeContrat() == null || infosPro.getTypeContrat().getId() == null) {
                    errors.append("Le type de contrat est obligatoire.\n");
                }
            }
        } else {
            errors.append("Les informations professionnelles sont obligatoires.\n");
        }
        // Validation du sexe et nationalité
        if (employeDTO.getSexe() == null || employeDTO.getSexe().getId() == null) {
            errors.append("Le sexe est obligatoire.\n");
        }
        
        if (errors.length() > 0) {
            throw new IllegalArgumentException("Erreurs de validation :\n" + errors.toString());
        }
    }

    // Méthode utilitaire pour valider les emails
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email != null && email.matches(emailRegex);
    }

    @Transactional
    public Employe insertionIntegraleEmploye(EmployeDTO employeDTO) throws Exception {
        try {
            System.out.println("aonaaa lalaaaaaaaa");
            System.out.println("Efa tafiditra ato anie zah eee !  : " + employeDTO.getRegion().getId());
            // 1. Validation du formulaire
            validation_formulaire_insertion(employeDTO);
            Employe employe = employeDTO.getEmploye();

            EmergencyContact emergencyContact = emergencyContactService.create(employeDTO.getEmergencyContact());
            Nationalite nationalite = nationaliteService.getNationaliteById(employeDTO.getNationalite().getId()).get();
            Sexe sexe = sexeService.getSexeById(employeDTO.getSexe().getId()); 
            Region region = regionService.findById(employeDTO.getRegion().getId()).get();
            
            employe.setEmergencyContact(emergencyContact);
            // employe.setCreatedAt(LocalDateTime.now());
            employe.setNationalite(nationalite);
            employe.setSexe(sexe);
            employe.setRegion(region);

            // Sauvegarde finale de l'employé
            employe = create(employe);
            
            employeDTO.setEmploye(employe);// 
            System.out.println("idEmp : " + employe.getId());// historiquePosteService.save(employeDTO.getHistoriquePoste());
            
            Employe emp_inserted = getById(employeDTO.getEmploye().getId()).get();
            
            List<InfosProfessionnelles> infosPros = employeDTO.getInfosProfessionnelles();
            Manager manager = managerService.findManagerActuelByDepartement(infosPros.get(0).getDepartement().getId());
            for (InfosProfessionnelles infosPro : infosPros) {
                infosPro.setEmploye(emp_inserted);
                InfosProfessionnelles newInfos = new InfosProfessionnelles();
                newInfos.setEmploye(infosPro.getEmploye());
                newInfos.setTypeContrat(infosPro.getTypeContrat());
                newInfos.setSalaireBase(infosPro.getSalaireBase());
                newInfos.setDateDebutAssignationPoste(infosPro.getDateDebutAssignationPoste());
                newInfos.setDateFinAssignationPoste(infosPro.getDateFinAssignationPoste());
                newInfos.setMatricule(infosPro.getMatricule());
                newInfos.setClassification(infosPro.getClassification());
                newInfos.setCategorieProfessionnelle(infosPro.getCategorieProfessionnelle());
                newInfos.setTypeTempsTravail(infosPro.getTypeTempsTravail());
                newInfos.setTypeEntree(infosPro.getTypeEntree());
                newInfos.setDepartement(infosPro.getDepartement());
                // Manager managerActuel = managerService.findManagerActuelByDepartement();
                if(infosPros.size() > 1){
                    if(infosPro.getDateDebutAssignationPoste().isEqual(infosPros.get(0).getDateDebutAssignationPoste())){
                        newInfos.setStatut(1); 
                    }
                }                
                
                if(manager != null){
                    System.out.println();
                    newInfos.setManager(manager);
                } 
                newInfos.setDateEmbauche(infosPro.getDateEmbauche());
                newInfos.setDateDebutAssignationPoste(infosPro.getDateDebutAssignationPoste());
                newInfos.setPoste(infosPro.getPoste());

                // System.out.println("infoPro : "+ infosPro.getDepartement().getId());
                System.out.println("emp dans pro : " + infosPro.getEmploye().getId());
                // infosPro.setEmploye(employe); 
                infosPro = infosProfessionnellesService.create(newInfos); 

                // ✅ DÉBOGUAGE COMPLET
                System.out.println("=== DÉBOGUAGE RELATIONS INFO_PRO ===");
                // System.out.println("Departement: " + infosPro.getDepartement());
                // System.out.println("Departement ID: " + (infosPro.getDepartement() != null ? infosPro.getDepartement().getId() : "null"));
                System.out.println("Poste: " + infosPro.getPoste());
                System.out.println("Poste ID: " + (infosPro.getPoste() != null ? infosPro.getPoste().getId() : "null"));
                System.out.println("TypeContrat: " + infosPro.getTypeContrat());
                System.out.println("TypeContrat ID: " + (infosPro.getTypeContrat() != null ? infosPro.getTypeContrat().getId() : "null"));
                System.out.println("Employe: " + infosPro.getEmploye());
                System.out.println("Employe ID: " + (infosPro.getEmploye() != null ? infosPro.getEmploye().getId() : "null"));
                // System.out.println("Manager: " + infosPro.getManager());
                System.out.println("=====================================");
            }
            if(employeDTO.getModePaiements() != null){
                System.out.println("non null tyh e");
                for (ModePaiement modePaiement : employeDTO.getModePaiements()) {
                    modePaiement.setEmploye(emp_inserted);
                    System.out.println("mode de paiement : "+ modePaiement.getCleRib());
                    if(modePaiement != null){
                        modePaiementService.createModePaiement(modePaiement);
                    } 
                }
            }
            return employe;
        } catch (Exception e) {
            // Log détaillé de l'erreur
            System.out.println("Erreur lors de l'insertion intégrale de l'employé : " + e.getMessage());
            e.printStackTrace();
            // Relancer l'exception pour rollback
            throw e;
        }
    } 

    // Récupérer tous les employés actifs (statut = 0)
    public List<Employe> getEmployesActifs() {
        return employeRepository.findByStatut(0);
    }
    
    // Récupérer un employé actif par ID
    public Optional<Employe> getEmployeActifById(String id) {
        return employeRepository.findByIdAndStatut(id, 0);
        // return Optional.ofNullable(employe);
    }
    
    // Récupérer les infos professionnelles avec statut = 0 d'un employé
    public List<InfosProfessionnelles> getInfosProfessionnellesByEmployeId(String employeId) {
        return infosProfessionnellesRepository.findByEmployeIdAndStatut(employeId);
    } 
    
    // Récupérer la dernière info professionnelle avec statut = 0 d'un employé
    public Optional<InfosProfessionnelles> getDerniereInfoProfessionnelleByEmployeId(String employeId) {
        List<InfosProfessionnelles> infos = infosProfessionnellesRepository.findLatestByEmployeIdAndStatut(employeId);
        return infos.isEmpty() ? Optional.empty() : Optional.of(infos.get(0));
    }
    
    // Récupérer une info professionnelle avec statut = 0 d'un employé
    public List<InfosProfessionnelles> getInfoProfessionnelleByEmployeId(String employeId) {
        return infosProfessionnellesRepository.findByEmployeIdAndStatut(employeId);
    }
    
    // Vérifier si un employé a des infos professionnelles avec statut = 0
    // public boolean employeAvecInfosProfessionnelles(String employeId) {
    //     List<InfosProfessionnelles> infos = infosProfessionnellesRepository.findByEmployeIdAndStatut(employeId, 0);
    //     return !infos.isEmpty();
    // } 

    public Employe update_emp(Employe employe, String id) {
        try {

            System.out.println("tafiditra ato e ++++++++++++++++");
            // Vérifier si l'employé existe
            // Employe emp_to_update = getById(id)
            //     .orElseThrow(() -> new RuntimeException("Employé non trouvé avec l'ID: " + id));
            
            // System.out.println("employe : "+ emp_to_update.getId());    
            // emp_to_update.setNom(employe.getNom());
            // emp_to_update.setPrenom(employe.getPrenom());
            employe.setId(id);
            EtatCivil etatCivil = employe.getEtatCivil();
            if (etatCivil == EtatCivil.CELIBATAIRE) {
                // emp_to_update.setEtatCivil(EtatCivil.CELIBATAIRE);
                employe.setEtatCivil(EtatCivil.CELIBATAIRE);

            } else if (etatCivil == EtatCivil.MARIE) {
                // emp_to_update.setEtatCivil(EtatCivil.MARIE);
                employe.setEtatCivil(EtatCivil.MARIE);

            } else if (etatCivil == EtatCivil.DIVORCE) {
                // emp_to_update.setEtatCivil(EtatCivil.DIVORCE);
                employe.setEtatCivil(EtatCivil.DIVORCE);

            } else if (etatCivil == EtatCivil.VEUF) {
                // emp_to_update.setEtatCivil(EtatCivil.VEUF);
                employe.setEtatCivil(EtatCivil.VEUF);
            }
            
            // emp_to_update.setDateNaissance(employe.getDateNaissance());
            // emp_to_update.setLieuNaissance(employe.getLieuNaissance());
            // emp_to_update.setTelephone(employe.getTelephone());
            // emp_to_update.setEmail(employe.getEmail());
            // emp_to_update.setAdresse(employe.getAdresse());
            // emp_to_update.setNomMere(employe.getNomMere());
            // emp_to_update.setNomPere(employe.getNomPere());
            // emp_to_update.setNumCnaps(employe.getNumCnaps());
            // emp_to_update.setNumOstie(employe.getNumOstie());
            // emp_to_update.setCodePostal(employe.getCodePostal());
            // emp_to_update.setRegion(employe.getRegion());

            // System.out.println("eto mihitsy");
            
            // // Sauvegarder et retourner
            // Employe emp_updated = update(emp_to_update);
            // return emp_updated; 
            return update(employe);
            
        } catch (RuntimeException e) {
            // Relancer les exceptions métier (employé non trouvé)
            throw e;
        } catch (Exception e) {
            // Logger l'erreur technique
            System.out.println("Erreur technique lors de la modification de l'employé " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur technique lors de la modification de l'employé", e);
        }
    }

//     @Transactional
// public void insertionIntegraleEmploye(EmployeDTO employeDTO) throws Exception {
//     try {
//         // ... code existant jusqu'à la création de l'employé ...
//         System.out.println("ato1");
//         Employe emp_inserted = getById(employeDTO.getEmploye().getId()).get();
//         System.out.println("ato2");
//         InfosProfessionnelles infosPro = employeDTO.getInfosProfessionnelles();

//         // ✅ GESTION CORRECTE DU MANAGER
//         Manager managerPersiste = null;
        
//         if (infosPro.getManager() != null) {
//             System.out.println("ato");
//             // CAS 1: Manager a un ID → vérifier s'il existe en base
//             if (infosPro.getManager().getId() != null) {
//                 Optional<Manager> existingManager = managerService.getManagerById(infosPro.getManager().getId());
//                 if (existingManager.isPresent()) {
//                     managerPersiste = existingManager.get(); // ✅ Manager existant
//                     System.out.println("✅ Manager existant assigné: " + managerPersiste.getId());
//                 } else {
//                     System.out.println("⚠️ Manager avec ID non trouvé, mis à null");
//                     managerPersiste = null;
//                 }
//             } 
//             // CAS 2: Manager sans ID → nouvel objet non persisté → ERREUR POTENTIELLE
//             else {
//                 System.out.println("❌ Manager sans ID - objet non persisté, mis à null");
//                 managerPersiste = null;
//             }
//         }
//         // CAS 3: Manager est null → aucun problème
//         else {
//             System.out.println("✅ Aucun manager assigné");
//             managerPersiste = null;
//         }

//         // ✅ CRÉATION DE L'INFO PRO
//         InfosProfessionnelles newInfos = new InfosProfessionnelles();
//         newInfos.setEmploye(emp_inserted);
//         newInfos.setDepartement(infosPro.getDepartement());
//         newInfos.setTypeContrat(infosPro.getTypeContrat());
//         newInfos.setPoste(infosPro.getPoste());
//         newInfos.setDateEmbauche(infosPro.getDateEmbauche());
//         newInfos.setDateDebut(infosPro.getDateDebut());
//         newInfos.setStatut(1);
        
//         // ✅ ASSIGNATION SÉCURISÉE DU MANAGER
//         newInfos.setManager(managerPersiste); // Soit null, soit Manager persisté

//         System.out.println("Sauvegarde avec manager: " + (managerPersiste != null ? managerPersiste.getId() : "null"));
//         infosPro = infosProfessionnellesService.create(newInfos);
        
//     } catch (Exception e) {
//         System.err.println("Erreur lors de l'insertion intégrale de l'employé : " + e.getMessage());
//         e.printStackTrace();
//         throw e;
//     }
// }

    // public List<Employe> getAllManagers(){
        
    // }
}
