package com.example.service.impl;

import com.example.model.ProductionSchedule;
import com.example.model.Product; // To validate product existence
import com.example.model.WorkOrder; // To validate work order existence
import com.example.dao.ProductionScheduleRepository;
import com.example.dao.ProductRepository; // Assuming you have a ProductRepository
import com.example.dao.WorkOrderRepository; // Assuming you have a WorkOrderRepository
import com.example.service.ProductionScheduleService;
import com.example.exception.InvalidMaterialDataException;
import com.example.exception.ProductNotFoundException; // Reusing existing exception
import com.example.exception.WorkOrderNotFoundException; // Reusing existing exception

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ProductionScheduleService interface implementation.
 * Responsible for production schedule related business logic and calling Spring Data JPA Repository for database operations.
 */
@Service
public class ProductionScheduleServiceImpl implements ProductionScheduleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductionScheduleServiceImpl.class);

    private final ProductionScheduleRepository productionScheduleRepository;
    private final ProductRepository productRepository;
    private final WorkOrderRepository workOrderRepository;

    private static final List<String> VALID_STATUSES = Arrays.asList("Planned", "In Progress", "Completed", "Cancelled");

    @Autowired
    public ProductionScheduleServiceImpl(ProductionScheduleRepository productionScheduleRepository,
                                         ProductRepository productRepository,
                                         WorkOrderRepository workOrderRepository) {
        this.productionScheduleRepository = productionScheduleRepository;
        this.productRepository = productRepository;
        this.workOrderRepository = workOrderRepository;
        LOGGER.info("ProductionScheduleServiceImpl has been initialized.");
    }

    /**
     * Helper method for common production schedule validation logic.
     * Throws InvalidProductionScheduleDataException if validation fails.
     */
    private void validateProductionSchedule(ProductionSchedule productionSchedule, boolean isNew) {
        Map<String, String> fieldErrors = new HashMap<>();

        if (productionSchedule == null) {
            throw new InvalidMaterialDataException("生產排程物件不能為空。");
        }

        // Validate schedule number
        if (productionSchedule.getScheduleNumber() == null || productionSchedule.getScheduleNumber().trim().isEmpty()) {
            fieldErrors.put("scheduleNumber", "排程編號為必填項。");
        } else {
            Optional<ProductionSchedule> existingPsByNumber = productionScheduleRepository.findByScheduleNumber(productionSchedule.getScheduleNumber());
            if (existingPsByNumber.isPresent()) {
                if (isNew || !existingPsByNumber.get().getScheduleId().equals(productionSchedule.getScheduleId())) {
                    fieldErrors.put("scheduleNumber", "排程編號 '" + productionSchedule.getScheduleNumber() + "' 已存在。");
                }
            }
        }

        // Validate productId
        if (productionSchedule.getProductId() == null || productionSchedule.getProductId() <= 0) {
            fieldErrors.put("productId", "產品ID無效，必須是正數。");
        } else if (!productRepository.existsById(productionSchedule.getProductId())) {
            fieldErrors.put("productId", "指定產品ID: " + productionSchedule.getProductId() + " 不存在。");
        }

        // Validate workOrderId (if provided)
        if (productionSchedule.getWorkOrderId() != null && productionSchedule.getWorkOrderId() <= 0) {
            fieldErrors.put("workOrderId", "工單ID無效，必須是正數。");
        } else if (productionSchedule.getWorkOrderId() != null && !workOrderRepository.existsById(productionSchedule.getWorkOrderId())) {
            fieldErrors.put("workOrderId", "指定工單ID: " + productionSchedule.getWorkOrderId() + " 不存在。");
        }

        // Validate scheduledDate
        if (productionSchedule.getScheduledDate() == null) {
            fieldErrors.put("scheduledDate", "計劃生產日期為必填項。");
        }

        // Validate plannedQuantity
        if (productionSchedule.getPlannedQuantity() == null || productionSchedule.getPlannedQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            fieldErrors.put("plannedQuantity", "計劃生產數量不能為空且必須大於零。");
        }

        // Validate status
        if (productionSchedule.getStatus() == null || productionSchedule.getStatus().trim().isEmpty()) {
            fieldErrors.put("status", "排程狀態為必填項。");
        } else if (!VALID_STATUSES.contains(productionSchedule.getStatus())) {
            fieldErrors.put("status", "無效的排程狀態: " + productionSchedule.getStatus());
        }

        if (!fieldErrors.isEmpty()) {
            throw new InvalidMaterialDataException("生產排程數據驗證失敗。", fieldErrors);
        }
    }

    @Override
    @Transactional
    public ProductionSchedule createProductionSchedule(ProductionSchedule productionSchedule) {
        LOGGER.info("Service: Attempting to create production schedule.");
        validateProductionSchedule(productionSchedule, true); // True for a new schedule

        // Ensure ID is null for new entity (handled by JPA @GeneratedValue)
        productionSchedule.setScheduleId(null);

        try {
            ProductionSchedule savedSchedule = productionScheduleRepository.save(productionSchedule);
            LOGGER.info("Service: Successfully created production schedule (ID: {}, Number: {}).", savedSchedule.getScheduleId(), savedSchedule.getScheduleNumber());
            return savedSchedule;
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Service: Data integrity error while creating production schedule: {}", e.getMessage(), e);
            throw new InvalidMaterialDataException("創建生產排程失敗：數據重複或不符合約束。", e);
        } catch (Exception e) {
            LOGGER.error("Service: Unexpected error while creating production schedule: {}", e.getMessage(), e);
            throw new RuntimeException("無法創建生產排程：" + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public Optional<ProductionSchedule> getProductionScheduleById(Integer scheduleId) {
        LOGGER.info("Service: Attempting to get production schedule by ID: {}.", scheduleId);
        if (scheduleId == null || scheduleId <= 0) {
            LOGGER.warn("獲取生產排程時ID無效: {}", scheduleId);
            throw new IllegalArgumentException("排程ID無效，必須是正數。");
        }
        Optional<ProductionSchedule> schedule = productionScheduleRepository.findById(scheduleId);
        if (schedule.isPresent()) {
            LOGGER.info("Service: Found production schedule with ID: {}.", scheduleId);
        } else {
            LOGGER.warn("Service: Production schedule with ID: {} not found.", scheduleId);
        }
        return schedule;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductionSchedule> getAllProductionSchedules() {
        LOGGER.info("Service: Attempting to get all production schedules.");
        List<ProductionSchedule> schedules = productionScheduleRepository.findAll();
        // Populate transient fields (workOrderNumber, productCode, productName)
        // This is a simple approach. For large datasets, consider DTOs or custom queries.
        schedules.forEach(schedule -> {
            if (schedule.getWorkOrderId() != null) {
                workOrderRepository.findById(schedule.getWorkOrderId()).ifPresent(wo -> {
                    schedule.setWorkOrderNumber(wo.getWorkOrderNumber());
                    LOGGER.debug("Populated workOrderNumber {} for schedule ID {}", wo.getWorkOrderNumber(), schedule.getScheduleId()); // Added logging
                });
            } else {
                LOGGER.debug("WorkOrder ID is null for schedule ID {}", schedule.getScheduleId()); // Added logging
            }
            if (schedule.getProductId() != null) {
                productRepository.findById(schedule.getProductId()).ifPresent(p -> {
                    schedule.setProductCode(p.getProductCode());
                    schedule.setProductName(p.getProductName());
                    LOGGER.debug("Populated productCode {} and productName {} for schedule ID {}", p.getProductCode(), p.getProductName(), schedule.getScheduleId()); // Added logging
                });
            } else {
                LOGGER.debug("Product ID is null for schedule ID {}", schedule.getScheduleId()); // Added logging
            }
        });
        LOGGER.info("Service: Retrieved {} production schedules.", schedules.size());
        return schedules;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductionSchedule> getProductionSchedulesByStatus(String status) {
        LOGGER.info("Service: Attempting to get production schedules by status: {}.", status);
        if (status == null || status.trim().isEmpty() || !VALID_STATUSES.contains(status)) {
            LOGGER.warn("獲取生產排程時狀態無效或為空: {}", status);
            throw new IllegalArgumentException("無效或為空的排程狀態。");
        }
        List<ProductionSchedule> schedules = productionScheduleRepository.findByStatus(status);
        schedules.forEach(schedule -> {
            if (schedule.getWorkOrderId() != null) {
                workOrderRepository.findById(schedule.getWorkOrderId()).ifPresent(wo ->
                    schedule.setWorkOrderNumber(wo.getWorkOrderNumber())
                );
            }
            if (schedule.getProductId() != null) {
                productRepository.findById(schedule.getProductId()).ifPresent(p -> {
                    schedule.setProductCode(p.getProductCode());
                    schedule.setProductName(p.getProductName());
                });
            }
        });
        LOGGER.info("Service: Retrieved {} production schedules with status: {}.", schedules.size(), status);
        return schedules;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductionSchedule> getProductionSchedulesByProductId(Integer productId) {
        LOGGER.info("Service: Attempting to get production schedules by product ID: {}.", productId);
        if (productId == null || productId <= 0) {
            LOGGER.warn("獲取生產排程時產品ID無效: {}", productId);
            throw new IllegalArgumentException("產品ID無效，必須大於0。");
        }
        if (!productRepository.existsById(productId)) {
            LOGGER.warn("獲取生產排程失敗: 產品ID {} 不存在。", productId);
            throw new ProductNotFoundException(productId);
        }
        List<ProductionSchedule> schedules = productionScheduleRepository.findByProductId(productId);
        schedules.forEach(schedule -> {
            if (schedule.getWorkOrderId() != null) {
                workOrderRepository.findById(schedule.getWorkOrderId()).ifPresent(wo ->
                    schedule.setWorkOrderNumber(wo.getWorkOrderNumber())
                );
            }
            productRepository.findById(schedule.getProductId()).ifPresent(p -> {
                schedule.setProductCode(p.getProductCode());
                schedule.setProductName(p.getProductName());
            });
        });
        LOGGER.info("Service: Retrieved {} production schedules for product ID: {}.", schedules.size(), productId);
        return schedules;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductionSchedule> getProductionSchedulesByWorkOrderId(Integer workOrderId) {
        LOGGER.info("Service: Attempting to get production schedules by work order ID: {}.", workOrderId);
        if (workOrderId == null || workOrderId <= 0) {
            LOGGER.warn("獲取生產排程時工單ID無效: {}", workOrderId);
            throw new IllegalArgumentException("工單ID無效，必須大於0。");
        }
        if (!workOrderRepository.existsById(workOrderId)) {
            LOGGER.warn("獲取生產排程失敗: 工單ID {} 不存在。", workOrderId);
            throw new WorkOrderNotFoundException(workOrderId);
        }
        List<ProductionSchedule> schedules = productionScheduleRepository.findByWorkOrderId(workOrderId);
        schedules.forEach(schedule -> {
            workOrderRepository.findById(schedule.getWorkOrderId()).ifPresent(wo ->
                schedule.setWorkOrderNumber(wo.getWorkOrderNumber())
            );
            if (schedule.getProductId() != null) { // Ensure productId is not null before fetching product details
                productRepository.findById(schedule.getProductId()).ifPresent(p -> {
                    schedule.setProductCode(p.getProductCode());
                    schedule.setProductName(p.getProductName());
                });
            }
        });
        LOGGER.info("Service: Retrieved {} production schedules for work order ID: {}.", schedules.size(), workOrderId);
        return schedules;
    }

    @Override
    @Transactional
    public Optional<ProductionSchedule> updateProductionSchedule(ProductionSchedule productionSchedule) {
        LOGGER.info("Service: Attempting to update production schedule with ID: {}.", productionSchedule != null ? productionSchedule.getScheduleId() : "null");

        if (productionSchedule == null || productionSchedule.getScheduleId() == null || productionSchedule.getScheduleId() <= 0) {
            LOGGER.warn("更新生產排程時，排程物件和有效ID不能為空。排程ID: {}", productionSchedule != null ? productionSchedule.getScheduleId() : "null");
            throw new IllegalArgumentException("排程ID不能為空且必須是正數。");
        }

        Optional<ProductionSchedule> existingScheduleOptional = productionScheduleRepository.findById(productionSchedule.getScheduleId());
        if (existingScheduleOptional.isEmpty()) {
            LOGGER.warn("更新生產排程失敗，排程ID不存在: {}", productionSchedule.getScheduleId());
            throw new ProductNotFoundException(productionSchedule.getScheduleId());
        }
        ProductionSchedule scheduleToUpdate = existingScheduleOptional.get();

        validateProductionSchedule(productionSchedule, false); // False for an existing schedule

        // Manually copy updated fields to the managed entity
        scheduleToUpdate.setScheduleNumber(productionSchedule.getScheduleNumber());
        scheduleToUpdate.setWorkOrderId(productionSchedule.getWorkOrderId());
        scheduleToUpdate.setProductId(productionSchedule.getProductId());
        scheduleToUpdate.setScheduledDate(productionSchedule.getScheduledDate());
        scheduleToUpdate.setShift(productionSchedule.getShift());
        scheduleToUpdate.setPlannedQuantity(productionSchedule.getPlannedQuantity());
        scheduleToUpdate.setActualQuantity(productionSchedule.getActualQuantity());
        scheduleToUpdate.setStatus(productionSchedule.getStatus());
        scheduleToUpdate.setNotes(productionSchedule.getNotes());
        // createDate is not updated
        scheduleToUpdate.setUpdateDate(LocalDateTime.now()); // Update timestamp

        // Handle actual start/completion dates if they are being updated via this method
        if (productionSchedule.getActualStartDate() != null) {
            scheduleToUpdate.setActualStartDate(productionSchedule.getActualStartDate());
        }
        if (productionSchedule.getActualCompletionDate() != null) {
            scheduleToUpdate.setActualCompletionDate(productionSchedule.getActualCompletionDate());
        }

        LOGGER.info("Service: Attempting to save updated production schedule, ID: {}", productionSchedule.getScheduleId());
        try {
            return Optional.of(productionScheduleRepository.save(scheduleToUpdate));
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Service: Data integrity error while updating production schedule (ID: {}): {}", productionSchedule.getScheduleId(), e.getMessage(), e);
            throw new InvalidMaterialDataException("更新生產排程失敗：數據重複或不符合約束。", e);
        } catch (Exception e) {
            LOGGER.error("Service: Unexpected error while updating production schedule (ID: {}): {}", productionSchedule.getScheduleId(), e.getMessage(), e);
            throw new RuntimeException("無法更新生產排程：" + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public boolean deleteProductionSchedule(Integer scheduleId) {
        LOGGER.info("Service: Attempting to delete production schedule with ID {}.", scheduleId);

        // CORRECTED: Changed error message to be more accurate for ProductionSchedule ID
        if (scheduleId == null || scheduleId <= 0) {
            throw new IllegalArgumentException("排程ID無效，無法刪除。");
        }

        if (!productionScheduleRepository.existsById(scheduleId)) {
            LOGGER.warn("Service: Attempted to delete production schedule (ID: {}) that does not exist.", scheduleId);
            throw new ProductNotFoundException(scheduleId);
        }

        try {
            productionScheduleRepository.deleteById(scheduleId);
            LOGGER.info("Service: Successfully deleted production schedule with ID {}.", scheduleId);
            return true;
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Service: Data integrity error while deleting production schedule (ID: {}): {}", scheduleId, e.getMessage(), e);
            // This exception typically means a foreign key constraint violation.
            // You might want a specific exception like ProductionScheduleReferencedException if it's referenced by other entities.
            throw new RuntimeException("無法刪除生產排程 ID: " + scheduleId + "，因為它可能被其他數據引用。", e);
        } catch (Exception e) {
            LOGGER.error("Service: Unexpected error while deleting production schedule (ID: {}): {}", scheduleId, e.getMessage(), e);
            throw new RuntimeException("無法刪除生產排程：" + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Optional<ProductionSchedule> startProductionSchedule(Integer scheduleId, LocalDateTime actualStartDate) {
        LOGGER.info("Service: Attempting to start production schedule ID: {} with actual start date: {}.", scheduleId, actualStartDate);
        if (scheduleId == null || scheduleId <= 0) {
            throw new IllegalArgumentException("排程ID無效，必須大於0。");
        }
        if (actualStartDate == null) {
            throw new IllegalArgumentException("實際開始日期不能為空。");
        }

        Optional<ProductionSchedule> scheduleOptional = productionScheduleRepository.findById(scheduleId);
        if (scheduleOptional.isEmpty()) {
            LOGGER.warn("要開始的生產排程不存在，ID: {}", scheduleId);
            throw new ProductNotFoundException(scheduleId);
        }
        ProductionSchedule schedule = scheduleOptional.get();

        String currentStatus = schedule.getStatus();
        if (!"Planned".equalsIgnoreCase(currentStatus) && !"Cancelled".equalsIgnoreCase(currentStatus)) { // Allow starting from Planned or Cancelled
            throw new IllegalStateException("只有 '已計劃' 或 '已取消' 狀態的排程才能開始。當前狀態: " + currentStatus);
        }

        if (schedule.getScheduledDate() != null && actualStartDate.isBefore(schedule.getScheduledDate())) {
            LOGGER.warn("生產排程ID {}: 實際開始日期 {} 早於計劃日期 {}", scheduleId, actualStartDate, schedule.getScheduledDate());
        }

        schedule.setActualStartDate(actualStartDate);
        schedule.setStatus("In Progress");

        LOGGER.info("Service: Attempting to save started production schedule, ID: {}", scheduleId);
        return Optional.of(productionScheduleRepository.save(schedule));
    }

    @Override
    @Transactional
    public Optional<ProductionSchedule> completeProductionSchedule(Integer scheduleId, BigDecimal actualQuantity, LocalDateTime actualCompletionDate) {
        LOGGER.info("Service: Attempting to complete production schedule ID: {} with actual completion date: {}.", scheduleId, actualCompletionDate);
        if (scheduleId == null || scheduleId <= 0) {
            throw new IllegalArgumentException("排程ID無效，必須大於0。");
        }
        if (actualCompletionDate == null) {
            throw new IllegalArgumentException("實際完成日期不能為空。");
        }
        if (actualQuantity == null || actualQuantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("實際完成數量不能為空且必須大於或等於零。");
        }

        Optional<ProductionSchedule> scheduleOptional = productionScheduleRepository.findById(scheduleId);
        if (scheduleOptional.isEmpty()) {
            LOGGER.warn("要完成的生產排程不存在，ID: {}", scheduleId);
            throw new ProductNotFoundException(scheduleId);
        }
        ProductionSchedule schedule = scheduleOptional.get();

        String currentStatus = schedule.getStatus();
        if (!"In Progress".equalsIgnoreCase(currentStatus) && !"Planned".equalsIgnoreCase(currentStatus)) { // Allow completing from In Progress or Planned (if direct completion)
            throw new IllegalStateException("只有 '進行中' 或 '已計劃' 狀態的排程才能完成。當前狀態: " + currentStatus);
        }

        if (schedule.getActualStartDate() != null && actualCompletionDate.isBefore(schedule.getActualStartDate())) {
            LOGGER.warn("生產排程ID {}: 實際完成日期 {} 早於實際開始日期 {}", scheduleId, actualCompletionDate, schedule.getActualStartDate());
            throw new IllegalArgumentException("實際完成日期不能早於實際開始日期。");
        }

        schedule.setActualCompletionDate(actualCompletionDate);
        schedule.setActualQuantity(actualQuantity);
        schedule.setStatus("Completed");

        LOGGER.info("Service: Attempting to save completed production schedule, ID: {}", scheduleId);
        return Optional.of(productionScheduleRepository.save(schedule));
    }

    @Transactional(readOnly = true)
    public List<ProductionSchedule> getProductionSchedulesByDate(LocalDateTime scheduledDate) {
        LOGGER.info("Service: Attempting to get production schedules by date: {}.", scheduledDate);
        // For range queries, you might need to adjust the repository method or perform date range logic here.
        // Assuming findByScheduledDate can handle exact matches or is adjusted in repository for date part only.
        List<ProductionSchedule> schedules = productionScheduleRepository.findByScheduledDate(scheduledDate);
        schedules.forEach(schedule -> {
            if (schedule.getWorkOrderId() != null) {
                workOrderRepository.findById(schedule.getWorkOrderId()).ifPresent(wo ->
                    schedule.setWorkOrderNumber(wo.getWorkOrderNumber())
                );
            }
            if (schedule.getProductId() != null) {
                productRepository.findById(schedule.getProductId()).ifPresent(p -> {
                    schedule.setProductCode(p.getProductCode());
                    schedule.setProductName(p.getProductName());
                });
            }
        });
        LOGGER.info("Service: Retrieved {} production schedules for date: {}.", schedules.size(), scheduledDate);
        return schedules;
    }

}
