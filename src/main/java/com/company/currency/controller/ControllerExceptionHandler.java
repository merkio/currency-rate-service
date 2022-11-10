package com.company.currency.controller;

import com.company.currency.dto.ErrorResponse;
import com.company.currency.exception.CurrencyRateException;
import com.company.currency.exception.IllegalArgumentsException;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {IllegalArgumentsException.class})
  protected ResponseEntity<ErrorResponse> handleIllegalArgument(
      RuntimeException ex, WebRequest request) {
    return ResponseEntity.badRequest()
        .body(
            ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .title(ex.getMessage())
                .description(
                    Objects.nonNull(ex.getCause()) ? ex.getCause().toString() : StringUtils.EMPTY)
                .build());
  }

  @ExceptionHandler(value = {CurrencyRateException.class})
  protected ResponseEntity<ErrorResponse> handleCurrencyRateException(
      RuntimeException ex, WebRequest request) {
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
        .body(
            ErrorResponse.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .title(ex.getMessage())
                .description(
                    Objects.nonNull(ex.getCause()) ? ex.getCause().toString() : StringUtils.EMPTY)
                .build());
  }

  @ExceptionHandler(value = {Exception.class})
  protected ResponseEntity<ErrorResponse> handleException(RuntimeException ex, WebRequest request) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .title(ex.getMessage())
                .description(
                    Objects.nonNull(ex.getCause()) ? ex.getCause().toString() : StringUtils.EMPTY)
                .build());
  }
}
