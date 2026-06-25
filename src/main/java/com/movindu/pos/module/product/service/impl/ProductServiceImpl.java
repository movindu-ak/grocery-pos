package com.movindu.pos.module.product.service.impl;

import com.movindu.pos.common.enums.DiscountType;
import com.movindu.pos.common.enums.ProductStatus;
import com.movindu.pos.common.exception.BusinessException;
import com.movindu.pos.common.exception.ResourceNotFoundException;
import com.movindu.pos.module.category.entity.Category;
import com.movindu.pos.module.category.repository.CategoryRepository;
import com.movindu.pos.module.product.dto.request.ProductRequest;
import com.movindu.pos.module.product.dto.response.ProductResponse;
import com.movindu.pos.module.product.entity.Product;
import com.movindu.pos.module.product.repository.ProductRepository;
import com.movindu.pos.module.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        if (productRepository.existsBySku(request.getSku())) {
            throw new BusinessException(
                    "SKU already exists: " + request.getSku());
        }
        if (request.getBarcode() != null &&
                productRepository.existsByBarcode(request.getBarcode())) {
            throw new BusinessException(
                    "Barcode already exists: " + request.getBarcode());
        }

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity() != null ?
                request.getStockQuantity() : 0);
        product.setSku(request.getSku());
        product.setBarcode(request.getBarcode());
        product.setGoodThreshold(request.getGoodThreshold() != null ?
                request.getGoodThreshold() : 50);
        product.setAverageThreshold(request.getAverageThreshold() != null ?
                request.getAverageThreshold() : 20);
        product.setDiscountType(request.getDiscountType() != null ?
                request.getDiscountType() : DiscountType.NONE);
        product.setDiscountValue(request.getDiscountValue() != null ?
                request.getDiscountValue() : BigDecimal.ZERO);
        product.setStatus(request.getStatus() != null ?
                request.getStatus() : ProductStatus.ACTIVE);

        if (request.getCategoryId() != null) {
            Category category = categoryRepository
                    .findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Category", request.getCategoryId()));
            product.setCategory(category);
        }

        return mapToResponse(productRepository.save(product));
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        return mapToResponse(product);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));

        // only update editable fields
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setBarcode(request.getBarcode());
        product.setGoodThreshold(request.getGoodThreshold() != null ?
                request.getGoodThreshold() : product.getGoodThreshold());
        product.setAverageThreshold(request.getAverageThreshold() != null ?
                request.getAverageThreshold() : product.getAverageThreshold());
        product.setDiscountType(request.getDiscountType() != null ?
                request.getDiscountType() : product.getDiscountType());
        product.setDiscountValue(request.getDiscountValue() != null ?
                request.getDiscountValue() : product.getDiscountValue());
        product.setStatus(request.getStatus() != null ?
                request.getStatus() : product.getStatus());

        // SKU and category are NOT updated — locked after creation

        return mapToResponse(productRepository.save(product));
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        product.setStatus(ProductStatus.INACTIVE);
        product.setIsDeleted(true);
        productRepository.save(product);
    }

    @Override
    public List<ProductResponse> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getActiveProducts() {
        return productRepository.findByStatus(ProductStatus.ACTIVE)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ── Calculate effective price after discount ──
    private BigDecimal calculateEffectivePrice(Product product) {
        if (product.getDiscountType() == DiscountType.NONE ||
                product.getDiscountValue().compareTo(BigDecimal.ZERO) == 0) {
            return product.getPrice();
        }
        if (product.getDiscountType() == DiscountType.PERCENTAGE) {
            BigDecimal discountAmount = product.getPrice()
                    .multiply(product.getDiscountValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            return product.getPrice().subtract(discountAmount);
        }
        // FIXED
        BigDecimal effective = product.getPrice()
                .subtract(product.getDiscountValue());
        return effective.compareTo(BigDecimal.ZERO) < 0
                ? BigDecimal.ZERO : effective;
    }

    // ── Calculate stock level ──
    private String calculateStockLevel(Product product) {
        int stock = product.getStockQuantity();
        if (stock <= 0) return "BAD";
        if (stock <= product.getAverageThreshold()) return "BAD";
        if (stock <= product.getGoodThreshold()) return "AVERAGE";
        return "GOOD";
    }

    private ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setSku(product.getSku());
        response.setBarcode(product.getBarcode());
        response.setGoodThreshold(product.getGoodThreshold());
        response.setAverageThreshold(product.getAverageThreshold());
        response.setStockLevel(calculateStockLevel(product));
        response.setDiscountType(product.getDiscountType());
        response.setDiscountValue(product.getDiscountValue());
        response.setEffectivePrice(calculateEffectivePrice(product));
        response.setStatus(product.getStatus());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        if (product.getCategory() != null) {
            response.setCategoryId(product.getCategory().getId());
            response.setCategoryName(product.getCategory().getName());
        }

        return response;
    }
}