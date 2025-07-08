package com.example.exception;

import java.util.Collections;
import java.util.Map;

/**
 * Custom exception indicating that the provided Work Order data is invalid.
 * This can include missing required fields, invalid formats, or business rule violations.
 * It's an unchecked exception, extending RuntimeException.
 */
public class InvalidWorkOrderDataException extends RuntimeException {

    private final Map<String, String> fieldErrors;

    // Default constructor
    public InvalidWorkOrderDataException() {
        super("工單數據無效。");
        this.fieldErrors = Collections.emptyMap();
    }

    // Constructor with a custom message
    public InvalidWorkOrderDataException(String message) {
        super(message);
        this.fieldErrors = Collections.emptyMap();
    }

    // Constructor with a map of field-specific errors
    public InvalidWorkOrderDataException(Map<String, String> fieldErrors) {
        super("工單數據驗證失敗。請檢查詳細錯誤。");
        this.fieldErrors = Collections.unmodifiableMap(fieldErrors); // Make map unmodifiable
    }

    // Constructor with a custom message and a map of field-specific errors
    public InvalidWorkOrderDataException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.fieldErrors = Collections.unmodifiableMap(fieldErrors);
    }

    // Constructor with a custom message and a cause
    public InvalidWorkOrderDataException(String message, Throwable cause) {
        super(message, cause);
        this.fieldErrors = Collections.emptyMap();
    }

    // Constructor with a custom message, field errors, and a cause
    public InvalidWorkOrderDataException(String message, Map<String, String> fieldErrors, Throwable cause) {
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

    public String getErrors() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getErrors'");
    }

    // Removed redundant and erroneous getErrors() method.
}
