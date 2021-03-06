package com.springboot.scraperservice.repository;

import com.springboot.scraperservice.constants.Constants;
import com.springboot.scraperservice.dto.QueryPropertiesDTO;
import com.springboot.scraperservice.exceptions.http.BadRequestException;
import com.springboot.scraperservice.model.Events;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is implementing customized query as the parameters are dynamic.
 */

@Repository
public class EventsRepositoryImpl {
    private final static Logger LOGGER = Logger.getLogger(String.valueOf(EventsRepositoryImpl.class));

    private final MongoTemplate mongoTemplate;

    @Autowired
    public EventsRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Events> findAllByProperties(QueryPropertiesDTO queryPropertiesDTO) {
        final Query query = new Query();
        final List<Criteria> criteria = new ArrayList<>();

        try {
            if (queryPropertiesDTO.getStartDate() != null && queryPropertiesDTO.getEndDate() != null) {
                query.addCriteria(Criteria.where(Constants.EVENTS_START_DATE)
                        .gte(queryPropertiesDTO.getStartDate())
                        .lte(queryPropertiesDTO.getEndDate()));
            }

            if (queryPropertiesDTO.getLocation() != null) {
                criteria.add(Criteria.where(Constants.EVENTS_LOCATION).is(queryPropertiesDTO.getLocation()));
            }

            if (queryPropertiesDTO.getWebsite() != null) {
                criteria.add(Criteria.where(Constants.EVENTS_WEBSITE).is(queryPropertiesDTO.getWebsite()));
            }

            if (!criteria.isEmpty()) {
                query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
            }

            if (queryPropertiesDTO.getSort() != null) {
                query.with(Sort.by(queryPropertiesDTO.getSort()).ascending());
            }
        } catch (Exception ex) {

            LOGGER.log(Level.WARNING, String.format("Unable to process query parameters [%s]. Reason: %s"
                    , queryPropertiesDTO.toString(), ex));
            throw new BadRequestException("Unable to process query parameters");
        }

        try {
            return mongoTemplate.find(query, Events.class);
        } catch (Exception ex) {

            LOGGER.log(Level.SEVERE,
                    String.format("Error occurred while fetching the data from database [%s]. Reason: %s"
                            , queryPropertiesDTO.toString(), ex));
        }
        return null;
    }

    public List<String> findDistinctValuesByAttributes(String attribute) {
        final Query query = new Query();
        query.addCriteria(Criteria.where(attribute).ne(null));

        try {
            return mongoTemplate.findDistinct(query, attribute, Events.class, String.class);
        } catch (Exception ex) {

            LOGGER.log(Level.SEVERE,
                    String.format("Error occurred while fetching the attributes from database [%s]. Reason: %s"
                            , attribute, ex));
        }
        return null;
    }
}
