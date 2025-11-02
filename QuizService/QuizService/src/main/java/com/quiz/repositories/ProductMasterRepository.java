package com.quiz.repositories;

import com.quiz.entities.ProductMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ProductMasterRepository extends JpaRepository<ProductMaster, Long> {

    @Query("SELECT DISTINCT p FROM ProductMaster p " +
            "WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:productCode IS NULL OR LOWER(p.productCode) LIKE LOWER(CONCAT('%', :productCode, '%'))) " +
            "AND (:serialNo IS NULL OR LOWER(p.serialNo) LIKE LOWER(CONCAT('%', :serialNo, '%'))) " +
            "AND (:orderNo IS NULL OR LOWER(p.orderNo) LIKE LOWER(CONCAT('%', :orderNo, '%'))) " +
            "AND (:hsnCode IS NULL OR LOWER(p.hsnCode) LIKE LOWER(CONCAT('%', :hsnCode, '%'))) " +
            "AND (:unit IS NULL OR LOWER(p.unit) LIKE LOWER(CONCAT('%', :unit, '%'))) " +
            "AND (:price IS NULL OR p.price = :price) " +
            "AND (:quantity IS NULL OR p.quantity = :quantity) " +
            "AND (:isPublished IS NULL OR p.isPublished = :isPublished) " +
            "AND (:status IS NULL OR p.status = :status)")
    Page<ProductMaster> searchProductMastersAdvanced(
            @Param("name") String name,
            @Param("productCode") String productCode,
            @Param("serialNo") String serialNo,
            @Param("orderNo") String orderNo,
            @Param("hsnCode") String hsnCode,
            @Param("unit") String unit,
            @Param("price") BigDecimal price,
            @Param("quantity") BigDecimal quantity,
            @Param("isPublished") Boolean isPublished,
            @Param("status") Boolean status,
            Pageable pageable);
}
