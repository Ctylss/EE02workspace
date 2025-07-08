package com.example.model;

import jakarta.persistence.*; // 使用 jakarta.persistence 命名空間，因為你正在使用 Tomcat 10 和 Spring 6
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 產品用料清單 (BOM) 項目模型類，對應資料庫中的 'bill_of_materials' 表。
 * 此版本將產品和物料資訊通過 JPA 關聯 (ManyToOne) 而非直接冗餘字段儲存。
 */
@Entity
@Table(name = "bill_of_materials")
public class BillOfMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bom_id")
    private Integer bomId;

    // RECOMMENDED: Establish Many-to-One relationship with Product
    // This allows you to get product details (like name) directly from the Product entity.
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false) // Maps to the product_id foreign key column
    private Product product;

    // RECOMMENDED: Establish Many-to-One relationship with Material
    // This allows you to get material details (like name, unit) directly from the Material entity.
    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false) // Maps to the material_id foreign key column
    private Material material;

    @Column(name = "quantity", nullable = false, precision = 19, scale = 4) // Example: 19 total digits, 4 after decimal
    private BigDecimal quantity;

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    // 預設構造函數
    public BillOfMaterial() {
    }

    // A more practical constructor for creating new BOM items (without ID and dates)
    // Takes Product and Material objects directly for relationships
    public BillOfMaterial(Product product, Material material, BigDecimal quantity) {
        this.product = product;
        this.material = material;
        this.quantity = quantity;
    }

    // --- Lifecycle Callbacks for automatic date management ---
    @PrePersist // Called before the entity is first persisted
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
    }

    @PreUpdate // Called before the entity is updated
    protected void onUpdate() {
        this.updateDate = LocalDateTime.now();
    }

    // --- Getter 和 Setter 方法 ---
    public Integer getBomId() {
        return bomId;
    }

    public void setBomId(Integer bomId) {
        this.bomId = bomId;
    }

    // Access Product entity to get its ID or Name
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // Access Material entity to get its ID, Name, or Unit
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        // Updated toString to reflect relationships
        return "BillOfMaterial{" +
               "bomId=" + bomId +
               ", productId=" + (product!= null ? product.getProductId() : "null") +
               ", productName='" + (product != null ? product.getProductName() : "null") + '\'' +
               ", materialId=" + (material != null ? material.getMaterialId() : "null") +
               ", materialName='" + (material != null ? material.getMaterialName() : "null") + '\'' +
               ", materialUnit='" + (material != null ? material.getUnit() : "null") + '\'' +
               ", quantity=" + quantity +
               '}';
    }
}
