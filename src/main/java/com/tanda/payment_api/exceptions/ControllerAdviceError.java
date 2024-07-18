package com.tanda.payment_api.exceptions;


import com.tanda.payment_api.models.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.util.List;

@ControllerAdvice
@Slf4j
public class ControllerAdviceError extends ResponseEntityExceptionHandler {

    @ExceptionHandler({HttpServerErrorException.class})
    public ResponseEntity<?> onHttpServerErrorException(HttpServerErrorException ex) {
        ApiResponse<?> response = ApiResponse.builder()
                .status(ex.getStatusCode().value())
                .description(ex.getStatusText())
                .build();
        return new ResponseEntity<>(response, ex.getStatusCode());
    }

    //HttpMessageConversionException
    @ExceptionHandler({HttpMessageConversionException.class})
    public ResponseEntity<?> onHttpMessageConversionException(HttpMessageConversionException ex) {
        HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiResponse<?> response = ApiResponse.builder()
                .status(internalServerError.value())
                .description(ex.getMessage())
                .build();

        return new ResponseEntity<>(response, internalServerError);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<?> onConstraintValidation(ConstraintViolationException ex) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        ApiResponse<?> response = ApiResponse.builder()
                .status(badRequest.value())
                .description(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, badRequest);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<?> onRuntimeError(RuntimeException ex) {
        HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiResponse<?> response = ApiResponse.builder()
                .status(internalServerError.value())
                .description(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, internalServerError);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex, HttpServletRequest request) {
        // Create an ErrorResponse object with the desired structure
        HttpStatus internalServerError = HttpStatus.INTERNAL_SERVER_ERROR;
        String error = internalServerError.getReasonPhrase();
        ApiResponse<?> response = ApiResponse.builder()
                .status(internalServerError.value())
                .description(error)
                .data(ex.getMessage())
                .build();
        return ResponseEntity.status(internalServerError).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        ApiResponse<?> response = ApiResponse.builder()
                .status(statusCode.value())
                .description(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, statusCode);
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiResponse<?> response = ApiResponse.builder()
                .status(status.value())
                .description(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, status);
    }


    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        ApiResponse<?> response = ApiResponse.builder()
                .status(statusCode.value())
                .description(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, statusCode);
    }



    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        ApiResponse<?> response = ApiResponse.builder()
                .status(statusCode.value())
                .description(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, statusCode);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        ApiResponse<?> response = ApiResponse.builder()
                .status(statusCode.value())
                .description(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, statusCode);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiResponse<?> response = ApiResponse.builder()
                .status(status.value())
                .description(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, status);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiResponse<?> response = ApiResponse.builder()
                .status(status.value())
                .description(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, status);
    }


    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiResponse<?> response = ApiResponse.builder()
                .status(status.value())
                .description(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        List<String> errors = ex.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        String description = errors.stream().findFirst().orElse(ex.getLocalizedMessage());
        ApiResponse<?> response = ApiResponse.builder()
                .status(statusCode.value())
                .description(description)
                .data(errors)
                .build();
        return new ResponseEntity<>(response, statusCode);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        ApiResponse<?> response = ApiResponse.builder()
                .status(statusCode.value())
                .description(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, statusCode);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        String error = ex.getParameterName() + " Parameter is Missing";

        ApiResponse<?> response = ApiResponse.builder()
                .status(statusCode.value())
                .description(error)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        ApiResponse<?> response = ApiResponse.builder()
                .status(statusCode.value())
                .description(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, statusCode);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        ApiResponse<?> response = ApiResponse.builder()
                .status(statusCode.value())
                .description(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, statusCode);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        ApiResponse<?> response = ApiResponse.builder()
                .status(statusCode.value())
                .description(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, statusCode);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        ApiResponse<?> response = ApiResponse.builder()
                .status(statusCode.value())
                .description(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, statusCode);
    }


}
