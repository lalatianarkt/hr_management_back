package com.rh.manage.Service;

import com.rh.manage.Dto.*;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.View.VueEmployeManagerComplet;
import com.rh.manage.Repository.VueEmployeManagerCompletRepository;
import com.rh.manage.Repository.ManagerRepository;
import com.rh.manage.Model.Manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VueEmployeManagerCompletService {
    @Autowired
    InfosProfessionnellesService infosProfessionnellesService;

    @Autowired
    ManagerRepository managerRepository;

    private final VueEmployeManagerCompletRepository repository;
    
    public VueEmployeManagerCompletService(VueEmployeManagerCompletRepository repository) {
        this.repository = repository;
    }

    public Map<String, Object> getOrganigrammeCompact() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<VueEmployeManagerComplet> toutesDonnees = repository.findAll();
            List<Manager> managersActifs = managerRepository.findByStatut(Manager.Statut.ACTIF);
            
            // DEBUG
            System.out.println("=== DEBUG: Données récupérées ===");
            toutesDonnees.forEach(emp -> {
                System.out.println("Employé: " + emp.getNomComplet() + 
                                ", Manager: " + emp.getNomCompletManager() +
                                ", Département: " + emp.getNomDepartement());
            });
            
            Map<String, List<VueEmployeManagerComplet>> parDepartement = new HashMap<>();
            
            for (VueEmployeManagerComplet emp : toutesDonnees) {
                String departement = emp.getNomDepartement() != null ? 
                    emp.getNomDepartement().trim() : "Non assigné";
                
                parDepartement.computeIfAbsent(departement, k -> new ArrayList<>())
                    .add(emp);
            }
            List<DepartementHierarchiqueDTO> hierarchie = new ArrayList<>();
            
            for (Map.Entry<String, List<VueEmployeManagerComplet>> entry : parDepartement.entrySet()) {
                String nomDepartement = entry.getKey();
                List<VueEmployeManagerComplet> employesDuDepartement = entry.getValue();
                
                System.out.println("=== Département: " + nomDepartement + " ===");
                
                // Grouper les employés par manager
                Map<String, List<VueEmployeManagerComplet>> subordonnesParManager = new HashMap<>();
                
                for (VueEmployeManagerComplet emp : employesDuDepartement) {
                    String manager = emp.getNomCompletManager();
                    if (manager != null && !manager.trim().isEmpty()) {
                        subordonnesParManager.computeIfAbsent(manager, k -> new ArrayList<>())
                            .add(emp);
                        System.out.println("  " + emp.getNomComplet() + " → Manager: " + manager);
                    }
                }
                
                // Identifier les managers (ceux qui ont des subordonnés)
                Map<String, VueEmployeManagerComplet> infosManagers = new HashMap<>();
                
                for (String nomManager : subordonnesParManager.keySet()) {
                    Optional<VueEmployeManagerComplet> infoManager = employesDuDepartement.stream()
                        .filter(emp -> nomManager.equals(emp.getNomComplet()))
                        .findFirst();
                    
                    if (infoManager.isPresent()) {
                        infosManagers.put(nomManager, infoManager.get());
                        System.out.println("  Manager " + nomManager + " trouvé dans les données");
                    } else {
                        System.out.println("  Manager " + nomManager + " NON trouvé (externe)");
                    }
                }
                
                // Créer les DTO managers
                List<ManagerHierarchiqueDTO> managers = new ArrayList<>();
                
                // 1. Managers qui ont des subordonnés
                for (Map.Entry<String, List<VueEmployeManagerComplet>> entryManager : subordonnesParManager.entrySet()) {
                    String nomManager = entryManager.getKey();
                    List<VueEmployeManagerComplet> subordonnes = entryManager.getValue();
                    VueEmployeManagerComplet infoManager = infosManagers.get(nomManager);
                    
                    // Convertir les subordonnés en format compact
                    List<EmployeCompactDTO> subordonnesCompacts = subordonnes.stream()
                        // Exclure le manager lui-meme si present dans sa propre liste
                        .filter(emp -> {
                            if (infoManager != null) {
                                if (infoManager.getIdEmploye() != null && infoManager.getIdEmploye().equals(emp.getIdEmploye())) {
                                    return false;
                                }
                                if (infoManager.getEmployeMatricule() != null && infoManager.getEmployeMatricule().equals(emp.getEmployeMatricule())) {
                                    return false;
                                }
                                if (infoManager.getNomComplet() != null && infoManager.getNomComplet().equals(emp.getNomComplet())) {
                                    return false;
                                }
                            } else {
                                if (nomManager != null && nomManager.equals(emp.getNomComplet())) {
                                    return false;
                                }
                            }
                            return true;
                        })
                        .map(this::convertirEnEmployeCompactDTO)
                        .collect(Collectors.toList());
                    
                    ManagerHierarchiqueDTO managerDTO;
                    
                    if (infoManager != null) {
                        // Manager trouvé dans les données
                        managerDTO = ManagerHierarchiqueDTO.createWithSubordonnesCompacts(
                            infoManager.getNomComplet(),
                            infoManager.getEmployeMatricule(),
                            infoManager.getNomPoste(),
                            infoManager.getNomNiveau(),
                            infoManager.getRang(),
                            subordonnesCompacts
                        );
                    } else {
                        // Manager non trouvé (externe) - utiliser les infos des subordonnés
                        VueEmployeManagerComplet premierSubordonne = subordonnes.get(0);
                        managerDTO = ManagerHierarchiqueDTO.createWithSubordonnesCompacts(
                            nomManager,
                            null, // Pas de matricule
                            premierSubordonne.getNomPosteManager(),
                            premierSubordonne.getNomNiveauManager(),
                            premierSubordonne.getRangPosteManager(),
                            subordonnesCompacts
                        );
                    }
                    
                    managers.add(managerDTO);
                    System.out.println("  Créé manager: " + nomManager + " avec " + subordonnes.size() + " subordonnés");
                }
                // 3. Ajouter le manager du departement meme si aucun employe ne le reference
                Map<String, VueEmployeManagerComplet> employeParId = employesDuDepartement.stream()
                    .filter(emp -> emp.getIdEmploye() != null)
                    .collect(Collectors.toMap(
                        VueEmployeManagerComplet::getIdEmploye,
                        emp -> emp,
                        (a, b) -> a
                    ));

                Set<String> nomsManagers = managers.stream()
                    .map(ManagerHierarchiqueDTO::getNomComplet)
                    .collect(Collectors.toSet());

                Set<String> matriculesManagers = managers.stream()
                    .map(ManagerHierarchiqueDTO::getMatricule)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

                for (Manager managerActif : managersActifs) {
                    if (managerActif.getDepartement() == null || managerActif.getDepartement().getNom() == null) {
                        continue;
                    }
                    if (!managerActif.getDepartement().getNom().equalsIgnoreCase(nomDepartement)) {
                        continue;
                    }
                    if (managerActif.getEmploye() == null || managerActif.getEmploye().getId() == null) {
                        continue;
                    }

                    VueEmployeManagerComplet infoManager = employeParId.get(managerActif.getEmploye().getId());
                    if (infoManager == null) {
                        // Le manager n'existe pas dans la vue -> on ne l'ajoute pas
                        continue;
                    }

                    String nomCompletManager = infoManager.getNomComplet();
                    String matriculeManager = infoManager.getEmployeMatricule();

                    if (nomsManagers.contains(nomCompletManager)) {
                        continue;
                    }
                    if (matriculeManager != null && matriculesManagers.contains(matriculeManager)) {
                        continue;
                    }

                    ManagerHierarchiqueDTO managerSansSub = ManagerHierarchiqueDTO.createWithSubordonnesCompacts(
                        nomCompletManager,
                        matriculeManager,
                        infoManager.getNomPoste(),
                        infoManager.getNomNiveau(),
                        infoManager.getRang(),
                        new ArrayList<>()
                    );

                    managers.add(managerSansSub);
                    nomsManagers.add(nomCompletManager);
                    if (matriculeManager != null) {
                        matriculesManagers.add(matriculeManager);
                    }
                }

                // Trier les managers par rang (décroissant) puis par nombre de subordonnés
                managers.sort((m1, m2) -> {
                    // D'abord par rang
                    Integer rang1 = m1.getRang() != null ? m1.getRang() : 0;
                    Integer rang2 = m2.getRang() != null ? m2.getRang() : 0;
                    int compareRang = rang2.compareTo(rang1);
                    if (compareRang != 0) return compareRang;
                    
                    // Ensuite par nombre de subordonnés
                    int sub1 = m1.getNombreSubordonnes() != null ? m1.getNombreSubordonnes() : 0;
                    int sub2 = m2.getNombreSubordonnes() != null ? m2.getNombreSubordonnes() : 0;
                    return Integer.compare(sub2, sub1);
                });
                
                hierarchie.add(new DepartementHierarchiqueDTO(nomDepartement, managers));
            }
            
            // Trier les départements par nom
            hierarchie.sort(Comparator.comparing(DepartementHierarchiqueDTO::getNomDepartement));
            
            // Calculer les statistiques
            Map<String, Object> statistiques = new HashMap<>();
            statistiques.put("totalDepartements", hierarchie.size());
            statistiques.put("totalManagers", hierarchie.stream()
                .mapToInt(dep -> dep.getManagers().size()).sum());
            statistiques.put("totalEmployes", hierarchie.stream()
                .flatMap(dep -> dep.getManagers().stream())
                .mapToInt(m -> m.getNombreSubordonnes() != null ? m.getNombreSubordonnes() : 0)
                .sum());
            
            response.put("status", "success");
            response.put("timestamp", new Date());
            response.put("hierarchie", hierarchie);
            response.put("statistiques", statistiques);
            
            // DEBUG final
            System.out.println("=== DEBUG FINAL ===");
            System.out.println("Départements: " + hierarchie.size());
            hierarchie.forEach(dep -> {
                System.out.println("  " + dep.getNomDepartement() + ": " + 
                    dep.getManagers().size() + " managers");
                dep.getManagers().forEach(m -> {
                    System.out.println("    - " + m.getNomComplet() + " (" + 
                        m.getNombreSubordonnes() + " subordonnés)");
                });
            });
            
        } catch (Exception e) {
            System.err.println("=== ERREUR dans getOrganigrammeCompact ===");
            e.printStackTrace();
            
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("hierarchie", new ArrayList<>());
            response.put("statistiques", Map.of(
                "totalDepartements", 0,
                "totalManagers", 0,
                "totalEmployes", 0
            ));
        }
        
        return response;
    }

    public Map<String, Object> getOrganigrammeCompactByDepartement(String idEmploye) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String nomDepartement = infosProfessionnellesService.findLastByEmployeId(idEmploye).getDepartement().getNom();
            List<VueEmployeManagerComplet> toutesDonnees = repository.findAll();
            List<Manager> managersActifs = managerRepository.findByStatut(Manager.Statut.ACTIF);
            
            // DEBUG
            System.out.println("=== DEBUG: Données récupérées ===");
            toutesDonnees.forEach(emp -> {
                System.out.println("Employé: " + emp.getNomComplet() + 
                                ", Manager: " + emp.getNomCompletManager() +
                                ", Département: " + emp.getNomDepartement());
            });
            
            Map<String, List<VueEmployeManagerComplet>> parDepartement = new HashMap<>();
            
            for (VueEmployeManagerComplet emp : toutesDonnees) {
                String departement = emp.getNomDepartement() != null ? 
                    emp.getNomDepartement().trim() : "Non assigné";
                
                parDepartement.computeIfAbsent(departement, k -> new ArrayList<>())
                    .add(emp);
            }
            List<DepartementHierarchiqueDTO> hierarchie = new ArrayList<>();
            
            for (Map.Entry<String, List<VueEmployeManagerComplet>> entry : parDepartement.entrySet()) {
                String nomDepartemantCourant = entry.getKey();
                List<VueEmployeManagerComplet> employesDuDepartement = entry.getValue();
                
                // Filtrer par département demandé
                if (!nomDepartemantCourant.equals(nomDepartement)) {
                    continue;
                }
                
                System.out.println("=== Département: " + nomDepartemantCourant + " ===");
                
                // Grouper les employés par manager
                Map<String, List<VueEmployeManagerComplet>> subordonnesParManager = new HashMap<>();
                
                for (VueEmployeManagerComplet emp : employesDuDepartement) {
                    String manager = emp.getNomCompletManager();
                    if (manager != null && !manager.trim().isEmpty()) {
                        subordonnesParManager.computeIfAbsent(manager, k -> new ArrayList<>())
                            .add(emp);
                        System.out.println("  " + emp.getNomComplet() + " → Manager: " + manager);
                    }
                }
                
                // Identifier les managers (ceux qui ont des subordonnés)
                Map<String, VueEmployeManagerComplet> infosManagers = new HashMap<>();
                
                for (String nomManager : subordonnesParManager.keySet()) {
                    Optional<VueEmployeManagerComplet> infoManager = employesDuDepartement.stream()
                        .filter(emp -> nomManager.equals(emp.getNomComplet()))
                        .findFirst();
                    
                    if (infoManager.isPresent()) {
                        infosManagers.put(nomManager, infoManager.get());
                        System.out.println("  Manager " + nomManager + " trouvé dans les données");
                    } else {
                        System.out.println("  Manager " + nomManager + " NON trouvé (externe)");
                    }
                }
                
                // Créer les DTO managers
                List<ManagerHierarchiqueDTO> managers = new ArrayList<>();
                
                // 1. Managers qui ont des subordonnés
                for (Map.Entry<String, List<VueEmployeManagerComplet>> entryManager : subordonnesParManager.entrySet()) {
                    String nomManager = entryManager.getKey();
                    List<VueEmployeManagerComplet> subordonnes = entryManager.getValue();
                    VueEmployeManagerComplet infoManager = infosManagers.get(nomManager);
                    
                    // Convertir les subordonnés en format compact
                    List<EmployeCompactDTO> subordonnesCompacts = subordonnes.stream()
                        // Exclure le manager lui-meme si present dans sa propre liste
                        .filter(emp -> {
                            if (infoManager != null) {
                                if (infoManager.getIdEmploye() != null && infoManager.getIdEmploye().equals(emp.getIdEmploye())) {
                                    return false;
                                }
                                if (infoManager.getEmployeMatricule() != null && infoManager.getEmployeMatricule().equals(emp.getEmployeMatricule())) {
                                    return false;
                                }
                                if (infoManager.getNomComplet() != null && infoManager.getNomComplet().equals(emp.getNomComplet())) {
                                    return false;
                                }
                            } else {
                                if (nomManager != null && nomManager.equals(emp.getNomComplet())) {
                                    return false;
                                }
                            }
                            return true;
                        })
                        .map(this::convertirEnEmployeCompactDTO)
                        .collect(Collectors.toList());
                    
                    ManagerHierarchiqueDTO managerDTO;
                    
                    if (infoManager != null) {
                        // Manager trouvé dans les données
                        managerDTO = ManagerHierarchiqueDTO.createWithSubordonnesCompacts(
                            infoManager.getNomComplet(),
                            infoManager.getEmployeMatricule(),
                            infoManager.getNomPoste(),
                            infoManager.getNomNiveau(),
                            infoManager.getRang(),
                            subordonnesCompacts
                        );
                    } else {
                        // Manager non trouvé (externe) - utiliser les infos des subordonnés
                        VueEmployeManagerComplet premierSubordonne = subordonnes.get(0);
                        managerDTO = ManagerHierarchiqueDTO.createWithSubordonnesCompacts(
                            nomManager,
                            null, // Pas de matricule
                            premierSubordonne.getNomPosteManager(),
                            premierSubordonne.getNomNiveauManager(),
                            premierSubordonne.getRangPosteManager(),
                            subordonnesCompacts
                        );
                    }
                    
                    managers.add(managerDTO);
                    System.out.println("  Créé manager: " + nomManager + " avec " + subordonnes.size() + " subordonnés");
                }
                // 3. Ajouter le manager du departement meme si aucun employe ne le reference
                Map<String, VueEmployeManagerComplet> employeParId = employesDuDepartement.stream()
                    .filter(emp -> emp.getIdEmploye() != null)
                    .collect(Collectors.toMap(
                        VueEmployeManagerComplet::getIdEmploye,
                        emp -> emp,
                        (a, b) -> a
                    ));

                Set<String> nomsManagers = managers.stream()
                    .map(ManagerHierarchiqueDTO::getNomComplet)
                    .collect(Collectors.toSet());

                Set<String> matriculesManagers = managers.stream()
                    .map(ManagerHierarchiqueDTO::getMatricule)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

                for (Manager managerActif : managersActifs) {
                    if (managerActif.getDepartement() == null || managerActif.getDepartement().getNom() == null) {
                        continue;
                    }
                    if (!managerActif.getDepartement().getNom().equalsIgnoreCase(nomDepartemantCourant)) {
                        continue;
                    }
                    if (managerActif.getEmploye() == null || managerActif.getEmploye().getId() == null) {
                        continue;
                    }

                    VueEmployeManagerComplet infoManager = employeParId.get(managerActif.getEmploye().getId());
                    if (infoManager == null) {
                        // Le manager n'existe pas dans la vue -> on ne l'ajoute pas
                        continue;
                    }

                    String nomCompletManager = infoManager.getNomComplet();
                    String matriculeManager = infoManager.getEmployeMatricule();

                    if (nomsManagers.contains(nomCompletManager)) {
                        continue;
                    }
                    if (matriculeManager != null && matriculesManagers.contains(matriculeManager)) {
                        continue;
                    }

                    ManagerHierarchiqueDTO managerSansSub = ManagerHierarchiqueDTO.createWithSubordonnesCompacts(
                        nomCompletManager,
                        matriculeManager,
                        infoManager.getNomPoste(),
                        infoManager.getNomNiveau(),
                        infoManager.getRang(),
                        new ArrayList<>()
                    );

                    managers.add(managerSansSub);
                    nomsManagers.add(nomCompletManager);
                    if (matriculeManager != null) {
                        matriculesManagers.add(matriculeManager);
                    }
                }

                // Trier les managers par rang (décroissant) puis par nombre de subordonnés
                managers.sort((m1, m2) -> {
                    // D'abord par rang
                    Integer rang1 = m1.getRang() != null ? m1.getRang() : 0;
                    Integer rang2 = m2.getRang() != null ? m2.getRang() : 0;
                    int compareRang = rang2.compareTo(rang1);
                    if (compareRang != 0) return compareRang;
                    
                    // Ensuite par nombre de subordonnés
                    int sub1 = m1.getNombreSubordonnes() != null ? m1.getNombreSubordonnes() : 0;
                    int sub2 = m2.getNombreSubordonnes() != null ? m2.getNombreSubordonnes() : 0;
                    return Integer.compare(sub2, sub1);
                });
                
                hierarchie.add(new DepartementHierarchiqueDTO(nomDepartemantCourant, managers));
            }
            
            // Calculer les statistiques
            Map<String, Object> statistiques = new HashMap<>();
            statistiques.put("totalDepartements", hierarchie.size());
            statistiques.put("totalManagers", hierarchie.stream()
                .mapToInt(dep -> dep.getManagers().size()).sum());
            statistiques.put("totalEmployes", hierarchie.stream()
                .flatMap(dep -> dep.getManagers().stream())
                .mapToInt(m -> m.getNombreSubordonnes() != null ? m.getNombreSubordonnes() : 0)
                .sum());
            
            response.put("status", "success");
            response.put("timestamp", new Date());
            response.put("hierarchie", hierarchie);
            response.put("statistiques", statistiques);
            
            // DEBUG final
            System.out.println("=== DEBUG FINAL ===");
            System.out.println("Départements: " + hierarchie.size());
            hierarchie.forEach(dep -> {
                System.out.println("  " + dep.getNomDepartement() + ": " + 
                    dep.getManagers().size() + " managers");
                dep.getManagers().forEach(m -> {
                    System.out.println("    - " + m.getNomComplet() + " (" + 
                        m.getNombreSubordonnes() + " subordonnés)");
                });
            });
            
        } catch (Exception e) {
            System.err.println("=== ERREUR dans getOrganigrammeCompactByDepartement ===");
            e.printStackTrace();
            
            response.put("status", "error");
            response.put("message", e.getMessage());
            response.put("hierarchie", new ArrayList<>());
            response.put("statistiques", Map.of(
                "totalDepartements", 0,
                "totalManagers", 0,
                "totalEmployes", 0
            ));
        }
        
        return response;
    }

    public Map<String, Object> getOrganigrammeForManager(String idEmploye) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 1. Récupérer les infos du manager connecté
            InfosProfessionnelles infosPro = infosProfessionnellesService.findLastByEmployeId(idEmploye);
            
            if (infosPro == null) {
                response.put("status", "error");
                response.put("message", "Informations professionnelles non trouvées");
                return response;
            }
            
            String nomDepartement = infosPro.getDepartement().getNom();
            
            // 2. Récupérer TOUS les employés du département
            List<VueEmployeManagerComplet> employesDuDepartement = repository.findByNomDepartement(nomDepartement);
            
            // 3. Le manager est l'utilisateur connecté
            // On crée un objet manager à partir de ses infos
            ManagerHierarchiqueDTO managerDTO = new ManagerHierarchiqueDTO();
            managerDTO.setNomComplet(infosPro.getEmploye().getNom() + " " + infosPro.getEmploye().getPrenom());
            managerDTO.setMatricule(infosPro.getMatricule());
            managerDTO.setNomPoste(infosPro.getPoste().getNom());
            managerDTO.setNomNiveau(infosPro.getPoste().getNiveauHierarchique().getNom());
            managerDTO.setRang(infosPro.getPoste().getNiveauHierarchique().getRang());
            
            // 4. Les subordonnés = tous les employés du département (sauf le manager lui-même)
            List<EmployeCompactDTO> subordonnes = employesDuDepartement.stream()
                .filter(emp -> !emp.getEmployeMatricule().equals(infosPro.getMatricule()))
                .map(this::convertirEnEmployeCompactDTO)
                .collect(Collectors.toList());
            
            managerDTO.setSubordonnesCompacts(subordonnes);
            managerDTO.setNombreSubordonnes(subordonnes.size());
            
            // 5. Construire la réponse
            response.put("status", "success");
            response.put("timestamp", new Date());
            response.put("manager", managerDTO);
            response.put("departement", nomDepartement);
            
            Map<String, Object> statistiques = new HashMap<>();
            statistiques.put("totalEmployes", subordonnes.size());
            statistiques.put("totalManagers", 1);
            response.put("statistiques", statistiques);
            
            // Debug
            System.out.println("=== Vue Manager (simplifiée) ===");
            System.out.println("Manager: " + managerDTO.getNomComplet());
            System.out.println("Département: " + nomDepartement);
            System.out.println("Nombre de subordonnés: " + subordonnes.size());
            
        } catch (Exception e) {
            System.err.println("=== ERREUR ===");
            e.printStackTrace();
            
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        
        return response;
    }

    private EmployeCompactDTO convertirEnEmployeCompactDTO(VueEmployeManagerComplet employe) {
        if (employe == null) {
            return new EmployeCompactDTO(null, "Inconnu", "?", "Poste inconnu", "Niveau inconnu", 0, null);
        }
        
        // Formater le nom abrégé : "J. Tsilavinay"
        String nomAbrege = "";
        
        if (employe.getEmployePrenom() != null && !employe.getEmployePrenom().trim().isEmpty()) {
            nomAbrege = employe.getEmployePrenom().trim().charAt(0) + ". ";
        }
        
        if (employe.getEmployeNom() != null && !employe.getEmployeNom().trim().isEmpty()) {
            nomAbrege += employe.getEmployeNom().trim();
        } else if (employe.getNomComplet() != null) {
            // Fallback: prendre le début du nom complet
            String[] parties = employe.getNomComplet().split(" ");
            if (parties.length > 0) {
                nomAbrege = parties[0].charAt(0) + ". ";
                if (parties.length > 1) {
                    nomAbrege += parties[1];
                }
            } else {
                nomAbrege = employe.getNomComplet();
            }
        }
        
        return new EmployeCompactDTO(
            employe.getEmployeMatricule(),
            employe.getNomComplet(),
            nomAbrege,
            employe.getNomPoste() != null ? employe.getNomPoste() : "Poste non défini",
            employe.getNomNiveau() != null ? employe.getNomNiveau() : "Niveau non défini",
            employe.getRang() != null ? employe.getRang() : 0,
            employe.getNomDepartement()
        );
    }  
} 
    // public Map<String, Object> getOrganigrammeComplet() {
    //     Map<String, Object> response = new HashMap<>();
        
    //     try {
    //         // 1. Récupérer toutes les données
    //         List<VueEmployeManagerComplet> toutesDonnees = repository.findAll();
            
    //         // 2. Identifier tous les managers uniques
    //         Set<String> nomsManagers = toutesDonnees.stream()
    //             .map(VueEmployeManagerComplet::getNomCompletManager)
    //             .filter(Objects::nonNull)
    //             .filter(manager -> !manager.isEmpty())
    //             .collect(Collectors.toSet());
            
    //         // 3. Créer une map pour regrouper les subordonnés par manager
    //         Map<String, List<VueEmployeManagerComplet>> subordonnesParManager = new HashMap<>();
            
    //         for (VueEmployeManagerComplet emp : toutesDonnees) {
    //             String manager = emp.getNomCompletManager();
    //             if (manager != null && !manager.isEmpty()) {
    //                 subordonnesParManager.computeIfAbsent(manager, k -> new ArrayList<>()).add(emp);
    //             }
    //         }
            
    //         // 4. Identifier les employés qui sont managers
    //         Map<String, VueEmployeManagerComplet> managersInfo = new HashMap<>();
    //         for (VueEmployeManagerComplet emp : toutesDonnees) {
    //             if (nomsManagers.contains(emp.getNomComplet())) {
    //                 managersInfo.put(emp.getNomComplet(), emp);
    //             }
    //         }
            
    //         // 5. Grouper par département
    //         Map<String, List<ManagerHierarchiqueDTO>> managersParDepartement = new HashMap<>();
            
    //         // 5a. D'abord, traiter les managers qui ont des subordonnés
    //         for (String managerNom : nomsManagers) {
    //             List<VueEmployeManagerComplet> subordonnes = subordonnesParManager.get(managerNom);
    //             if (subordonnes != null && !subordonnes.isEmpty()) {
    //                 // Prendre le département du premier subordonné
    //                 String departement = subordonnes.get(0).getNomDepartement();
                    
    //                 // Récupérer les infos du manager
    //                 VueEmployeManagerComplet infoManager = managersInfo.get(managerNom);
                    
    //                 ManagerHierarchiqueDTO managerDTO;
    //                 if (infoManager != null) {
    //                     // Manager présent dans les données
    //                     managerDTO = new ManagerHierarchiqueDTO(
    //                         infoManager.getNomComplet(),
    //                         infoManager.getEmployeMatricule(),
    //                         infoManager.getNomPoste(),
    //                         infoManager.getNomNiveau(),
    //                         infoManager.getRang(),
    //                         subordonnes.stream()
    //                             .map(this::convertirEnEmployeDTO)
    //                             .collect(Collectors.toList())
    //                     );
    //                 } else {
    //                     // Manager non présent dans les données (cas de Rakoto Lala)
    //                     managerDTO = new ManagerHierarchiqueDTO(
    //                         managerNom,
    //                         null, // Pas de matricule
    //                         subordonnes.get(0).getNomPosteManager(),
    //                         subordonnes.get(0).getNomNiveauManager(),
    //                         subordonnes.get(0).getRangPosteManager(),
    //                         subordonnes.stream()
    //                             .map(this::convertirEnEmployeDTO)
    //                             .collect(Collectors.toList())
    //                     );
    //                 }
                    
    //                 managersParDepartement.computeIfAbsent(departement, k -> new ArrayList<>())
    //                     .add(managerDTO);
    //             }
    //         }
            
    //         // 5b. Ensuite, ajouter les employés sans manager (isolés)
