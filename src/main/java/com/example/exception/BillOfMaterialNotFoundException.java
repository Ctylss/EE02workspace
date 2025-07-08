package com.example.exception;

/**
 * Custom exception indicating that a BillOfMaterial item with a given ID was not found.
 * This is an unchecked exception (extends RuntimeException).
 */
public class BillOfMaterialNotFoundException extends RuntimeException {

    private final Integer bomId; // Consistent with BillOfMaterial entity's ID type

    // Default constructor
    public BillOfMaterialNotFoundException() {
        super("產品用料清單（BOM）項目未找到。");
        this.bomId = null;
    }

    // Constructor with a specific BOM ID
    public BillOfMaterialNotFoundException(Integer bomId) {
        super("產品用料清單（BOM）項目 ID: " + bomId + " 未找到。");
        this.bomId = bomId;
    }

    // Constructor with a custom message
    public BillOfMaterialNotFoundException(String message) {
        super(message);
        this.bomId = null;
    }

    // Constructor with a custom message and an underlying cause
    public BillOfMaterialNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.bomId = null;
    }

    // Constructor with BOM ID, custom message, and cause
    public BillOfMaterialNotFoundException(Integer bomId, String message, Throwable cause) {
        super(message, cause);
        this.bomId = bomId;
    }

    /**
     * Returns the ID of the BillOfMaterial item that was not found, if available.
     * @return The BOM ID, or null if not set.
     */
    public Integer getBomId() {
        return bomId;
    }
}