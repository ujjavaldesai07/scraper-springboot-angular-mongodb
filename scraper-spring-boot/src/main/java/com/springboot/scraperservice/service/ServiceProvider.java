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
    private EventsService eventsService;

    @Autowired
    public void setEventService(EventsService eventsService) {
        this.eventsService = eventsService;
    }

    public EventsService getEventsService() {
        return this.eventsService;
    }
}
