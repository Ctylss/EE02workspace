package com.example.service.impl;

import com.example.dao.ProductRepository; // 引入新的 Repository 介面
import com.example.exception.InvalidProductDataException;
import com.example.exception.ProductNotFoundException;
import com.example.exception.ProductReferencedException;
import com.example.model.Product;
import com.example.service.ProductService; // 引入 ProductService 介面

import java.math.BigDecimal; // 用於檢查數值型別
import java.util.List;
import java.util.Optional; // 用於處理 Optional 返回值
import java.util.HashMap; // 用於 InvalidProductDataException 的 fieldErrors
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException; // 引入 Spring 的數據完整性異常
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 引入 Spring 事務註解

/**
 * ProductService 介面的實現類，基於 Spring Boot 和 Spring Data JPA。
 * 負責產品相關的業務邏輯處理，利用聲明式事務和自定義業務異常。
 */
@Service // 標識這是一個 Spring 管理的服務組件
public class ProductServiceImpl implements ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository; // 依賴新的 ProductRepository

    // 透過建構子注入 ProductRepository 實例 (Spring 推薦的注入方式)
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @Transactional // 啟用事務管理：方法開始時啟動事務，方法結束時提交或回滾
    public Product saveProduct(Product product) { // 方法簽名已調整
        if (product == null) {
            throw new InvalidProductDataException("產品物件不能為空。");
        }
        LOGGER.info("Service: Attempting to save product (ID: {}), product name: {}", product.getProductId(), product.getProductName());

        // 1. Business logic/parameter validation (using InvalidProductDataException)
        Map<String, String> fieldErrors = new HashMap<>();
        if (product.getProductCode() == null || product.getProductCode().trim().isEmpty()) {
            fieldErrors.put("productCode", "產品代碼為必填項。");
        }
        if (product.getProductName() == null || product.getProductName().trim().isEmpty()) {
            fieldErrors.put("productName", "產品名稱為必填項。");
        }
        // Assuming BigDecimal type for sellingPrice and cost fields in Product class
        if (product.getSellingPrice() == null || product.getSellingPrice().compareTo(BigDecimal.ZERO) < 0) {
            fieldErrors.put("sellingPrice", "銷售價格不能為空且必須大於或等於零。");
        }
        if (product.getCost() == null || product.getCost().compareTo(BigDecimal.ZERO) < 0) {
            fieldErrors.put("cost", "成本不能為空且必須大於或等於零。");
        }

        // Check product code uniqueness (business validation)
        if (product.getProductCode() != null && !product.getProductCode().trim().isEmpty()) {
            Optional<Product> existingProductByCode = productRepository.findByProductCode(product.getProductCode());
            // If the product code already exists, and it's not the product being updated (if it has an ID)
            if (existingProductByCode.isPresent() &&
                (product.getProductId() == null || !existingProductByCode.get().getProductId().equals(product.getProductId()))) {
                fieldErrors.put("productCode", "產品代碼 '" + product.getProductCode() + "' 已存在。");
            }
        }

        if (!fieldErrors.isEmpty()) {
            throw new InvalidProductDataException("產品數據驗證失敗。", fieldErrors);
        }

        // 2. Call the Repository layer method (Spring Data JPA's save() method automatically handles add or update)
        try {
            Product savedProduct = productRepository.save(product);
            LOGGER.info("Service: Successfully saved product (ID: {}), product name: {}", savedProduct.getProductId(), savedProduct.getProductName());
            return savedProduct;
        } catch (DataIntegrityViolationException e) {
            // Catch database integrity exceptions that might occur due to unique constraints, etc.
            LOGGER.error("Service: Data integrity error occurred while saving product: {}", e.getMessage(), e);
            // Can determine error type more precisely based on e.getMessage() or e.getCause()
            throw new InvalidProductDataException("保存產品失敗：數據重複或不符合約束。", e);
        } catch (Exception e) {
            LOGGER.error("Service: Unexpected error occurred while saving product: {}", e.getMessage(), e);
            throw new RuntimeException("無法保存產品：" + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true) // Read operations are typically set to read-only transactions
    public Optional<Product> getProductById(Integer productId) { // ID type adjusted to Integer
        LOGGER.info("Service: Attempting to retrieve product with ID {}.", productId);

        // 1. Parameter validation (business judgment for invalid productId)
        if (productId == null || productId <= 0) {
            throw new InvalidProductDataException("產品ID無效，必須是正數。");
        }

        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            LOGGER.info("Service: Successfully retrieved product with ID {}.", productId);
        } else {
            LOGGER.warn("Service: Product with ID {} not found.", productId);
            // Here, ProductNotFoundException is no longer thrown; instead, Optional.empty() is returned.
            // This allows the Controller or client consuming the Service to decide how to handle the case where the product does not exist.
        }
        return product;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        LOGGER.info("Service: Attempting to retrieve all products.");
        List<Product> products = productRepository.findAll();
        LOGGER.info("Service: Successfully retrieved {} products.", products.size());
        return products;
    }

    @Override
    @Transactional // Delete operations require transactions
    public boolean deleteProduct(Integer productId) throws ProductNotFoundException, ProductReferencedException {
        LOGGER.info("Service: Attempting to delete product with ID {}.", productId);

        // 1. Parameter validation
        if (productId == null || productId <= 0) {
            throw new InvalidProductDataException("產品ID無效，無法刪除。");
        }

        // 2. Business logic: Check if product exists (throws ProductNotFoundException)
        if (!productRepository.existsById(productId)) {
            LOGGER.warn("Service: Attempted to delete product (ID: {}) that does not exist.", productId);
            throw new ProductNotFoundException(productId);
        }

        // 3. Attempt to delete (handle ProductReferencedException)
        try {
            productRepository.deleteById(productId);
            LOGGER.info("Service: Successfully deleted product with ID {}.", productId);
            return true;
        } catch (DataIntegrityViolationException e) {
            // Spring Data JPA throws this exception when encountering a foreign key constraint violation
            LOGGER.warn("Service: Failed to delete product (ID: {}) because it is referenced by other data.", productId, e);
            throw new ProductReferencedException(productId); // Throw product referenced exception
        } catch (Exception e) {
            LOGGER.error("Service: Unexpected error occurred while deleting product (ID: {}): {}", productId, e.getMessage(), e);
            throw new RuntimeException("無法刪除ID為 " + productId + " 的產品：" + e.getMessage(), e);
        }
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getProductsByCategory'");
    }
}
