package com.shan.learnkafka.kafkaproducer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class LibraryEventControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleRequestBody(MethodArgumentNotValidException e) {
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        String errorMsg = errors.stream()
                .map(fe -> fe.getField() + " - " + fe.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining(", "));
        log.info("Error Message - {}", errorMsg);
        return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);

    }
}
