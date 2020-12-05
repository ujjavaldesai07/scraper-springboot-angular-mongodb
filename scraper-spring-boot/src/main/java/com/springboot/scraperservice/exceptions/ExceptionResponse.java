package com.springboot.scraperservice.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * This class is send the error response in customized format.
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {

    private Integer errorCode;
    private HttpStatus errorStatus;
    private String errorMessage;
    private String path;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp;

    public ExceptionResponse(Integer errorCode, HttpStatus errorStatus, String errorMessage, String path) {
        this.errorCode = errorCode;
        this.errorStatus = errorStatus;
        this.errorMessage = errorMessage;
        this.path = path;
        timestamp = LocalDateTime.now();
    }
}
