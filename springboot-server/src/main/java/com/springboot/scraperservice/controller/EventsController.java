package com.springboot.scraperservice.controller;

import com.springboot.scraperservice.constants.Routes;
import com.springboot.scraperservice.dto.QueryPropertiesDTO;
import com.springboot.scraperservice.model.Events;
import com.springboot.scraperservice.service.DataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Api(value = "Supports GET operation", tags = {"Event"})
@RestController
@RequestMapping(Routes.EVENT_API)
public class EventsController {

    private final DataService dataService;

    @Autowired
    public EventsController(DataService dataService) {
        this.dataService = dataService;
    }

    /**
     * This function fetches the events based on the query parameters. If no parameter is
     * given then it will fetch all the events.
     *
     * @param location:  location of the event
     * @param website:   website name on which event is found
     * @param startDate: start date of the event
     * @param endDate:   end date of the event
     * @param sort:      sort events as per location, website, startDate, endDate
     * @return : EventsDTO list
     */
    @ApiOperation(value = "Get all events by filtering and sorting.",
            notes = "For filtering use query params as startDate, endDate," +
                    " location, title, website. For sorting use sort.")
    @GetMapping()
    public ResponseEntity<?> getEvents(@RequestParam(required = false) String location,
                                       @RequestParam(required = false) String website,
                                       @RequestParam(required = false) Date startDate,
                                       @RequestParam(required = false) Date endDate,
                                       @RequestParam(required = false) String sort) {

        // I could have used here query map to take the parameters but
        // I want spring to handle the parameters name and value types.

        // set all the query parameters
        QueryPropertiesDTO queryPropertiesDTO = new QueryPropertiesDTO();
        queryPropertiesDTO.setLocation(location);
        queryPropertiesDTO.setWebsite(website);
        queryPropertiesDTO.setStartDate(startDate);
        queryPropertiesDTO.setEndDate(endDate);
        queryPropertiesDTO.setSort(sort);

        // if result is null then send response as Not Found.
        Object result = dataService.findByParameter(queryPropertiesDTO);
        if (result != null) {
            return ResponseEntity.ok((List<Events>) result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * This endpoint provides the data for filter supported options. i.e. distinct website names,locations etc.
     *
     * @return list of options
     */
    @ApiOperation(value = "Endpoint to provide data for the attributes such website names, locations etc.")
    @GetMapping(value = Routes.EVENTS_FILTER_SORT_ATTRIBUTES_ENDPOINT)
    public ResponseEntity<?> getWebsiteNames(@RequestParam(required = false) String attr) {

        // if result is null then send response as Not Found.
        Object result = dataService.findByParameter(attr);
        if (result != null) {
            return ResponseEntity.ok((List<String>) result);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
