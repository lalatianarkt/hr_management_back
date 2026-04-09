package com.rh.manage.Repository;

import com.rh.manage.Model.UserRole;
import com.rh.manage.Model.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    
    List<UserRole> findByTypeUserId(Integer typeUserId);
    
    @Query("SELECT ur FROM UserRole ur WHERE ur.typeUser.type = :type")
    List<UserRole> findByTypeUserType(@Param("type") String type);
    
    @Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId")
    List<UserRole> findByUserId(@Param("userId") String userId);

    @Query("SELECT ur FROM UserRole ur WHERE ur.user.employe.id = :employeId")
    List<UserRole> findByEmployeId(@Param("employeId") String employeId);
    
    @Query("SELECT ur FROM UserRole ur WHERE ur.typeUser.id = :typeUserId")
    List<UserRole> findByTypeUserIdWithQuery(@Param("typeUserId") Integer typeUserId);
    
    @Query("""
        SELECT ur
        FROM UserRole ur
        JOIN FETCH ur.typeUser tu
        WHERE ur.user.id = :userId
    """)
    List<UserRole> findByUserIdWithType(@Param("userId") String userId);
    
    @Query(value = """
        SELECT ur.id_user, 
            ur.id_type, 
            ur.created_at, 
            ur.modified_at, 
            ur.statut,
            tu.id AS type_id,
            tu.type, 
            tu.created_at AS type_created_at, 
            tu.modified_at AS type_modified_at
        FROM user_role ur
        INNER JOIN type_user tu ON ur.id_type = tu.id
        WHERE ur.id_user = :userId 
        AND ur.statut = :statut
        ORDER BY ur.created_at DESC
        """, nativeQuery = true)
    List<UserRole> findByUserIdAndStatutWithTypeNative(
            @Param("userId") String userId,
            @Param("statut") Integer statut
    );
    
    @Query("""
        SELECT ur
        FROM UserRole ur
        JOIN FETCH ur.typeUser tu
        WHERE ur.user.id = :userId AND ur.statut = 1
        ORDER BY ur.createdAt DESC
        LIMIT 1
    """)
    Optional<UserRole> findActiveRoleByUserId(@Param("userId") String userId);
    
    @Query("""
        SELECT COUNT(ur) > 0
        FROM UserRole ur
        WHERE ur.user.id = :userId 
        AND ur.typeUser.type = :typeName 
        AND ur.statut = 1
    """)
    boolean hasRole(@Param("userId") String userId, @Param("typeName") String typeName);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM UserRole ur WHERE ur.user.id = :userId")
    void deleteByUserId(@Param("userId") String userId);
}
