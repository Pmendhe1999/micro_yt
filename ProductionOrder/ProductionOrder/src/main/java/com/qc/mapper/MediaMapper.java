package com.qc.mapper;

import com.qc.dto.MediaDTO;
import com.qc.dto.MediaDTOResponse;
import com.qc.entities.Media;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MediaMapper {
    Media toEntity(MediaDTO dto);

    MediaDTOResponse toDto(Media entity);

    List<Media> toEntityList(List<MediaDTO> dtoList);

    List<MediaDTOResponse> toDtoList(List<Media> entityList);
}
