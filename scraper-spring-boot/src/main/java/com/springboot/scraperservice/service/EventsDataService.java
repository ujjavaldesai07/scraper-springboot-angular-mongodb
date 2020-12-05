package com.springboot.scraperservice.service;

import com.springboot.scraperservice.constants.Constants;
import com.springboot.scraperservice.dto.EventsDTO;
import com.springboot.scraperservice.dto.QueryPropertiesDTO;
import com.springboot.scraperservice.exceptions.http.BadRequestException;
import com.springboot.scraperservice.helper.DateConversion;
import com.springboot.scraperservice.model.Events;
import com.springboot.scraperservice.repository.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class EventsDataService implements DataService {
    private final static Logger LOGGER = Logger.getLogger(String.valueOf(EventsDataService.class));

    private final MongoTemplate mongoTemplate;
    private final EventsRepository eventsRepository;

    @Autowired
    public EventsDataService(MongoTemplate mongoTemplate, EventsRepository eventsRepository) {
        this.mongoTemplate = mongoTemplate;
        this.eventsRepository = eventsRepository;
    }

    /**
     * This function finds all the events by query string parameters and transfer the
     * data on the DTO. This is needed to reformat the mongoDB stored date in standard format.
     *
     * @param queryPropertiesDTO: All the query parameters is hold by this DTO
     * @return : List of events
     */
    public List<EventsDTO> findAllByProperties(QueryPropertiesDTO queryPropertiesDTO) {
        List<Events> queriedEventList = this.eventsRepository.findAllByProperties(queryPropertiesDTO);

        List<EventsDTO> eventsDTOList = new LinkedList<>();
        for (Events events : queriedEventList) {

            EventsDTO eventsDTO = new EventsDTO();
            eventsDTO.setLocation(events.getLocation());
            eventsDTO.setWebsite(events.getWebsite());
            eventsDTO.setTitle(events.getTitle());
            eventsDTO.setStartDate(DateConversion.convertDateToStr(events.getStartDate(),
                    Constants.STANDARD_DATE_FORMAT));
            eventsDTO.setEndDate(DateConversion.convertDateToStr(events.getEndDate(),
                    Constants.STANDARD_DATE_FORMAT));

            eventsDTOList.add(eventsDTO);
        }

        return eventsDTOList;
    }

    /**
     * This function update the event if found else adds new event in the collection.
     *
     * @param data: Takes event object to add in the collection.
     */
    public void upsert(Object data) {
        Events event;

        if (data instanceof Events) {
            event = (Events) data;
        } else {
            LOGGER.log(Level.SEVERE,
                    "Unable to cast to Events type. Reason: Wrong service is used for this operation.");
            return;
        }

        // find the event by title which will be unique
        Query query = new Query();
        query.addCriteria(Criteria.where("title").is(event.getTitle()));

        // prepare update object
        Update update = new Update();
        update.set("title", event.getTitle());
        update.set("website", event.getWebsite());
        update.set("startDate", event.getStartDate());
        update.set("endDate", event.getEndDate());
        update.set("location", event.getLocation());

        // execute the query
        mongoTemplate.upsert(query, update, Events.class);
    }

    public Object findByParameter(Object data) {

        if (data instanceof QueryPropertiesDTO) {
            return findAllByProperties((QueryPropertiesDTO) data);
        } else if (data instanceof String) {
            return eventsRepository.findDistinctByAttributes((String) data);
        } else {
            LOGGER.log(Level.SEVERE,
                    "Unable to cast to supported types. Reason: Wrong service is used for this operation.");
            throw new BadRequestException("Unsupported query parameters found.");
        }
    }
}
