package com.springboot.scraperservice.exceptions;

import com.springboot.scraperservice.exceptions.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final static Logger LOGGER = Logger.getLogger(String.valueOf(GlobalExceptionHandler.class));

    /**
     * Custom Resource Not Found Exception Handler
     *
     * @param ex
     * @param webRequest
     * @return
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(ResourceNotFoundException ex, HttpServletRequest webRequest) {

        ExceptionResponse exceptionResponse = new ExceptionResponse
                (404, HttpStatus.NOT_FOUND, "Resource Not Found",
                        webRequest.getRequestURI());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Custom Bad Request Exception Handler
     *
     * @param ex
     * @param webRequest
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex,
                                                            HttpServletRequest webRequest) {

        ExceptionResponse exceptionResponse = new ExceptionResponse
                (400, HttpStatus.BAD_REQUEST, ex.getStatusText(),
                        webRequest.getRequestURI());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Custom No Content Exception Handler
     *
     * @param ex
     * @param webRequest
     * @return
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler({NoContentException.class})
    public ResponseEntity<Object> handleNoContentException(NoContentException ex,
                                                            HttpServletRequest webRequest) {

        ExceptionResponse exceptionResponse = new ExceptionResponse
                (204, HttpStatus.NO_CONTENT, ex.getStatusText(),
                        webRequest.getRequestURI());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NO_CONTENT);
    }

    /**
     * Custom Generic Exception Handler
     *
     * @param ex
     * @param webRequest
     * @return
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllException(Exception ex, HttpServletRequest webRequest) {

        ExceptionResponse exceptionResponse = new ExceptionResponse
                (500, HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(),
                        webRequest.getRequestURI());

        ex.printStackTrace();
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
