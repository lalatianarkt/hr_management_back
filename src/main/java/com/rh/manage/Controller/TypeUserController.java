package com.rh.manage.Controller;

import com.rh.manage.Model.TypeUser;
import com.rh.manage.Service.TypeUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/typeUsers")
// @CrossOrigin(origins = "*")

public class TypeUserController {

    @Autowired
    private TypeUserService typeUserService;

    @GetMapping
    public List<TypeUser> getAllTypeUsers() {
        return typeUserService.getAllTypeUsers();
    }

    @GetMapping("/{id}")
    public TypeUser getTypeUserById(@PathVariable Integer id) {
        return typeUserService.getTypeUserById(id)
                .orElseThrow(() -> new RuntimeException("TypeUser introuvable avec id : " + id));
    }

    @PostMapping
    public TypeUser createTypeUser(@RequestBody TypeUser typeUser) {
        return typeUserService.createTypeUser(typeUser);
    }

    @PutMapping("/{id}")
    public TypeUser updateTypeUser(@PathVariable Integer id, @RequestBody TypeUser typeUserDetails) {
        return typeUserService.updateTypeUser(id, typeUserDetails);
    }

    @DeleteMapping("/{id}")
    public String deleteTypeUser(@PathVariable Integer id) {
        typeUserService.deleteTypeUser(id);
        return "TypeUser supprimé avec succès";
    }
}
