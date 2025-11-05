package com.qc.mapper;

import com.qc.dto.ProductDTO;
import com.qc.dto.ProductDTOResponse;
import com.qc.entities.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
     Product toEntity(ProductDTO dto);


    ProductDTOResponse toDto(Product entity);

    List<Product> toEntityList(List<ProductDTO> dtoList);

    List<ProductDTOResponse> toDtoList(List<Product> entityList);


}
