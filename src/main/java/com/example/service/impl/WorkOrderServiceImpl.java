package com.example.service.impl;

import com.example.model.WorkOrder;
import com.example.model.Product; // Still needed for productRepository interaction
import com.example.dao.WorkOrderRepository;
import com.example.dao.ProductRepository;
import com.example.service.WorkOrderService;
import com.example.exception.InvalidWorkOrderDataException;
import com.example.exception.WorkOrderNotFoundException;
import com.example.exception.ProductNotFoundException;

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
 * WorkOrderService interface implementation.
 * Responsible for work order related business logic and calling Spring Data JPA Repository for database operations.
 */
@Service
public class WorkOrderServiceImpl implements WorkOrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkOrderServiceImpl.class);

    private final WorkOrderRepository workOrderRepository;
    private final ProductRepository productRepository; // Still needed to validate productId existence

    private static final List<String> VALID_STATUSES = Arrays.asList("Pending", "In Progress", "Completed", "Cancelled");

    @Autowired
    public WorkOrderServiceImpl(WorkOrderRepository workOrderRepository, ProductRepository productRepository) {
        this.workOrderRepository = workOrderRepository;
        this.productRepository = productRepository;
        LOGGER.info("WorkOrderServiceImpl has been initialized, with repositories injected via constructor.");
    }

    /**
     * Helper method for common work order validation logic.
     * Throws InvalidWorkOrderDataException if validation fails.
     *
     * IMPORTANT: This method now aligns with WorkOrder having a @ManyToOne Product relationship.
     */
    private void validateWorkOrder(WorkOrder workOrder, boolean isNew) {
        Map<String, String> fieldErrors = new HashMap<>();

        if (workOrder == null) {
            throw new InvalidWorkOrderDataException("工單物件不能為空。");
        }

        // Basic field checks
        if (workOrder.getWorkOrderNumber() == null || workOrder.getWorkOrderNumber().trim().isEmpty()) {
            fieldErrors.put("workOrderNumber", "工單編號為必填項。");
        }

        // Validate Product entity and its ID
        if (workOrder.getProduct() == null || workOrder.getProduct().getProductId() == null || workOrder.getProduct().getProductId() <= 0) {
            fieldErrors.put("productId", "產品信息無效或產品ID無效，必須是正數。");
        } else {
            // Validate if product actually exists
            Optional<Product> productOptional = productRepository.findById(workOrder.getProduct().getProductId());
            if (productOptional.isEmpty()) {
                fieldErrors.put("productId", "指定產品ID: " + workOrder.getProduct().getProductId() + " 不存在。");
            } else {
                // Set the actual managed Product entity on the WorkOrder object
                // This is crucial for JPA to correctly manage the relationship
                workOrder.setProduct(productOptional.get());

                // Validate unit consistency with product
                if (workOrder.getProduct().getUnit() != null && !((String) workOrder.getProduct().getUnit()).trim().isEmpty()) {
                    if (workOrder.getUnit() == null || !workOrder.getUnit().equalsIgnoreCase((String) workOrder.getProduct().getUnit())) {
                        fieldErrors.put("unit", "工單單位必須與產品單位 '" + workOrder.getProduct().getUnit() + "' 匹配。");
                    }
                } else if (workOrder.getUnit() == null || workOrder.getUnit().trim().isEmpty()){
                    fieldErrors.put("unit", "工單單位不能為空。"); // If product unit is not defined, ensure work order unit is set manually
                }
            }
        }

        if (workOrder.getQuantity() == null || workOrder.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            fieldErrors.put("quantity", "計劃數量不能為空且必須大於零。");
        }
        if (workOrder.getScheduledStartDate() == null) {
            fieldErrors.put("scheduledStartDate", "計劃開始日期為必填項。");
        }
        if (workOrder.getScheduledDueDate() == null) {
            fieldErrors.put("scheduledDueDate", "計劃預計完成日期為必填項。");
        }
        if (workOrder.getScheduledStartDate() != null && workOrder.getScheduledDueDate() != null &&
            workOrder.getScheduledStartDate().isAfter(workOrder.getScheduledDueDate())) {
            fieldErrors.put("scheduledDates", "計劃開始日期不能晚於計劃預計完成日期。");
        }
        if (workOrder.getStatus() == null || workOrder.getStatus().trim().isEmpty()) {
            fieldErrors.put("status", "工單狀態為必填項。");
        } else if (!VALID_STATUSES.contains(workOrder.getStatus())) {
            fieldErrors.put("status", "無效的工單狀態: " + workOrder.getStatus());
        }

        // Validate work order number uniqueness
        if (workOrder.getWorkOrderNumber() != null && !workOrder.getWorkOrderNumber().trim().isEmpty()) {
            Optional<WorkOrder> existingWoByNumber = workOrderRepository.findByWorkOrderNumber(workOrder.getWorkOrderNumber());
            if (existingWoByNumber.isPresent()) {
                // If it's a new work order, or an existing one with a different ID
                if (isNew || !existingWoByNumber.get().getWorkOrderId().equals(workOrder.getWorkOrderId())) {
                    fieldErrors.put("workOrderNumber", "工單編號 '" + workOrder.getWorkOrderNumber() + "' 已存在。");
                }
            }
        }

        if (!fieldErrors.isEmpty()) {
            throw new InvalidWorkOrderDataException("工單數據驗證失敗。", fieldErrors);
        }
    }


    @Override
    @Transactional
    public WorkOrder createWorkOrder(WorkOrder workOrder) throws IllegalArgumentException {
        LOGGER.info("Service: Attempting to create work order.");
        // Validate and ensure Product entity is set on workOrder
        validateWorkOrder(workOrder, true); // True for a new work order

        // Ensure ID is null for new entity (handled by JPA @GeneratedValue)
        workOrder.setWorkOrderId(null);

        try {
            WorkOrder savedWorkOrder = workOrderRepository.save(workOrder);
            LOGGER.info("Service: Successfully created work order (ID: {}, Number: {}).", savedWorkOrder.getWorkOrderId(), savedWorkOrder.getWorkOrderNumber());
            return savedWorkOrder;
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Service: Data integrity error while creating work order: {}", e.getMessage(), e);
            throw new InvalidWorkOrderDataException("創建工單失敗：數據重複或不符合約束。", e);
        } catch (Exception e) {
            LOGGER.error("Service: Unexpected error while creating work order: {}", e.getMessage(), e);
            throw new RuntimeException("無法創建工單：" + e.getMessage(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<WorkOrder> getWorkOrderById(Integer workOrderId) throws IllegalArgumentException {
        LOGGER.info("Service: Attempting to get work order by ID: {}.", workOrderId);
        if (workOrderId == null || workOrderId <= 0) {
            LOGGER.warn("獲取工單時ID無效: {}", workOrderId);
            throw new IllegalArgumentException("工單ID無效，必須是正數。");
        }
        Optional<WorkOrder> workOrder = workOrderRepository.findById(workOrderId);
        if (workOrder.isPresent()) {
            LOGGER.info("Service: Found work order with ID: {}.", workOrderId);
        } else {
            LOGGER.warn("Service: Work order with ID: {} not found.", workOrderId);
        }
        return workOrder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> getAllWorkOrders() {
        LOGGER.info("Service: Attempting to get all work orders.");
        List<WorkOrder> workOrders = workOrderRepository.findAll();
        LOGGER.info("Service: Retrieved {} work orders.", workOrders.size());
        return workOrders;
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> getWorkOrdersByStatus(String status) throws IllegalArgumentException {
        LOGGER.info("Service: Attempting to get work orders by status: {}.", status);
        if (status == null || status.trim().isEmpty() || !VALID_STATUSES.contains(status)) {
            LOGGER.warn("獲取工單時狀態無效或為空: {}", status);
            throw new IllegalArgumentException("無效或為空的工單狀態。");
        }
        List<WorkOrder> workOrders = workOrderRepository.findByStatus(status);
        LOGGER.info("Service: Retrieved {} work orders with status: {}.", workOrders.size(), status);
        return workOrders;
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkOrder> getWorkOrdersByProductId(Integer productId) throws IllegalArgumentException {
        LOGGER.info("Service: Attempting to get work orders by product ID: {}.", productId);
        if (productId == null || productId <= 0) {
            LOGGER.warn("獲取工單時產品ID無效: {}", productId);
            throw new IllegalArgumentException("產品ID無效，必須大於0。");
        }
        if (!productRepository.existsById(productId)) {
            LOGGER.warn("獲取工單失敗: 產品ID {} 不存在。", productId);
            throw new ProductNotFoundException(productId);
        }
        // Using the correct repository method for ManyToOne relationship
        List<WorkOrder> workOrders = workOrderRepository.findByProduct_ProductId(productId);
        LOGGER.info("Service: Retrieved {} work orders for product ID: {}.", workOrders.size(), productId);
        return workOrders;
    }

    @Override
    @Transactional
    public Optional<WorkOrder> updateWorkOrder(WorkOrder workOrder) throws IllegalArgumentException {
        LOGGER.info("Service: Attempting to update work order with ID: {}.", workOrder != null ? workOrder.getWorkOrderId() : "null");

        if (workOrder == null || workOrder.getWorkOrderId() == null || workOrder.getWorkOrderId() <= 0) {
            LOGGER.warn("更新工單時，工單物件和有效ID不能為空。工單ID: {}", workOrder != null ? workOrder.getWorkOrderId() : "null");
            throw new IllegalArgumentException("工單ID不能為空且必須是正數。");
        }

        Optional<WorkOrder> existingWorkOrderOptional = workOrderRepository.findById(workOrder.getWorkOrderId());
        if (existingWorkOrderOptional.isEmpty()) {
            LOGGER.warn("更新工單失敗，工單ID不存在: {}", workOrder.getWorkOrderId());
            throw new WorkOrderNotFoundException(workOrder.getWorkOrderId());
        }
        WorkOrder workOrderToUpdate = existingWorkOrderOptional.get();

        // Validate and ensure Product entity is set on the incoming workOrder object
        validateWorkOrder(workOrder, false); // False for an existing work order

        // Manually copy updated fields to the managed entity
        workOrderToUpdate.setWorkOrderNumber(workOrder.getWorkOrderNumber());
        workOrderToUpdate.setProduct(workOrder.getProduct()); // Set the updated Product entity
        workOrderToUpdate.setQuantity(workOrder.getQuantity());
        workOrderToUpdate.setUnit(workOrder.getUnit());
        workOrderToUpdate.setScheduledStartDate(workOrder.getScheduledStartDate());
        workOrderToUpdate.setScheduledDueDate(workOrder.getScheduledDueDate());
        workOrderToUpdate.setNotes(workOrder.getNotes());

        // Handle actual dates and status updates carefully based on business rules
        if (workOrder.getActualStartDate() != null) {
            workOrderToUpdate.setActualStartDate(workOrder.getActualStartDate());
        }
        if (workOrder.getActualCompletionDate() != null) {
            workOrderToUpdate.setActualCompletionDate(workOrder.getActualCompletionDate());
        }
        workOrderToUpdate.setStatus(workOrder.getStatus()); // Status should be handled by specific status change methods usually


        LOGGER.info("Service: Attempting to save updated work order, ID: {}", workOrder.getWorkOrderId());
        try {
            return Optional.of(workOrderRepository.save(workOrderToUpdate));
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Service: Data integrity error while updating work order (ID: {}): {}", workOrder.getWorkOrderId(), e.getMessage(), e);
            throw new InvalidWorkOrderDataException("更新工單失敗：數據重複或不符合約束。", e);
        } catch (Exception e) {
            LOGGER.error("Service: Unexpected error while updating work order (ID: {}): {}", workOrder.getWorkOrderId(), e.getMessage(), e);
            throw new RuntimeException("無法更新工單：" + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public boolean deleteWorkOrder(Integer workOrderId) throws IllegalArgumentException {
        LOGGER.info("Service: Attempting to delete work order with ID: {}.", workOrderId);
        if (workOrderId == null || workOrderId <= 0) {
            LOGGER.warn("刪除工單時ID無效: {}", workOrderId);
            throw new IllegalArgumentException("工單ID無效，必須大於0。");
        }

        if (!workOrderRepository.existsById(workOrderId)) {
            LOGGER.warn("刪除工單失敗，工單ID不存在: {}", workOrderId);
            throw new WorkOrderNotFoundException(workOrderId);
        }

        try {
            workOrderRepository.deleteById(workOrderId);
            LOGGER.info("Service: Successfully deleted work order with ID: {}.", workOrderId);
            return true;
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Service: Data integrity error while deleting work order (ID: {}): {}", workOrderId, e.getMessage(), e);
            throw new RuntimeException("無法刪除工單 ID: " + workOrderId + "，因為它可能被其他數據引用。", e);
        } catch (Exception e) {
            LOGGER.error("Service: Unexpected error while deleting work order (ID: {}): {}", workOrderId, e.getMessage(), e);
            throw new RuntimeException("無法刪除工單：" + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Optional<WorkOrder> startWorkOrder(Integer workOrderId, LocalDateTime actualStartDate) throws IllegalArgumentException, IllegalStateException {
        LOGGER.info("Service: Attempting to start work order ID: {} with actual start date: {}.", workOrderId, actualStartDate);
        if (workOrderId == null || workOrderId <= 0) {
            throw new IllegalArgumentException("工單ID無效，必須大於0。");
        }
        if (actualStartDate == null) {
            throw new IllegalArgumentException("實際開始日期不能為空。");
        }

        Optional<WorkOrder> workOrderOptional = workOrderRepository.findById(workOrderId);
        if (workOrderOptional.isEmpty()) {
            LOGGER.warn("要開始的工單不存在，ID: {}", workOrderId);
            throw new WorkOrderNotFoundException(workOrderId);
        }
        WorkOrder workOrder = workOrderOptional.get();

        String currentStatus = workOrder.getStatus();
        if (!"Pending".equalsIgnoreCase(currentStatus) && !"New".equalsIgnoreCase(currentStatus)) {
            throw new IllegalStateException("只有 '待處理' 或 '新建' 狀態的工單才能開始。當前狀態: " + currentStatus);
        }

        if (workOrder.getScheduledStartDate() != null && actualStartDate.isBefore(workOrder.getScheduledStartDate())) {
            LOGGER.warn("工單ID {}: 實際開始日期 {} 早於計劃開始日期 {}", workOrderId, actualStartDate, workOrder.getScheduledStartDate());
        }

        workOrder.setActualStartDate(actualStartDate);
        workOrder.setStatus("In Progress");

        LOGGER.info("Service: Attempting to save started work order, ID: {}", workOrderId);
        return Optional.of(workOrderRepository.save(workOrder));
    }

    @Override
    @Transactional
    public Optional<WorkOrder> completeWorkOrder(Integer workOrderId, LocalDateTime actualCompletionDate) throws IllegalArgumentException, IllegalStateException {
        LOGGER.info("Service: Attempting to complete work order ID: {} with actual completion date: {}.", workOrderId, actualCompletionDate);
        if (workOrderId == null || workOrderId <= 0) {
            throw new IllegalArgumentException("工單ID無效，必須大於0。");
        }
        if (actualCompletionDate == null) {
            throw new IllegalArgumentException("實際完成日期不能為空。");
        }

        Optional<WorkOrder> workOrderOptional = workOrderRepository.findById(workOrderId);
        if (workOrderOptional.isEmpty()) {
            LOGGER.warn("要完成的工單不存在，ID: {}", workOrderId);
            throw new WorkOrderNotFoundException(workOrderId);
        }
        WorkOrder workOrder = workOrderOptional.get();

        String currentStatus = workOrder.getStatus();
        if (!"In Progress".equalsIgnoreCase(currentStatus)) {
            throw new IllegalStateException("只有 '進行中' 狀態的工單才能完成。當前狀態: " + currentStatus);
        }

        if (workOrder.getActualStartDate() != null && actualCompletionDate.isBefore(workOrder.getActualStartDate())) {
            LOGGER.warn("工單ID {}: 實際完成日期 {} 早於實際開始日期 {}", workOrderId, actualCompletionDate, workOrder.getActualStartDate());
            throw new IllegalArgumentException("實際完成日期不能早於實際開始日期。");
        }

        workOrder.setActualCompletionDate(actualCompletionDate);
        workOrder.setStatus("Completed");

        LOGGER.info("Service: Attempting to save completed work order, ID: {}", workOrderId);
        return Optional.of(workOrderRepository.save(workOrder));
    }
}
