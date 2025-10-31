package com.quiz.mapper;

import com.quiz.dto.DeliveryChallanMasterDTO;
import com.quiz.dto.DeliveryChallanMasterDTOResponse;
import com.quiz.entities.DeliveryChallanMaster;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeliveryChallanMasterMapper {
    DeliveryChallanMaster toEntity(DeliveryChallanMasterDTO dto);

    DeliveryChallanMasterDTOResponse toDto(DeliveryChallanMaster entity);

    List<DeliveryChallanMaster> toEntityList(List<DeliveryChallanMasterDTO> dtoList);

    List<DeliveryChallanMasterDTOResponse> toDtoList(List<DeliveryChallanMaster> entityList);
}
