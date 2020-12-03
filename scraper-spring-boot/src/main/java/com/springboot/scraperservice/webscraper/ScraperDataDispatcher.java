package com.springboot.scraperservice.webscraper;

import com.springboot.scraperservice.constants.Constants;
import com.springboot.scraperservice.model.Events;
import com.springboot.scraperservice.service.ServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ScraperDataDispatcher implements Runnable {
    private final static Logger LOGGER = Logger.getLogger(String.valueOf(ScraperDataDispatcher.class));
    private final ScraperStateHolder scraperStateHolder;
    private final ServiceProvider serviceProvider;
    private final ExecutorServiceWrapper executorServiceWrapper;
    private ExecutorService executorService;

    @Autowired
    public ScraperDataDispatcher(ScraperStateHolder scraperStateHolder,
                                 ServiceProvider serviceProvider,
                                 ExecutorServiceWrapper executorServiceWrapper) {
        this.scraperStateHolder = scraperStateHolder;
        this.serviceProvider = serviceProvider;
        this.executorServiceWrapper = executorServiceWrapper;

        setExecutorService();
    }

    private void setExecutorService() {
        executorService = executorServiceWrapper.getCachedThreadPool();
    }

    private void cleanup() {
        scraperStateHolder.unregisterEventsStates();
    }

    private void dispatchEventsData(List<ScraperEventsState> scraperEventsStates) {
        LOGGER.log(Level.INFO, "Started dispatching events");

        try {
            // iterate over all the queues and upsert the data concurrently into db.
            for (ScraperEventsState scraperEventsState : scraperEventsStates) {

                executorService.execute(() -> {
                    // check if the document is still processing
                    while (scraperEventsState.getIsActive() || scraperEventsState.getEventsQueue().size() > 0) {

                        // grab the first event
                        Events event = scraperEventsState.getEventsQueue().poll();

                        // upsert the event
                        if (event != null) {
                            serviceProvider.getEventService().upsertEvent(event);
                        }
                    }
                });

            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error occurred while dispatching events data to database");
        }

        LOGGER.log(Level.INFO, "Finished dispatching events");
    }

    private void startDispatch() {
        LOGGER.log(Level.INFO, "Started dispatching the data");

        dispatchEventsData(scraperStateHolder.getScraperEventsStateList());

        // forced shutdown of the executor service if the processing is not finished within 10 seconds
        executorServiceWrapper.forcedShutDown(10, Constants.SCRAPER_DISPATCHER_EXECUTOR_SERVICE);

        // release all the memory from states and containers
        cleanup();

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
        startDispatch();
    }
}
