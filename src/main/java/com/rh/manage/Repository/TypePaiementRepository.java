package com.rh.manage.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rh.manage.Model.TypePaiement;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypePaiementRepository extends JpaRepository<TypePaiement, String> {
    
    Optional<TypePaiement> findByCode(String code);
    
    Optional<TypePaiement> findByLibelle(String libelle);
    
    @Query("SELECT tp FROM TypePaiement tp WHERE tp.libelle LIKE %:keyword% OR tp.code LIKE %:keyword%")
    List<TypePaiement> searchByKeyword(@Param("keyword") String keyword);
    
    boolean existsByCode(String code);
}
