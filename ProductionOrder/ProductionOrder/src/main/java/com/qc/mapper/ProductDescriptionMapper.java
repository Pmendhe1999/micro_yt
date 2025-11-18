package com.qc.mapper;

import com.qc.dto.ProductDescriptionDTO;
import com.qc.dto.ProductDescriptionDTOResponse;
import com.qc.entities.ProductDescription;
import com.qc.entities.ProductMaster;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
@Mapper(componentModel = "spring")
public interface ProductDescriptionMapper {
    @Mapping(target = "product", source = "productId", qualifiedByName = "mapToProductMaster")
    ProductDescription toEntity(ProductDescriptionDTO dto);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    ProductDescriptionDTOResponse toDto(ProductDescription entity);

    List<ProductDescription> toEntityList(List<ProductDescriptionDTO> dtoList);

    List<ProductDescriptionDTOResponse> toDtoList(List<ProductDescription> entityList);

    @Named("mapToProductMaster")
    default ProductMaster mapToProductMaster(Long productId) {
        if (productId == null) return null;
        ProductMaster product = new ProductMaster();
        product.setId(productId);
        return product;
    }
}
