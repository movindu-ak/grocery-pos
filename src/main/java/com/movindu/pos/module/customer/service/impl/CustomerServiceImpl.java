package com.movindu.pos.module.customer.service.impl;

import com.movindu.pos.common.exception.BusinessException;
import com.movindu.pos.common.exception.ResourceNotFoundException;
import com.movindu.pos.module.customer.dto.request.CustomerRequest;
import com.movindu.pos.module.customer.dto.response.CustomerResponse;
import com.movindu.pos.module.customer.entity.Customer;
import com.movindu.pos.module.customer.repository.CustomerRepository;
import com.movindu.pos.module.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {
        if (request.getPhone() != null &&
                customerRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException("Customer with this phone already exists");
        }
        if (request.getEmail() != null &&
                customerRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Customer with this email already exists");
        }

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        customer.setCreditLimit(request.getCreditLimit() != null ?
                request.getCreditLimit() : BigDecimal.ZERO);
        customer.setLoyaltyPoints(0);
        customer.setOutstandingBalance(BigDecimal.ZERO);
        customer.setTotalPurchases(BigDecimal.ZERO);
        customer.setIsActive(true);

        return mapToResponse(customerRepository.save(customer));
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
        return mapToResponse(customer);
    }

    @Override
    public CustomerResponse getCustomerByPhone(String phone) {
        Customer customer = customerRepository.findByPhone(phone)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer not found with phone: " + phone));
        return mapToResponse(customer);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));

        if (request.getPhone() != null &&
                !request.getPhone().equals(customer.getPhone()) &&
                customerRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException("Customer with this phone already exists");
        }

        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());
        customer.setAddress(request.getAddress());
        if (request.getCreditLimit() != null) {
            customer.setCreditLimit(request.getCreditLimit());
        }

        return mapToResponse(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
        customerRepository.delete(customer);
    }

    @Override
    public List<CustomerResponse> searchCustomers(String name) {
        return customerRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerResponse> getActiveCustomers() {
        return customerRepository.findByIsActive(true)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CustomerResponse mapToResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setPhone(customer.getPhone());
        response.setEmail(customer.getEmail());
        response.setAddress(customer.getAddress());
        response.setLoyaltyPoints(customer.getLoyaltyPoints());
        response.setCreditLimit(customer.getCreditLimit());
        response.setOutstandingBalance(customer.getOutstandingBalance());
        response.setTotalPurchases(customer.getTotalPurchases());
        response.setIsActive(customer.getIsActive());
        response.setCreatedAt(customer.getCreatedAt());
        response.setUpdatedAt(customer.getUpdatedAt());
        return response;
    }
}