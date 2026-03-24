package com.rh.manage.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rh.manage.Enum.ModeCalcul;

@RestController
@RequestMapping("/api/modes-calcul")
public class ModeCalculController {

    @GetMapping
    public ModeCalcul[] getModesCalcul() {
        return ModeCalcul.values();
    }
}


