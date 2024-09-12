package com.clap.pause.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NotFoundElementException.class)
    public ResponseEntity<ExceptionResponse> notFoundElementExceptionHandling(NotFoundElementException exception) {
        return getExceptionResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = InvalidPostTypeException.class)
    public ResponseEntity<ExceptionResponse> invalidPostTypeExceptionHandling(InvalidPostTypeException exception) {
        return getExceptionResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = DuplicatedException.class)
    public ResponseEntity<ExceptionResponse> duplicatedExceptionHandling(DuplicatedException exception) {
        return getExceptionResponse(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = InvalidLoginInfoException.class)
    public ResponseEntity<ExceptionResponse> invalidLoginInfoExceptionHandling(InvalidLoginInfoException exception) {
        return getExceptionResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = ImageProcessingFailedException.class)
    public ResponseEntity<ExceptionResponse> imageProcessingFailedExceptionHandling(InvalidLoginInfoException exception) {
        return getExceptionResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidRequestException.class)
    public ResponseEntity<ExceptionResponse> invalidRequestExceptionHandling(InvalidRequestException exception) {
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
        return getExceptionResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ExceptionResponse> getExceptionResponse(String message, HttpStatus status) {
        var response = new ExceptionResponse(status.value(), message);
        return ResponseEntity.status(status).body(response);
    }
}
