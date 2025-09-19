package com.project.library.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.project.library.models.WrapperResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<WrapperResponse<String>> handleException(
            HttpServletRequest request,
            Exception exception) {
        WrapperResponse<String> wrapperResponse = new WrapperResponse<>(
                false, exception.getMessage());
        log.error("Exception occurred while hitting request : {}\n{}",
                  request.getRequestURI(), exception.getMessage());
        return new ResponseEntity<>(wrapperResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<WrapperResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> fieldErrors
                .put(error.getField(), error.getDefaultMessage()));
        WrapperResponse<Map<String, String>> wrapperResponse = new WrapperResponse<>(
                false, fieldErrors);
        return new ResponseEntity<>(wrapperResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<WrapperResponse<String>> handleInvalidFormat(
            HttpMessageNotReadableException ex) {
        String message = "Unable to parse Data, Please check all parameters";

        if (ex.getCause() instanceof InvalidFormatException invalidFormat) {
            String fieldName = invalidFormat.getPath().get(0).getFieldName();
            String target = invalidFormat.getTargetType().getSimpleName();
            message = String.format("Field %s must be a valid %s", fieldName, target);
        }
        WrapperResponse<String> wrapperResponse = new WrapperResponse<>(
                false, message);
        return new ResponseEntity<>(wrapperResponse, HttpStatus.BAD_REQUEST);
    }

}
