package com.springboot.scraperservice.service;


import com.springboot.scraperservice.dto.EventsDTO;
import com.springboot.scraperservice.dto.QueryPropertiesDTO;
import com.springboot.scraperservice.model.Events;

import java.util.List;

public interface Service {

    List<EventsDTO> findAllEventsByProperties(QueryPropertiesDTO queryPropertiesDTO);

    void dropCollection(String collectionName);

    void insertEvents(List<Events> eventList);

    void upsertData(Object data);
}
