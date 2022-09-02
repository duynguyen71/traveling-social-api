package com.tc.tcapi.config;

import com.tc.core.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ValidationException {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleException(MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();

        List<String> details =
                errors.stream().map(err -> (((FieldError) err).getField()) + " " + err.getDefaultMessage()).collect(Collectors.toList());
        ErrorResponse res = new ErrorResponse("Validation exception ", details);
        return ResponseEntity.badRequest().body(res);
    }
}
