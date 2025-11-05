package com.qc.mapper;

import com.qc.dto.DeliveryChallanDTO;
import com.qc.dto.DeliveryChallanDTOResponse;
import com.qc.entities.DeliveryChallan;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeliveryChallanMapper {

    DeliveryChallan toEntity(DeliveryChallanDTO dto);

    DeliveryChallanDTOResponse toDto(DeliveryChallan entity);

    List<DeliveryChallan> toEntityList(List<DeliveryChallanDTO> dtoList);

    List<DeliveryChallanDTOResponse> toDtoList(List<DeliveryChallan> entityList);
}
