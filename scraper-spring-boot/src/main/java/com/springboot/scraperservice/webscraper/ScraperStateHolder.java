package com.springboot.scraperservice.webscraper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * This class is used to hold all the scrapers state and data.
 */

@Setter
@Getter
@Component
public class ScraperStateHolder {
    private List<ScraperEventsState> scraperEventsStateList;

    public ScraperStateHolder() {
        scraperEventsStateList = new LinkedList<>();
    }

    public void registerEventsState(ScraperEventsState scraperEventsState) {
        scraperEventsStateList.add(scraperEventsState);
    }

    public void unregisterEventsStates() {
        for (ScraperEventsState scraperEventsState : scraperEventsStateList) {
            scraperEventsState.getEventsQueue().clear();
        }
        scraperEventsStateList.clear();
    }
}
