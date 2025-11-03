package com.quiz.mapper;

import com.quiz.dto.MediaDetailsDTO;
import com.quiz.dto.MediaDetailsDTOResponse;
import com.quiz.entities.Media;
import com.quiz.entities.MediaDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
@Mapper(componentModel = "spring")
public interface MediaDetailsMapper {

    @Mapping(target = "media", source = "mediaId", qualifiedByName = "mapToMedia")
    MediaDetails toEntity(MediaDetailsDTO dto);

    @Mapping(target = "mediaId", source = "media.id")
    @Mapping(target = "mediaName", source = "media.name")
    MediaDetailsDTOResponse toDto(MediaDetails entity);

    List<MediaDetails> toEntityList(List<MediaDetailsDTO> dtoList);
    List<MediaDetailsDTOResponse> toDtoList(List<MediaDetails> entityList);

    @Named("mapToMedia")
    default Media mapToMedia(Long mediaId) {
        if (mediaId == null) return null;
        Media media = new Media();
        media.setId(mediaId);
        return media;
    }
}
