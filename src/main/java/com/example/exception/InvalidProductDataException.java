 package com.example.exception;

import java.util.HashMap;
import java.util.Map;

public class InvalidProductDataException extends RuntimeException {

    private Map<String, String> fieldErrors;

    // (原有的) 基本建構子，只包含總體訊息
    public InvalidProductDataException(String message) {
        super(message);
        this.fieldErrors = new HashMap<>();
    }

    // (原有的) 建構子，包含總體訊息和字段錯誤
    public InvalidProductDataException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors != null ? fieldErrors : new HashMap<>();
    }

    // *** 新增的建構子：接受訊息和一個 Throwable cause ***
    public InvalidProductDataException(String message, Throwable cause) {
        super(message, cause); // 調用父類 RuntimeException 的建構子
        this.fieldErrors = new HashMap<>(); // 初始化字段錯誤，或者你可以選擇讓它為 null
    }

    // *** 可選地：只接受 Throwable cause 的建構子 (如果不需要自定義訊息) ***
    public InvalidProductDataException(Throwable cause) {
        super(cause);
        this.fieldErrors = new HashMap<>();
    }

    // --- 自定義方法 ---
    public void addFieldError(String fieldName, String errorMessage) {
        if (this.fieldErrors == null) {
            this.fieldErrors = new HashMap<>();
        }
        this.fieldErrors.put(fieldName, errorMessage);
    }

    // --- Getters ---
    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
