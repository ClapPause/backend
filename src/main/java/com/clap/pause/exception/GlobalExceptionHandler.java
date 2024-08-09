package com.clap.pause.exception;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String NOT_FOUND_MESSAGE = "존재하지 않는 리소스에 대한 접근입니다.";
    private static final String INVALID_POST_TYPE_MESSAGE = "잘못된 글 타입입니다.";

    @ExceptionHandler(value = NotFoundElementException.class)
    public ResponseEntity<ExceptionResponse> notFoundElementExceptionHandling() {
        return getExceptionResponse(NOT_FOUND_MESSAGE, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InvalidPostTypeException.class)
    public ResponseEntity<ExceptionResponse> invalidPostTypeExceptionHandling() {
        return getExceptionResponse(INVALID_POST_TYPE_MESSAGE, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<ExceptionResponse> badRequestExceptionHandling(BadRequestException exception) {
        return getExceptionResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> methodArgumentNotValidExceptionHandling(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append(fieldError.getDefaultMessage());
        }

        return getExceptionResponse(builder.toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionResponse> internalServerExceptionHandling(Exception exception) {
        System.out.println("exception : " + exception);
        return getExceptionResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ExceptionResponse> getExceptionResponse(String message, HttpStatus status) {
        var response = new ExceptionResponse(status.value(), message);
        return ResponseEntity.status(status).body(response);
    }
}
