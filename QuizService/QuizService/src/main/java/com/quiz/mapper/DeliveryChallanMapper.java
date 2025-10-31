package com.quiz.mapper;

import com.quiz.dto.DeliveryChallanDTO;
import com.quiz.dto.DeliveryChallanDTOResponse;
import com.quiz.entities.DeliveryChallan;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeliveryChallanMapper {

    DeliveryChallan toEntity(DeliveryChallanDTO dto);

    DeliveryChallanDTOResponse toDto(DeliveryChallan entity);

    List<DeliveryChallan> toEntityList(List<DeliveryChallanDTO> dtoList);

    List<DeliveryChallanDTOResponse> toDtoList(List<DeliveryChallan> entityList);
}
