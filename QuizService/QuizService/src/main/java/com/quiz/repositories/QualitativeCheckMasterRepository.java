package com.quiz.repositories;

import com.quiz.entities.QualitativeCheckMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QualitativeCheckMasterRepository extends JpaRepository<QualitativeCheckMaster, Long> {
}
