package com.qc.mapper;

import com.qc.dto.DeliveryItemsDTO;
import com.qc.dto.DeliveryItemsDTOResponse;
import com.qc.entities.DeliveryChallan;
import com.qc.entities.DeliveryItems;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeliveryItemsMapper {
    @Mapping(target = "challan", source = "challanId", qualifiedByName = "mapToChallan")
    DeliveryItems toEntity(DeliveryItemsDTO dto);

    @Mapping(target = "challanId", source = "challan.id")
    @Mapping(target = "challanName", source = "challan.name")
    DeliveryItemsDTOResponse toDto(DeliveryItems entity);

    List<DeliveryItems> toEntityList(List<DeliveryItemsDTO> dtoList);
    List<DeliveryItemsDTOResponse> toDtoList(List<DeliveryItems> entityList);

    @Named("mapToChallan")
    default DeliveryChallan mapToChallan(Long challanId) {
        if (challanId == null) return null;
        DeliveryChallan challan = new DeliveryChallan();
        challan.setId(challanId);
        return challan;
    }
}
