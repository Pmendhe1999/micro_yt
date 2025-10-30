package com.quiz.mapper;

import com.quiz.dto.QualitativeCheckMasterDTO;
import com.quiz.dto.QualitativeCheckMasterDTOResponse;
import com.quiz.entities.LabelScanMaster;
import com.quiz.entities.QualitativeCheckMaster;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
@Mapper(componentModel = "spring")
public interface QualitativeCheckMasterMapper {

    @Mappings({
            @Mapping(target = "scanMaster", expression = "java(mapScanMaster(dto.getScanMasterId()))")
    })
    QualitativeCheckMaster qualitativeCheckMasterDTOToEntity(QualitativeCheckMasterDTO dto);

    @Mappings({
            @Mapping(target = "scanMasterId", source = "scanMaster.id"),
            @Mapping(target = "scanMasterName", source = "scanMaster.name")
    })
    QualitativeCheckMasterDTOResponse entityToQualitativeCheckMasterDTOResponse(QualitativeCheckMaster entity);

    List<QualitativeCheckMaster> qualitativeCheckMasterDTOListToEntityList(List<QualitativeCheckMasterDTO> dtoList);

    List<QualitativeCheckMasterDTOResponse> entityListToQualitativeCheckMasterDTOResponseList(List<QualitativeCheckMaster> entityList);

    default LabelScanMaster mapScanMaster(Long id) {
        if (id == null) return null;
        LabelScanMaster scanMaster = new LabelScanMaster();
        scanMaster.setId(id);
        return scanMaster;
    }
}
