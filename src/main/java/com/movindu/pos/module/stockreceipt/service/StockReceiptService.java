package com.movindu.pos.module.stockreceipt.service;

import com.movindu.pos.module.stockreceipt.service.StockReceiptService;
import com.movindu.pos.module.stockreceipt.dto.request.StockReceiptRequest;
import com.movindu.pos.module.stockreceipt.dto.response.StockReceiptResponse;

import java.util.List;

public interface StockReceiptService {

    StockReceiptResponse createReceipt(StockReceiptRequest request);

    StockReceiptResponse getReceiptById(Long id);

    List<StockReceiptResponse> getAllReceipts();

    StockReceiptResponse confirmReceipt(Long id);

    StockReceiptResponse cancelReceipt(Long id);

    List<StockReceiptResponse> getReceiptsBySupplier(Long supplierId);
}