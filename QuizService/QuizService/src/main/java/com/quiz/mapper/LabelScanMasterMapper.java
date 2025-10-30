package com.quiz.mapper;

import com.quiz.dto.LabelScanMasterDTO;
import com.quiz.dto.LabelScanMasterDTOResponse;
import com.quiz.entities.LabelScanMaster;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LabelScanMasterMapper {

    LabelScanMaster labelScanMasterDTOToLabelScanMaster(LabelScanMasterDTO dto);

    LabelScanMasterDTOResponse labelScanMasterToLabelScanMasterDTOResponse(LabelScanMaster entity);

    List<LabelScanMaster> labelScanMasterDTOListToLabelScanMasterList(List<LabelScanMasterDTO> dtoList);

    List<LabelScanMasterDTOResponse> labelScanMasterListToLabelScanMasterDTOResponseList(List<LabelScanMaster> entities);
}
