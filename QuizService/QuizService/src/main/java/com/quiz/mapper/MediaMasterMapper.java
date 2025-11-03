package com.quiz.mapper;

import com.quiz.dto.MediaMasterDTO;
import com.quiz.dto.MediaMasterDTOResponse;
import com.quiz.entities.MediaMaster;
import com.quiz.entities.ProductMaster;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MediaMasterMapper {

    @Mapping(target = "product", source = "productId", qualifiedByName = "mapToProductMaster")
    MediaMaster toEntity(MediaMasterDTO dto);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    MediaMasterDTOResponse toDto(MediaMaster entity);

    List<MediaMaster> toEntityList(List<MediaMasterDTO> dtoList);

    List<MediaMasterDTOResponse> toDtoList(List<MediaMaster> entityList);

    @Named("mapToProductMaster")
    default ProductMaster mapToProductMaster(Long productId) {
        if (productId == null) return null;
        ProductMaster product = new ProductMaster();
        product.setId(productId);
        return product;
    }
}
