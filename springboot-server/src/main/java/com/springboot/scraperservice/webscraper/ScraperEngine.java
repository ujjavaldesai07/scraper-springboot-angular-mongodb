package com.springboot.scraperservice.webscraper;

import com.springboot.scraperservice.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

        // get the executor service
        ExecutorService executorService = executorServiceManager
                .getNewFixedThreadPool(System.getenv("MAX_SCRAPER_THREAD_COUNT"),
                        Constants.SCRAPER_ENGINE_EXECUTOR_SERVICE);

        // iterate over the Scraper instances
        for (ScraperInfo scraperInfo : ScraperInfo.values()) {

            // execute Scrapers concurrently.
            executorService.execute((Runnable) scraperFactory.createScraper(scraperInfo));
        }

        // execute the dispatcher to dispatch the data provided by scraper to database.
        executorService.execute(scraperDataDispatcher);

        // forced shutdown of the executor service if the processing is not finished within 20 seconds
        executorServiceManager.terminate(20, Constants.SCRAPER_ENGINE_EXECUTOR_SERVICE);

        LOGGER.log(Level.INFO, "Stopping the Scraper Engine.");
    }
}
