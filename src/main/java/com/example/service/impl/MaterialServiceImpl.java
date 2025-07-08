package com.example.service.impl;

import com.example.dao.BillOfMaterialRepository; // Assumed for checking BOM references
import com.example.dao.MaterialRepository;
import com.example.exception.InvalidMaterialDataException;
import com.example.exception.MaterialNotFoundException;
import com.example.exception.MaterialReferencedException;
import com.example.model.Material;
import com.example.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // For transactional methods

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * MaterialService 介面的實現類。
 * 負責物料相關的業務邏輯，並調用 MaterialRepository 進行數據庫操作。
 * 使用 Spring 的 @Service 和 @Autowired 進行依賴注入。
 */
@Service // Marks this class as a Spring service component
public class MaterialServiceImpl implements MaterialService {

    private static final Logger LOGGER = Logger.getLogger(MaterialServiceImpl.class.getName());

    private final MaterialRepository materialRepository;
    private final BillOfMaterialRepository billOfMaterialRepository; // We'll need to define this repository next for the BOM check

    /**
     * 通過構造函數注入 MaterialRepository 和 BillOfMaterialRepository。
     * Spring 會自動處理這些依賴。
     * @param materialRepository MaterialRepository 實例。
     * @param billOfMaterialRepository BillOfMaterialRepository 實例。
     */
    @Autowired // Marks the constructor for dependency injection
    public MaterialServiceImpl(MaterialRepository materialRepository, BillOfMaterialRepository billOfMaterialRepository) {
        this.materialRepository = materialRepository;
        this.billOfMaterialRepository = billOfMaterialRepository;
        LOGGER.info("MaterialServiceImpl 已初始化並注入 Repository。");
    }

    /**
     * 執行物料數據的基本驗證。
     * @param material 要驗證的物料物件。
     * @return 包含錯誤信息的 Map，如果沒有錯誤則為空。
     */
    private Map<String, String> validateMaterialData(Material material, boolean isNew) {
        Map<String, String> errors = new HashMap<>();

        if (material == null) {
            errors.put("material", "物料數據不能為空。");
            return errors;
        }
        if (material.getMaterialCode() == null || material.getMaterialCode().trim().isEmpty()) {
            errors.put("materialCode", "物料代碼為必填項。");
        } else if (isNew) { // Only check for uniqueness if it's a new material
            Optional<Material> existingMaterial = materialRepository.findByMaterialCode(material.getMaterialCode());
            if (existingMaterial.isPresent()) {
                errors.put("materialCode", "物料代碼 '" + material.getMaterialCode() + "' 已存在。");
            }
        }
        if (material.getMaterialName() == null || material.getMaterialName().trim().isEmpty()) {
            errors.put("materialName", "物料名稱為必填項。");
        }
        if (material.getUnit() == null || material.getUnit().trim().isEmpty()) {
            errors.put("unit", "單位為必填項。");
        }
        if (material.getUnitCost() == null) {
            errors.put("unitCost", "單位成本為必填項。");
        } else if (material.getUnitCost().compareTo(BigDecimal.ZERO) < 0) {
            errors.put("unitCost", "單位成本不能為負數。");
        }

        return errors;
    }

    @Override
    @Transactional // Ensures the entire method runs within a transaction
    public Material createMaterial(Material material) {
        LOGGER.log(Level.INFO, "Service: 嘗試新增物料: {0}", material.getMaterialName());

        Map<String, String> errors = validateMaterialData(material, true); // true for new material
        if (!errors.isEmpty()) {
            LOGGER.log(Level.WARNING, "新增物料時數據無效: {0}", errors);
            throw new InvalidMaterialDataException(errors);
        }

        try {
            // Material entity has @PrePersist to set createDate/updateDate
            return materialRepository.save(material);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "新增物料時發生數據庫錯誤: " + e.getMessage(), e);
            // Re-throw as a business-level exception or a more general service exception
            throw new RuntimeException("新增物料失敗。", e);
        }
    }

    @Override
    @Transactional(readOnly = true) // Read-only transaction for read operations
    public Optional<Material> getMaterialById(Integer materialId) {
        LOGGER.log(Level.INFO, "Service: 嘗試獲取物料，ID: {0}", materialId);
        if (materialId == null || materialId <= 0) {
            LOGGER.log(Level.WARNING, "獲取物料時物料ID無效: {0}", materialId);
            throw new IllegalArgumentException("物料ID無效，必須大於0。");
        }
        return materialRepository.findById(materialId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Material> getMaterialByCode(String materialCode) {
        LOGGER.log(Level.INFO, "Service: 嘗試獲取物料，Code: {0}", materialCode);
        if (materialCode == null || materialCode.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "獲取物料時物料代碼無效: {0}", materialCode);
            throw new IllegalArgumentException("物料代碼不能為空。");
        }
        return materialRepository.findByMaterialCode(materialCode);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Material> getAllMaterials() {
        LOGGER.info("Service: 嘗試獲取所有物料。");
        return materialRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Material> getAllActiveMaterials() {
        LOGGER.info("Service: 嘗試獲取所有活躍物料。");
        return materialRepository.findByIsActive(true);
    }

    @Override
    @Transactional
    public Material updateMaterial(Material material) {
        LOGGER.log(Level.INFO, "Service: 嘗試更新物料，ID: {0}", material != null ? material.getMaterialId() : "null");

        if (material == null || material.getMaterialId() == null || material.getMaterialId() <= 0) {
            LOGGER.log(Level.WARNING, "更新物料時提供的物料或ID無效。");
            throw new IllegalArgumentException("要更新的物料ID無效。");
        }

        // Validate basic data first
        Map<String, String> errors = validateMaterialData(material, false); // false for existing material

        // Check for duplicate material code ONLY if it's different from the original material's code
        Optional<Material> existingMaterialByCode = materialRepository.findByMaterialCode(material.getMaterialCode());
        if (existingMaterialByCode.isPresent() && !existingMaterialByCode.get().getMaterialId().equals(material.getMaterialId())) {
            errors.put("materialCode", "物料代碼 '" + material.getMaterialCode() + "' 已被其他物料使用。");
        }

        if (!errors.isEmpty()) {
            LOGGER.log(Level.WARNING, "更新物料時數據無效: {0}", errors);
            throw new InvalidMaterialDataException(errors);
        }

        // Ensure the material exists before updating
        Material existingMaterial = materialRepository.findById(material.getMaterialId())
            .orElseThrow(() -> {
                LOGGER.log(Level.WARNING, "更新物料失敗，物料ID不存在: {0}", material.getMaterialId());
                return new MaterialNotFoundException(material.getMaterialId());
            });

        // Copy updatable fields from the provided material to the existing one
        existingMaterial.setMaterialCode(material.getMaterialCode());
        existingMaterial.setMaterialName(material.getMaterialName());
        existingMaterial.setDescription(material.getDescription());
        existingMaterial.setUnit(material.getUnit());
        existingMaterial.setUnitCost(material.getUnitCost());
        existingMaterial.setCurrentStock(material.getCurrentStock());
        existingMaterial.setActive(material.isActive());
        // createDate is set @PrePersist and is not updatable
        // updateDate is set @PreUpdate automatically

        try {
            return materialRepository.save(existingMaterial); // Save the updated existing entity
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "更新物料時發生數據庫錯誤: " + e.getMessage(), e);
            throw new RuntimeException("更新物料失敗。", e); // Re-throw as a service-level exception
        }
    }

    @Override
    @Transactional
    public void deleteMaterial(Integer materialId) {
        LOGGER.log(Level.INFO, "Service: 嘗試刪除物料，ID: {0}", materialId);

        if (materialId == null || materialId <= 0) {
            LOGGER.log(Level.WARNING, "刪除物料時物料ID無效: {0}", materialId);
            throw new IllegalArgumentException("物料ID無效，必須大於0。");
        }

        // First, check if the material exists
        if (!materialRepository.existsById(materialId)) {
            LOGGER.log(Level.WARNING, "刪除物料失敗，物料ID不存在: {0}", materialId);
            throw new MaterialNotFoundException(materialId);
        }

        // Check if the material is referenced by any Bill of Material items
        // CORRECTED: Use existsByMaterial_MaterialId to navigate through the 'material' relationship
        if (billOfMaterialRepository.existsByMaterial_MaterialId(materialId)) {
             LOGGER.log(Level.WARNING, "物料ID {0} 被 BOM 項目引用，無法刪除。", materialId);
             throw new MaterialReferencedException(materialId);
        }

        try {
            materialRepository.deleteById(materialId);
            LOGGER.log(Level.INFO, "物料 ID: {0} 已成功刪除。", materialId);
        } catch (Exception e) { // Catching a generic Exception here to wrap potential persistence errors
            LOGGER.log(Level.SEVERE, "刪除物料時發生數據庫錯誤: " + e.getMessage(), e);
            throw new RuntimeException("刪除物料失敗。", e); // Re-throw as a service-level exception
        }
    }
}
