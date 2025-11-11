package com.qc.repositories;

import com.qc.entities.MediaDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaDetailsRepository  extends JpaRepository<MediaDetails, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM MediaDetails md WHERE md.media.id = :mediaId")
    void deleteByMediaId(@Param("mediaId") Long mediaId);
}
