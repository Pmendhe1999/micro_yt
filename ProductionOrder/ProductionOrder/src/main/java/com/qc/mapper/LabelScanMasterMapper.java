package com.qc.mapper;

import com.qc.dto.LabelScanMasterDTO;
import com.qc.dto.LabelScanMasterDTOResponse;
import com.qc.entities.LabelScanMaster;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LabelScanMasterMapper {

    LabelScanMaster labelScanMasterDTOToLabelScanMaster(LabelScanMasterDTO dto);

    LabelScanMasterDTOResponse labelScanMasterToLabelScanMasterDTOResponse(LabelScanMaster entity);

    List<LabelScanMaster> labelScanMasterDTOListToLabelScanMasterList(List<LabelScanMasterDTO> dtoList);

    List<LabelScanMasterDTOResponse> labelScanMasterListToLabelScanMasterDTOResponseList(List<LabelScanMaster> entities);
}
