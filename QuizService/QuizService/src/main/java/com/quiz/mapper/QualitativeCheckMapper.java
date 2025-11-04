package com.quiz.mapper;

import com.quiz.dto.QualitativeCheckDTO;
import com.quiz.dto.QualitativeCheckDTOResponse;
import com.quiz.entities.QualitativeCheck;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
@Mapper(componentModel = "spring")
public interface QualitativeCheckMapper {


    @Mapping(source = "qualitativeCheckMasterId", target = "qualitativeCheckMaster.id")
    @Mapping(source = "productId", target = "product.id") // ✅ new mapping
    QualitativeCheck qualitativeCheckDTOToQualitativeCheck(QualitativeCheckDTO dto);

    @Mapping(source = "qualitativeCheckMaster.id", target = "qualitativeCheckMasterId")
    @Mapping(source = "qualitativeCheckMaster.name", target = "qualitativeCheckMasterName")
    @Mapping(source = "product.id", target = "productId") // ✅ new mapping
    @Mapping(source = "product.name", target = "productName") // ✅ new mapping
    QualitativeCheckDTOResponse qualitativeCheckToQualitativeCheckDTOResponse(QualitativeCheck entity);

    List<QualitativeCheck> qualitativeCheckDTOListToQualitativeCheckList(List<QualitativeCheckDTO> dtoList);
    List<QualitativeCheckDTOResponse> qualitativeCheckListToQualitativeCheckDTOResponseList(List<QualitativeCheck> entityList);
}
