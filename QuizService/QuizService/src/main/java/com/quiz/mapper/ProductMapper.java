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
    @Mapping(target = "qualitativeCheck", source = "qualitativeCheckId", qualifiedByName = "mapToQualitativeCheck")
    @Mapping(target = "quantitativeCheck", source = "quantitativeCheckId", qualifiedByName = "mapToQuantitativeCheck")
    Product toEntity(ProductDTO dto);

    @Mapping(target = "qualitativeCheckId", source = "qualitativeCheck.id")
    @Mapping(target = "qualitativeCheckName", source = "qualitativeCheck.description")
    @Mapping(target = "quantitativeCheckId", source = "quantitativeCheck.id")
    @Mapping(target = "quantitativeCheckName", source = "quantitativeCheck.description")
    ProductDTOResponse toDto(Product entity);

    List<Product> toEntityList(List<ProductDTO> dtoList);

    List<ProductDTOResponse> toDtoList(List<Product> entityList);

    @Named("mapToQualitativeCheck")
    default QualitativeCheck mapToQualitativeCheck(Long id) {
        if (id == null) return null;
        QualitativeCheck qc = new QualitativeCheck();
        qc.setId(id);
        return qc;
    }

    @Named("mapToQuantitativeCheck")
    default QuantitativeCheck mapToQuantitativeCheck(Long id) {
        if (id == null) return null;
        QuantitativeCheck qc = new QuantitativeCheck();
        qc.setId(id);
        return qc;
    }
}
