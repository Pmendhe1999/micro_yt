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
    QuantitativeCheck quantitativeCheckDTOToQuantitativeCheck(QuantitativeCheckDTO dto);

    @Mapping(source = "quantitativeCheckMaster.id", target = "quantitativeCheckMasterId")
    @Mapping(source = "quantitativeCheckMaster.name", target = "quantitativeCheckMasterName")
    QuantitativeCheckDTOResponse quantitativeCheckToQuantitativeCheckDTOResponse(QuantitativeCheck entity);

    List<QuantitativeCheck> quantitativeCheckDTOListToQuantitativeCheckList(List<QuantitativeCheckDTO> dtoList);

    List<QuantitativeCheckDTOResponse> quantitativeCheckListToQuantitativeCheckDTOResponseList(List<QuantitativeCheck> entities);
}
