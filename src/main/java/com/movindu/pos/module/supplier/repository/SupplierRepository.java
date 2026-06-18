package com.movindu.pos.module.supplier.repository;

import com.movindu.pos.module.supplier.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    boolean existsByEmail(String email);

    Optional<Supplier> findByEmail(String email);

    List<Supplier> findByIsActive(Boolean isActive);

    List<Supplier> findByNameContainingIgnoreCase(String name);
}