package com.qc.repositories;

import com.qc.entities.ProductionOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductionOrderRepository extends JpaRepository<ProductionOrder, Long> {
    @Query("SELECT p FROM ProductionOrder p " +
            "WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:description IS NULL OR LOWER(p.description) LIKE LOWER(CONCAT('%', :description, '%'))) " +
            "AND (:status IS NULL OR p.status = :status) " +
            "AND (:priority IS NULL OR p.priority = :priority) " +
            "AND (:productionOrderNo IS NULL OR LOWER(p.productionOrderNo) LIKE LOWER(CONCAT('%', :productionOrderNo, '%')))")
    Page<ProductionOrder> searchProductionOrders(
            @Param("name") String name,
            @Param("description") String description,
            @Param("status") Boolean status,
            @Param("priority") Long priority,
            @Param("productionOrderNo") String productionOrderNo,
            Pageable pageable);
}
