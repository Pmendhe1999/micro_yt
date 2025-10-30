package com.quiz.mapper;

import com.quiz.dto.ProductMasterDTO;
import com.quiz.dto.ProductMasterDTOResponse;
import com.quiz.entities.ProductMaster;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring")
public interface ProductMasterMapper {

    ProductMasterDTOResponse productMasterToProductMasterDTOResponse(ProductMaster productMaster);

    List<ProductMasterDTOResponse> productMasterListToProductMasterDTOListResponse(List<ProductMaster> productMasterList);

    //ProductMaster productMasterResponseToProductMaster(ProductMasterDTOResponse productMasterResponse);

    //List<ProductMasterDTOResponse> taskListToTaskResponseList(List<ProductMaster> productMasterList);

    ProductMaster productMasterDTOToProductMaster(ProductMasterDTO productMasterDTO);

    List<ProductMaster> productMasterDTOListToProductMasterList(List<ProductMasterDTO> productMasterDTOList);
}
