package com.springboot.scraperservice.repository;

import com.springboot.scraperservice.dto.QueryPropertiesDTO;
import com.springboot.scraperservice.model.Events;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventsRepository extends MongoRepository<Events, String> {
    List<Events> findAllByProperties(QueryPropertiesDTO queryPropertiesDTO);
}
