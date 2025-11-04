package com.quiz.mapper;

import com.quiz.dto.QuantitativeCheckDTO;
import com.quiz.dto.QuantitativeCheckDTOResponse;
import com.quiz.entities.QuantitativeCheck;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
@Mapper(componentModel = "spring")
public interface QuantitativeCheckMapper {
    @Mapping(source = "quantitativeCheckMasterId", target = "quantitativeCheckMaster.id")
    @Mapping(source = "productId", target = "product.id")
    QuantitativeCheck quantitativeCheckDTOToQuantitativeCheck(QuantitativeCheckDTO dto);

    @Mapping(source = "quantitativeCheckMaster.id", target = "quantitativeCheckMasterId")
    @Mapping(source = "quantitativeCheckMaster.name", target = "quantitativeCheckMasterName")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    QuantitativeCheckDTOResponse quantitativeCheckToQuantitativeCheckDTOResponse(QuantitativeCheck entity);

    List<QuantitativeCheck> quantitativeCheckDTOListToQuantitativeCheckList(List<QuantitativeCheckDTO> dtoList);

    List<QuantitativeCheckDTOResponse> quantitativeCheckListToQuantitativeCheckDTOResponseList(List<QuantitativeCheck> entities);
}
