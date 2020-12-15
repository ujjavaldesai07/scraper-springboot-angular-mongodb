package com.springboot.scraperservice.service;

import com.springboot.scraperservice.constants.Constants;
import com.springboot.scraperservice.dto.EventsDTO;
import com.springboot.scraperservice.dto.QueryPropertiesDTO;
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
import java.util.logging.Logger;

@Service
public class EventsServiceImpl implements EventsService {
    private final static Logger LOGGER = Logger.getLogger(String.valueOf(EventsServiceImpl.class));

    private final MongoTemplate mongoTemplate;
    private final EventsRepository eventsRepository;

    @Autowired
    public EventsServiceImpl(MongoTemplate mongoTemplate, EventsRepository eventsRepository) {
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
    public List<EventsDTO> findAllEventsByProperties(QueryPropertiesDTO queryPropertiesDTO) {
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
        Events event = (Events) data;;

        // find the event by title which will be unique
        Query query = new Query();
        query.addCriteria(Criteria.where(Constants.EVENTS_TITLE).is(event.getTitle()));

        // prepare update object
        Update update = new Update();
        update.set(Constants.EVENTS_TITLE, event.getTitle());
        update.set(Constants.EVENTS_WEBSITE, event.getWebsite());
        update.set(Constants.EVENTS_START_DATE, event.getStartDate());
        update.set(Constants.EVENTS_END_DATE, event.getEndDate());
        update.set(Constants.EVENTS_LOCATION, event.getLocation());

        // execute the query
        mongoTemplate.upsert(query, update, Events.class);
    }

    /**
     * This function find the distinct values of an Events attribute.
     * for eg. website, location etc.
     * @param attribute: name of the Events attribute
     * @return: list of strings
     */
    public List<String> findDistinctValuesByAttribute(String attribute) {
        return eventsRepository.findDistinctValuesByAttributes(attribute);
    }
}
