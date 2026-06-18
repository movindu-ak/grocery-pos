package com.movindu.pos.module.product.service;

import com.movindu.pos.module.product.dto.request.ProductRequest;
import com.movindu.pos.module.product.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);
    ProductResponse getProductById(Long id);
    List<ProductResponse> getAllProducts();
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
    List<ProductResponse> searchProducts(String name);
    List<ProductResponse> getActiveProducts();
    List<ProductResponse> getProductsByCategory(Long categoryId);
}