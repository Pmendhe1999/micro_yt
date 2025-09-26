package com.identity.reository;

import com.identity.entity.AppFunction;
import com.identity.entity.Authority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppFunctionRepository extends JpaRepository<AppFunction, Long> {
    Page<AppFunction> findByNameContainingIgnoreCase(String name, Pageable pageable);
    boolean existsByName(String name);

    @Query("SELECT af FROM AppFunction af " +
            "WHERE (:search IS NULL OR LOWER(af.name) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            "AND (:applicationIds IS NULL OR af.application.id IN :applicationIds) " +
            "AND (:appFunTypesMasterIds IS NULL OR af.appFunTypesMaster.id IN :appFunTypesMasterIds)")
    Page<AppFunction> findByFilters(@Param("search") String search,
                                    @Param("applicationIds") List<Long> applicationIds,
                                    @Param("appFunTypesMasterIds") List<Long> appFunTypesMasterIds,
                                    Pageable pageable);
}
