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


}
