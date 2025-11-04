package com.quiz.repositories;

import com.quiz.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT p FROM Product p " +
            "WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:productCode IS NULL OR LOWER(p.productCode) LIKE LOWER(CONCAT('%', :productCode, '%'))) " +
            "AND (:serialNo IS NULL OR LOWER(p.serialNo) LIKE LOWER(CONCAT('%', :serialNo, '%'))) " +
            "AND (:orderNo IS NULL OR LOWER(p.orderNo) LIKE LOWER(CONCAT('%', :orderNo, '%'))) " +
            "AND (:batchNo IS NULL OR LOWER(p.batchNo) LIKE LOWER(CONCAT('%', :batchNo, '%'))) " +
            "AND (:hsnCode IS NULL OR LOWER(p.hsnCode) LIKE LOWER(CONCAT('%', :hsnCode, '%'))) " +
            "AND (:unit IS NULL OR LOWER(p.unit) LIKE LOWER(CONCAT('%', :unit, '%'))) " +
            "AND (:price IS NULL OR p.price = :price) " +
            "AND (:inStockQuantity IS NULL OR p.inStockQuantity = :inStockQuantity) " +
            "AND (:mfgDate IS NULL OR p.mfgDate = :mfgDate) " +
            "AND (:expDate IS NULL OR p.expDate = :expDate) " +
            "AND (:isPublished IS NULL OR p.isPublished = :isPublished) " +
            "AND (:status IS NULL OR p.status = :status)")
    Page<Product> searchProductsAdvanced(
            @Param("name") String name,
            @Param("productCode") String productCode,
            @Param("serialNo") String serialNo,
            @Param("orderNo") String orderNo,
            @Param("batchNo") String batchNo,
            @Param("hsnCode") String hsnCode,
            @Param("unit") String unit,
            @Param("price") BigDecimal price,
            @Param("inStockQuantity") Long inStockQuantity,
            @Param("mfgDate") LocalDate mfgDate,
            @Param("expDate") LocalDate expDate,
            @Param("isPublished") Boolean isPublished,
            @Param("status") Boolean status,
            Pageable pageable);
}
