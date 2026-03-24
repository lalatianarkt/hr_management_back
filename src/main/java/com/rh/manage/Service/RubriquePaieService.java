package com.rh.manage.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rh.manage.Enum.ModeCalcul;
import com.rh.manage.Model.Abreviation;
import com.rh.manage.Model.CategorieRub;
import com.rh.manage.Model.Formule;
import com.rh.manage.Model.RubriquePaie;
import com.rh.manage.Model.RubriqueType;
import com.rh.manage.Repository.CategorieRubRepository;
import com.rh.manage.Repository.RubriquePaieRepository;
import com.rh.manage.Repository.RubriqueTypeRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
public class RubriquePaieService {
    
    @Autowired
    private RubriquePaieRepository rubriquePaieRepository;
    
    @Autowired
    private CategorieRubRepository categorieRubRepository;
    
    @Autowired
    private RubriqueTypeRepository rubriqueTypeRepository;

    @Autowired
    private AbreviationService abreviationService;

    @Autowired
    private FormuleService formuleService;

        // Créer une nouvelle rubrique
    @Transactional
    public RubriquePaie create(RubriquePaie rubrique) throws Exception {
        System.out.println("ato ++++++++++++++++++++ : " + rubrique.getCode());
        // Vérifier si l'ID existe déjà
        if (rubrique.getId() != null && rubriquePaieRepository.existsById(rubrique.getId())) {
            throw new RuntimeException("Une rubrique avec l'ID '" + rubrique.getId() + "' existe déjà");
        }
        
        // Vérifier si le code existe déjà
        if (rubriquePaieRepository.existsByCodeIgnoreCase(rubrique.getCode())) {
            throw new RuntimeException("Le code rubrique existe déjà: " + rubrique.getCode());
        }
        
        // Si ordre non défini, prendre le prochain disponible
        if (rubrique.getOrdre() == null) {
            rubrique.setOrdre(rubriquePaieRepository.findNextOrdre());
        }
        Formule formule = rubrique.getFormule();
        Formule formule_created = formuleService.createFormule(formule);
        rubrique.setFormule(formule_created);
        // Valider et charger les relations
        // validateAndLoadRelations(rubrique);
        RubriquePaie rubrique_inserted = rubriquePaieRepository.save(rubrique);
        Abreviation abrev = new Abreviation();
        abrev.setAbreviation(rubrique_inserted.getCode());
        abrev.setLibelle(rubrique_inserted.getLibelle());
        abreviationService.createAbreviation(abrev);
        return rubrique_inserted;
    }
    
    @Transactional
    // Mettre à jour une rubrique
    public RubriquePaie update(String id, RubriquePaie rubriqueDetails) {

        System.out.println("misy ve : " + rubriqueDetails.getModeCalcul());
        return rubriquePaieRepository.findById(id)
                .map(rubrique -> {
                    // Ne pas modifier le code (clé métier)
                    if (!rubrique.getCode().equalsIgnoreCase(rubriqueDetails.getCode())) {
                        // Vérifier si le nouveau code existe déjà
                        if (rubriquePaieRepository.existsByCodeIgnoreCase(rubriqueDetails.getCode())) {
                            throw new RuntimeException("Le code rubrique existe déjà: " + rubriqueDetails.getCode());
                        }
                        rubrique.setCode(rubriqueDetails.getCode());
                    }
                    
                    // Mettre à jour seulement si la valeur n'est pas null
                    if (rubriqueDetails.getLibelle() != null && !rubriqueDetails.getLibelle().trim().isEmpty()) {
                        rubrique.setLibelle(rubriqueDetails.getLibelle());
                    }
                    
                    if (rubriqueDetails.getPlafondMensuel() != null) {
                        rubrique.setPlafondMensuel(rubriqueDetails.getPlafondMensuel());
                    }
                    
                    // Gérer la formule
                    if (rubriqueDetails.getFormule() != null) {
                        // Si formule est envoyée, la traiter
                        Formule formuleDetails = rubriqueDetails.getFormule();
                        System.out.println("nombre : " + rubriqueDetails.getFormule().getNombre());
                        System.out.println("taux : " + rubriqueDetails.getFormule().getTaux());
                        System.out.println("base : " + rubriqueDetails.getFormule().getBase());
                        if (formuleDetails.getBase() == null && 
                            formuleDetails.getMontantFixe() == null && 
                            formuleDetails.getNombre() == null && 
                            formuleDetails.getTaux() == null) {
                            // Si tous les champs de formule sont null, supprimer la formule
                            if (rubrique.getFormule() != null) {
                                formuleService.deleteFormule(rubrique.getFormule().getId());
                                rubrique.setFormule(null);
                            }
                        } else {
                            // Créer ou mettre à jour la formule
                            if (rubrique.getFormule() != null) {
                                // Mettre à jour la formule existante
                                // Formule formuleExistante = rubrique.getFormule();
                                formuleService.updateFormule(formuleDetails, rubrique.getFormule().getId());
                                // formuleService.createFormule(formuleDetails);
                                // // Mettre à jour seulement les champs non null
                                // if (formuleDetails.getBase() != null) {
                                //     formuleExistante.setBase(formuleDetails.getBase());
                                // }
                                // if (formuleDetails.getMontantFixe() != null) {
                                //     formuleExistante.setMontantFixe(formuleDetails.getMontantFixe());
                                // }
                                // if (formuleDetails.getNombre() != null) {
                                //     formuleExistante.setNombre(formuleDetails.getNombre());
                                // }
                                // if (formuleDetails.getTaux() != null) {
                                //     formuleExistante.setTaux(formuleDetails.getTaux());
                                // }
                                
                                // formuleService.createFormule(formuleExistante);
                            } else {
                                // Créer une nouvelle formule
                                Formule nouvelleFormule = new Formule();
                                nouvelleFormule.setBase(formuleDetails.getBase());
                                nouvelleFormule.setMontantFixe(formuleDetails.getMontantFixe());
                                nouvelleFormule.setNombre(formuleDetails.getNombre());
                                nouvelleFormule.setTaux(formuleDetails.getTaux());
                                nouvelleFormule.setCreatedAt(LocalDateTime.now());
                                
                                Formule formuleSauvegardee = formuleService.createFormule(nouvelleFormule);
                                rubrique.setFormule(formuleSauvegardee);
                            }
                        }
                    } 
                    
                    // Boolean: toujours mettre à jour (peut être false)
                    if (rubriqueDetails.getEstImposable() != null) {
                        rubrique.setEstImposable(rubriqueDetails.getEstImposable());
                    }
                    
                    if (rubriqueDetails.getEstSoumisCotisations() != null) {
                        rubrique.setEstSoumisCotisations(rubriqueDetails.getEstSoumisCotisations());
                    }

                    if(rubriqueDetails.getEstDeductibleIrsa() != null){
                        rubrique.setEstDeductibleIrsa(rubriqueDetails.getEstDeductibleIrsa());
                    }
                    
                    // Compte comptable: peut être null (pas obligatoire)
                    rubrique.setCompteComptable(rubriqueDetails.getCompteComptable());
                    
                    // Ordre: toujours mettre à jour avec une valeur par défaut si null
                    if (rubriqueDetails.getOrdre() != null) {
                        rubrique.setOrdre(rubriqueDetails.getOrdre());
                    } else {
                        // Garder l'ordre existant ou mettre une valeur par défaut
                        rubrique.setOrdre(rubrique.getOrdre() != null ? rubrique.getOrdre() : 0);
                    }
                    
                    // Boolean: toujours mettre à jour
                    if (rubriqueDetails.getEstActif() != null) {
                        rubrique.setEstActif(rubriqueDetails.getEstActif());
                    }
                    
                    // Plafond annuel: peut être null
                    rubrique.setPlafondAnnuel(rubriqueDetails.getPlafondAnnuel());
                    
                    // Commentaire: peut être null ou vide
                    if (rubriqueDetails.getCommentaire() != null) {
                        rubrique.setCommentaire(rubriqueDetails.getCommentaire());
                    }

                    if (rubriqueDetails.getModeCalcul() != null) {
                        System.out.println("taifiditra ato eeeeeeeeeeeeee");
                        System.out.println("ModeCalcul reçu: " + rubriqueDetails.getModeCalcul());
                        System.out.println("Type: " + rubriqueDetails.getModeCalcul().getClass());
                        
                        // Utiliser .name() pour obtenir le nom de l'enum comme String
                        String modeCalculStr = rubriqueDetails.getModeCalcul().name();
                        
                        if ("AUTO".equals(modeCalculStr)) {
                            rubrique.setModeCalcul(ModeCalcul.AUTO);
                        } else if ("CALCULE".equals(modeCalculStr)) {
                            rubrique.setModeCalcul(ModeCalcul.CALCULE);
                        } else if ("MANUEL".equals(modeCalculStr)) {
                            rubrique.setModeCalcul(ModeCalcul.MANUEL);
                        }
                    }
                    System.out.println("mode calcul : " + rubrique.getModeCalcul());
                    
                    // Valider et charger les relations
                    if (rubriqueDetails.getCategorie() != null && rubriqueDetails.getCategorie().getId() != null) {
                        rubrique.setCategorie(rubriqueDetails.getCategorie());
                    }
                    
                    if (rubriqueDetails.getType() != null && rubriqueDetails.getType().getId() != null) {
                        rubrique.setType(rubriqueDetails.getType());
                    }
                                        
                    return rubriquePaieRepository.save(rubrique);
                })
                .orElseThrow(() -> new RuntimeException("Rubrique non trouvée avec id: " + id));
    }
    
    // Valider et charger les relations
    private void validateAndLoadRelations(RubriquePaie rubrique) {
        // Catégorie
        if (rubrique.getCategorie() != null && rubrique.getCategorie().getId() != null) {
            CategorieRub categorie = categorieRubRepository.findById(rubrique.getCategorie().getId())
                    .orElseThrow(() -> new RuntimeException("Catégorie non trouvée: " + rubrique.getCategorie().getId()));
            rubrique.setCategorie(categorie);
        }
        
        // Type
        if (rubrique.getType() != null && rubrique.getType().getId() != null) {
            RubriqueType type = rubriqueTypeRepository.findById(rubrique.getType().getId())
                    .orElseThrow(() -> new RuntimeException("Type de rubrique non trouvé: " + rubrique.getType().getId()));
            rubrique.setType(type);
        }
    }

    // Méthodes auxiliaires
    private boolean hasConsecutiveOperators(String formule) {
        // Supprimer les espaces pour faciliter la détection
        String withoutSpaces = formule.replaceAll("\\s+", "");
        
        // Vérifier les opérateurs consécutifs
        Pattern operatorPattern = Pattern.compile("[+\\-*/]{2,}");
        return operatorPattern.matcher(withoutSpaces).find();
    }

    public boolean validateFormuleCalcul(String formule) throws ValidationException {
    if (formule == null || formule.trim().isEmpty()) {
        return true;
    }
    
    String formuleClean = formule.trim();
    
    // 1. Vérifier caractères autorisés
    String caracteresNonAutorises = formuleClean.replaceAll("[a-zA-Z0-9_\\s+\\-*/().]", "");
    if (!caracteresNonAutorises.isEmpty()) {
        throw new ValidationException(
            "Caractères non autorisés: " + 
            Arrays.stream(caracteresNonAutorises.split(""))
                  .distinct()
                  .collect(Collectors.joining(", "))
        );
    }

    // 2. Vérifier la syntaxe de base (opérateurs consécutifs)
    if (hasConsecutiveOperators(formuleClean)) {
        throw new ValidationException("Syntaxe incorrecte: opérateurs consécutifs détectés");
    }
    
    // 2. Vérifier parenthèses équilibrées
    if (!areParenthesesBalanced(formuleClean)) {
        throw new ValidationException("Parenthèses non équilibrées");
    }
    
    // 3. Vérifier les parenthèses vides
    if (hasEmptyParentheses(formuleClean)) {
        throw new ValidationException("Parenthèses vides détectées: ()");
    }
    
    // 4. Vérifier opérateur suivi immédiatement de '(' sans opérande
    if (hasOperatorBeforeOpenParenthesis(formuleClean)) {
        throw new ValidationException("Opérateur suivi directement d'une parenthèse ouverte");
    }
    
    // 5. Extraire tous les identifiants (mots qui ne sont pas des nombres)
    List<String> identifiants = new ArrayList<>();
    Pattern pattern = Pattern.compile("[a-zA-Z_][a-zA-Z0-9_]*");
    Matcher matcher = pattern.matcher(formuleClean);
    
    while (matcher.find()) {
        String identifiant = matcher.group();
        if (!identifiant.matches("\\d+(\\.\\d+)?")) {
            identifiants.add(identifiant);
        }
    }
    
    // 6. Vérifier chaque identifiant contre les abréviations
    if (!identifiants.isEmpty()) {
        List<Abreviation> toutesAbreviations = abreviationService.getAllOrderedByAbreviation();
        Map<String, String> mapAbreviations = toutesAbreviations.stream()
            .collect(Collectors.toMap(
                Abreviation::getAbreviation,
                Abreviation::getLibelle,
                (a, b) -> a
            ));
        
        List<String> abreviationsInvalides = identifiants.stream()
            .filter(id -> !mapAbreviations.containsKey(id))
            .collect(Collectors.toList());
        
        if (!abreviationsInvalides.isEmpty()) {
            String message = "Abréviation(s) non reconnue(s): " + 
                String.join(", ", abreviationsInvalides) + 
                "\nAbréviations valides: " + 
                toutesAbreviations.stream()
                    .map(Abreviation::getAbreviation)
                    .sorted()
                    .collect(Collectors.joining(", "));
            
            throw new ValidationException(message);
        }
    }
    
    // 7. Validation syntaxique finale
    if (!isValidSyntax(formuleClean)) {
        throw new ValidationException("Syntaxe de formule incorrecte");
    }
    
    return true;
}

    // Méthodes auxiliaires
    private boolean areParenthesesBalanced(String formule) {
        int count = 0;
        for (char c : formule.toCharArray()) {
            if (c == '(') {
                count++;
            } else if (c == ')') {
                count--;
                if (count < 0) return false;
            }
        }
        return count == 0;
    }

    private boolean hasEmptyParentheses(String formule) {
        // Détecte les parenthèses vides avec ou sans espaces
        return formule.matches(".*\\(\\s*\\).*");
    }

    private boolean hasOperatorBeforeOpenParenthesis(String formule) {
        // Détecte les motifs comme: + (, * (, - (, etc.
        Pattern pattern = Pattern.compile("[+\\-*/]\\s*\\(");
        return pattern.matcher(formule).find();
    }

    private boolean isValidSyntax(String formule) {
        // Supprimer les espaces pour faciliter l'analyse
        String noSpaces = formule.replaceAll("\\s+", "");
        
        // Expressions régulières pour validation syntaxique
        // 1. Pas d'opérateurs consécutifs
        if (noSpaces.matches(".*[+\\-*/]{2,}.*")) {
            return false;
        }
        
        // 2. Pas d'opérateurs au début (sauf + ou - unaire)
        if (noSpaces.matches("^[*/].*")) {
            return false;
        }
        
        // 3. Pas d'opérateurs à la fin
        if (noSpaces.matches(".*[+\\-*/]$")) {
            return false;
        }
        
        // 4. Pas de caractères invalides après nombre
        if (noSpaces.matches(".*\\d[a-zA-Z_].*")) {
            return false;
        }
        
        // 5. Validation avec expression régulière complète
        String validPattern = "^" +
            "([+\\-]?)" + // Opérateur unaire optionnel au début
            "(" +
                "([a-zA-Z_][a-zA-Z0-9_]*)" + // Identifiant
                "|" +
                "(\\d+(\\.\\d+)?)" + // Nombre
                "|" +
                "\\([^)]+\\)" + // Expression entre parenthèses (non vide)
            ")" +
            "(([+\\-*/])" + // Opérateur
                "(" +
                    "([a-zA-Z_][a-zA-Z0-9_]*)" +
                    "|" +
                    "(\\d+(\\.\\d+)?)" +
                    "|" +
                    "\\([^)]+\\)" +
                ")" +
            ")*" +
            "$";
        
        return noSpaces.matches(validPattern);
    }

    public static class ValidationException extends Exception {
        public ValidationException(String message) {
            super(message);
        }
    }
    
    // Récupérer toutes les rubriques
    @Transactional(readOnly = true)
    public List<RubriquePaie> getAll() {
        return rubriquePaieRepository.findAll();
    }
    
    // Récupérer toutes les rubriques actives
    @Transactional(readOnly = true)
    public List<RubriquePaie> getAllActive() {
        return rubriquePaieRepository.findByEstActifTrueOrderByOrdreAsc();
    }
    
    // Récupérer par ID
    @Transactional(readOnly = true)
    public Optional<RubriquePaie> getById(String id) {
        return rubriquePaieRepository.findById(id);
    }
    
    // Récupérer par code
    @Transactional(readOnly = true)
    public Optional<RubriquePaie> getByCode(String code) {
        return rubriquePaieRepository.findByCode(code);
    }

    // NOUVELLE : Méthode avec pagination
    public Page<RubriquePaie> getAllRubriquesPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("ordre").ascending());
        return rubriquePaieRepository.findAllByOrderByOrdreAsc(pageable);
    }

    public List<RubriquePaie> getAllRubriqueParOrdre(){
        return rubriquePaieRepository.findAllByOrderByOrdreAsc();
    }

    // Méthode principale de recherche
    public Page<RubriquePaie> searchRubriques(
            String search,
            String typeId,
            Boolean estImposable,
            Boolean estSoumisCotisations,
            Boolean estDeductibleIrsa,
            Boolean estActif,
            int page,
            int size,
            String sortBy,
            String sortDirection) {
        
        // Créer le tri
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Sort sort = Sort.by(direction, sortBy);
        
        // Créer la pagination
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // Appeler la méthode du repository
        // Tous les paramètres peuvent être null
        return rubriquePaieRepository.searchByCriteria(
            search, typeId, estImposable, estSoumisCotisations, estDeductibleIrsa, estActif, pageable
        );
    }
    
    // Version adaptée pour le frontend (gestion des string au lieu de Boolean)
    public Page<RubriquePaie> searchForFrontend(
            String search,
            String typeId,
            String estImposable,    // "true", "false", ou null/"TOUS"
            String estSoumisCotisations,
            String estDeductibleIrsa,
            String estActif,
            int page,
            int size,
            String sortBy,
            String sortDirection) {
        
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // Convertir les string en Boolean (null si "TOUS" ou vide)
        Boolean imposableBool = convertStringToBoolean(estImposable);
        Boolean cotisationsBool = convertStringToBoolean(estSoumisCotisations);
        Boolean deductibleIrsaBool = convertStringToBoolean(estDeductibleIrsa);
        Boolean actifBool = convertStringToBoolean(estActif);
        
        return rubriquePaieRepository.searchByCriteria(
            search, typeId, imposableBool, cotisationsBool, deductibleIrsaBool, actifBool, pageable
        );
    }
    
    private Boolean convertStringToBoolean(String value) {
        if (value == null || value.isEmpty() || value.equalsIgnoreCase("TOUS")) {
            return null;
        }
        return Boolean.parseBoolean(value);
    }
    
    private Boolean convertToBooleanOrNull(String value) {
        if (value == null || value.equals("TOUS")) {
            return null;
        }
        return Boolean.parseBoolean(value);
    }
    
    // Activer une rubrique
    public RubriquePaie activate(String id) {
        return rubriquePaieRepository.findById(id)
                .map(rubrique -> {
                    rubrique.setEstActif(true);
                    rubrique.setModifiedAt(LocalDateTime.now());
                    return rubriquePaieRepository.save(rubrique);
                })
                .orElseThrow(() -> new RuntimeException("Rubrique non trouvée avec id: " + id));
    }
    
    // Désactiver une rubrique
    public RubriquePaie deactivate(String id) {
        return rubriquePaieRepository.findById(id)
                .map(rubrique -> {
                    rubrique.setEstActif(false);
                    rubrique.setModifiedAt(LocalDateTime.now());
                    return rubriquePaieRepository.save(rubrique);
                })
                .orElseThrow(() -> new RuntimeException("Rubrique non trouvée avec id: " + id));
    }
    
    // Supprimer (désactiver) une rubrique
    public void delete(String id) {
        rubriquePaieRepository.findById(id)
                .ifPresent(rubrique -> {
                    // Soft delete: on désactive plutôt que de supprimer
                    rubrique.setEstActif(false);
                    rubrique.setModifiedAt(LocalDateTime.now());
                    rubriquePaieRepository.save(rubrique);
                });
    }
    
    // Rechercher par mot-clé
    @Transactional(readOnly = true)
    public List<RubriquePaie> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        return rubriquePaieRepository.searchByKeyword(keyword.trim());
    }
    
    // Récupérer par catégorie
    @Transactional(readOnly = true)
    public List<RubriquePaie> getByCategorie(String categorieId) {
        return rubriquePaieRepository.findByCategorieId(categorieId);
    }
    
    // Récupérer par type
    @Transactional(readOnly = true)
    public List<RubriquePaie> getByType(String typeId) {
        return rubriquePaieRepository.findByTypeId(typeId);
    }
    
    // Récupérer par statut
    @Transactional(readOnly = true)
    public List<RubriquePaie> getByActifStatus(Boolean estActif) {
        return rubriquePaieRepository.findByEstActifOrderByOrdreAsc(estActif);
    }

    public List<RubriquePaie> getAllRubriqueActifs(){
        return rubriquePaieRepository.findByEstActifOrderByOrdreAsc(true);
    }
    
    // Vérifier l'existence
    @Transactional(readOnly = true)
    public boolean exists(String id) {
        return rubriquePaieRepository.existsById(id);
    }
    
    // Vérifier l'existence par code
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return rubriquePaieRepository.existsByCode(code);
    }
    
    // Réorganiser les ordres
    public void reorderRubriques() {
        List<RubriquePaie> rubriques = rubriquePaieRepository.findByEstActifTrueOrderByOrdreAsc();
        int order = 1;
        for (RubriquePaie rubrique : rubriques) {
            rubrique.setOrdre(order++);
            rubrique.setModifiedAt(LocalDateTime.now());
            rubriquePaieRepository.save(rubrique);
        }
    }
    
    // Récupérer les statistiques par type
    @Transactional(readOnly = true)
    public List<Object[]> getStatsByType() {
        return rubriquePaieRepository.countByType();
    }
    
    // Récupérer les statistiques par catégorie
    @Transactional(readOnly = true)
    public List<Object[]> getStatsByCategorie() {
        return rubriquePaieRepository.countByCategorie();
    }
    
    // Récupérer les statistiques des actives par catégorie
    @Transactional(readOnly = true)
    public List<Object[]> getStatsActiveByCategorie() {
        return rubriquePaieRepository.countActiveByCategorie();
    }
    
    // Compter le nombre total de rubriques
    @Transactional(readOnly = true)
    public long count() {
        return rubriquePaieRepository.count();
    }
    
    // Compter les rubriques actives
    @Transactional(readOnly = true)
    public long countActive() {
        return rubriquePaieRepository.findByEstActifTrue().size();
    }
    
    // Trouver le prochain ordre disponible
    @Transactional(readOnly = true)
    public Integer findNextOrdre() {
        return rubriquePaieRepository.findNextOrdre();
    }
}
