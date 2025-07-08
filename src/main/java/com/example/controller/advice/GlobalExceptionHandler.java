package com.example.controller.advice; // 建議的套件路徑

import com.example.exception.InvalidProductDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // 讓這個類能夠處理所有 Controller 的異常
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidProductDataException.class)
    public ResponseEntity<String> handleInvalidProductDataException(InvalidProductDataException ex) {
        // 返回 HTTP 400 Bad Request
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        // 或者返回更複雜的錯誤響應對象
    }

    // 你也可以處理其他通用異常
    // @ExceptionHandler(Exception.class)
    // public ResponseEntity<String> handleGeneralException(Exception ex) {
    //     return new ResponseEntity<>("An unexpected error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    // }
}