package com.movindu.pos.module.sale.service.impl;

import com.movindu.pos.common.enums.PaymentMethod;
import com.movindu.pos.common.enums.SaleStatus;
import com.movindu.pos.common.exception.BusinessException;
import com.movindu.pos.common.exception.ResourceNotFoundException;
import com.movindu.pos.module.customer.entity.Customer;
import com.movindu.pos.module.customer.repository.CustomerRepository;
import com.movindu.pos.module.product.entity.Product;
import com.movindu.pos.module.product.repository.ProductRepository;
import com.movindu.pos.module.sale.dto.request.SaleItemRequest;
import com.movindu.pos.module.sale.dto.request.SaleRequest;
import com.movindu.pos.module.sale.dto.response.SaleItemResponse;
import com.movindu.pos.module.sale.dto.response.SaleResponse;
import com.movindu.pos.module.sale.entity.Sale;
import com.movindu.pos.module.sale.entity.SaleItem;
import com.movindu.pos.module.sale.repository.SaleRepository;
import com.movindu.pos.module.sale.service.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleServiceImpl implements SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public SaleResponse createSale(SaleRequest request) {

        Sale sale = new Sale();
        sale.setInvoiceNumber(generateInvoiceNumber());
        sale.setSaleDate(LocalDateTime.now());
        sale.setPaymentMethod(request.getPaymentMethod());
        sale.setNotes(request.getNotes());
        sale.setStatus(SaleStatus.PENDING);
        sale.setDiscountAmount(request.getDiscountAmount() != null ?
                request.getDiscountAmount() : BigDecimal.ZERO);

        // link customer if provided
        if (request.getCustomerId() != null) {
            Customer customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer",
                            request.getCustomerId()));
            sale.setCustomer(customer);
        }

        // process each item
        BigDecimal subtotal = BigDecimal.ZERO;
        for (SaleItemRequest itemRequest : request.getItems()) {

            // find product by id or barcode
            Product product;
            if (itemRequest.getProductId() != null) {
                product = productRepository.findById(itemRequest.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product",
                                itemRequest.getProductId()));
            } else if (itemRequest.getBarcode() != null) {
                product = productRepository.findByBarcode(itemRequest.getBarcode())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Product not found with barcode: " +
                                        itemRequest.getBarcode()));
            } else {
                throw new BusinessException(
                        "Either productId or barcode must be provided");
            }

            // check stock
            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new BusinessException("Insufficient stock for product: "
                        + product.getName() + ". Available: "
                        + product.getStockQuantity());
            }

            // calculate item total
            BigDecimal itemDiscount = itemRequest.getDiscountAmount() != null ?
                    itemRequest.getDiscountAmount() : BigDecimal.ZERO;
            BigDecimal unitPrice = product.getPrice();
            BigDecimal itemTotal = unitPrice
                    .multiply(BigDecimal.valueOf(itemRequest.getQuantity()))
                    .subtract(itemDiscount);

            // create sale item
            SaleItem saleItem = new SaleItem();
            saleItem.setSale(sale);
            saleItem.setProduct(product);
            saleItem.setQuantity(itemRequest.getQuantity());
            saleItem.setUnitPrice(unitPrice);
            saleItem.setDiscountAmount(itemDiscount);
            saleItem.setTotalPrice(itemTotal);

            sale.getItems().add(saleItem);
            subtotal = subtotal.add(itemTotal);

            // reduce stock
            product.setStockQuantity(
                    product.getStockQuantity() - itemRequest.getQuantity());
            productRepository.save(product);
        }

        // calculate totals
        BigDecimal totalDiscount = sale.getDiscountAmount();
        BigDecimal totalAmount = subtotal.subtract(totalDiscount);
        BigDecimal paidAmount = request.getPaidAmount();
        BigDecimal changeAmount = paidAmount.subtract(totalAmount);

        if (changeAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Paid amount is less than total amount");
        }

        sale.setSubtotal(subtotal);
        sale.setTotalAmount(totalAmount);
        sale.setPaidAmount(paidAmount);
        sale.setChangeAmount(changeAmount);

        // handle split payment
        if (request.getPaymentMethod() == PaymentMethod.SPLIT) {
            sale.setCashAmount(request.getCashAmount() != null ?
                    request.getCashAmount() : BigDecimal.ZERO);
            sale.setCardAmount(request.getCardAmount() != null ?
                    request.getCardAmount() : BigDecimal.ZERO);
        }

        // calculate loyalty points (1 point per 100 LKR)
        int loyaltyPoints = totalAmount
                .divide(BigDecimal.valueOf(100), 0,
                        java.math.RoundingMode.FLOOR).intValue();
        sale.setLoyaltyPointsEarned(loyaltyPoints);

        // update customer if linked
        if (sale.getCustomer() != null) {
            Customer customer = sale.getCustomer();
            customer.setLoyaltyPoints(
                    customer.getLoyaltyPoints() + loyaltyPoints);
            customer.setTotalPurchases(
                    customer.getTotalPurchases().add(totalAmount));
            customerRepository.save(customer);
        }

        sale.setStatus(SaleStatus.COMPLETED);
        return mapToResponse(saleRepository.save(sale));
    }

    @Override
    public SaleResponse getSaleById(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", id));
        return mapToResponse(sale);
    }

    @Override
    public SaleResponse getSaleByInvoiceNumber(String invoiceNumber) {
        Sale sale = saleRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sale not found with invoice: " + invoiceNumber));
        return mapToResponse(sale);
    }

    @Override
    public List<SaleResponse> getAllSales() {
        return saleRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SaleResponse> getSalesByCustomer(Long customerId) {
        return saleRepository.findByCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SaleResponse cancelSale(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sale", id));

        if (sale.getStatus() == SaleStatus.CANCELLED) {
            throw new BusinessException("Sale is already cancelled");
        }

        // restore stock
        for (SaleItem item : sale.getItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(
                    product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        // reverse loyalty points
        if (sale.getCustomer() != null) {
            Customer customer = sale.getCustomer();
            customer.setLoyaltyPoints(Math.max(0,
                    customer.getLoyaltyPoints() - sale.getLoyaltyPointsEarned()));
            customer.setTotalPurchases(
                    customer.getTotalPurchases().subtract(sale.getTotalAmount()));
            customerRepository.save(customer);
        }

        sale.setStatus(SaleStatus.CANCELLED);
        return mapToResponse(saleRepository.save(sale));
    }

    private String generateInvoiceNumber() {
        String prefix = "INV-";
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        return prefix + timestamp;
    }

    private SaleResponse mapToResponse(Sale sale) {
        SaleResponse response = new SaleResponse();
        response.setId(sale.getId());
        response.setInvoiceNumber(sale.getInvoiceNumber());
        response.setSaleDate(sale.getSaleDate());
        response.setSubtotal(sale.getSubtotal());
        response.setDiscountAmount(sale.getDiscountAmount());
        response.setTotalAmount(sale.getTotalAmount());
        response.setPaidAmount(sale.getPaidAmount());
        response.setChangeAmount(sale.getChangeAmount());
        response.setPaymentMethod(sale.getPaymentMethod());
        response.setCashAmount(sale.getCashAmount());
        response.setCardAmount(sale.getCardAmount());
        response.setStatus(sale.getStatus());
        response.setNotes(sale.getNotes());
        response.setLoyaltyPointsEarned(sale.getLoyaltyPointsEarned());
        response.setCreatedAt(sale.getCreatedAt());

        if (sale.getCustomer() != null) {
            response.setCustomerId(sale.getCustomer().getId());
            response.setCustomerName(sale.getCustomer().getName());
            response.setCustomerPhone(sale.getCustomer().getPhone());
        }

        List<SaleItemResponse> itemResponses = sale.getItems()
                .stream()
                .map(this::mapItemToResponse)
                .collect(Collectors.toList());
        response.setItems(itemResponses);

        return response;
    }

    private SaleItemResponse mapItemToResponse(SaleItem item) {
        SaleItemResponse response = new SaleItemResponse();
        response.setId(item.getId());
        response.setQuantity(item.getQuantity());
        response.setUnitPrice(item.getUnitPrice());
        response.setDiscountAmount(item.getDiscountAmount());
        response.setTotalPrice(item.getTotalPrice());

        if (item.getProduct() != null) {
            response.setProductId(item.getProduct().getId());
            response.setProductName(item.getProduct().getName());
            response.setProductSku(item.getProduct().getSku());
            response.setBarcode(item.getProduct().getBarcode());
        }

        return response;
    }
}