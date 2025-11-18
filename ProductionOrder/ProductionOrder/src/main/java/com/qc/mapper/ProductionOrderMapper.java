package com.qc.mapper;

import com.qc.dto.ProductionOrderDTO;
import com.qc.dto.ProductionOrderDTOResponse;
import com.qc.entities.ProductionOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductionOrderMapper {

    ProductionOrder toEntity(ProductionOrderDTO dto);

    List<ProductionOrder> toEntityList(List<ProductionOrderDTO> dtoList);

    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "lastModifiedDate", target = "lastModifiedDate")
    ProductionOrderDTOResponse toDto(ProductionOrder entity);

    List<ProductionOrderDTOResponse> toDtoList(List<ProductionOrder> entityList);
}
