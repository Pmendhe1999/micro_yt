package com.identity.service;

import com.identity.dto.MediaDTO;
import com.identity.entity.Media;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.Optional;

public interface MediaService {
    Media saveMedia(MediaDTO mediaDTO, String token);

    Page<Media> getAllMedia(String search, Pageable pageable);

    Optional<Media> getMediaById(Long id);

    Media updateMediaReturnEntity(Long id, MediaDTO updatedMediaDTO, String token);

    Media deleteMediaReturnEntity(Long id, String token);
}
