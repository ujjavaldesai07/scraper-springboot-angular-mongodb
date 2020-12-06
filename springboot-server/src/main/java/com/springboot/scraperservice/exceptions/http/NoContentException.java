package com.springboot.scraperservice.exceptions.http;


import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class NoContentException extends HttpClientErrorException {

    public NoContentException(String statusText) {
        super(HttpStatus.NO_CONTENT, statusText);
    }
}