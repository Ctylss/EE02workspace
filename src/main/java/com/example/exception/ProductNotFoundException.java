package com.example.exception;

/**
 * Custom exception indicating that a product with a given ID was not found.
 * For example: thrown when querying, updating, or deleting a product by ID, and the product does not exist.
 * This is an unchecked exception (extends RuntimeException).
 */
public class ProductNotFoundException extends RuntimeException {

    private final Integer productId; // Storing the problematic product ID, consistent with Integer type

    // Default constructor, providing a default message
    public ProductNotFoundException() {
        super("產品未找到。");
        this.productId = null; // No specific ID associated with this general message
    }

    /**
     * Constructor with a specific product ID. Automatically generates a more specific message.
     * @param productId The ID of the product that was not found.
     */
    public ProductNotFoundException(Integer productId) { // Changed parameter from Long to Integer
        super("產品 ID: " + productId + " 未找到。");
        this.productId = productId;
    }

    // Constructor accepting a custom message
    public ProductNotFoundException(String message) {
        super(message);
        this.productId = null;
    }

    // Constructor accepting a custom message and an underlying cause
    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.productId = null;
    }

    /**
     * Constructor with a product ID, custom message, and an underlying cause.
     * Useful for providing more specific details along with the ID and original error.
     * @param productId The ID of the product that was not found.
     * @param message The detail message.
     * @param cause The cause of the exception.
     */
    public ProductNotFoundException(Integer productId, String message, Throwable cause) {
        super(message, cause);
        this.productId = productId;
    }

    /**
     * Returns the ID of the product that was not found, if available.
     * @return The product ID, or null if not set.
     */
    public Integer getProductId() {
        return productId;
    }
}
