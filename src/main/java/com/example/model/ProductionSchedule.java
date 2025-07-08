package com.example.model;

import jakarta.persistence.*; // 使用 jakarta.persistence 命名空間
import java.time.LocalDate; // 假設排程日期使用 LocalDate
import java.time.LocalDateTime; // 用於創建和更新日期
import java.math.BigDecimal;
import java.util.Objects;

/**
 * 生產排程模型類，對應資料庫中的 'production_schedules' 表。
 */
@Entity
@Table(name = "production_schedules") // CORRECTED: 確保你的資料表名稱是 production_schedules
public class ProductionSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id") // 假設資料庫欄位名稱是 schedule_id
    private Integer scheduleId;

    // 新增 scheduleNumber 屬性，作為排程的唯一識別碼
    @Column(name = "schedule_number", unique = true, nullable = false, length = 50)
    private String scheduleNumber; // 排程編號 (例如: PS20250708-001)

    @Column(name = "work_order_id") // 可以為空，Integer 允許 null
    private Integer workOrderId; // 相關聯的工單 ID (可選)

    @Transient // 不持久化到資料庫，用於顯示或透過 JOIN 取得
    private String workOrderNumber; // 工單號碼 (用於顯示，可透過 JOIN 取得)

    @Column(name = "product_id", nullable = false)
    private Integer productId; // 此排程的產品 ID

    @Transient // 不持久化到資料庫，用於顯示或透過 JOIN 取得
    private String productCode; // 產品代碼 (用於顯示，可透過 JOIN 取得)

    @Transient // 不持久化到資料庫，用於顯示或透過 JOIN 取得
    private String productName; // 產品名稱 (用於顯示，可透過 JOIN 取得)

    @Column(name = "scheduled_date", nullable = false)
    private LocalDateTime scheduledDate; // 計劃生產日期

    @Column(name = "shift", length = 50) // 班次 (例如: "Day", "Night", "A", "B", "C")
    private String shift;

    @Column(name = "planned_quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal plannedQuantity; // 計劃生產數量

    @Column(name = "actual_quantity", precision = 10, scale = 2) // 可選，可為 null
    private BigDecimal actualQuantity; // 實際生產數量 (可選)

    @Column(name = "status", nullable = false, length = 50) // 排程狀態 (例如: "Planned", "In Progress", "Completed", "Cancelled")
    private String status;

    @Column(name = "notes", columnDefinition = "NVARCHAR(MAX)") // 使用 NVARCHAR(MAX) 儲存長文本
    private String notes; // 備註

    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate; // 創建時間戳

    @Column(name = "update_date") // 最後更新時間戳
    private LocalDateTime updateDate;

    @Column(name = "actual_start_date")
    private LocalDateTime actualStartDate;

    @Column(name = "actual_completion_date")
    private LocalDateTime actualCompletionDate;


    // 預設構造函數
    public ProductionSchedule() {
    }

    // 全參構造函數 (通常用於從資料庫檢索)
    public ProductionSchedule(Integer scheduleId, String scheduleNumber, Integer workOrderId, String workOrderNumber,
                              Integer productId, String productCode, String productName,
                              LocalDateTime scheduledDate, String shift, BigDecimal plannedQuantity,
                              BigDecimal actualQuantity, String status, String notes,
                              LocalDateTime createDate, LocalDateTime updateDate,
                              LocalDateTime actualStartDate, LocalDateTime actualCompletionDate) {
        this.scheduleId = scheduleId;
        this.scheduleNumber = scheduleNumber; // 初始化新字段
        this.workOrderId = workOrderId;
        this.workOrderNumber = workOrderNumber;
        this.productId = productId;
        this.productCode = productCode;
        this.productName = productName;
        this.scheduledDate = scheduledDate;
        this.shift = shift;
        this.plannedQuantity = plannedQuantity;
        this.actualQuantity = actualQuantity;
        this.status = status;
        this.notes = notes;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.actualStartDate = actualStartDate;
        this.actualCompletionDate = actualCompletionDate;
    }

    // 常用構造函數 (通常用於新創建)
    // 注意：createDate 和 updateDate 由 @PrePersist 處理
    public ProductionSchedule(String scheduleNumber, Integer workOrderId, Integer productId, LocalDateTime scheduledDate, String shift, BigDecimal plannedQuantity, String status, String notes) {
        this.scheduleNumber = scheduleNumber; // 初始化新字段
        this.workOrderId = workOrderId;
        this.productId = productId;
        this.scheduledDate = scheduledDate;
        this.shift = shift;
        this.plannedQuantity = plannedQuantity;
        this.status = status;
        this.notes = notes;
    }

    // JPA 生命周期回調，自動設定日期
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

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleNumber() {
        return scheduleNumber;
    }

    public void setScheduleNumber(String scheduleNumber) {
        this.scheduleNumber = scheduleNumber;
    }

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

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public BigDecimal getPlannedQuantity() {
        return plannedQuantity;
    }

    public void setPlannedQuantity(BigDecimal plannedQuantity) {
        this.plannedQuantity = plannedQuantity;
    }

    public BigDecimal getActualQuantity() {
        return actualQuantity;
    }

    public void setActualQuantity(BigDecimal actualQuantity) {
        this.actualQuantity = actualQuantity;
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

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductionSchedule that = (ProductionSchedule) o;
        return Objects.equals(scheduleId, that.scheduleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scheduleId);
    }

    @Override
    public String toString() {
        return "ProductionSchedule{" +
                "scheduleId=" + scheduleId +
                ", scheduleNumber='" + scheduleNumber + '\'' +
                ", workOrderId=" + workOrderId +
                ", workOrderNumber='" + workOrderNumber + '\'' +
                ", productId=" + productId +
                ", productCode='" + productCode + '\'' +
                ", productName='" + productName + '\'' +
                ", scheduledDate=" + scheduledDate +
                ", shift='" + shift + '\'' +
                ", plannedQuantity=" + plannedQuantity +
                ", actualQuantity=" + actualQuantity +
                ", status='" + status + '\'' +
                ", notes='" + notes + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", actualStartDate=" + actualStartDate +
                ", actualCompletionDate=" + actualCompletionDate +
                '}';
    }
}
