package com.springboot.scraperservice.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This helper class is used to provide any service from the service layer.
 * This is used by the ScraperDataDispatcher to get the service and execute the
 * queries.
 */

@Component
public class ServiceProvider {
    private EventService eventService;

    @Autowired
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    public EventService getEventService() {
        return this.eventService;
    }
}
