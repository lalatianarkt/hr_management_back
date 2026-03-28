package com.rh.manage.Service;

import com.rh.manage.Model.TypeUser;
import com.rh.manage.Model.User;
import com.rh.manage.Model.UserRole;
import com.rh.manage.Model.UserRoleId;
import com.rh.manage.Repository.UserRoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public List<UserRole> getAll() {
        return userRoleRepository.findAll();
    }

    public Optional<UserRole> getById(String userId, Integer typeUserId) {
        // Correction : les noms des paramètres doivent correspondre à UserRoleId
        return userRoleRepository.findById(new UserRoleId(userId, typeUserId));
    }

    public List<UserRole> getByUserId(String userId) {
        // Correction : utiliser findById_UserId
        return userRoleRepository.findByUserId(userId);
    }

    public List<UserRole> getByTypeId(Integer typeUserId) {
        // Correction : utiliser findById_TypeUserId
        return userRoleRepository.findByTypeUserId(typeUserId);
    }

    @Transactional
    public UserRole create(UserRole userRole) {
        // S'assurer que les champs obligatoires sont définis
        if (userRole.getCreatedAt() == null) {
            userRole.setCreatedAt(LocalDateTime.now());
        }
        if (userRole.getStatut() == null) {
            userRole.setStatut(1); // 1 = actif par défaut
        }
        // S'assurer que l'ID est correctement défini
        // if (userRole.getUser() == null && userRole.getUser() != null && userRole.getTypeUser() != null) {
        //     userRole.setUser(user);
        //     userRole.setId(new UserRoleId(userRole.getUser().getId(), userRole.getTypeUser().getId()));
        // }
        return userRoleRepository.save(userRole);
    }

    @Transactional
    public UserRole update(UserRole userRole) {
        // Mise à jour de l'entité existante
        userRole.setModifiedAt(LocalDateTime.now());
        return userRoleRepository.save(userRole);
    }

    @Transactional
    public void delete(String userId, Integer typeUserId) {
        userRoleRepository.deleteById(new UserRoleId(userId, typeUserId));
    }

    public TypeUser getPrimaryRoleForUser(String userId) {
        List<UserRole> roles = userRoleRepository.findByUserIdWithType(userId);
        if (roles == null || roles.isEmpty()) {
            return null;
        }
        // Retourner le rôle actif en priorité
        for (UserRole role : roles) {
            if (role.getStatut() != null && role.getStatut() == 1) {
                return role.getTypeUser();
            }
        }
        // Sinon retourner le premier
        return roles.get(0).getTypeUser();
    }

    @Transactional
    public UserRole assignRole(User user, TypeUser typeUser) {
        // Vérifier si le rôle existe déjà
        Optional<UserRole> existingRole = userRoleRepository.findById(
            new UserRoleId(user.getId(), typeUser.getId())
        );
        
        if (existingRole.isPresent()) {
            // Si le rôle existe, le réactiver
            UserRole userRole = existingRole.get();
            userRole.setStatut(1);
            userRole.setModifiedAt(LocalDateTime.now());
            return userRoleRepository.save(userRole);
        }
        
        // Sinon, créer un nouveau UserRole
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setTypeUser(typeUser);
        // userRole.setId(new UserRoleId(user.getId(), typeUser.getId()));
        userRole.setStatut(1);
        userRole.setCreatedAt(LocalDateTime.now());
        
        return userRoleRepository.save(userRole);
    }

    @Transactional
    public UserRole assignRole(String userId, Integer typeUserId) {
        // Méthode alternative utilisant les IDs
        // Note: Cette méthode suppose que vous avez accès aux services User et TypeUser
        throw new UnsupportedOperationException(
            "Utilisez assignRole(User user, TypeUser typeUser) ou passez les objets complets"
        );
    }

    public boolean hasRole(String userId, String roleType) {
        return userRoleRepository.hasRole(userId, roleType);
    }

    public List<UserRole> getUserRolesByUserIdAndStatut(String userId, Integer statut) {
        return userRoleRepository.findByUserIdAndStatutWithTypeNative(userId, statut);
    }                       

    @Transactional
    public void deactivateAllRolesForUser(String userId) {
        List<UserRole> userRoles = getByUserId(userId);
        for (UserRole userRole : userRoles) {
            userRole.setStatut(0);
            userRole.setModifiedAt(LocalDateTime.now());
            userRoleRepository.save(userRole);
        }
    }

    @Transactional
    public void activateRole(String userId, Integer typeUserId) {
        Optional<UserRole> userRoleOpt = getById(userId, typeUserId);
        if (userRoleOpt.isPresent()) {
            UserRole userRole = userRoleOpt.get();
            userRole.setStatut(1);
            userRole.setModifiedAt(LocalDateTime.now());
            userRoleRepository.save(userRole);
        }
    }

    public Optional<UserRole> getActiveRoleForUser(String userId) {
        return userRoleRepository.findActiveRoleByUserId(userId);
    }

    @Transactional
    public void deleteAllRolesForUser(String userId) {
        userRoleRepository.deleteByUserId(userId);
    }
    
    // Méthode utilitaire pour vérifier si un utilisateur a un rôle actif
    public boolean hasActiveRole(String userId) {
        return getActiveRoleForUser(userId).isPresent();
    }
    
    // Méthode pour obtenir le rôle actif avec son type
    public Optional<TypeUser> getActiveTypeUserForUser(String userId) {
        return getActiveRoleForUser(userId)
            .map(UserRole::getTypeUser);
    }
}