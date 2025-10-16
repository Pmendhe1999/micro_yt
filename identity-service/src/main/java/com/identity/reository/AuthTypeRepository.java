package com.identity.reository;

import com.identity.entity.AppFunction;
import com.identity.entity.AuthType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AuthTypeRepository extends JpaRepository<AuthType, Long> {
    boolean existsByName(String name);

    @Query("SELECT at FROM AuthType at " +
            "WHERE (:search IS NULL OR LOWER(at.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:appFuncIds IS NULL OR at.appFunction.id IN :appFuncIds) " +
            "AND (:name IS NULL OR LOWER(at.name) LIKE LOWER(CONCAT('%', :name, '%'))) "+
            "AND (:authTypeMasterIds IS NULL OR at.authTypeMaster.id IN :authTypeMasterIds)")
    Page<AuthType> findByFilters(@Param("search") String search,
                                 @Param("name") String name,
                                 @Param("appFuncIds") java.util.List<Long> appFuncIds,
                                 @Param("authTypeMasterIds") java.util.List<Long> authTypeMasterIds,
                                 Pageable pageable);

    // ✅ Add this method to fix the error
    Optional<AuthType> findByAppFunction(AppFunction appFunction);
}
