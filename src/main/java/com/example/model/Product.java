package com.example.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a Product entity mapped to the 'products' table.
 * Includes fields for product details, pricing, and timestamps.
 */
@Entity
@Table(name = "products") // Maps to the 'products' table in the database
public class Product {

    @Id // Denotes the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing ID
    @Column(name = "product_id") // Maps to the 'product_id' column
    private Integer productId;

    @Column(name = "product_code", unique = true, nullable = false, length = 50)
    private String productCode; // Unique code for the product

    @Column(name = "product_name", nullable = false, length = 100)
    private String productName; // Name of the product

    @Column(name = "category", length = 50) // ADDED: Mapped to 'category' column
    private String category; // Product category

    @Column(name = "description", length = 500)
    private String description; // Product description

    @Column(name = "unit", length = 20) // Mapped to 'unit' column in DB
    private String unitOfMeasure; // Unit of measure (e.g., PCS, KG, M)

    @Column(name = "selling_price", precision = 10, scale = 4) // Example: 10 total digits, 4 after decimal
    private BigDecimal sellingPrice; // Selling price of the product

    @Column(name = "cost", precision = 10, scale = 4) // Example: 10 total digits, 4 after decimal
    private BigDecimal cost; // Cost of the product

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate; // Timestamp for creation

    @Column(name = "update_date")
    private LocalDateTime updateDate; // Timestamp for last update

    // --- Constructors ---
    public Product() {
        // Default constructor for JPA
    }

    public Product(String productCode, String productName, String category, String description, String unitOfMeasure, BigDecimal sellingPrice, BigDecimal cost) {
        this.productCode = productCode;
        this.productName = productName;
        this.category = category; // ADDED to constructor
        this.description = description;
        this.unitOfMeasure = unitOfMeasure;
        this.sellingPrice = sellingPrice;
        this.cost = cost;
    }

    // --- Lifecycle Callbacks (for automatic timestamping) ---
    @PrePersist // Called before the entity is first persisted (inserted)
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now(); // Also set updateDate on creation
    }

    @PreUpdate // Called before the entity is updated
    protected void onUpdate() {
        this.updateDate = LocalDateTime.now();
    }

    // --- Getters and Setters ---
    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    // ADDED: Getter and Setter for category
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    // No setter for createDate as it's set automatically on creation

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    // --- hashCode and equals (important for JPA entity comparison) ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productId, product.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    // --- toString (for logging and debugging) ---
    @Override
    public String toString() {
        return "Product{" +
               "productId=" + productId +
               ", productCode='" + productCode + '\'' +
               ", productName='" + productName + '\'' +
               ", category='" + category + '\'' + // ADDED to toString
               ", unitOfMeasure='" + unitOfMeasure + '\'' +
               ", sellingPrice=" + sellingPrice +
               ", cost=" + cost +
               '}';
    }

    public Object getUnit() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUnit'");
    }
}
