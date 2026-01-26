package com.university.cosmocats.mapper;

import com.university.cosmocats.dto.customer.CustomerDto;
import com.university.cosmocats.entity.CustomerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerDto toCustomerDto(CustomerEntity customerEntity);
}
