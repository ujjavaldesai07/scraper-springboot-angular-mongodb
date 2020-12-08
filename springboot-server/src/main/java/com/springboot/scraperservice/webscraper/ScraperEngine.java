package com.springboot.scraperservice.webscraper;

import com.springboot.scraperservice.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
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
    private final ScraperStateManager<Object> scraperStateManager;
    private List<Future<?>> futureList;
    private boolean appRunningState;
    private boolean isExecutorServiceRunning;

    @Autowired
    public ScraperEngine(ScraperFactory scraperFactory,
                         ExecutorServiceManager executorServiceManager,
                         ScraperDataDispatcher<Object> scraperDataDispatcher,
                         ScraperStateManager<Object> scraperStateManager) {
        this.scraperFactory = scraperFactory;
        this.executorServiceManager = executorServiceManager;
        this.scraperDataDispatcher = scraperDataDispatcher;
        this.scraperStateManager = scraperStateManager;

        appRunningState = true;
        isExecutorServiceRunning = true;
    }

    /**
     * Starts the ScraperEngine
     */
    public void start() {
        LOGGER.log(Level.INFO, "Starting the Scraper Engine.");
        addShutDownHook();

        // get the executor service
        ExecutorService executorService = executorServiceManager
                .getNewFixedThreadPool(3,
                        Constants.SCRAPER_ENGINE_EXECUTOR_SERVICE);

        futureList = new LinkedList<>();

        while (appRunningState) {
            isExecutorServiceRunning = true;
            LOGGER.log(Level.INFO, String.format("[**Starting Scraper Engine Thread**]: %s", Thread.currentThread().getName()));
            LOGGER.log(Level.INFO, String.format("Scraper Engine executorServiceManager = %d", executorServiceManager.hashCode()));

            // return if unable to start executorService
            if (executorService == null) {
                LOGGER.log(Level.SEVERE,
                        "Error occurred while starting the executor service for Scraper Engine.");
                return;
            }

            // iterate over the Scraper instances
            for (ScraperInfo scraperInfo : ScraperInfo.values()) {
                try {
                    // execute Scrapers concurrently.
                    futureList.add(executorService.submit((Runnable) scraperFactory.createScraper(scraperInfo)));
                } catch (Exception ex) {
                    LOGGER.log(Level.SEVERE, String.format("Unable to submit task to %s. Reason: %s",
                            Constants.SCRAPER_ENGINE_EXECUTOR_SERVICE, ex));
                }

            }

            // execute the dispatcher to dispatch the data provided by scraper to database.
            executorService.execute(scraperDataDispatcher);

            // check the completion status
            executorServiceManager.waitForTaskCompletion(futureList, executorService);

            LOGGER.log(Level.INFO, String.format("[**Ending Scraper Engine Thread**]: %s\n\n",
                    Thread.currentThread().getName()));

            futureList.clear();

            try {

                // sleep for 7 seconds.
                Thread.sleep(7000);
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Unable to sleep the main thread. Reason: %s" + ex);
            }
            scraperStateManager.reactivateScraperStates();
            isExecutorServiceRunning = false;
        }
        LOGGER.log(Level.INFO, "Stopping the Scraper Engine.");
    }

    void addShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            appRunningState = false;
            System.out.println("Shutdown Hook is running !");
            try {
                while (isExecutorServiceRunning) {
                    Thread.sleep(10000);
                }
                System.out.println("Scraper Engine shutdown successful....");
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }));
    }
}
