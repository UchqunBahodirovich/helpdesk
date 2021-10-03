package com.example.sdc.exception;

import com.example.sdc.module.Result;
import com.example.sdc.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;


@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomizedResponseEntityExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlingAllExceptions(Exception ex, WebRequest request) {
        logger.error("1: Exception: " + Util.stackTrace(ex));
        Result result = new Result(1, "Exception: " + ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({IllegalStateException.class, ConstraintViolationException.class})
    public final ResponseEntity<?> handleIllegalExceptions(Exception ex, WebRequest request) {
        logger.error("2: " + Util.stackTrace(ex));
        Result result = new Result(2, "IllegalStateException:" + ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    //to handle exception when tyring to pass wrong Enum value
    @ExceptionHandler(HttpMessageConversionException.class)
    public final ResponseEntity<?> handleHttpMessageConversionException(HttpMessageConversionException ex, WebRequest request) {
        logger.error("3: " + Util.stackTrace(ex));
        Result result = new Result(3, "HttpMessageConversionException:" + ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    //to handle exception when tyring to pass wrong SexEnum value
    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<?> handleIllegalArgumentException(HttpMessageConversionException ex, WebRequest request) {
        logger.error("4: " + Util.stackTrace(ex));
        Result result = new Result(4, "IllegalArgumentException: " + ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
    //this will handle situation when there's exception during binding, for example you accept number and user passess string (A123.00 for example)
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        logger.error("5: " + Util.stackTrace(ex));
        Result result = new Result(5, "handleTypeMismatch: " + ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    //this will handle situation when user sending empty request body in post request
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        logger.error("6: " + Util.stackTrace(ex));
        Result result = new Result(6, "handleHttpMessageNotReadable: " + ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    //this will handle situation when user sending without required request parameter
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        return super.handleMissingServletRequestParameter(ex, headers, status, request);
        logger.error("7: " + Util.stackTrace(ex));
        Result result = new Result(7, "handleMissingServletRequestParameter: " + ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        final StringBuffer errors = new StringBuffer();

        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.append(error.getField() + ": " + error.getDefaultMessage());
            errors.append(", ");
        }
        logger.error("8: " + Util.stackTrace(ex));
        Result result = new Result(8, "handleMethodArgumentNotValid: " + errors.toString());
        return handleExceptionInternal(ex, result, headers, HttpStatus.BAD_REQUEST, request);
    }
    @ExceptionHandler(CustomizedRequestException.class)
    public final ResponseEntity<?> handleCustomizedException(CustomizedRequestException ex, WebRequest request) {
        logger.error("" + Util.stackTrace(ex));
        Result result = new Result(ex.getCode(), "CustomizedRequestException: " + ex.getMessage());
        return new ResponseEntity<>(result, HttpStatus.valueOf(ex.getHttpResponseCode()));
    }



}
