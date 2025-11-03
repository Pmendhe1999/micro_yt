package com.quiz.repositories;

import com.quiz.entities.MediaDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaDetailsRepository  extends JpaRepository<MediaDetails, Long> {
}
