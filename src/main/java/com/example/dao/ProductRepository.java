package com.example.dao; // 保持在 dao 包下是常見做法

import com.example.model.Product; // 確保導入你的 Product 實體類
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; // 可選，但建議加上，明確標識為 Repository
import java.util.Optional;

/**
 * Spring Data JPA Repository 介面，用於對 Product 實體進行數據庫操作。
 * Spring Data JPA 會自動為這個介面生成實現。
 */
@Repository // 標識這是一個 Spring 管理的 Repository 組件
public interface ProductRepository extends JpaRepository<Product, Integer> {
    // CrudRepository 提供了：
    // Product save(Product product);           // 用於 addProduct 或 updateProduct
    // Optional<Product> findById(Long id);     // 用於 getProductById (返回 Optional 以處理未找到的情況)
    // List<Product> findAll();                 // 用於 getAllProducts
    // void deleteById(Long id);                // 用於 deleteProduct
    // boolean existsById(Long id);             // 用於檢查是否存在

    // JpaRepository 在此基礎上增加了分頁、排序和更多 JPA 特有方法。

    // 如果你有 ProductDao 中沒有的特定查詢需求，可以在這裡定義方法簽名，
    // Spring Data JPA 會根據命名規則自動生成查詢：
    // 例如：
    // List<Product> findByName(String name);
    // List<Product> findByPriceGreaterThan(Double price);
        Optional<Product> findByProductCode(String productCode);

}
