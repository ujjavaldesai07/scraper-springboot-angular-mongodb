package com.springboot.scraperservice.service;

import com.springboot.scraperservice.ScraperServiceApplication;
import com.springboot.scraperservice.constants.Constants;
import com.springboot.scraperservice.dto.EventsDTO;
import com.springboot.scraperservice.dto.QueryPropertiesDTO;
import com.springboot.scraperservice.model.Events;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.logging.Logger;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ScraperServiceApplication.class, loader = AnnotationConfigContextLoader.class)
public class EventEventsServiceTest {
    private final static Logger LOGGER = Logger.getLogger(String.valueOf(EventEventsServiceTest.class));

    @Autowired
    private EventsService eventsService;

    @Test
    public void testFindAllByPropertiesSuccessCase() {
        List<String> websiteList = eventsService.findDistinctValuesByAttribute(Constants.EVENTS_WEBSITE);
        String[] actual = new String[websiteList.size()];
        actual = websiteList.toArray(actual);
        String[] expected = {"ComputerWorld", "Techmeme"};
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testFindAllByPropertiesFailureCase() {
        List<String> websiteList = eventsService.findDistinctValuesByAttribute(Constants.EVENTS_WEBSITE);
        String[] actual = new String[websiteList.size()];
        actual = websiteList.toArray(actual);
        String[] expected = {"fake website", "fake website 2"};
        assertNotEquals(expected, actual);
    }

    @Test
    public void testFindAllEventsWithoutProperties() {
        List<EventsDTO> eventsList = eventsService.findAllEventsByProperties(new QueryPropertiesDTO());
        assertNotEquals(0, eventsList.size());
    }

    @Test
    public void testFindLocationByProperties() {
        List<String> locationList = eventsService.findDistinctValuesByAttribute(Constants.EVENTS_LOCATION);
        assertTrue(locationList.contains("Amsterdam"));
    }
}
