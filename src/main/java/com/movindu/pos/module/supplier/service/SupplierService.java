package com.movindu.pos.module.supplier.service;

import com.movindu.pos.module.supplier.dto.request.SupplierRequest;
import com.movindu.pos.module.supplier.dto.response.SupplierResponse;

import java.util.List;

public interface SupplierService {

    SupplierResponse createSupplier(SupplierRequest request);

    SupplierResponse getSupplierById(Long id);

    List<SupplierResponse> getAllSuppliers();

    SupplierResponse updateSupplier(Long id, SupplierRequest request);

    void deleteSupplier(Long id);

    List<SupplierResponse> getActiveSuppliers();

    List<SupplierResponse> searchSuppliers(String name);
}