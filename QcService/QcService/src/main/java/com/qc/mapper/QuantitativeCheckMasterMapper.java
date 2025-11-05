package com.qc.mapper;

import com.qc.dto.QuantitativeCheckMasterDTO;
import com.qc.dto.QuantitativeCheckMasterDTOResponse;
import com.qc.entities.LabelScanMaster;
import com.qc.entities.QuantitativeCheckMaster;
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
