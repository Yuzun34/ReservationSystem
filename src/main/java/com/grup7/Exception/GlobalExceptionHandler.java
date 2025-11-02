package com.grup7.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {


    @Data
    public static class ApiError {
        private final int status;       // HTTP durum kodu
        private final String message;   // Hata mesajı
        private Map<String, String> errors; // Detaylı hata açıklamaları

        // Temel hata bilgisi için constructor
        public ApiError(int status, String message) {
            this.status = status;
            this.message = message;
        }

        // Detaylı hata bilgisi için constructor
        public ApiError(int status, String message, Map<String, String> errors) {
            this.status = status;
            this.message = message;
            this.errors = errors;
        }
    }

    // Masa bulunamadığında fırlatılan hata için handler
    @ExceptionHandler(TableNotFoundException.class)
    public ResponseEntity<ApiError> handleTableNotFoundException(TableNotFoundException ex) {
        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // Masa zaten rezerve edilmiş hatası için handler
    @ExceptionHandler(TableAlreadyReservedException.class)
    public ResponseEntity<ApiError> handleTableAlreadyReservedException(TableAlreadyReservedException ex) {
        ApiError error = new ApiError(
                HttpStatus.CONFLICT.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // Rezervasyon işlemi hatası için handler
    @ExceptionHandler(ReservationException.class)
    public ResponseEntity<ApiError> handleReservationException(ReservationException ex) {
        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Doğrulama hatası için handler
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(ValidationException ex) {
        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Sipariş log hatası için handler
    @ExceptionHandler(OrderLogException.class)
    public ResponseEntity<ApiError> handleOrderLogException(OrderLogException ex) {
        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Eksik HTTP başlığı hatası için handler
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiError> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Eksik başlık: " + ex.getHeaderName()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Metod argümanı doğrulama hatası için handler
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Doğrulama hatası",
                errors
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Parametre tipi uyuşmazlığı hatası için handler
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Parametre tipi uyuşmazlığı: " + ex.getName() + " parametresi için geçersiz değer"
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Geçersiz argüman hatası için handler
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Geçersiz argüman: " + ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Yakalanmayan tüm hatalar için genel handler
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllUncaughtException(Exception ex) {
        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Beklenmeyen bir hata oluştu: " + ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}