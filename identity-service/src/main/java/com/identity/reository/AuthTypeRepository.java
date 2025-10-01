package com.identity.reository;

import com.identity.entity.AuthType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthTypeRepository extends JpaRepository<AuthType, Long> {
    boolean existsByName(String name);

    @Query("SELECT at FROM AuthType at " +
            "WHERE (:search IS NULL OR LOWER(at.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:appFuncIds IS NULL OR at.appFunction.id IN :appFuncIds) " +
            "AND (:authTypeMasterIds IS NULL OR at.authTypeMaster.id IN :authTypeMasterIds)")
    Page<AuthType> findByFilters(@Param("search") String search,
                                 @Param("authFuncIds") java.util.List<Long> authFuncIds,
                                 @Param("authTypeMasterIds") java.util.List<Long> authTypeMasterIds,
                                 Pageable pageable);
}
