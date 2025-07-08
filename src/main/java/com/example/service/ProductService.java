package com.example.service;

import com.example.model.Product; // 確保導入 Product 實體
import java.util.List;
import java.util.Optional; // 引入 Optional
import com.example.exception.*; // 導入你的自定義異常

/**
 * ProductService 介面定義了產品相關的業務操作。
 * 這些操作通常會利用 ProductRepository 來與資料庫交互，並包含業務邏輯。
 */
public interface ProductService {

    /**
     * 新增或更新一個產品。
     * 如果產品ID為空或數據庫中不存在，則執行新增；
     * 如果產品ID存在，則執行更新。
     *
     * @param product 要保存的產品物件。
     * @return 保存後，包含完整數據（如自動生成ID）的產品物件。
     * @throws InvalidProductDataException 如果產品數據無效（例如，缺少必要的字段）。
     * （此處假定你可能定義了一個自定義的非檢查型業務異常）
     */
    Product saveProduct(Product product) throws InvalidProductDataException; // 統一新增和更新

    /**
     * 根據產品ID獲取產品資訊。
     *
     * @param productId 產品的唯一識別ID。請使用與實體主鍵匹配的類型（通常為 Long 或 Integer）。
     * @return 匹配指定ID的產品物件的 Optional 包裹，如果未找到則返回 Optional.empty()。
     */
    Optional<Product> getProductById(Integer  productId); // 改變參數類型，返回 Optional

    /**
     * 獲取所有產品的列表。
     *
     * @return 包含所有產品物件的列表。
     */
    List<Product> getAllProducts();

    /**
     * 刪除指定ID的產品。
     *
     * @param productId 要刪除產品的ID。請使用與實體主鍵匹配的類型。
     * @return 如果刪除成功返回 true，否則返回 false（例如，產品不存在）。
     * @throws ProductReferencedException 如果產品被其他數據引用而無法刪除。
     * （此處假定你可能定義了一個自定義的非檢查型業務異常）
     * @throws ProductNotFoundException 如果產品ID不存在。
     * （此處假定你可能定義了一個自定義的非檢查型業務異常）
     */
    boolean deleteProduct(Integer  productId) throws ProductReferencedException, ProductNotFoundException; // 改變參數類型

    List<Product> getProductsByCategory(String category);
}