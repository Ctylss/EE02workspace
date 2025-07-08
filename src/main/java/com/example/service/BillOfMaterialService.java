package com.example.service;

import com.example.model.BillOfMaterial;
import com.example.exception.BillOfMaterialNotFoundException; // You'll need to create this exception
import com.example.exception.InvalidBillOfMaterialDataException; // You'll need to create this exception
import com.example.exception.ProductNotFoundException; // From your existing product exceptions
import com.example.exception.MaterialNotFoundException; // From your existing material exceptions

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * BillOfMaterialService 介面定義了產品用料清單（BOM）相關的業務操作。
 * 這些操作將利用 BillOfMaterialRepository 來與資料庫交互，並包含業務邏輯和異常處理。
 */
public interface BillOfMaterialService {

    /**
     * 新增一個 BOM 項目。
     *
     * @param bom 要新增的 BillOfMaterial 物件。
     * @return 成功新增並持久化後的 BillOfMaterial 物件，包含其資料庫生成的ID。
     * @throws InvalidBillOfMaterialDataException 如果 BOM 數據無效 (例如，數量為負、產品/物料組合已存在)。
     * @throws ProductNotFoundException 如果 BOM 中引用的產品不存在。
     * @throws MaterialNotFoundException 如果 BOM 中引用的物料不存在。
     */
    BillOfMaterial createBillOfMaterial(BillOfMaterial bom); // Changed return type, removed SQLException

    /**
     * 根據 BOM ID 獲取 BOM 項目資訊。
     *
     * @param bomId BOM 項目的唯一識別ID。
     * @return 一個包含匹配指定ID的 BillOfMaterial 物件的 {@link Optional} 物件，如果未找到則返回空的 Optional。
     * @throws IllegalArgumentException 如果 bomId 無效 (例如，小於等於0)。
     */
    Optional<BillOfMaterial> getBillOfMaterialById(Integer bomId); // Changed int to Integer, return to Optional

    /**
     * 獲取所有 BOM 項目的列表。
     *
     * @return 包含所有 BillOfMaterial 物件的列表。
     */
    List<BillOfMaterial> getAllBillOfMaterials(); // Removed SQLException

    /**
     * 根據產品ID獲取該產品的所有 BOM 項目列表。
     *
     * @param productId 產品的ID。
     * @return 該產品的所有 BillOfMaterial 物件的列表。
     * @throws IllegalArgumentException 如果產品ID無效。
     * @throws ProductNotFoundException 如果產品ID不存在。
     */
    List<BillOfMaterial> getBillOfMaterialsByProductId(Integer productId); // Changed int to Integer

    /**
     * 根據物料ID獲取包含該物料的所有 BOM 項目列表。
     * This method is useful for finding where a specific material is used.
     *
     * @param materialId 物料的ID。
     * @return 包含該物料的所有 BillOfMaterial 物件的列表。
     * @throws IllegalArgumentException 如果物料ID無效。
     * @throws MaterialNotFoundException 如果物料ID不存在。
     */
    List<BillOfMaterial> getBillOfMaterialsByMaterialId(Integer materialId); // Added for comprehensive lookups
    void addBillOfMaterial(BillOfMaterial bom) throws SQLException;
    /**
     * 更新 BOM 項目資訊。
     *
     * @param bom 包含更新資訊的 BillOfMaterial 物件 (必須包含有效的 BOM ID)。
     * @return 成功更新並持久化後的 BillOfMaterial 物件。
     * @throws InvalidBillOfMaterialDataException 如果 BOM 數據無效 (例如，數量為負、產品/物料組合已存在)。
     * @throws BillOfMaterialNotFoundException 如果要更新的 BOM ID 不存在。
     * @throws ProductNotFoundException 如果 BOM 中引用的產品不存在。
     * @throws MaterialNotFoundException 如果 BOM 中引用的物料不存在。
     */
    BillOfMaterial updateBillOfMaterial(BillOfMaterial bom); // Changed return type, removed SQLException

    /**
     * 刪除指定ID的 BOM 項目。
     *
     * @param bomId 要刪除 BOM 項目的ID。
     * @throws IllegalArgumentException 如果 BOM ID 無效。
     * @throws BillOfMaterialNotFoundException 如果要刪除的 BOM ID 不存在。
     */
    void deleteBillOfMaterial(Integer bomId); // Changed int to Integer, return to void

    /**
     * 刪除指定產品的所有 BOM 項目。
     *
     * @param productId 要刪除其 BOM 項目的產品ID。
     * @throws IllegalArgumentException 如果產品ID無效。
     * @throws ProductNotFoundException 如果產品ID不存在。
     */
    void deleteBillOfMaterialsByProductId(Integer productId); // Changed int to Integer, return to void
}
