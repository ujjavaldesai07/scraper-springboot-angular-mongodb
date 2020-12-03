package com.springboot.scraperservice.exceptions.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class ResourceNotFoundException extends HttpClientErrorException {
    public ResourceNotFoundException(String statusText) {
        super(HttpStatus.NOT_FOUND, statusText);
    }
}
