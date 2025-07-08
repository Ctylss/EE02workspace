package com.example.exception;

/**
 * Custom exception indicating that a Material with a given ID was not found.
 * This is an unchecked exception (extends RuntimeException).
 */
public class MaterialNotFoundException extends RuntimeException {

    private final Integer materialId; // Consistent with Material entity's ID type

    // Default constructor
    public MaterialNotFoundException() {
        super("物料未找到。");
        this.materialId = null;
    }

    // Constructor with a specific Material ID
    public MaterialNotFoundException(Integer materialId) {
        super("物料 ID: " + materialId + " 未找到。");
        this.materialId = materialId;
    }

    // Constructor with a custom message
    public MaterialNotFoundException(String message) {
        super(message);
        this.materialId = null;
    }

    // Constructor with a custom message and an underlying cause
    public MaterialNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.materialId = null;
    }

    // Constructor with Material ID, custom message, and cause
    public MaterialNotFoundException(Integer materialId, String message, Throwable cause) {
        super(message, cause);
        this.materialId = materialId;
    }

    /**
     * Returns the ID of the Material that was not found, if available.
     * @return The Material ID, or null if not set.
     */
    public Integer getMaterialId() {
        return materialId;
    }
}
