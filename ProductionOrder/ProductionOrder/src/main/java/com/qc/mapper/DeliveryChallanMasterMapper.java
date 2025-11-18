package com.qc.mapper;

import com.qc.dto.DeliveryChallanMasterDTO;
import com.qc.dto.DeliveryChallanMasterDTOResponse;
import com.qc.entities.DeliveryChallanMaster;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeliveryChallanMasterMapper {
    DeliveryChallanMaster toEntity(DeliveryChallanMasterDTO dto);

    DeliveryChallanMasterDTOResponse toDto(DeliveryChallanMaster entity);

    List<DeliveryChallanMaster> toEntityList(List<DeliveryChallanMasterDTO> dtoList);

    List<DeliveryChallanMasterDTOResponse> toDtoList(List<DeliveryChallanMaster> entityList);
}
