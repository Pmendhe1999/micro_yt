package com.qc.mapper;

import com.qc.dto.ProductionOrderDTO;
import com.qc.dto.ProductionOrderDTOResponse;
import com.qc.entities.ProductionOrder;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductionOrderMapper {
    // DTO → Entity
    ProductionOrder toEntity(ProductionOrderDTO dto);

    List<ProductionOrder> toEntityList(List<ProductionOrderDTO> dtoList);

    // Entity → ResponseDTO
    ProductionOrderDTOResponse toDto(ProductionOrder entity);

    List<ProductionOrderDTOResponse> toDtoList(List<ProductionOrder> entityList);
}
