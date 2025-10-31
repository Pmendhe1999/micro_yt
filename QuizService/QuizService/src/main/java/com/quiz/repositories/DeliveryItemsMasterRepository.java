package com.quiz.repositories;

import com.quiz.entities.DeliveryItemsMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryItemsMasterRepository extends JpaRepository<DeliveryItemsMaster, Long> {

}
