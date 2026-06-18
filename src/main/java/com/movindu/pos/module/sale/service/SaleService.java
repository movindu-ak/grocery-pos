package com.movindu.pos.module.sale.service;

import com.movindu.pos.module.sale.dto.request.SaleRequest;
import com.movindu.pos.module.sale.dto.response.SaleResponse;

import java.util.List;

public interface SaleService {

    SaleResponse createSale(SaleRequest request);

    SaleResponse getSaleById(Long id);

    SaleResponse getSaleByInvoiceNumber(String invoiceNumber);

    List<SaleResponse> getAllSales();

    List<SaleResponse> getSalesByCustomer(Long customerId);

    SaleResponse cancelSale(Long id);
}