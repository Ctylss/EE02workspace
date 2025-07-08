package com.example.service;

import com.example.model.Material;
// Import custom exceptions for more specific error handling
import com.example.exception.InvalidMaterialDataException; // You'll need to create this
import com.example.exception.MaterialNotFoundException;   // You'll need to create this
import com.example.exception.MaterialReferencedException; // Consider creating this for delete operations

import java.util.List;
import java.util.Optional; // Use Optional for methods that might not find a result

/**
 * MaterialService 介面定義了物料相關的業務操作。
 * 這些操作將利用 MaterialRepository 來與資料庫交互，並包含業務邏輯和異常處理。
 */
public interface MaterialService {

    /**
     * 新增一個物料到系統。
     *
     * @param material 要新增的物料物件。
     * @return 成功新增並持久化後的物料物件，包含其資料庫生成的ID。
     * @throws InvalidMaterialDataException 如果物料數據無效（例如，缺少必要字段、重複編號）。
     */
    Material createMaterial(Material material); // Changed return type, removed SQLException

    /**
     * 根據物料ID從系統中獲取物料資訊。
     *
     * @param materialId 物料的唯一識別ID。
     * @return 一個包含匹配指定ID物料的 {@link Optional} 物件，如果未找到則返回空的 Optional。
     * @throws IllegalArgumentException 如果 materialId 無效（例如，小於等於0）。
     */
    Optional<Material> getMaterialById(Integer materialId); // Changed int to Integer, return to Optional

    /**
     * 根據物料編號從系統中獲取物料資訊。
     *
     * @param materialCode 物料的唯一編號。
     * @return 一個包含匹配指定編號物料的 {@link Optional} 物件，如果未找到則返回空的 Optional。
     * @throws IllegalArgumentException 如果 materialCode 無效（例如，為空）。
     */
    Optional<Material> getMaterialByCode(String materialCode); // Added for common lookup

    /**
     * 獲取所有物料的列表。
     *
     * @return 包含所有物料物件的列表。
     */
    List<Material> getAllMaterials(); // Removed SQLException

    /**
     * 獲取所有活躍 (isActive = true) 的物料列表。
     *
     * @return 包含所有活躍物料物件的列表。
     */
    List<Material> getAllActiveMaterials(); // Added for common filtering

    /**
     * 更新物料資訊。
     *
     * @param material 包含更新資訊的物料物件 (必須包含有效的物料ID)。
     * @return 成功更新並持久化後的物料物件。
     * @throws InvalidMaterialDataException 如果物料數據無效或物料編號重複。
     * @throws MaterialNotFoundException 如果要更新的物料ID不存在。
     */
    Material updateMaterial(Material material); // Changed return type, removed SQLException

    /**
     * 刪除指定ID的物料。
     *
     * @param materialId 要刪除物料的ID。
     * @throws IllegalArgumentException 如果 materialId 無效。
     * @throws MaterialNotFoundException 如果要刪除的物料ID不存在。
     * @throws MaterialReferencedException 如果物料被其他數據（如 BOM 項目、庫存記錄）引用而無法刪除。
     */
    void deleteMaterial(Integer materialId); // Changed int to Integer, return to void, added specific exceptions
}
