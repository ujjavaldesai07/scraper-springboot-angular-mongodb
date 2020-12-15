package com.springboot.scraperservice.service;


import com.springboot.scraperservice.dto.EventsDTO;
import com.springboot.scraperservice.dto.QueryPropertiesDTO;
import com.springboot.scraperservice.model.Events;

import java.util.List;

public interface EventsService {

    void upsert(Object data);

    List<EventsDTO> findAllEventsByProperties(QueryPropertiesDTO queryPropertiesDTO);

    List<String> findDistinctValuesByAttribute(String attribute);
}
