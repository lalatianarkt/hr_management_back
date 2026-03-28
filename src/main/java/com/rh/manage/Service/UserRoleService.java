package com.rh.manage.Service;

import com.rh.manage.Model.TypeUser;
import com.rh.manage.Model.UserRole;
import com.rh.manage.Model.UserRoleId;
import com.rh.manage.Repository.UserRoleRepository;
import org.springframework.stereotype.Service;

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

    public Optional<UserRole> getById(String idUser, Integer idType) {
        return userRoleRepository.findById(new UserRoleId(idUser, idType));
    }

    public List<UserRole> getByUserId(String idUser) {
        return userRoleRepository.findByIdUser(idUser);
    }

    public List<UserRole> getByTypeId(Integer idType) {
        return userRoleRepository.findByIdType(idType);
    }

    public UserRole create(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    public UserRole update(String idUser, Integer idType, UserRole userRoleDetails) {
        userRoleDetails.setIdUser(idUser);
        userRoleDetails.setIdType(idType);
        return userRoleRepository.save(userRoleDetails);
    }

    public void delete(String idUser, Integer idType) {
        userRoleRepository.deleteById(new UserRoleId(idUser, idType));
    }

    public TypeUser getPrimaryRoleForUser(String userId) {
        List<UserRole> roles = userRoleRepository.findByUserIdWithType(userId);
        if (roles == null || roles.isEmpty()) {
            return null;
        }
        return roles.get(0).getTypeUser();
    }

    public UserRole assignRole(String userId, TypeUser typeUser) {
        UserRoleId id = new UserRoleId(userId, typeUser.getId());
        return userRoleRepository.findById(id)
                .orElseGet(() -> userRoleRepository.save(new UserRole(userId, typeUser.getId())));
    }

    public boolean hasRole(String userId, String roleType) {
        List<UserRole> roles = userRoleRepository.findByUserIdWithType(userId);
        if (roles == null || roles.isEmpty()) {
            return false;
        }
        for (UserRole role : roles) {
            if (role != null && role.getTypeUser() != null &&
                role.getTypeUser().getType() != null &&
                role.getTypeUser().getType().equalsIgnoreCase(roleType)) {
                return true;
            }
        }
        return false;
    }

    public List<UserRole> getTypeUserByIdUserAndStatut(String idUser, Integer statut) {
        return userRoleRepository.findByUserIdAndStatutWithType(idUser, statut);
    }
}
