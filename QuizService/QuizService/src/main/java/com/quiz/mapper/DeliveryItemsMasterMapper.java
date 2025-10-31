package com.quiz.mapper;

import com.quiz.dto.DeliveryItemsMasterDTO;
import com.quiz.dto.DeliveryItemsMasterDTOResponse;
import com.quiz.entities.DeliveryChallanMaster;
import com.quiz.entities.DeliveryItemsMaster;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeliveryItemsMasterMapper {
    @Mapping(target = "challan", source = "challanId", qualifiedByName = "mapToChallanMaster")
    DeliveryItemsMaster toEntity(DeliveryItemsMasterDTO dto);

    @Mapping(target = "challanId", source = "challan.id")
    @Mapping(target = "name", source = "challan.name")
    DeliveryItemsMasterDTOResponse toDto(DeliveryItemsMaster entity);

    List<DeliveryItemsMaster> toEntityList(List<DeliveryItemsMasterDTO> dtoList);
    List<DeliveryItemsMasterDTOResponse> toDtoList(List<DeliveryItemsMaster> entityList);

    @Named("mapToChallanMaster")
    default DeliveryChallanMaster mapToChallanMaster(Long challanId) {
        if (challanId == null) return null;
        DeliveryChallanMaster challan = new DeliveryChallanMaster();
        challan.setId(challanId);
        return challan;
    }
}
