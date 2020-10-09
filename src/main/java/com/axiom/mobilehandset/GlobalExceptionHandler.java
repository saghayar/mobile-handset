package com.axiom.mobilehandset;


import com.axiom.mobilehandset.exception.ErrorResponse;
import com.axiom.mobilehandset.exception.InvalidRequestParameterException;
import com.axiom.mobilehandset.exception.InvalidResponseExceptionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandler {
    private static final String MESSAGE = "Error message :{}";

    @ExceptionHandler(value = {InvalidRequestParameterException.class, InvalidResponseExceptionException.class})
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(InvalidRequestParameterException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        log.error(MESSAGE, e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse("Something went wrong");
        log.error(MESSAGE, e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(value = NumberFormatException.class)
    public ResponseEntity<ErrorResponse> handleNUmberFormatException(NumberFormatException e) {
        ErrorResponse errorResponse = new ErrorResponse("Value should be number " + e.getMessage());
        log.error(MESSAGE, e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
