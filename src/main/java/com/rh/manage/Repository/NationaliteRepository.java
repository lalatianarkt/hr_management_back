package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.rh.manage.Model.Nationalite;

@Repository
public interface NationaliteRepository extends JpaRepository<Nationalite, Integer> {
}

