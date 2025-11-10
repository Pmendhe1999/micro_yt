package com.qc.mapper;

import com.qc.dto.MediaDTOResponse;
import com.qc.dto.MediaDetailsDTOResponse;
import com.qc.dto.ProductMasterDTO;
import com.qc.dto.ProductMasterDTOResponse;
import com.qc.entities.Media;
import com.qc.entities.MediaDetails;
import com.qc.entities.ProductMaster;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
@Mapper(componentModel = "spring")
public interface ProductMasterMapper {

    // ---------- ENTITY → DTO ----------
    @Mapping(target = "mediaDetailsList", source = "mediaDetailsList")
    ProductMasterDTOResponse productMasterToProductMasterDTOResponse(ProductMaster productMaster);

    List<ProductMasterDTOResponse> productMasterListToProductMasterDTOListResponse(List<ProductMaster> productMasterList);

    @Mapping(target = "media", source = "media")
    MediaDetailsDTOResponse mediaDetailsToMediaDetailsDTOResponse(MediaDetails mediaDetails);

    MediaDTOResponse mediaToMediaDTOResponse(Media media);

    // ---------- DTO → ENTITY ----------
    ProductMaster productMasterDTOToProductMaster(ProductMasterDTO dto);

    List<ProductMaster> productMasterDTOListToProductMasterList(List<ProductMasterDTO> dtoList);
}
