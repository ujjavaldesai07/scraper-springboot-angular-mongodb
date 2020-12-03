package com.springboot.scraperservice.webscraper;

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
        LOGGER.log(Level.INFO, "Started dispatching events...");
        for (ScraperEventsState scraperEventsState : scraperEventsStates) {
            System.out.println("address = " + scraperEventsState.hashCode());
            executorService.execute(() -> {
                LOGGER.log(Level.INFO, "STARTED scraperEventsState = " + scraperEventsState.hashCode() + ", Size = " + scraperEventsState.getEventsQueue().size());
                while (scraperEventsState.getIsActive() || scraperEventsState.getEventsQueue().size() > 0) {
                    Events event = scraperEventsState.getEventsQueue().poll();
                    if (event != null) {
                        serviceProvider.getEventService().upsertEvent(event);
                    }
                }
                LOGGER.log(Level.INFO, "DONE scraperEventsState = " + scraperEventsState.hashCode() + ", Size = " + scraperEventsState.getEventsQueue().size());
            });
        }
        LOGGER.log(Level.INFO, "Finished dispatching events...");
    }

    private void startDispatch() {
        LOGGER.log(Level.INFO, "Started dispatching...");
        dispatchEventsData(scraperStateHolder.getScraperEventsStateList());
        executorServiceWrapper.prepareToShutDown(10, "SCRAPER_DISPATCHER_EXECUTOR_SERVICE");
        cleanup();
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
