package com.rh.manage.Controller;

import com.rh.manage.Model.UserRole;
import com.rh.manage.Service.UserRoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-roles")
public class UserRoleController {

    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @GetMapping
    public List<UserRole> getAll() {
        return userRoleService.getAll();
    }

    @GetMapping("/{idUser}/{idType}")
    public UserRole getById(@PathVariable String idUser, @PathVariable Integer idType) {
        return userRoleService.getById(idUser, idType)
                .orElseThrow(() -> new RuntimeException("UserRole introuvable pour idUser: " + idUser + " et idType: " + idType));
    }

    @GetMapping("/user/{idUser}")
    public List<UserRole> getByUser(@PathVariable String idUser) {
        return userRoleService.getByUserId(idUser);
    }

    @GetMapping("/type/{idType}")
    public List<UserRole> getByType(@PathVariable Integer idType) {
        return userRoleService.getByTypeId(idType);
    }

    @PostMapping
    public UserRole create(@RequestBody UserRole userRole) {
        return userRoleService.create(userRole);
    }

    @PutMapping("/{idUser}/{idType}")
    public UserRole update(@PathVariable String idUser, @PathVariable Integer idType, @RequestBody UserRole userRoleDetails) {
        return userRoleService.update(idUser, idType, userRoleDetails);
    }

    @DeleteMapping("/{idUser}/{idType}")
    public String delete(@PathVariable String idUser, @PathVariable Integer idType) {
        userRoleService.delete(idUser, idType);
        return "UserRole supprimé avec succès";
    }
}
