package com.quiz.repositories;

import com.quiz.entities.DeliveryChallanMaster;
import com.quiz.entities.DeliveryItemsMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeliveryChallanMasterRepository extends JpaRepository<DeliveryChallanMaster, Long> {
    @Query("SELECT DISTINCT d FROM DeliveryChallanMaster d " +
            "WHERE (:name IS NULL OR LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:descriptions IS NULL OR LOWER(d.descriptions) LIKE LOWER(CONCAT('%', :descriptions, '%'))) " +
            "AND (:status IS NULL OR d.status = :status) ")
    Page<DeliveryChallanMaster> searchDeliveryChallans(
            @Param("name") String name,
            @Param("descriptions") String descriptions,
            @Param("status") Boolean status,
            Pageable pageable);


}
