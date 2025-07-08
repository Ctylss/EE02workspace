package com.example.exception;

/**
 * Custom exception indicating that a Work Order with a given ID was not found.
 * This is an unchecked exception (extends RuntimeException).
 */
public class WorkOrderNotFoundException extends RuntimeException {

    private final Integer workOrderId;

    // Default constructor
    public WorkOrderNotFoundException() {
        super("工單未找到。");
        this.workOrderId = null;
    }

    // Constructor with a specific Work Order ID
    public WorkOrderNotFoundException(Integer workOrderId) {
        super("工單 ID: " + workOrderId + " 未找到。");
        this.workOrderId = workOrderId;
    }

    // Constructor with a custom message
    public WorkOrderNotFoundException(String message) {
        super(message);
        this.workOrderId = null;
    }

    // Constructor with a custom message and an underlying cause
    public WorkOrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.workOrderId = null;
    }

    // Constructor with Work Order ID, custom message, and cause
    public WorkOrderNotFoundException(Integer workOrderId, String message, Throwable cause) {
        super(message, cause);
        this.workOrderId = workOrderId;
    }

    /**
     * Returns the ID of the Work Order that was not found, if available.
     * @return The Work Order ID, or null if not set.
     */
    public Integer getWorkOrderId() {
        return workOrderId;
    }
}
