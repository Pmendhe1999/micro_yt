package com.qc.mapper;

import com.qc.dto.ProductDTO;
import com.qc.dto.ProductDTOResponse;
import com.qc.entities.DeliveryChallan;
import com.qc.entities.DeliveryItemsMaster;
import com.qc.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    // ---------- ENTITY MAPPING ----------
    @Mapping(target = "deliveryChallan", source = "deliveryChallanId", qualifiedByName = "mapToDeliveryChallan")
    @Mapping(target = "deliveryItem", source = "deliveryItemId", qualifiedByName = "mapToDeliveryItem")
    Product toEntity(ProductDTO dto);

    // ---------- RESPONSE MAPPING ----------
    @Mapping(target = "deliveryChallanId", source = "deliveryChallan.id")
    @Mapping(target = "deliveryChallanName", source = "deliveryChallan.name") // or challanNo if applicable
    @Mapping(target = "deliveryItemId", source = "deliveryItem.id")
    @Mapping(target = "deliveryItemName", source = "deliveryItem.name")
    ProductDTOResponse toDto(Product entity);

    // ---------- LIST MAPPINGS ----------
    List<Product> toEntityList(List<ProductDTO> dtoList);
    List<ProductDTOResponse> toDtoList(List<Product> entityList);

    // ---------- HELPERS ----------
    @Named("mapToDeliveryChallan")
    default DeliveryChallan mapToDeliveryChallan(Long challanId) {
        if (challanId == null) return null;
        DeliveryChallan challan = new DeliveryChallan();
        challan.setId(challanId);
        return challan;
    }

    @Named("mapToDeliveryItem")
    default DeliveryItemsMaster mapToDeliveryItem(Long itemId) {
        if (itemId == null) return null;
        DeliveryItemsMaster item = new DeliveryItemsMaster();
        item.setId(itemId);
        return item;
    }
}
