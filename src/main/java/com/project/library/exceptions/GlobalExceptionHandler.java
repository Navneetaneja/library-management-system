package com.project.library.exceptions;

import com.project.library.models.WrapperResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(wrapperResponse);
    }
}
