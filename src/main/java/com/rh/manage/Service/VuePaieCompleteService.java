package com.rh.manage.Service;

import com.rh.manage.Model.VuePaieComplete;
import com.rh.manage.Repository.VuePaieCompleteRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.data.domain.PageRequest;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class VuePaieCompleteService {
    
    @Autowired
    private VuePaieCompleteRepository repository;
    
    // Récupérer toutes les données
    public List<VuePaieComplete> getAll() {
        return repository.findAll();
    }

    
    // Récupérer par ID employé
    public List<VuePaieComplete> getByIdEmploye(String idEmploye) {
        return repository.findByIdEmploye(idEmploye);
    }
    
    // Récupérer par matricule
    public List<VuePaieComplete> getByMatricule(String matricule) {
        return repository.findByMatricule(matricule);
    }
    
    // Récupérer par département
    public List<VuePaieComplete> getByDepartement(String departement) {
        return repository.findByDepartement(departement);
    }
    
    // Récupérer par statut de clôture
    public List<VuePaieComplete> getByStatutCloture(Integer statutCloture) {
        return repository.findByStatutCloture(statutCloture);
    }
    
    // Récupérer par période
    public List<VuePaieComplete> getByPeriode(LocalDate dateDebut, LocalDate dateFin) {
        return repository.findByDateDebutPeriodeBetween(dateDebut, dateFin);
    }
    
    // Récupérer par mois et année
    public List<VuePaieComplete> getByMoisAnnee(Integer mois, Integer annee) {
        return repository.findByMoisAndAnnee(mois, annee);
    }

    public VuePaieComplete getByIdPaie(String idPaie){
        return repository.findByPaieId(idPaie);
    }
    
    // Recherche avec critères multiples
    public List<VuePaieComplete> searchByCriteria(String departement, Integer statutCloture, 
                                                  String categorieSalaire, LocalDate dateDebut, 
                                                  LocalDate dateFin) {
        return repository.findByCriteria(departement, statutCloture, categorieSalaire, dateDebut, dateFin);
    }
    
    // Statistiques par département
    public Map<String, Object> getStatsDepartement() {
        List<Object[]> results = repository.getStatsByDepartement();
        Map<String, Object> stats = new HashMap<>();
        
        List<Map<String, Object>> departementsStats = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> deptStat = new HashMap<>();
            deptStat.put("departement", row[0]);
            deptStat.put("nombrePaies", row[1]);
            deptStat.put("masseSalarialeBrute", row[2]);
            deptStat.put("salaireMoyen", row[3]);
            departementsStats.add(deptStat);
        }
        
        stats.put("departements", departementsStats);
        return stats;
    }
    
    // Statistiques par mois/année
    public Map<String, Object> getStatsMoisAnnee() {
        List<Object[]> results = repository.getStatsByMoisAnnee();
        Map<String, Object> stats = new HashMap<>();
        
        List<Map<String, Object>> periodesStats = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> periodeStat = new HashMap<>();
            periodeStat.put("annee", row[0]);
            periodeStat.put("mois", row[1]);
            periodeStat.put("nombrePaies", row[2]);
            periodeStat.put("masseSalarialeBrute", row[3]);
            periodeStat.put("masseSalarialeNette", row[4]);
            periodesStats.add(periodeStat);
        }
        
        stats.put("periodes", periodesStats);
        return stats;
    }
    
    // Obtenir les périodes distinctes
    public List<LocalDate> getPeriodesDistinctes() {
        return repository.findDistinctPeriodes();
    }
    
    // Obtenir les départements distincts
    public List<String> getDepartementsDistincts() {
        return repository.findDistinctDepartements();
    }
    
    // Obtenir les catégories de salaire distinctes
    public List<String> getCategoriesSalaireDistinctes() {
        return repository.findDistinctCategoriesSalaire();
    }
    
    // Calculer les totaux globaux
    public Map<String, BigDecimal> getTotauxGlobaux(LocalDate dateDebut, LocalDate dateFin) {
        List<VuePaieComplete> paies = repository.findByDateDebutPeriodeBetween(dateDebut, dateFin);
        
        BigDecimal totalSalaireBrut = BigDecimal.ZERO;
        BigDecimal totalSalaireNet = BigDecimal.ZERO;
        BigDecimal totalRetenues = BigDecimal.ZERO;
        BigDecimal totalCotisations = BigDecimal.ZERO;
        
        for (VuePaieComplete paie : paies) {
            totalSalaireBrut = totalSalaireBrut.add(paie.getSalaireBrut() != null ? paie.getSalaireBrut() : BigDecimal.ZERO);
            totalSalaireNet = totalSalaireNet.add(paie.getSalaireNet() != null ? paie.getSalaireNet() : BigDecimal.ZERO);
            totalRetenues = totalRetenues.add(paie.getTotalRetenue() != null ? paie.getTotalRetenue() : BigDecimal.ZERO);
            totalCotisations = totalCotisations.add(paie.getTotalCotisations() != null ? paie.getTotalCotisations() : BigDecimal.ZERO);
        }
        
        Map<String, BigDecimal> totaux = new HashMap<>();
        totaux.put("totalSalaireBrut", totalSalaireBrut);
        totaux.put("totalSalaireNet", totalSalaireNet);
        totaux.put("totalRetenues", totalRetenues);
        totaux.put("totalCotisations", totalCotisations);
        
        return totaux;
    }
    
    // Recherche avancée avec pagination (méthode d'exemple)
    public List<VuePaieComplete> searchAvancee(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAll();
        }
        
        String term = searchTerm.toLowerCase().trim();
        List<VuePaieComplete> result = new ArrayList<>();
        
        // Recherche dans différents champs
        result.addAll(repository.findByNomContainingIgnoreCase(term));
        result.addAll(repository.findByPrenomContainingIgnoreCase(term));
        result.addAll(repository.findByNomCompletContainingIgnoreCase(term));
        result.addAll(repository.findByMatricule(term));
        result.addAll(repository.findByDepartement(term));
        result.addAll(repository.findByFonction(term));
        
        // Éliminer les doublons
        Set<String> ids = new HashSet<>();
        List<VuePaieComplete> uniqueResults = new ArrayList<>();
        for (VuePaieComplete paie : result) {
            if (ids.add(paie.getIdEmploye() + "_" + paie.getPaieId())) {
                uniqueResults.add(paie);
            }
        }
        
        return uniqueResults;
    }

    public List<VuePaieComplete> findByDepartement(String departement) {
        System.out.println("ato hely ee++++++");
        System.out.println("departement : " + departement);
        return repository.findByDepartementAndStatutCloture(departement, 0);
    } 

    public Map<String, Object> findByDepartementAndStatutClotureWithPagination(
        String departement,
        Integer statutCloture,
        String matricule,
        String search,
        String dateDebut,    // Changé de LocalDate à String
        String dateFin,      // Changé de LocalDate à String
        int page,
        int size,
        String sortBy,
        String sortDirection) {
    
        System.out.println("📥 Appel pagination - Département: " + departement + 
                        ", Statut: " + statutCloture + 
                        ", Search: '" + search + 
                        "', Date début: " + dateDebut +
                        ", Date fin: " + dateFin +
                        ", Page: " + page + 
                        ", Size: " + size + 
                        ", Sort: " + sortBy + " " + sortDirection);
        
        // 1. Validation des paramètres obligatoires
        if (departement == null || departement.trim().isEmpty()) {
            throw new IllegalArgumentException("Le département est requis");
        }
        
        // 2. Normalisation
        departement = departement.trim();
        matricule = normalizeString(matricule);
        search = normalizeString(search);
        
        // 3. Gestion spéciale pour statutCloture = 2 (tous)
        if (statutCloture != null && statutCloture == 2) {
            System.out.println("🔄 Conversion statutCloture=2 → null (tous les statuts)");
            statutCloture = null;
        }
        
        // 4. Validation des dates (en tant que strings)
        if (dateDebut != null && !dateDebut.isEmpty() && 
            dateFin != null && !dateFin.isEmpty()) {
            // Convertir en LocalDate pour validation
            LocalDate debut = LocalDate.parse(dateDebut);
            LocalDate fin = LocalDate.parse(dateFin);
            if (debut.isAfter(fin)) {
                throw new IllegalArgumentException("La date de début doit être antérieure à la date de fin");
            }
        }
        
        // 5. Validation pagination
        page = Math.max(0, page);
        size = Math.min(Math.max(size, 1), 100);
        
        // 6. Validation direction de tri
        if (!"asc".equalsIgnoreCase(sortDirection) && !"desc".equalsIgnoreCase(sortDirection)) {
            sortDirection = "asc";
            System.out.println("⚠️ Direction de tri invalide, utilisation de 'asc' par défaut");
        }
        
        // 7. Déterminer le champ de tri
        String sortField = determineSortField(sortBy);
        
        // 8. Créer le Sort et Pageable
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        PageRequest pageable = PageRequest.of(page, size, sort);
        
        // 9. LOG avant appel repository
        System.out.println("🔍 Appel repository avec: page=" + page + 
                        ", size=" + size + 
                        ", sort=" + sort + 
                        ", statutCloture=" + statutCloture +
                        ", dateDebut=" + dateDebut +
                        ", dateFin=" + dateFin);
        
        // 10. Appel repository AVEC les dates (en String)
        Page<VuePaieComplete> pageResult = repository
                .findByDepartementAndStatutClotureWithPagination(
                    departement, 
                    statutCloture,
                    matricule, 
                    search,
                    dateDebut,
                    dateFin,
                    pageable
                );
        
        // 11. LOG résultats
        System.out.println("📊 Résultats pagination - Total: " + pageResult.getTotalElements() + 
                        ", Page actuelle: " + pageResult.getNumber() + 
                        ", Nombre pages: " + pageResult.getTotalPages() + 
                        ", Nombre éléments: " + pageResult.getNumberOfElements());
        
        // 12. Calcul statistiques
        Map<String, Long> stats = calculateStats(departement, statutCloture, matricule, search, dateDebut, dateFin);
        
        // 13. Construction réponse
        return buildPaginationResponse(pageResult, stats, page, size, sortBy, sortDirection);
    }

//     public Map<String, Object> findByDepartementAndStatutClotureWithPagination(
//         String departement,
//         Integer statutCloture,
//         String matricule,
//         String search,
//         int page,
//         int size,
//         String sortBy,
//         String sortDirection) {
    
//     // LOG des paramètres d'entrée avec System.out.println
//     System.out.println("📥 Appel pagination - Département: " + departement + 
//                      ", Statut: " + statutCloture + 
//                      ", Search: '" + search + 
//                      "', Page: " + page + 
//                      ", Size: " + size + 
//                      ", Sort: " + sortBy + " " + sortDirection);
    
//     // 1. Validation des paramètres obligatoires
//     if (departement == null || departement.trim().isEmpty()) {
//         throw new IllegalArgumentException("Le département est requis");
//     }
    
//     // 2. Normalisation
//     departement = departement.trim();
//     matricule = normalizeString(matricule);
//     search = normalizeString(search);
    
//     // 3. CORRECTION IMPORTANTE : Si statutCloture = 2, mettre à null pour "tous"
//     if (statutCloture != null && statutCloture == 2) {
//         System.out.println("🔄 Conversion statutCloture=2 → null (tous les statuts)");
//         statutCloture = null;
//     }
    
//     // 4. Validation pagination
//     page = Math.max(0, page);
//     size = Math.min(Math.max(size, 1), 100);
    
//     // 5. Validation direction de tri
//     if (!"asc".equalsIgnoreCase(sortDirection) && !"desc".equalsIgnoreCase(sortDirection)) {
//         sortDirection = "asc";
//         System.out.println("⚠️ Direction de tri invalide: " + sortDirection + ", utilisation de 'asc' par défaut");
//     }
    
//     // 6. Déterminer le champ de tri avec sécurité
//     String sortField = determineSortField(sortBy);
    
//     // 7. Créer le Sort et Pageable
//     Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
//     PageRequest pageable = PageRequest.of(page, size, sort);
    
//     // 8. LOG avant appel repository
//     System.out.println("🔍 Appel repository avec: page=" + page + 
//                      ", size=" + size + 
//                      ", sort=" + sort + 
//                      ", statutCloture=" + statutCloture);
    
//     // 9. Appel repository
//     Page<VuePaieComplete> pageResult = repository
//             .findByDepartementAndStatutClotureWithPagination(
//                 departement, 
//                 statutCloture,  // ← Ici statutCloture peut être null pour "tous"
//                 matricule, 
//                 search, 
//                 pageable
//             );
    
//     // 10. LOG résultats
//     System.out.println("📊 Résultats pagination - Total: " + pageResult.getTotalElements() + 
//                      ", Page actuelle: " + pageResult.getNumber() + 
//                      ", Nombre pages: " + pageResult.getTotalPages() + 
//                      ", Nombre éléments: " + pageResult.getNumberOfElements());
    
//     // 11. Si aucun résultat, vérifier si le département existe
//     if (pageResult.isEmpty()) {
//         System.out.println("⚠️ Aucun résultat trouvé pour département: " + departement);
        
//         // Optionnel: vérifier si le département existe
//         // boolean departementExists = repository.existsByDepartement(departement);
//         // if (!departementExists) {
//         //     System.out.println("❌ Département non trouvé: " + departement);
//         //     throw new EntityNotFoundException("Département non trouvé: " + departement);
//         // }
//     }
    
//     // 12. Calcul statistiques
//     Map<String, Long> stats = calculateStats(departement, statutCloture, matricule, search);
    
//     // 13. Construction réponse
//     return buildPaginationResponse(pageResult, stats, page, size, sortBy, sortDirection);
// }

// Méthode utilitaire pour normalisation
private String normalizeString(String input) {
    if (input == null) return null;
    String trimmed = input.trim();
    return trimmed.isEmpty() ? null : trimmed;
}

// Ajoutez cette méthode pour debugger
// public void testFindData(String departement) {
//     System.out.println("🔍 Test données pour département: " + departement);
    
//     // Test 1: Vérifier si le département existe
//     long countByDepartement = repository.countByDepartement(departement);
//     System.out.println("✅ Nombre total d'entrées pour département " + departement + ": " + countByDepartement);
    
//     // Test 2: Vérifier par statut
//     long countCloture = repository.countByDepartementAndStatutCloture(departement, 0);
//     long countNonCloture = repository.countByDepartementAndStatutCloture(departement, 1);
//     System.out.println("📊 Statut clôturé (0): " + countCloture + ", Statut non clôturé (1): " + countNonCloture);
    
//     // Test 3: Prendre quelques exemples
//     List<VuePaieComplete> examples = repository.findTop5ByDepartement(departement);
//     if (!examples.isEmpty()) {
//         System.out.println("📋 Exemples de données:");
//         examples.forEach(example -> {
//             System.out.println("  - " + example.getMatricule() + 
//                              " | " + example.getNomComplet() +
//                              " | Statut: " + example.getStatutCloture() +
//                              " | Département: " + example.getDepartement());
//         });
//     } else {
//         System.out.println("⚠️ Aucune donnée trouvée même sans filtres!");
//     }
// }
    private String determineSortField(String sortBy) {
        // Liste des champs autorisés pour le tri
        String[] allowedSortFields = {
            "matricule", "nomComplet", "fonction", "salaireBrut", 
            "salaireNet", "dateDebutPeriode", "dateFinPeriode", "statutCloture"
        };
        
        // Vérifier si le champ demandé est autorisé
        for (String field : allowedSortFields) {
            if (field.equalsIgnoreCase(sortBy)) {
                return field;
            }
        }
        
        // Par défaut, trier par nom
        return "nomComplet";
    }

    private Map<String, Long> calculateStats(
        String departement,
        Integer statutCloture,
        String matricule,
        String search,
        String dateDebut,    // Changé de LocalDate à String
        String dateFin) {    // Changé de LocalDate à String
    
    Map<String, Long> stats = new HashMap<>();
    
        try {
            // Compter le total selon les filtres actuels
            long totalWithFilters = repository
                    .countByDepartementAndStatutClotureWithFilters(
                        departement, statutCloture, matricule, search, dateDebut, dateFin);
            
            // Compter par statut spécifique (0=inactif, 1=en attente, 2=généré)
            long inactifCount = repository
                    .countByDepartementAndStatutClotureWithFiltersAndStatut(
                        departement, statutCloture, 0, matricule, search, dateDebut, dateFin);
            
            long enAttenteCount = repository
                    .countByDepartementAndStatutClotureWithFiltersAndStatut(
                        departement, statutCloture, 1, matricule, search, dateDebut, dateFin);
            
            long actifCount = repository
                    .countByDepartementAndStatutClotureWithFiltersAndStatut(
                        departement, statutCloture, 2, matricule, search, dateDebut, dateFin);
            
            stats.put("total", totalWithFilters);
            stats.put("inactif", inactifCount);
            stats.put("enAttente", enAttenteCount);
            stats.put("actif", actifCount);
            
            System.out.println("📊 Statistiques calculées:");
            System.out.println("   - Total: " + totalWithFilters);
            System.out.println("   - Inactif (0): " + inactifCount);
            System.out.println("   - En attente (1): " + enAttenteCount);
            System.out.println("   - Généré (2): " + actifCount);
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors du calcul des statistiques: " + e.getMessage());
            e.printStackTrace();
            stats.put("total", 0L);
            stats.put("inactif", 0L);
            stats.put("enAttente", 0L);
            stats.put("actif", 0L);
        }
        
        return stats;
    }

    private Map<String, Object> buildPaginationResponse(
            Page<VuePaieComplete> pageResult,
            Map<String, Long> stats,
            int requestedPage,
            int requestedSize,
            String sortBy,
            String sortDirection) {
        
        Map<String, Object> response = new HashMap<>();
        
        // Données principales
        response.put("content", pageResult.getContent());
        
        // Métadonnées de pagination
        response.put("currentPage", pageResult.getNumber());
        response.put("pageSize", pageResult.getSize());
        response.put("totalElements", pageResult.getTotalElements());
        response.put("totalPages", pageResult.getTotalPages());
        response.put("hasNext", pageResult.hasNext());
        response.put("hasPrevious", pageResult.hasPrevious());
        response.put("first", pageResult.isFirst());
        response.put("last", pageResult.isLast());
        
        // Paramètres de la requête (pour référence)
        response.put("requestedPage", requestedPage);
        response.put("requestedSize", requestedSize);
        response.put("sortBy", sortBy);
        response.put("sortDirection", sortDirection);
        
        // Statistiques pour les filtres
        response.put("statistics", stats);
        
        // Informations sur la pagination
        response.put("paginationInfo", Map.of(
            "startIndex", pageResult.getNumber() * pageResult.getSize() + 1,
            "endIndex", Math.min(
                (pageResult.getNumber() + 1) * pageResult.getSize(), 
                pageResult.getTotalElements()
            ),
            "empty", pageResult.isEmpty()
        ));
        
        return response;
    }
}
