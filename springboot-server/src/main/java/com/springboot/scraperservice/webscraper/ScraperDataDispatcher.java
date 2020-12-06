package com.springboot.scraperservice.webscraper;

import com.springboot.scraperservice.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class dispatches the data to database in concurrently and once it is done
 * all the memory will be cleared.
 *
 * @param <T>
 */

@Component
public class ScraperDataDispatcher<T> implements Runnable {
    private final static Logger LOGGER = Logger.getLogger(String.valueOf(ScraperDataDispatcher.class));
    private final ScraperStateManager<T> scraperStateManager;
    private final ExecutorServiceManager executorServiceManager;
    private ExecutorService executorService;
    private final List<Future<?>> futureList;

    @Autowired
    public ScraperDataDispatcher(ScraperStateManager<T> scraperStateManager,
                                 ExecutorServiceManager executorServiceManager) {
        this.scraperStateManager = scraperStateManager;
        this.executorServiceManager = executorServiceManager;
        futureList = new LinkedList<>();

        setExecutorService();
    }

    /**
     * Sets the getCachedThreadPool to perform I/O or database operations.
     */
    private void setExecutorService() {
        LOGGER.log(Level.INFO, String.format("Scraper Data Dispatcher executorServiceManager = %d", executorServiceManager.hashCode()));
        executorService = executorServiceManager.getCachedThreadPool(Constants.SCRAPER_DISPATCHER_EXECUTOR_SERVICE);
    }

    /**
     * Dispatches the data based on the collection object type
     */
    private void dispatchData() {
        LOGGER.log(Level.INFO, "Starting dispatching events");

        try {
            // iterate over all the queues and upsert the data concurrently into db.
            for (Map.Entry<Integer, List<ScraperDataState<T>>> serviceDataStateEntry :
                    scraperStateManager.getServiceDataStateMap().entrySet()) {

                for (ScraperDataState<T> scraperDataState : serviceDataStateEntry.getValue()) {

                    LOGGER.log(Level.INFO, String.format("Dispatching data to database for scrapper id = %d"
                            , scraperDataState.getScraperId()));

                    // check if we have got the consumer function or not.
                    if (scraperDataState.getConsumer() == null) {
                        throw new Exception("Dispatch failed due to unable find consumer for the scraper id = "
                                + scraperDataState.getScraperId());
                    }

                    futureList.add(executorService.submit(() -> {
                        LOGGER.log(Level.INFO, String.format("[**Dispatching Data Thread**]: %s",
                                Thread.currentThread().getName()));

                        // check if the document is still processing
                        while (scraperDataState.getIsActive() || scraperDataState.getDataQueue().size() > 0) {

                            // grab the first data
                            T data = scraperDataState.getDataQueue().poll();

                            // upsert the data
                            if (data != null) {
                                try {
                                    // if unable process consumer then break out of loop
                                    scraperDataState.getConsumer().accept(data);
                                } catch (Exception ex) {
                                    LOGGER.log(Level.SEVERE, String.format(
                                            "Unable to process the consumer for scraper id = %d",
                                            scraperDataState.getScraperId()));
                                    break;
                                }
                            }
                        }

                        // release the memory hold by the state and container
                        scraperStateManager.unregisterScraperState(scraperDataState);
                    }));
                }

            }

        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, String.format(
                    "Error occurred while dispatching events data to database. Reason: %s", ex));
        }

        executorService.shutdown();
        LOGGER.log(Level.INFO, "Finished dispatching events");
    }

    private void startDispatch() {
        LOGGER.log(Level.INFO, "Started dispatching the data");

        // return if unable to start executorService
        if(executorService == null) {
            LOGGER.log(Level.SEVERE,
                    "Error occurred while starting the executor service for Scraper Data Dispatcher ");
            return;
        }

        dispatchData();

        // check the completion status and shut down the executor service
        executorServiceManager.shutdownUponTaskCompletion(futureList, executorService);

        // double check whether the executor service is shutdown or not.
        // If not then after 10 seconds do force shutdown.
        executorServiceManager.scheduleTermination(10, Constants.SCRAPER_DISPATCHER_EXECUTOR_SERVICE, executorService);

        // tear down the map as we are done now.
        scraperStateManager.destroyScraperStateMap();

        LOGGER.log(Level.INFO, "Finished dispatching the data");
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        LOGGER.log(Level.INFO, String.format("[**Scraper Dispatcher Thread**]: %s", Thread.currentThread().getName()));
        startDispatch();
    }
}
