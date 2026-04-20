
package com.rh.manage.Service;

import com.rh.manage.Dto.UserListDTO;
import com.rh.manage.Dto.UserRequest;
import com.rh.manage.Model.Employe;
import com.rh.manage.Model.InfosProfessionnelles;
import com.rh.manage.Model.Token;
import com.rh.manage.Model.TypeUser;
import com.rh.manage.Model.User;
import com.rh.manage.Model.UserRole;
import com.rh.manage.Repository.UserRepository;
import com.rh.manage.Service.UserService.ResourceNotFoundException;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired 
    private InfosProfessionnellesService infosProfessionnellesService;

    @Autowired 
    private EmployeService employeService;

    @Autowired 
    private TypeUserService typeUserService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRoleService roleUserService;
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public Optional<User> findByEmployeId(String employeId) {
        return userRepository.findByEmployeIdAndStatut(employeId, 1);
    }
    
    public User save(User user) {
        return userRepository.save(user);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    public void deleteUserById(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Utilisateur non trouve");
        }
        userRepository.deleteById(id);
    }

    public Optional<User> getById(String id){
        return userRepository.findById(id);
    }

    public List<UserListDTO> getAllUsersForAdminList() {
        List<User> users = userRepository.findAllWithEmployeAndTypeUser();
        List<UserListDTO> result = new ArrayList<>();

        for (User user : users) {
            Employe employe = user.getEmploye();
            if (employe == null) {
                continue;
            }

            InfosProfessionnelles infosPro = infosProfessionnellesService.findInfosProfessionnellesByIdEmploye(employe.getId());
            String departement = "Non assigne";

            if (infosPro != null && infosPro.getDepartement() != null) {
                departement = infosPro.getDepartement().getNom();
            }

            result.add(new UserListDTO(
                user.getId(),
                infosPro != null ? infosPro.getMatricule() : null,
                employe.getNom(),
                employe.getPrenom(),
                resolveRoleForAdminList(user),
                departement,
                user.getStatut(),
                extractRoleIds(user),
                extractRoleTypes(user)
            ));
        }

        return result;
    }

    public Map<String, Object> getAllUsersForAdminListPaged(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("employe.nom").ascending().and(Sort.by("employe.prenom").ascending()));
        Page<User> usersPage = userRepository.findAll(pageable);

        List<UserListDTO> result = new ArrayList<>();
        for (User user : usersPage.getContent()) {
            Employe employe = user.getEmploye();
            if (employe == null) {
                continue;
            }

            InfosProfessionnelles infosPro = infosProfessionnellesService.findInfosProfessionnellesByIdEmploye(employe.getId());
            String departement = "Non assigne";

            if (infosPro != null && infosPro.getDepartement() != null) {
                departement = infosPro.getDepartement().getNom();
            }

            result.add(new UserListDTO(
                user.getId(),
                infosPro != null ? infosPro.getMatricule() : null,
                employe.getNom(),
                employe.getPrenom(),
                resolveRoleForAdminList(user),
                departement,
                user.getStatut(),
                extractRoleIds(user),
                extractRoleTypes(user)
            ));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("content", result);
        response.put("page", usersPage.getNumber());
        response.put("size", usersPage.getSize());
        response.put("totalElements", usersPage.getTotalElements());
        response.put("totalPages", usersPage.getTotalPages());
        response.put("last", usersPage.isLast());
        return response;
    }

    public User updateUserStatut(String userId, Integer statut) {
        if (statut == null || (statut != 1 && statut != 3)) {
            throw new IllegalArgumentException("Statut invalide. Utilisez 1 pour accepter ou 3 pour refuser.");
        }

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouve"));

        boolean hasAdminRole = false;
        if (user.getUserRoles() != null && !user.getUserRoles().isEmpty()) {
            hasAdminRole = user.getUserRoles().stream()
                .anyMatch(role -> role.getTypeUser() != null && "Admin".equalsIgnoreCase(role.getTypeUser().getType()));
        }

        if (!hasAdminRole) {
            hasAdminRole = userRoleService.hasRole(userId, "Admin");
        }

        if (!hasAdminRole) {
            throw new IllegalArgumentException("Cette action est reservee aux utilisateurs de type Admin.");
        }

        if (user.getStatut() == null || user.getStatut() != 2) {
            throw new IllegalArgumentException("Seuls les utilisateurs Admin en attente peuvent etre acceptes ou refuses.");
        }

        user.setStatut(statut);
        user.setModifiedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public UserRole toggleUserRole(String userId, Integer typeUserId, boolean authorized) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouve"));

        TypeUser typeUser = typeUserService.getTypeUserById(typeUserId)
            .orElseThrow(() -> new RuntimeException("Type utilisateur introuvable"));

        if (authorized) {
            return userRoleService.assignRole(user, typeUser);
        }

        userRoleService.delete(userId, typeUserId);
        return null;
    }

    private List<Integer> extractRoleIds(User user) {
        List<Integer> roleIds = new ArrayList<>();

        if (user.getUserRoles() != null && !user.getUserRoles().isEmpty()) {
            for (UserRole role : user.getUserRoles()) {
                if (role.getTypeUser() != null) {
                    roleIds.add(role.getTypeUser().getId());
                }
            }
        } else {
            List<UserRole> persistedRoles = userRoleService.getByUserId(user.getId());
            for (UserRole role : persistedRoles) {
                if (role.getTypeUser() != null) {
                    roleIds.add(role.getTypeUser().getId());
                }
            }
        }

        return roleIds;
    }

    private List<String> extractRoleTypes(User user) {
        List<String> roleTypes = new ArrayList<>();

        if (user.getUserRoles() != null && !user.getUserRoles().isEmpty()) {
            for (UserRole role : user.getUserRoles()) {
                if (role.getTypeUser() != null && role.getTypeUser().getType() != null) {
                    roleTypes.add(role.getTypeUser().getType());
                }
            }
        } else {
            List<UserRole> persistedRoles = userRoleService.getByUserId(user.getId());
            for (UserRole role : persistedRoles) {
                if (role.getTypeUser() != null && role.getTypeUser().getType() != null) {
                    roleTypes.add(role.getTypeUser().getType());
                }
            }
        }

        return roleTypes;
    }

    private String resolveRoleForAdminList(User user) {
        if (user.getUserRoles() != null && !user.getUserRoles().isEmpty()) {
            Optional<UserRole> adminRole = user.getUserRoles().stream()
                .filter(role -> role.getTypeUser() != null && role.getTypeUser().getType() != null)
                .filter(role -> "Admin".equalsIgnoreCase(role.getTypeUser().getType()))
                .findFirst();

            if (adminRole.isPresent()) {
                return adminRole.get().getTypeUser().getType();
            }

            Optional<UserRole> activeRole = user.getUserRoles().stream()
                .filter(role -> role.getTypeUser() != null && role.getTypeUser().getType() != null)
                .filter(role -> Integer.valueOf(1).equals(role.getStatut()))
                .findFirst();

            if (activeRole.isPresent()) {
                return activeRole.get().getTypeUser().getType();
            }

            UserRole firstRole = user.getUserRoles().get(0);
            if (firstRole.getTypeUser() != null && firstRole.getTypeUser().getType() != null) {
                return firstRole.getTypeUser().getType();
            }
        }

        TypeUser primaryRole = userRoleService.getPrimaryRoleForUser(user.getId());
        return primaryRole != null ? primaryRole.getType() : "Non defini";
    }

    public Map<String, Object> authenticateUser(UserRequest userRequest) throws Exception, AuthenticationException, ResourceNotFoundException {
        User user = authenticate(userRequest.getUser());
        System.out.println("idUser : " + user.getId());
        
        if (user == null) {
            throw new AuthenticationException("Identifiants invalides");
        }
        
        Employe employe = employeService.getById(user.getEmploye().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé"));

        InfosProfessionnelles infosPro = infosProfessionnellesService.findInfosProfessionnellesByIdEmploye(employe.getId());
        
        // Récupérer tous les rôles actifs de l'utilisateur
        List<UserRole> listeUserRoles = roleUserService.getUserRolesByUserIdAndStatut(user.getId(), 1);
        System.out.println("Nombre de rôles trouvés : " + listeUserRoles.size());
        
        if (listeUserRoles.isEmpty()) {
            throw new ResourceNotFoundException("Aucun rôle actif trouvé pour cet utilisateur");
        }
        
        // Déterminer le rôle par défaut (le premier de la liste)
        String defaultRole = listeUserRoles.get(0).getTypeUser().getType();
        
        // Générer le token avec le rôle par défaut
        String jwt = jwtService.generateToken(user, defaultRole);
        Token token = tokenService.generateToken(user, "AUTH", jwt);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "Authentification réussie");
        response.put("token", token.getTokenGenere());
        response.put("infosPro", infosPro);
        response.put("user", buildUserResponse(user, employe, defaultRole));
        
        // Toujours envoyer la liste des rôles disponibles
        List<Map<String, Object>> rolesList = new ArrayList<>();
        for (UserRole userRole : listeUserRoles) {
            Map<String, Object> roleInfo = new HashMap<>();
            roleInfo.put("id", userRole.getTypeUser().getId());
            roleInfo.put("type", userRole.getTypeUser().getType());
            roleInfo.put("libelle", getRoleLibelle(userRole.getTypeUser().getType()));
            roleInfo.put("path", determineRedirectPath(userRole.getTypeUser().getType()));
            rolesList.add(roleInfo);
        }
        
        response.put("hasMultipleRoles", listeUserRoles.size() > 1);
        response.put("roles", rolesList);
        response.put("currentRole", defaultRole);
        response.put("path", determineRedirectPath(defaultRole));
        
        return response;
    }

    public User authenticate(User userRequest) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(userRequest.getEmail());
        User utilisateur = null;

        if (optionalUser.isEmpty()) {
            throw new Exception("Identifiant ou mot de passe incorrect !");
        } else {
            utilisateur = optionalUser.get();
        }

        if (utilisateur.getStatut() != 1) { 
            throw new Exception("Votre compte est en cours de validation");
        }       
        
        if (!BCrypt.checkpw(userRequest.getPassword(), utilisateur.getPassword())) {
            throw new Exception("Identifiant ou mot de passe incorrect !");
        }

        employeService.getById(utilisateur.getEmploye().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé"));

        List<UserRole> typeUsers = roleUserService.getUserRolesByUserIdAndStatut(utilisateur.getId(), 1);
        
        if (!typeUsers.isEmpty()) {
            utilisateur.setUserRoles(typeUsers);
        } else {
            throw new ResourceNotFoundException("Aucun rôle actif trouvé pour cet utilisateur");
        }
        
        return utilisateur;
    } 

    // Méthode pour sélectionner un rôle après authentification
    public Map<String, Object> selectUserRole(String userId, String selectedRoleType) throws Exception {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
        
        // Vérifier que l'utilisateur a bien ce rôle
        List<UserRole> userRoles = roleUserService.getUserRolesByUserIdAndStatut(userId, 0);
        boolean hasRole = userRoles.stream()
            .anyMatch(ur -> ur.getTypeUser().getType().equals(selectedRoleType));
        
        if (!hasRole) {
            throw new Exception("L'utilisateur n'a pas le rôle sélectionné");
        }
        
        // Récupérer les informations de l'employé
        Employe employe = employeService.getById(user.getEmploye().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Employé non trouvé"));
        
        InfosProfessionnelles infosPro = infosProfessionnellesService.findInfosProfessionnellesByIdEmploye(employe.getId());
        
        // Générer un nouveau token avec le rôle sélectionné
        String jwt = jwtService.generateToken(user, selectedRoleType);
        Token token = tokenService.generateToken(user, "AUTH", jwt);
        
        String path = determineRedirectPath(selectedRoleType);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", "Rôle sélectionné avec succès");
        response.put("token", token.getTokenGenere());
        response.put("infosPro", infosPro);
        response.put("user", buildUserResponse(user, employe, selectedRoleType));
        response.put("path", path);
        response.put("role", selectedRoleType);
        
        return response;
    }                       

    // Méthode utilitaire pour obtenir le libellé du rôle
    private String getRoleLibelle(String roleType) {
        switch (roleType) {
            case "Admin":
                return "Administrateur RH";
            case "Manager":
                return "Manager";
            case "Employe":
                return "Employé";
            case "IT":
                return "Administrateur IT";
            default:
                return roleType;
        }
    }

    public String determineRedirectPath(String userType) {
        switch (userType) {
            case "Admin_RH":
                return "/dashboard-RH/";
            case "Manager":
                return "/dashboard-Manager/";
            case "Employe":
                return "/emp/infos/fiche-perso";
            case "Admin_Systeme":
                return "/dashboard-IT/";
            default:
                return "/";
        }
    }
    
    private Map<String, Object> buildUserResponse(User user, Employe employe, String role) {
        Map<String, Object> userResponse = new HashMap<>();
        userResponse.put("email", user.getEmail());
        userResponse.put("role", role);
        userResponse.put("nomComplet", employe.getPrenom() + " " + employe.getNom());
        return userResponse;
    }
    
    public static class AuthenticationException extends RuntimeException {
        public AuthenticationException(String message) {
            super(message);
        }
    }
    
    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }

    @Transactional
    public void validateCountByEmail(String email, String tokenInput) throws Exception {
        Optional<User> userOpt = findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new Exception("Utilisateur non trouvé");
        }

        User user = userOpt.get();
        Token token = tokenService.getDernierTokenActiveParUser(user);

        if (token == null) {
            throw new Exception("Aucun token trouvé pour cet utilisateur");
        }

        LocalDateTime now = LocalDateTime.now();
        System.out.println("now amfiry : " + now);
        System.out.println("expiredAt : " + token.getExpiresAt());
        System.out.println("Aorès ve : " + now.isAfter(token.getExpiresAt()));
        
        if (now.isAfter(token.getExpiresAt())) {
            throw new Exception("Le token a expiré. Veuillez renvoyer un nouveau token.");
        }

        if (!token.getTokenGenere().equals(tokenInput)) {
            throw new Exception("Token invalide. Veuillez vérifier votre email.");
        }

        token.setIsActive(0); 
        tokenService.update(token);

        // Récupération des UserRole pour cet utilisateur
        List<UserRole> userRoles = userRoleService.getByUserId(user.getId());
        
        // Vérification si l'utilisateur a des rôles
        if (userRoles == null || userRoles.isEmpty()) {
            throw new Exception("Aucun rôle trouvé pour cet utilisateur");
        }
        
        // // Désactivation de tous les UserRole
        // for (UserRole userRole : userRoles) {
        //     userRole.setStatut(0); 
        //     userRoleService.update(userRole);
        // }
        
        // Récupération du type d'utilisateur principal (ex: le premier rôle actif ou un rôle spécifique)
        TypeUser typeUser = null;
        
        // Essayons d'abord de récupérer via user.getTypeUser() si cette relation existe
        // Sinon, on récupère le premier rôle de la liste
        if (userRoles != null && !userRoles.isEmpty()) {
            // Vous pouvez choisir un rôle spécifique, par exemple le premier
            typeUser = userRoles.get(0).getTypeUser();
            
            // Ou si vous avez une méthode pour récupérer le rôle principal
            // typeUser = userRoleService.getPrimaryRoleForUser(user.getId());
        }
        
        // Définition du statut de l'utilisateur en fonction de son rôle
        if (typeUser != null && "Admin".equalsIgnoreCase(typeUser.getType())) {
            user.setStatut(2); // Statut pour Admin
        } else {
            user.setStatut(1); // Statut pour les autres utilisateurs
        }
        
        user.setModifiedAt(LocalDateTime.now());
        save(user);
    }


    @Transactional
    public void register(User user, String matricule) throws Exception{
        System.out.println("eto 0");
        LocalDateTime now = LocalDateTime.now();

        Optional<User> existingUser = findByEmail(user.getEmail());
        if (!existingUser.isEmpty()) {
            System.out.println("ato ve ????");
            throw new Exception("Email déja existant. Veuillez utiliser un autre compte");
        }

        User userRegistered = registerUser(user);
        String tokenValue = emailService.sendTokenEmail(user.getEmail());
        System.out.println("token : " + tokenValue);
        System.out.println("eto 2");
        Token tok = new Token();
        tok.setCreatedAt(now);
        tok.setExpiresAt(now.plusSeconds(120)); 
        tok.setIsActive(1);
        tok.setTokenGenere(tokenValue);
        tok.setType("inscription");
        tok.setUser(userRegistered);
        tokenService.save(tok);
    }

    public UserRole registerUserRole(UserRole userRole) {
        return userRoleService.create(userRole);
    }

    public User registerUser(User user) {
        // Validation de l'email
        if(user.getEmail() == null || user.getEmail().isBlank()) {
            throw new RuntimeException("L'email ne peut pas être vide");
        }

        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if(!user.getEmail().matches(emailRegex)) {
            throw new RuntimeException("L'email n'a pas un format valide");
        }

        if(userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }
        
        // Vérification de l'existence de l'employé
        Optional<Employe> employeOpt = employeService.getByEmail(user.getEmail());
        if (employeOpt.isEmpty()) {
            throw new RuntimeException("Aucun employé trouvé avec cet email: " + user.getEmail());
        }
        Employe employe = employeOpt.get();
        user.setEmploye(employe);

        // Vérification des informations professionnelles
        Optional<InfosProfessionnelles> infosProOpt = infosProfessionnellesService
                .getByIdEmploye(employe.getId());
        if(infosProOpt.isEmpty()) {
            throw new RuntimeException("Le matricule n'existe pas. Veuillez mettre le bon matricule");
        }
        
        // Vérification qu'un utilisateur n'est pas déjà associé à cet employé
        if(userRepository.existsByEmploye_Id(employe.getId())) {
            throw new RuntimeException("Un utilisateur est déjà associé à cet employé");
        }
        
        // Détermination du type d'utilisateur (rôle)
        TypeUser typeUser = determineUserRole(employe, user);
        
        // Hashage du mot de passe
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        
        // Sauvegarde de l'utilisateur
        User savedUser = userRepository.save(user);
        
        // Création du UserRole (liaison entre l'utilisateur et le type)
        UserRole userRole = new UserRole();
        userRole.setUser(savedUser);
        userRole.setTypeUser(typeUser);
        userRole.setStatut(1); // 1 = actif
        userRole.setCreatedAt(LocalDateTime.now());
        userRoleService.create(userRole);
        
        return savedUser;
    }

    // Méthode pour déterminer le rôle de l'utilisateur
    private TypeUser determineUserRole(Employe employe, User user) {
        // Vérifier d'abord si l'employé est manager
        if (managerService.isManager(employe)) {
            return typeUserService.getByType("Manager");
        } 
        // Ensuite vérifier si un rôle a été spécifié dans l'objet user (pour Admin ou IT)
        else if (user.getUserRoles() != null && !user.getUserRoles().isEmpty()) {
            // Récupérer le type depuis les userRoles si disponibles
            TypeUser specifiedType = user.getUserRoles().get(0).getTypeUser();
            if (specifiedType != null) {
                String typeName = specifiedType.getType();
                if ("Admin".equals(typeName)) {
                    return typeUserService.getByType("Admin");
                } else if ("IT".equals(typeName)) {
                    return typeUserService.getByType("IT");
                }
            }
        }
        
        // Par défaut, rôle Employe
        return typeUserService.getByType("Employe");
    }
    
}









