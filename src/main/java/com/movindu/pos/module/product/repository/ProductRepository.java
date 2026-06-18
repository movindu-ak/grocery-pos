package com.movindu.pos.module.product.repository;

import com.movindu.pos.common.enums.ProductStatus;
import com.movindu.pos.module.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStatus(ProductStatus status);

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    Optional<Product> findByBarcode(String barcode);

    boolean existsByBarcode(String barcode);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByStockQuantityLessThanEqualAndStatus(Integer stock, ProductStatus status);

    Long countByStatus(ProductStatus status);
}