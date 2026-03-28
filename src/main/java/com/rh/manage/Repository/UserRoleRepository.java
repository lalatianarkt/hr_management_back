package com.rh.manage.Repository;

import com.rh.manage.Model.UserRole;
import com.rh.manage.Model.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
    List<UserRole> findByIdUser(String idUser);
    List<UserRole> findByIdType(Integer idType);

    @Query("""
        SELECT ur
        FROM UserRole ur
        JOIN FETCH ur.typeUser tu
        WHERE ur.idUser = :userId
    """)
    List<UserRole> findByUserIdWithType(@Param("userId") String userId);

    @Query("""
        SELECT ur
        FROM UserRole ur
        JOIN FETCH ur.typeUser tu
        WHERE ur.idUser = :userId AND ur.statut = :statut
        ORDER BY ur.createdAt DESC
    """)
    List<UserRole> findByUserIdAndStatutWithType(
            @Param("userId") String userId,
            @Param("statut") Integer statut
    );
}
