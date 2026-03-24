package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rh.manage.View.PaieFilleView;

@Repository
public interface  PaieFilleViewRepository extends JpaRepository<PaieFilleView, Long> {
    // Méthodes personnalisées si nécessaire (ex: findByCode, etc.)
}
