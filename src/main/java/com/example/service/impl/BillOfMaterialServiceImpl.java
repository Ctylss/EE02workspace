package com.example.service.impl;

import com.example.dao.BillOfMaterialRepository;
import com.example.dao.MaterialRepository;
import com.example.dao.ProductRepository;
import com.example.model.BillOfMaterial;
import com.example.model.Material;
import com.example.model.Product;
import com.example.service.BillOfMaterialService;
import com.example.exception.BillOfMaterialNotFoundException;
import com.example.exception.InvalidBillOfMaterialDataException;
import com.example.exception.ProductNotFoundException;
import com.example.exception.MaterialNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * BillOfMaterialService 介面的實現類。
 * 負責產品用料清單（BOM）相關的業務邏輯，並調用 BillOfMaterialRepository 進行數據庫操作。
 * 使用 Spring 的 @Service 和 @Autowired 進行依賴注入。
 */
@Service // Marks this class as a Spring service component
public class BillOfMaterialServiceImpl implements BillOfMaterialService {

    private static final Logger LOGGER = Logger.getLogger(BillOfMaterialServiceImpl.class.getName());

    private final BillOfMaterialRepository bomRepository;
    private final ProductRepository productRepository;
    private final MaterialRepository materialRepository;

    /**
     * 通過構造函數注入所有依賴的 Repository。
     * Spring 會自動處理這些依賴。
     */
    @Autowired
    public BillOfMaterialServiceImpl(
            BillOfMaterialRepository bomRepository,
            ProductRepository productRepository,
            MaterialRepository materialRepository) {
        this.bomRepository = bomRepository;
        this.productRepository = productRepository;
        this.materialRepository = materialRepository;
        LOGGER.info("BillOfMaterialServiceImpl 已初始化並注入 Repository。");
    }

    /**
     * 執行 BOM 項目數據的基本驗證。
     *
     * @param bom 要驗證的 BillOfMaterial 物件。
     * @return 包含錯誤信息的 Map，如果沒有錯誤則為空。
     */
    private Map<String, String> validateBillOfMaterialData(BillOfMaterial bom) {
        Map<String, String> errors = new HashMap<>();

        if (bom == null) {
            errors.put("bom", "BOM 數據不能為空。");
            return errors;
        }
        // Validate associated Product and Material entities exist
        // 檢查 Product 物件本身是否為空，以及其 ID 是否有效
        if (bom.getProduct() == null || bom.getProduct().getProductId() == null || bom.getProduct().getProductId() <= 0) {
            errors.put("productId", "產品ID無效或產品信息缺失。");
        }
        // 檢查 Material 物件本身是否為空，以及其 ID 是否有效
        if (bom.getMaterial() == null || bom.getMaterial().getMaterialId() == null || bom.getMaterial().getMaterialId() <= 0) {
            errors.put("materialId", "物料ID無效或物料信息缺失。");
        }
        if (bom.getQuantity() == null) {
            errors.put("quantity", "消耗數量為必填項。");
        } else if (bom.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            errors.put("quantity", "消耗數量必須大於零。");
        }

        return errors;
    }

    @Override
    @Transactional
    public BillOfMaterial createBillOfMaterial(BillOfMaterial bom) {
        LOGGER.log(Level.INFO, "Service: 嘗試新增 BOM 項目: 產品ID {0}, 物料ID {1}",
                new Object[]{bom.getProduct() != null ? bom.getProduct().getProductId() : "null",
                             bom.getMaterial() != null ? bom.getMaterial().getMaterialId() : "null"});

        // Perform basic data validation
        Map<String, String> errors = validateBillOfMaterialData(bom);
        if (!errors.isEmpty()) {
            LOGGER.log(Level.WARNING, "新增 BOM 項目時數據無效: {0}", errors);
            throw new InvalidBillOfMaterialDataException(errors);
        }

        // Validate that referenced Product and Material exist
        Product product = productRepository.findById(bom.getProduct().getProductId())
                .orElseThrow(() -> {
                    LOGGER.log(Level.WARNING, "新增 BOM 失敗: 產品ID {0} 不存在。", bom.getProduct().getProductId());
                    return new ProductNotFoundException(bom.getProduct().getProductId());
                });

        Material material = materialRepository.findById(bom.getMaterial().getMaterialId())
                .orElseThrow(() -> {
                    LOGGER.log(Level.WARNING, "新增 BOM 失敗: 物料ID {0} 不存在。", bom.getMaterial().getMaterialId());
                    return new MaterialNotFoundException(bom.getMaterial().getMaterialId());
                });

        // Set the actual Product and Material entities on the BOM object
        // This is crucial because BillOfMaterial now has @ManyToOne relationships
        bom.setProduct(product);
        bom.setMaterial(material);

        try {
            // Optional: If a product can only have one of a specific material, add a check here:
            // if (bomRepository.findByProductAndMaterial(product, material).isPresent()) {
            //    throw new InvalidBillOfMaterialDataException("該產品已包含此物料。");
            // }

            return bomRepository.save(bom);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "新增 BOM 項目時發生數據庫錯誤: " + e.getMessage(), e);
            throw new RuntimeException("新增 BOM 項目失敗。", e);
        }
    }

    // 重新實作 addBillOfMaterial 方法以符合介面要求
    @Override
    @Transactional
    public void addBillOfMaterial(BillOfMaterial bom) {
        // 直接呼叫 createBillOfMaterial 處理邏輯
        createBillOfMaterial(bom);
    }


    @Override
    @Transactional(readOnly = true) // Read-only transaction for read operations
    public Optional<BillOfMaterial> getBillOfMaterialById(Integer bomId) {
        LOGGER.log(Level.INFO, "Service: 嘗試獲取 BOM 項目，ID: {0}", bomId);
        if (bomId == null || bomId <= 0) {
            LOGGER.log(Level.WARNING, "獲取 BOM 項目時 BOM ID無效: {0}", bomId);
            throw new IllegalArgumentException("BOM ID無效，必須大於0。");
        }
        return bomRepository.findById(bomId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillOfMaterial> getAllBillOfMaterials() {
        LOGGER.info("Service: 嘗試獲取所有 BOM 項目。");
        return bomRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillOfMaterial> getBillOfMaterialsByProductId(Integer productId) {
        LOGGER.log(Level.INFO, "Service: 嘗試獲取產品ID {0} 的所有 BOM 項目。", productId);
        if (productId == null || productId <= 0) {
            LOGGER.log(Level.WARNING, "獲取產品 BOM 時產品ID無效: {0}", productId);
            throw new IllegalArgumentException("產品ID無效，必須大於0。");
        }
        // Optional: Check if product exists before querying
        if (!productRepository.existsById(productId)) {
            LOGGER.log(Level.WARNING, "獲取產品 BOM 失敗: 產品ID {0} 不存在。", productId);
            throw new ProductNotFoundException(productId);
        }
        // 使用更新後的儲存庫方法
        return bomRepository.findByProduct_ProductId(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillOfMaterial> getBillOfMaterialsByMaterialId(Integer materialId) {
        LOGGER.log(Level.INFO, "Service: 嘗試獲取物料ID {0} 的所有 BOM 項目。", materialId);
        if (materialId == null || materialId <= 0) {
            LOGGER.log(Level.WARNING, "獲取物料 BOM 時物料ID無效: {0}", materialId);
            throw new IllegalArgumentException("物料ID無效，必須大於0。");
        }
        // Optional: Check if material exists before querying
        if (!materialRepository.existsById(materialId)) {
            LOGGER.log(Level.WARNING, "獲取物料 BOM 失敗: 物料ID {0} 不存在。", materialId);
            throw new MaterialNotFoundException(materialId);
        }
        // 使用更新後的儲存庫方法
        return bomRepository.findByMaterial_MaterialId(materialId);
    }

    @Override
    @Transactional
    public BillOfMaterial updateBillOfMaterial(BillOfMaterial bom) {
        LOGGER.log(Level.INFO, "Service: 嘗試更新 BOM 項目，ID: {0}", bom != null ? bom.getBomId() : "null");

        if (bom == null || bom.getBomId() == null || bom.getBomId() <= 0) {
            LOGGER.log(Level.WARNING, "更新 BOM 項目時提供的 BOM 或 ID 無效。");
            throw new IllegalArgumentException("要更新的 BOM 項目ID無效。");
        }

        // 1. Validate BOM data itself (e.g., quantity)
        Map<String, String> errors = validateBillOfMaterialData(bom);
        if (!errors.isEmpty()) {
            LOGGER.log(Level.WARNING, "更新 BOM 項目時數據無效: {0}", errors);
            throw new InvalidBillOfMaterialDataException(errors);
        }

        // 2. Ensure the BOM item to be updated actually exists
        BillOfMaterial existingBom = bomRepository.findById(bom.getBomId())
                .orElseThrow(() -> {
                    LOGGER.log(Level.WARNING, "更新 BOM 失敗，BOM ID不存在: {0}", bom.getBomId());
                    return new BillOfMaterialNotFoundException(bom.getBomId());
                });

        // 3. Validate and set the associated Product entity (if product ID changed or needs re-validation)
        Product product = productRepository.findById(bom.getProduct().getProductId())
                .orElseThrow(() -> {
                    LOGGER.log(Level.WARNING, "更新 BOM 失敗: 產品ID {0} 不存在。", bom.getProduct().getProductId());
                    return new ProductNotFoundException(bom.getProduct().getProductId());
                });

        // 4. Validate and set the associated Material entity (if material ID changed or needs re-validation)
        Material material = materialRepository.findById(bom.getMaterial().getMaterialId())
                .orElseThrow(() -> {
                    LOGGER.log(Level.WARNING, "更新 BOM 失敗: 物料ID {0} 不存在。", bom.getMaterial().getMaterialId());
                    return new MaterialNotFoundException(bom.getMaterial().getMaterialId());
                });

        // 5. Update only allowed fields on the existing entity
        existingBom.setProduct(product);  // Update relationship
        existingBom.setMaterial(material); // Update relationship
        existingBom.setQuantity(bom.getQuantity());
        // createDate is set @PrePersist and should not be changed manually
        // updateDate is set @PreUpdate automatically

        try {
            return bomRepository.save(existingBom); // Save the updated existing entity
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "更新 BOM 項目時發生數據庫錯誤: " + e.getMessage(), e);
            throw new RuntimeException("更新 BOM 項目失敗。", e);
        }
    }

    @Override
    @Transactional
    public void deleteBillOfMaterial(Integer bomId) {
        LOGGER.log(Level.INFO, "Service: 嘗試刪除 BOM 項目，ID: {0}", bomId);

        if (bomId == null || bomId <= 0) {
            LOGGER.log(Level.WARNING, "刪除 BOM 項目時ID無效: {0}", bomId);
            throw new IllegalArgumentException("BOM ID無效，必須大於0。");
        }

        // Check if the BOM item exists before attempting deletion
        if (!bomRepository.existsById(bomId)) {
            LOGGER.log(Level.WARNING, "刪除 BOM 項目失敗，BOM ID不存在: {0}", bomId);
            throw new BillOfMaterialNotFoundException(bomId);
        }

        try {
            bomRepository.deleteById(bomId);
            LOGGER.log(Level.INFO, "BOM 項目 ID: {0} 已成功刪除。", bomId);
        } catch (Exception e) { // Catching a generic Exception here to wrap potential persistence errors
            LOGGER.log(Level.SEVERE, "刪除 BOM 項目時發生數據庫錯誤: " + e.getMessage(), e);
            throw new RuntimeException("刪除 BOM 項目失敗。", e);
        }
    }

    @Override
    @Transactional
    public void deleteBillOfMaterialsByProductId(Integer productId) {
        LOGGER.log(Level.INFO, "Service: 嘗試刪除產品ID {0} 的所有 BOM 項目。", productId);

        if (productId == null || productId <= 0) {
            LOGGER.log(Level.WARNING, "刪除產品所有 BOM 時產品ID無效: {0}", productId);
            throw new IllegalArgumentException("產品ID無效，必須大於0。");
        }

        // Optional: Check if product exists before attempting deletion of its BOMs
        if (!productRepository.existsById(productId)) {
            LOGGER.log(Level.WARNING, "刪除產品所有 BOM 失敗，產品ID不存在: {0}", productId);
            throw new ProductNotFoundException(productId);
        }

        try {
            // Using the custom repository method to delete all BOMs by product ID
            // 使用更新後的儲存庫方法
            bomRepository.deleteByProduct_ProductId(productId);
            LOGGER.log(Level.INFO, "產品ID {0} 的所有 BOM 項目已成功刪除。", productId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "刪除產品所有 BOM 項目時發生數據庫錯誤: " + e.getMessage(), e);
            throw new RuntimeException("刪除產品所有 BOM 項目失敗。", e);
        }
    }
}
