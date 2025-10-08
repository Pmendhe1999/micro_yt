package com.identity.service;

import com.identity.dto.MediaDetailsDTO;
import com.identity.entity.MediaDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MediaDetailsService {
    MediaDetails saveMediaDetails(MediaDetailsDTO dto, String token);

    Page<MediaDetails> getAllMediaDetails(String search, Pageable pageable);

    Optional<MediaDetails> getMediaDetailsById(Long id);

    MediaDetails updateMediaDetailsReturnEntity(Long id, MediaDetailsDTO dto, String token);

    MediaDetails deleteMediaDetailsReturnEntity(Long id, String token);

    List<MediaDetails> getMediaDetailsByUserId(Long userId);

    List<MediaDetails> getMediaDetailsByMediaId(Long mediaId);
}
