package com.movindu.pos.module.stockreceipt.service.impl;

import com.movindu.pos.module.stockreceipt.service.StockReceiptService;
import com.movindu.pos.common.enums.StockReceiptStatus;
import com.movindu.pos.common.exception.BusinessException;
import com.movindu.pos.common.exception.ResourceNotFoundException;
import com.movindu.pos.module.product.entity.Product;
import com.movindu.pos.module.product.repository.ProductRepository;
import com.movindu.pos.module.stockreceipt.dto.request.StockReceiptItemRequest;
import com.movindu.pos.module.stockreceipt.dto.request.StockReceiptRequest;
import com.movindu.pos.module.stockreceipt.dto.response.StockReceiptItemResponse;
import com.movindu.pos.module.stockreceipt.dto.response.StockReceiptResponse;
import com.movindu.pos.module.stockreceipt.entity.StockReceipt;
import com.movindu.pos.module.stockreceipt.entity.StockReceiptItem;
import com.movindu.pos.module.stockreceipt.repository.StockReceiptRepository;
import com.movindu.pos.module.supplier.entity.Supplier;
import com.movindu.pos.module.supplier.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockReceiptServiceImpl implements StockReceiptService {

    private final StockReceiptRepository stockReceiptRepository;
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;

    @Override
    @Transactional
    public StockReceiptResponse createReceipt(StockReceiptRequest request) {
        StockReceipt receipt = new StockReceipt();
        receipt.setReceiptNumber(generateReceiptNumber());
        receipt.setReceivedDate(request.getReceivedDate());
        receipt.setNotes(request.getNotes());
        receipt.setStatus(StockReceiptStatus.DRAFT);

        if (request.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(request.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier",
                            request.getSupplierId()));
            receipt.setSupplier(supplier);
        }

        for (StockReceiptItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product",
                            itemRequest.getProductId()));

            StockReceiptItem item = new StockReceiptItem();
            item.setStockReceipt(receipt);
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            item.setCostPrice(itemRequest.getCostPrice());
            item.setSellingPrice(itemRequest.getSellingPrice());
            item.setBatchNumber(itemRequest.getBatchNumber());
            item.setExpiryDate(itemRequest.getExpiryDate());

            receipt.getItems().add(item);
        }

        return mapToResponse(stockReceiptRepository.save(receipt));
    }

    @Override
    public StockReceiptResponse getReceiptById(Long id) {
        StockReceipt receipt = stockReceiptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StockReceipt", id));
        return mapToResponse(receipt);
    }

    @Override
    public List<StockReceiptResponse> getAllReceipts() {
        return stockReceiptRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StockReceiptResponse confirmReceipt(Long id) {
        StockReceipt receipt = stockReceiptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StockReceipt", id));

        if (receipt.getStatus() != StockReceiptStatus.DRAFT) {
            throw new BusinessException("Only DRAFT receipts can be confirmed");
        }

        for (StockReceiptItem item : receipt.getItems()) {
            Product product = item.getProduct();

            // update stock quantity
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());

            // update selling price if owner wants to change it
            product.setPrice(item.getSellingPrice());

            productRepository.save(product);
        }

        receipt.setStatus(StockReceiptStatus.CONFIRMED);
        return mapToResponse(stockReceiptRepository.save(receipt));
    }

    @Override
    @Transactional
    public StockReceiptResponse cancelReceipt(Long id) {
        StockReceipt receipt = stockReceiptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StockReceipt", id));

        if (receipt.getStatus() == StockReceiptStatus.CONFIRMED) {
            throw new BusinessException("Confirmed receipts cannot be cancelled");
        }

        receipt.setStatus(StockReceiptStatus.CANCELLED);
        return mapToResponse(stockReceiptRepository.save(receipt));
    }

    @Override
    public List<StockReceiptResponse> getReceiptsBySupplier(Long supplierId) {
        return stockReceiptRepository.findBySupplierId(supplierId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private String generateReceiptNumber() {
        String prefix = "GRN-";
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        return prefix + timestamp;
    }

    private StockReceiptResponse mapToResponse(StockReceipt receipt) {
        StockReceiptResponse response = new StockReceiptResponse();
        response.setId(receipt.getId());
        response.setReceiptNumber(receipt.getReceiptNumber());
        response.setReceivedDate(receipt.getReceivedDate());
        response.setStatus(receipt.getStatus());
        response.setNotes(receipt.getNotes());
        response.setCreatedAt(receipt.getCreatedAt());

        if (receipt.getSupplier() != null) {
            response.setSupplierId(receipt.getSupplier().getId());
            response.setSupplierName(receipt.getSupplier().getName());
        }

        List<StockReceiptItemResponse> itemResponses = receipt.getItems()
                .stream()
                .map(this::mapItemToResponse)
                .collect(Collectors.toList());
        response.setItems(itemResponses);

        return response;
    }

    private StockReceiptItemResponse mapItemToResponse(StockReceiptItem item) {
        StockReceiptItemResponse response = new StockReceiptItemResponse();
        response.setId(item.getId());
        response.setQuantity(item.getQuantity());
        response.setCostPrice(item.getCostPrice());
        response.setSellingPrice(item.getSellingPrice());
        response.setBatchNumber(item.getBatchNumber());
        response.setExpiryDate(item.getExpiryDate());

        if (item.getProduct() != null) {
            response.setProductId(item.getProduct().getId());
            response.setProductName(item.getProduct().getName());
            response.setProductSku(item.getProduct().getSku());
        }

        return response;
    }
}