package com.university.cosmocats.mapper;

import com.university.cosmocats.dto.orderitem.OrderItemResponseDto;
import com.university.cosmocats.entity.OrderItemEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface OrderItemMapper {
    OrderItemResponseDto toOrderItemResponseDto(OrderItemEntity orderItemEntity);
}
