package com.university.cosmocats.mapper;

import com.university.cosmocats.dto.order.OrderResponseDto;
import com.university.cosmocats.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, CustomerMapper.class})
public interface OrderMapper {

    @Mapping(target = "customerDetails", source = "customer")
    OrderResponseDto toOrderResponseDto(OrderEntity orderEntity);
}
