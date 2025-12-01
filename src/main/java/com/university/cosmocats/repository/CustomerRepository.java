package com.university.cosmocats.repository;

import com.university.cosmocats.entity.CustomerEntity;

import java.util.UUID;

public interface CustomerRepository extends NaturalIdRepository<CustomerEntity, UUID> {
}
