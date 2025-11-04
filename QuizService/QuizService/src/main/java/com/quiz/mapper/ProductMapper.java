package com.quiz.mapper;

import com.quiz.dto.ProductDTO;
import com.quiz.dto.ProductDTOResponse;
import com.quiz.entities.Product;
import com.quiz.entities.QualitativeCheck;
import com.quiz.entities.QuantitativeCheck;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
     Product toEntity(ProductDTO dto);


    ProductDTOResponse toDto(Product entity);

    List<Product> toEntityList(List<ProductDTO> dtoList);

    List<ProductDTOResponse> toDtoList(List<Product> entityList);


}
