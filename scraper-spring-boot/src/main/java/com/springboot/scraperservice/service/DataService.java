package com.springboot.scraperservice.service;


import com.springboot.scraperservice.dto.EventsDTO;
import com.springboot.scraperservice.dto.QueryPropertiesDTO;
import com.springboot.scraperservice.model.Events;

import java.util.List;

public interface DataService {

    List<EventsDTO> findAllByProperties(QueryPropertiesDTO queryPropertiesDTO);

    void upsert(Object data);
}
