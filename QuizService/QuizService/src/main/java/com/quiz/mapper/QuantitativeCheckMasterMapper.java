package com.quiz.mapper;

import com.quiz.dto.QuantitativeCheckMasterDTO;
import com.quiz.dto.QuantitativeCheckMasterDTOResponse;
import com.quiz.entities.LabelScanMaster;
import com.quiz.entities.QuantitativeCheckMaster;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuantitativeCheckMasterMapper {
    @Mapping(target = "scanMaster", expression = "java(mapScanMaster(dto.getScanMasterId()))")
    QuantitativeCheckMaster toEntity(QuantitativeCheckMasterDTO dto);

    @Mapping(target = "scanMasterId", source = "scanMaster.id")
    @Mapping(target = "scanMasterName", source = "scanMaster.name")
    QuantitativeCheckMasterDTOResponse toResponse(QuantitativeCheckMaster entity);

    List<QuantitativeCheckMaster> toEntityList(List<QuantitativeCheckMasterDTO> dtoList);

    List<QuantitativeCheckMasterDTOResponse> toResponseList(List<QuantitativeCheckMaster> entityList);

    default LabelScanMaster mapScanMaster(Long id) {
        if (id == null) return null;
        LabelScanMaster scanMaster = new LabelScanMaster();
        scanMaster.setId(id);
        return scanMaster;
    }
}
