package com.example.exception;

/**
 * Custom exception indicating that a product cannot be deleted because it is
 * referenced by other entities (e.g., work orders, production schedules, inventory records).
 * This is an unchecked exception (extends RuntimeException) to avoid forcing
 * calling methods to catch it, which is often suitable for business rule violations.
 */
public class ProductReferencedException extends RuntimeException {

    private final Integer productId; // Storing the problematic product ID, consistent with Integer type

    // Default constructor with a predefined message
    public ProductReferencedException() {
        super("產品無法刪除，因為它被其他數據引用。");
        this.productId = null; // No specific ID associated with this general message
    }

    // Constructor with a custom message
    public ProductReferencedException(String message) {
        super(message);
        this.productId = null;
    }

    /**
     * Constructor for a ProductReferencedException with a specific product ID.
     * Generates a default message including the product ID.
     * @param productId The ID of the product that cannot be deleted.
     */
    public ProductReferencedException(Integer productId) { // Changed parameter from Long to Integer
        super("產品 ID: " + productId + " 無法刪除，因為它被其他數據引用。");
        this.productId = productId;
    }

    /**
     * Constructor with a custom message and an underlying cause.
     * @param message The detail message.
     * @param cause The cause of the exception.
     */
    public ProductReferencedException(String message, Throwable cause) {
        super(message, cause);
        this.productId = null; // No specific ID for this constructor variant
    }

    /**
     * Constructor with a specific product ID and a custom message.
     * @param productId The ID of the product that cannot be deleted.
     * @param message The detail message.
     */
    public ProductReferencedException(Integer productId, String message) {
        super(message);
        this.productId = productId;
    }

    /**
     * Constructor with a specific product ID, custom message, and an underlying cause.
     * @param productId The ID of the product that cannot be deleted.
     * @param message The detail message.
     * @param cause The cause of the exception.
     */
    public ProductReferencedException(Integer productId, String message, Throwable cause) {
        super(message, cause);
        this.productId = productId;
    }

    /**
     * Returns the ID of the product that caused this exception, if available.
     * @return The product ID, or null if not set.
     */
    public Integer getProductId() {
        return productId;
    }
}
