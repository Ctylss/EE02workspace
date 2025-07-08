package com.example.model;

import jakarta.persistence.*;
// Import for BigDecimal is already there
import java.math.BigDecimal;
// CHANGED: Use java.time.LocalDateTime for date/time consistency
import java.time.LocalDateTime;

/**
 * 物料模型類，對應資料庫中的 'materials' 表。
 */
@Entity
@Table(name = "materials")
public class Material {

    @Id // Specifies the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configures auto-incrementing ID
    @Column(name = "material_id") // Maps to the database column name
    private Integer materialId; // CHANGED: int to Integer for consistency

    @Column(name = "material_code", unique = true, nullable = false) // Assuming unique and not null
    private String materialCode;

    @Column(name = "material_name", nullable = false)
    private String materialName;

    @Column(name = "description")
    private String description;

    @Column(name = "unit", nullable = false)
    private String unit;

    @Column(name = "unit_cost")
    private BigDecimal unitCost;

    @Column(name = "current_stock")
    private BigDecimal currentStock;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "create_date", nullable = false, updatable = false) // Not updatable after creation
    private LocalDateTime createDate; // CHANGED: Timestamp to LocalDateTime

    @Column(name = "update_date")
    private LocalDateTime updateDate; // CHANGED: Timestamp to LocalDateTime

    // 預設構造函數
    public Material() {
        this.isActive = true; // Default to active
    }

    // A more practical constructor for creating new materials (without ID and dates)
    public Material(String materialCode, String materialName, String description,
                    String unit, BigDecimal unitCost, BigDecimal currentStock, boolean isActive) {
        this.materialCode = materialCode;
        this.materialName = materialName;
        this.description = description;
        this.unit = unit;
        this.unitCost = unitCost;
        this.currentStock = currentStock;
        this.isActive = isActive;
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
    public Integer getMaterialId() { // CHANGED: int to Integer
        return materialId;
    }

    public void setMaterialId(Integer materialId) { // CHANGED: int to Integer
        this.materialId = materialId;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public BigDecimal getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(BigDecimal currentStock) {
        this.currentStock = currentStock;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreateDate() { // CHANGED: Timestamp to LocalDateTime
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) { // CHANGED: Timestamp to LocalDateTime
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() { // CHANGED: Timestamp to LocalDateTime
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) { // CHANGED: Timestamp to LocalDateTime
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return "Material{" +
                "materialId=" + materialId +
                ", materialCode='" + materialCode + '\'' +
                ", materialName='" + materialName + '\'' +
                ", unit='" + unit + '\'' +
                ", unitCost=" + unitCost +
                ", currentStock=" + currentStock +
                ", isActive=" + isActive +
                '}';
    }
}