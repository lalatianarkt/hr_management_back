package com.rh.manage.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rh.manage.Repository.PaieFilleRepository;
import com.rh.manage.Repository.PaieFilleViewRepository;
import com.rh.manage.View.PaieFilleView;

@Service
public class PaieFilleViewService 
{
    @Autowired
    private PaieFilleViewRepository paieFilleViewRepository;

    public List<PaieFilleView> getAllPaieFilles() {
        return paieFilleViewRepository.findAll();
    }

    public Optional<PaieFilleView> getPaieFilleById(Long id) {
        return paieFilleViewRepository.findById(id);
    }

}

