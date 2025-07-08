package com.example.api.error;

import com.fasterxml.jackson.annotation.JsonFormat; // 導入用於日期格式化的註解
import org.springframework.http.HttpStatus; // 導入 Spring 的 HttpStatus 枚舉

import java.time.LocalDateTime; // 導入 Java 8 的日期時間 API

/**
 * 標準化的 API 錯誤響應 DTO (Data Transfer Object)。
 * 用於在發生異常時，向客戶端返回一致的 JSON 錯誤格式。
 */
public class ErrorResponse {

    // 錯誤發生的時間戳，以指定格式序列化為字串
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    // HTTP 狀態碼 (例如：404, 500)
    private int status;

    // HTTP 狀態碼的文字描述 (例如：Not Found, Internal Server Error)
    private String error;

    // 詳細的錯誤訊息
    private String message;

    // 發生錯誤的請求路徑
    private String path;

    // 可選：用於字段驗證錯誤的詳細信息，例如 InvalidProductDataException 會用到
    // 如果你的錯誤響應需要這個字段，請取消註釋並添加 Getter/Setter
    // private Map<String, String> fieldErrors;

    /**
     * 私有建構子，用於初始化時間戳。
     * 通常由其他公共建構子調用。
     */
    private ErrorResponse() {
        this.timestamp = LocalDateTime.now(); // 初始化為當前時間
    }

    /**
     * 主建構子，用於構建錯誤響應。
     *
     * @param status  HTTP 狀態碼
     * @param message 錯誤訊息
     * @param path    請求路徑
     */
    public ErrorResponse(HttpStatus status, String message, String path) {
        this(); // 調用私有建構子設置時間戳
        this.status = status.value(); // 獲取 HttpStatus 的整數值
        this.error = status.getReasonPhrase(); // 獲取 HttpStatus 的文字描述
        this.message = message;
        this.path = path;
    }

    // --- Getter 和 Setter 方法 ---
    // (通常使用 Lombok 自動生成這些，但這裡手動提供以供參考)

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    // 如果你取消註釋了 fieldErrors 字段，請添加以下 Getter 和 Setter
    // public Map<String, String> getFieldErrors() {
    //     return fieldErrors;
    // }
    //
    // public void setFieldErrors(Map<String, String> fieldErrors) {
    //     this.fieldErrors = fieldErrors;
    // }

    @Override
    public String toString() {
        return "ErrorResponse{" +
               "timestamp=" + timestamp +
               ", status=" + status +
               ", error='" + error + '\'' +
               ", message='" + message + '\'' +
               ", path='" + path + '\'' +
               // (如果 fieldErrors 存在) + ", fieldErrors=" + fieldErrors +
               '}';
    }
}

