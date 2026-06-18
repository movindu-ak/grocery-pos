package com.movindu.pos.module.customer.service;

import com.movindu.pos.module.customer.dto.request.CustomerRequest;
import com.movindu.pos.module.customer.dto.response.CustomerResponse;

import java.util.List;

public interface CustomerService {

    CustomerResponse createCustomer(CustomerRequest request);

    CustomerResponse getCustomerById(Long id);

    CustomerResponse getCustomerByPhone(String phone);

    List<CustomerResponse> getAllCustomers();

    CustomerResponse updateCustomer(Long id, CustomerRequest request);

    void deleteCustomer(Long id);

    List<CustomerResponse> searchCustomers(String name);

    List<CustomerResponse> getActiveCustomers();
}