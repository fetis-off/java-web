package com.university.cosmocats.service;

import com.university.cosmocats.entity.CustomerEntity;
import com.university.cosmocats.exception.CustomerNotFoundException;
import com.university.cosmocats.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public CustomerEntity findCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer with id: " + id + "was not found"));
    }
}
