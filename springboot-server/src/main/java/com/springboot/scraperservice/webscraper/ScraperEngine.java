package com.springboot.scraperservice.webscraper;

import com.springboot.scraperservice.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entrypoint for scraping
 */

@Component
public class ScraperEngine {
    private final static Logger LOGGER = Logger.getLogger(String.valueOf(ScraperEngine.class));
    private final ScraperFactory scraperFactory;
    private final ExecutorServiceManager executorServiceManager;
    private final ScraperDataDispatcher<Object> scraperDataDispatcher;

    @Autowired
    public ScraperEngine(ScraperFactory scraperFactory,
                         ExecutorServiceManager executorServiceManager,
                         ScraperDataDispatcher<Object> scraperDataDispatcher) {
        this.scraperFactory = scraperFactory;
        this.executorServiceManager = executorServiceManager;
        this.scraperDataDispatcher = scraperDataDispatcher;
    }

    /**
     * Starts the ScraperEngine
     */
    public void start() {
        LOGGER.log(Level.INFO, "Starting the Scraper Engine.");
        LOGGER.log(Level.INFO, String.format("[**Starting Scraper Engine Thread**]: %s", Thread.currentThread().getName()));
        LOGGER.log(Level.INFO, String.format("Scraper Engine executorServiceManager = %d", executorServiceManager.hashCode()));

        // get the executor service
        ExecutorService executorService = executorServiceManager
                .getNewFixedThreadPool(3,
                        Constants.SCRAPER_ENGINE_EXECUTOR_SERVICE);

        // return if unable to start executorService
        if (executorService == null) {
            LOGGER.log(Level.SEVERE,
                    "Error occurred while starting the executor service for Scraper Engine.");
            return;
        }

        List<Future<?>> futureList = new LinkedList<>();

        // iterate over the Scraper instances
        for (ScraperInfo scraperInfo : ScraperInfo.values()) {
            // execute Scrapers concurrently.
            futureList.add(executorService.submit((Runnable) scraperFactory.createScraper(scraperInfo)));
        }

        // execute the dispatcher to dispatch the data provided by scraper to database.
        executorService.execute(scraperDataDispatcher);

        // check the completion status and shut down the executor service
        executorServiceManager.shutdownUponTaskCompletion(futureList, executorService);

        // double check whether the executor service is shutdown or not.
        // If not then after 20 seconds do force shutdown.
        executorServiceManager.scheduleTermination(20, Constants.SCRAPER_ENGINE_EXECUTOR_SERVICE, executorService);

        LOGGER.log(Level.INFO, "Stopping the Scraper Engine.");
        LOGGER.log(Level.INFO, String.format("[**Ending Scraper Engine Thread**]: %s", Thread.currentThread().getName()));
    }
}
