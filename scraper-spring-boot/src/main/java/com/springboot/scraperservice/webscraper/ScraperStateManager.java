package com.springboot.scraperservice.webscraper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to hold all the scrapers state and data of any type.
 */

@Setter
@Getter
@Component
public class ScraperStateManager<T> {
    private final static Logger LOGGER = Logger.getLogger(String.valueOf(ScraperStateManager.class));
    private Map<Integer, List<ScraperDataState<T>>> serviceDataStateMap;

    public ScraperStateManager() {
        registerScrapers();
    }

    public void registerScrapers() {
        serviceDataStateMap = new HashMap<>();

        // iterate over the Scraper Info
        for (ScraperInfo scraperInfo : ScraperInfo.values()) {
            // register scrapers ids
            LOGGER.log(Level.INFO, String.format("Registering scraper with id = %d", scraperInfo.ID));
            serviceDataStateMap.put(scraperInfo.ID, new LinkedList<>());
        }
    }

    public void registerScraperState(ScraperDataState<T> scraperDataState) {
        try {
            LOGGER.log(Level.INFO, String.format("Registering scraper state with id = %d",
                    scraperDataState.getScraperId()));
            serviceDataStateMap.get(scraperDataState.getScraperId()).add(scraperDataState);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to register scraper data state");
        }
    }

    public void unregisterScraperState(ScraperDataState<T> scraperDataState) {
        try {
            serviceDataStateMap.get(scraperDataState.getScraperId()).remove(scraperDataState);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to unregister scraper data state");
        }
    }

    public void destroyScraperStateMap() {
        serviceDataStateMap = null;
    }
}
