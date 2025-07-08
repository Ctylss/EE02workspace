package com.example.exception;

import java.util.Collections;
import java.util.Map;

/**
 * Custom exception indicating that the provided Material data is invalid.
 * This can include missing required fields, invalid formats, or business rule violations.
 * It's an unchecked exception, extending RuntimeException.
 */
public class InvalidMaterialDataException extends RuntimeException {

    private final Map<String, String> fieldErrors;

    // Default constructor
    public InvalidMaterialDataException() {
        super("物料數據無效。");
        this.fieldErrors = Collections.emptyMap();
    }

    // Constructor with a custom message
    public InvalidMaterialDataException(String message) {
        super(message);
        this.fieldErrors = Collections.emptyMap();
    }

    // Constructor with a map of field-specific errors
    public InvalidMaterialDataException(Map<String, String> fieldErrors) {
        super("物料數據驗證失敗。請檢查詳細錯誤。");
        // Ensure the map is unmodifiable to prevent external modification
        this.fieldErrors = Collections.unmodifiableMap(fieldErrors);
    }

    // Constructor with a custom message and a map of field-specific errors
    public InvalidMaterialDataException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.fieldErrors = Collections.unmodifiableMap(fieldErrors);
    }

    // Constructor with a custom message and a cause
    public InvalidMaterialDataException(String message, Throwable cause) {
        super(message, cause);
        this.fieldErrors = Collections.emptyMap();
    }

    // Constructor with a custom message, field errors, and a cause
    public InvalidMaterialDataException(String message, Map<String, String> fieldErrors, Throwable cause) {
        super(message, cause);
        this.fieldErrors = Collections.unmodifiableMap(fieldErrors);
    }

    /**
     * Returns a map of field names to their corresponding error messages.
     * @return An unmodifiable map of field errors.
     */
    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public Map<String, String> getErrors() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getErrors'");
    }
}
