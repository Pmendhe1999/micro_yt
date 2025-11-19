package com.qc.repositories;

import com.qc.entities.ProductMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductMasterRepository extends JpaRepository<ProductMaster, Long> {

    @EntityGraph(attributePaths = {"mediaDetailsList", "mediaDetailsList.media"})
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
            "AND (:status IS NULL OR p.status = :status) " +
            "AND (:mfgDate IS NULL OR p.mfgDate = :mfgDate) " +
            "AND (:expDate IS NULL OR p.expDate = :expDate)"+
            "AND (:sizeValue IS NULL OR LOWER(p.size) LIKE LOWER(CONCAT('%', :sizeValue, '%'))) " +
            "AND (:orientationValue IS NULL OR LOWER(p.orientation) LIKE LOWER(CONCAT('%', :orientationValue, '%')))")
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
            @Param("mfgDate") LocalDate mfgDate,
            @Param("expDate") LocalDate expDate,
            @Param("sizeValue") String sizeValue,
            @Param("orientationValue") String orientationValue,
            Pageable pageable);

    List<ProductMaster> findByName(String name);
    Optional<ProductMaster> findByNameAndProductCodeAndSizeAndOrientation(String name, String productCode, String size,
                                                     String orientation);

}
