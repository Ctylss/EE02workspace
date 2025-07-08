package com.example.exception;

/**
 * Custom exception indicating that a Material cannot be deleted because it is
 * referenced by other entities in the system (e.g., BOM items, inventory records, production schedules).
 * This is an unchecked exception (extends RuntimeException).
 */
public class MaterialReferencedException extends RuntimeException {

    private final Integer materialId;

    // Default constructor
    public MaterialReferencedException() {
        super("物料無法刪除，因為它被其他數據引用。");
        this.materialId = null;
    }

    // Constructor with the ID of the referenced material
    public MaterialReferencedException(Integer materialId) {
        super("物料 ID: " + materialId + " 無法刪除，因為它被其他數據引用。");
        this.materialId = materialId;
    }

    // Constructor with a custom message
    public MaterialReferencedException(String message) {
        super(message);
        this.materialId = null;
    }

    // Constructor with a custom message and an underlying cause
    public MaterialReferencedException(String message, Throwable cause) {
        super(message, cause);
        this.materialId = null;
    }

    // Constructor with material ID, custom message, and cause
    public MaterialReferencedException(Integer materialId, String message, Throwable cause) {
        super(message, cause);
        this.materialId = materialId;
    }

    /**
     * Returns the ID of the Material that could not be deleted due to references.
     * @return The Material ID, or null if not set.
     */
    public Integer getMaterialId() {
        return materialId;
    }
}