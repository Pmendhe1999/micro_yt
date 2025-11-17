package com.qc.mapper;

import com.qc.dto.ProductionOrderDTO;
import com.qc.dto.ProductionOrderDTOResponse;
import com.qc.entities.ProductionOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductionOrderMapper {

    ProductionOrder toEntity(ProductionOrderDTO dto);

    List<ProductionOrder> toEntityList(List<ProductionOrderDTO> dtoList);
    @Mapping(source = "createdDate", target = "createdDate")
    ProductionOrderDTOResponse toDto(ProductionOrder entity);

    List<ProductionOrderDTOResponse> toDtoList(List<ProductionOrder> entityList);
}
