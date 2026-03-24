package com.rh.manage.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rh.manage.Model.EtatCivil;

@RestController
@RequestMapping("/api/etat-civil")
public class EtatCivilController {

    @GetMapping
    public EtatCivil[] getModesCalcul() {
        return EtatCivil.values();
    }
}



