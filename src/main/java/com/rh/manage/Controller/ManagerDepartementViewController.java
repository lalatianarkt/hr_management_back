package com.rh.manage.Controller;

import org.springframework.web.bind.annotation.*;

import com.rh.manage.Service.ManagerDepartementViewService;
import com.rh.manage.View.ManagerDepartementView;

import java.util.List;

@RestController
@RequestMapping("/api/manager-departements")
public class ManagerDepartementViewController {

    private final ManagerDepartementViewService service;

    public ManagerDepartementViewController(ManagerDepartementViewService service) {
        this.service = service;
    }

    @GetMapping
    public List<ManagerDepartementView> getAll() {
        return service.getAll();
    }

    @GetMapping("/actifs")
    public List<ManagerDepartementView> getActifs() {
        return service.findActifDepWithActifManager();
    } 

    @GetMapping("/departement/{id}")
    public List<ManagerDepartementView> getByDepartement(@PathVariable String id) {
        return service.getByDepartement(id);
    }

    @GetMapping("/search")
    public List<ManagerDepartementView> search(@RequestParam String nom) {
        return service.searchByNom(nom);
    }
}

