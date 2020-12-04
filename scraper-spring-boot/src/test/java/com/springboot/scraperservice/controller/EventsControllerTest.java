package com.springboot.scraperservice.controller;

import com.springboot.scraperservice.constants.Routes;
import com.springboot.scraperservice.dto.EventsDTO;
import com.springboot.scraperservice.dto.QueryPropertiesDTO;
import com.springboot.scraperservice.service.Service;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RunWith(SpringRunner.class)
@WebMvcTest(value = EventsController.class)
@WithMockUser
public class EventsControllerTest {

    private final static Logger LOGGER = Logger.getLogger(String.valueOf(EventsControllerTest.class));

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Service service;

    @Test
    public void getEvents() throws Exception {
        // given
        QueryPropertiesDTO queryPropertiesDTO = new QueryPropertiesDTO();
        queryPropertiesDTO.setLocation("London");
        queryPropertiesDTO.setWebsite(null);
        queryPropertiesDTO.setStartDate(null);
        queryPropertiesDTO.setEndDate(null);
        queryPropertiesDTO.setSort(null);

        // expected
        List<EventsDTO> mockEventsDTOs = new LinkedList<>();
        EventsDTO mockEventDTO = new EventsDTO();
        mockEventDTO.setLocation("London");
        mockEventDTO.setStartDate("12/2/2021");
        mockEventDTO.setEndDate("17/2/2021");
        mockEventDTO.setTitle("Mock title");
        mockEventDTO.setWebsite("Mock website");

        mockEventsDTOs.add(mockEventDTO);

        // when
        Mockito.when(service.findAllEventsByProperties(queryPropertiesDTO)).thenReturn(mockEventsDTOs);

        // prepare the request
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                Routes.EVENT_API).accept(MediaType.APPLICATION_JSON);

        // fire the request
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        LOGGER.log(Level.INFO, "RESPONSE===============> " + result.getResponse().getContentAsString());
        String expected = "{:Course1,name:Spring,description:10Steps}";

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
    }
}
