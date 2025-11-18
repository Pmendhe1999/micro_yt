package com.qc.repositories;

import com.qc.entities.DeliveryChallan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryChallanRepository extends JpaRepository<DeliveryChallan, Long> {
    @Query("SELECT DISTINCT dc FROM DeliveryChallan dc " +
            "WHERE (:name IS NULL OR LOWER(dc.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:descriptions IS NULL OR LOWER(dc.descriptions) LIKE LOWER(CONCAT('%', :descriptions, '%'))) " +
            "AND (:status IS NULL OR dc.status = :status)")
    Page<DeliveryChallan> searchDeliveryChallansAdvanced(
            @Param("name") String name,
            @Param("descriptions") String descriptions,
            @Param("status") Boolean status,
            Pageable pageable
    );
}
