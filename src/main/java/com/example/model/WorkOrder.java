package com.example.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a Work Order entity mapped to the 'work_orders' table.
 * Includes a Many-to-One relationship with Product.
 */
@Entity
@Table(name = "work_orders") // Maps to the 'work_orders' table in the database
public class WorkOrder {

    @Id // Denotes the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing ID
    @Column(name = "work_order_id") // Maps to the 'work_order_id' column
    private Integer workOrderId;

    @Column(name = "work_order_number", unique = true, nullable = false, length = 50)
    private String workOrderNumber; // Unique work order number

    // Many-to-One relationship with Product
    // 'product_id' in 'work_orders' table is the foreign key
    @ManyToOne(fetch = FetchType.EAGER) // CORRECTED: Changed to EAGER to avoid LazyInitializationException
    @JoinColumn(name = "product_id", nullable = false) // Maps to the 'product_id' column in 'work_orders'
    private Product product; // Associated Product entity

    @Column(name = "planned_quantity", precision = 10, scale = 4) // Mapped to 'planned_quantity' column
    private BigDecimal quantity; // Planned quantity for the work order

    @Column(name = "unit", length = 20) // Unit of measure for the quantity
    private String unit;

    @Column(name = "scheduled_start_date", nullable = false)
    private LocalDateTime scheduledStartDate; // Planned start date and time

    @Column(name = "scheduled_due_date", nullable = false)
    private LocalDateTime scheduledDueDate; // Planned due date and time

    @Column(name = "actual_start_date")
    private LocalDateTime actualStartDate; // Actual start date and time

    @Column(name = "actual_completion_date")
    private LocalDateTime actualCompletionDate; // Actual completion date and time

    @Column(name = "status", nullable = false, length = 50)
    private String status; // Status of the work order (e.g., Pending, In Progress, Completed)

    @Column(name = "notes", length = 500)
    private String notes; // Additional notes

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate; // Timestamp for creation

    @Column(name = "update_date")
    private LocalDateTime updateDate; // Timestamp for last update

    // --- Transient fields for JSP display (not mapped to DB columns) ---
    // These will be populated by the Service layer
    @Transient
    private String productCode;
    @Transient
    private String productName;

    // --- Constructors ---
    public WorkOrder() {
        // Default constructor for JPA
    }

    // --- Lifecycle Callbacks (for automatic timestamping) ---
    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDate = LocalDateTime.now();
    }

    // --- Getters and Setters ---
    public Integer getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Integer workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getWorkOrderNumber() {
        return workOrderNumber;
    }

    public void setWorkOrderNumber(String workOrderNumber) {
        this.workOrderNumber = workOrderNumber;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public LocalDateTime getScheduledStartDate() {
        return scheduledStartDate;
    }

    public void setScheduledStartDate(LocalDateTime scheduledStartDate) {
        this.scheduledStartDate = scheduledStartDate;
    }

    public LocalDateTime getScheduledDueDate() {
        return scheduledDueDate;
    }

    public void setScheduledDueDate(LocalDateTime scheduledDueDate) {
        this.scheduledDueDate = scheduledDueDate;
    }

    public LocalDateTime getActualStartDate() {
        return actualStartDate;
    }

    public void setActualStartDate(LocalDateTime actualStartDate) {
        this.actualStartDate = actualStartDate;
    }

    public LocalDateTime getActualCompletionDate() {
        return actualCompletionDate;
    }

    public void setActualCompletionDate(LocalDateTime actualCompletionDate) {
        this.actualCompletionDate = actualCompletionDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    // --- Getters and Setters for Transient fields ---
    // These are populated by the Service layer for display purposes
    public String getProductCode() {
        // If product is loaded, return its code, otherwise null
        return (this.product != null) ? this.product.getProductCode() : productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        // If product is loaded, return its name, otherwise null
        return (this.product != null) ? this.product.getProductName() : productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    // --- hashCode and equals ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkOrder workOrder = (WorkOrder) o;
        return Objects.equals(workOrderId, workOrder.workOrderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workOrderId);
    }

    // --- toString ---
    @Override
    public String toString() {
        return "WorkOrder{" +
               "workOrderId=" + workOrderId +
               ", workOrderNumber='" + workOrderNumber + '\'' +
               ", productId=" + (product != null ? product.getProductId() : "null") +
               ", quantity=" + quantity +
               ", unit='" + unit + '\'' +
               ", status='" + status + '\'' +
               '}';
    }
}
