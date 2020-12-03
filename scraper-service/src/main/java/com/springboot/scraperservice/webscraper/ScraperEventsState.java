package com.springboot.scraperservice.webscraper;

import com.springboot.scraperservice.model.Events;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This class maintains the state of the Scraper Data for the events collection.
 */

@Setter
@Getter
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScraperEventsState {
        private Boolean isActive;
        private Queue<Events> eventsQueue;

        public ScraperEventsState() {
                this.eventsQueue = new LinkedList<>();
        }
}
