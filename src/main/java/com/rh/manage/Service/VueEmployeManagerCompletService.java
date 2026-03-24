package com.rh.manage.Service;

import com.rh.manage.Dto.*;
import com.rh.manage.View.VueEmployeManagerComplet;
import com.rh.manage.Repository.VueEmployeManagerCompletRepository;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VueEmployeManagerCompletService {
    
    private final VueEmployeManagerCompletRepository repository;
    
    public VueEmployeManagerCompletService(VueEmployeManagerCompletRepository repository) {
        this.repository = repository;
    }

    public Map<String, Object> getOrganigrammeCompact() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<VueEmployeManagerComplet> toutesDonnees = repository.findAll();
            
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
                
                // 2. Ajouter les employés sans manager (managers isolés ou employés seuls)
                List<VueEmployeManagerComplet> employesSansManager = employesDuDepartement.stream()
                    .filter(emp -> emp.getNomCompletManager() == null || emp.getNomCompletManager().trim().isEmpty())
                    .collect(Collectors.toList());
                
                for (VueEmployeManagerComplet emp : employesSansManager) {
                    // Vérifier si c'est déjà un manager (pour éviter les doublons)
                    boolean dejaManager = managers.stream()
                        .anyMatch(m -> m.getNomComplet().equals(emp.getNomComplet()));
                    
                    if (!dejaManager) {
                        ManagerHierarchiqueDTO managerIsole = ManagerHierarchiqueDTO.createWithSubordonnes(
                            emp.getNomComplet(),
                            emp.getEmployeMatricule(),
                            emp.getNomPoste(),
                            emp.getNomNiveau(),
                            emp.getRang(),
                            new ArrayList<>() // Pas de subordonnés
                        );
                        managers.add(managerIsole);
                        System.out.println("  Ajouté employé isolé: " + emp.getNomComplet());
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
    //         List<VueEmployeManagerComplet> employesSansManager = toutesDonnees.stream()
    //             .filter(emp -> emp.getNomCompletManager() == null || emp.getNomCompletManager().isEmpty())
    //             .collect(Collectors.toList());
            
    //         for (VueEmployeManagerComplet emp : employesSansManager) {
    //             String departement = emp.getNomDepartement();
    //             ManagerHierarchiqueDTO managerIsole = new ManagerHierarchiqueDTO(
    //                 emp.getNomComplet(),
    //                 emp.getEmployeMatricule(),
    //                 emp.getNomPoste(),
    //                 emp.getNomNiveau(),
    //                 emp.getRang(),
    //                 new ArrayList<>() // Pas de subordonnés
    //             );
                
    //             managersParDepartement.computeIfAbsent(departement, k -> new ArrayList<>())
    //                 .add(managerIsole);
    //         }
            
    //         // 6. Convertir en structure DTO par département
    //         List<DepartementHierarchiqueDTO> hierarchie = managersParDepartement.entrySet().stream()
    //             .map(entry -> {
    //                 List<ManagerHierarchiqueDTO> managers = entry.getValue();
    //                 // Trier les managers par rang (décroissant)
    //                 managers.sort((m1, m2) -> {
    //                     Integer rang1 = m1.getRang() != null ? m1.getRang() : 0;
    //                     Integer rang2 = m2.getRang() != null ? m2.getRang() : 0;
    //                     return rang2.compareTo(rang1);
    //                 });
                    
    //                 return new DepartementHierarchiqueDTO(entry.getKey(), managers);
    //             })
    //             .sorted(Comparator.comparing(DepartementHierarchiqueDTO::getNomDepartement))
    //             .collect(Collectors.toList());
            
    //         // 7. Calculer les statistiques
    //         Map<String, Object> statistiques = new HashMap<>();
            
    //         int totalManagers = hierarchie.stream()
    //             .mapToInt(dep -> dep.getManagers().size())
    //             .sum();
    //         int totalEmployes = hierarchie.stream()
    //             .flatMap(dep -> dep.getManagers().stream())
    //             .mapToInt(manager -> manager.getSubordonnes().size())
    //             .sum();
            
    //         // Calculer les écarts hiérarchiques
    //         List<Integer> ecarts = hierarchie.stream()
    //             .flatMap(dep -> dep.getManagers().stream())
    //             .flatMap(manager -> manager.getSubordonnes().stream())
    //             .map(EmployeHierarchiqueDTO::getEcartHierarchique)
    //             .filter(Objects::nonNull)
    //             .collect(Collectors.toList());
            
    //         long grandsEcarts = ecarts.stream().filter(ecart -> ecart >= 2).count();
            
    //         statistiques.put("totalDepartements", hierarchie.size());
    //         statistiques.put("totalManagers", totalManagers);
    //         statistiques.put("totalEmployes", totalEmployes);
    //         statistiques.put("totalPersonnel", totalManagers + totalEmployes);
    //         statistiques.put("grandsEcarts", grandsEcarts);
            
    //         // Statistiques par département
    //         List<Map<String, Object>> statsParDepartement = hierarchie.stream()
    //             .map(dep -> {
    //                 Map<String, Object> stats = new HashMap<>();
    //                 stats.put("nom", dep.getNomDepartement());
    //                 stats.put("managers", dep.getManagers().size());
    //                 int employesDep = dep.getManagers().stream()
    //                     .mapToInt(manager -> manager.getSubordonnes().size())
    //                     .sum();
    //                 stats.put("employes", employesDep);
    //                 stats.put("total", dep.getManagers().size() + employesDep);
    //                 return stats;
    //             })
    //             .collect(Collectors.toList());
            
    //         statistiques.put("parDepartement", statsParDepartement);
            
    //         // 8. Construire la réponse finale
    //         response.put("status", "success");
    //         response.put("timestamp", System.currentTimeMillis());
    //         response.put("hierarchie", hierarchie);
    //         response.put("statistiques", statistiques);
            
    //     } catch (Exception e) {
    //         response.put("status", "error");
    //         response.put("message", "Erreur: " + e.getMessage());
    //         response.put("hierarchie", new ArrayList<>());
    //         response.put("statistiques", new HashMap<>());
    //     }
        
    //     return response;
    // }
    
    private EmployeHierarchiqueDTO convertirEnEmployeDTO(VueEmployeManagerComplet employe) {
        Integer ecart = null;
        if (employe.getRang() != null && employe.getRangPosteManager() != null) {
            ecart = employe.getRangPosteManager() - employe.getRang();
        }
        
        return new EmployeHierarchiqueDTO(
            employe.getEmployeMatricule(),
            employe.getNomComplet(),
            employe.getNomPoste(),
            employe.getNomNiveau(),
            employe.getRang(),
            employe.getNomDepartement(),
            employe.getNomCompletManager(),
            ecart
        );
    }
    
    // Méthode simple pour afficher les données brutes (debug)
    public List<Map<String, Object>> getDonneesBrutes() {
        return repository.findAll().stream()
            .map(emp -> {
                Map<String, Object> map = new HashMap<>();
                map.put("matricule", emp.getEmployeMatricule());
                map.put("nomComplet", emp.getNomComplet());
                map.put("poste", emp.getNomPoste());
                map.put("niveau", emp.getNomNiveau());
                map.put("rang", emp.getRang());
                map.put("departement", emp.getNomDepartement());
                map.put("manager", emp.getNomCompletManager());
                map.put("posteManager", emp.getNomPosteManager());
                map.put("niveauManager", emp.getNomNiveauManager());
                map.put("rangManager", emp.getRangPosteManager());
                return map;
            })
            .collect(Collectors.toList());
    }
}