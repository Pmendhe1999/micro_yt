package com.quiz.mapper;

import com.quiz.dto.MediaDTO;
import com.quiz.dto.MediaDTOResponse;
import com.quiz.entities.Media;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MediaMapper {
    Media toEntity(MediaDTO dto);

    MediaDTOResponse toDto(Media entity);

    List<Media> toEntityList(List<MediaDTO> dtoList);

    List<MediaDTOResponse> toDtoList(List<Media> entityList);
}
