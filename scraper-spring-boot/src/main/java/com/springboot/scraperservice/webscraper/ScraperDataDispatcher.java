package com.springboot.scraperservice.webscraper;

import com.springboot.scraperservice.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ScraperDataDispatcher<T> implements Runnable {
    private final static Logger LOGGER = Logger.getLogger(String.valueOf(ScraperDataDispatcher.class));
    private final ScraperStateManager<T> scraperStateManager;
    private final ExecutorServiceWrapper executorServiceWrapper;
    private ExecutorService executorService;

    @Autowired
    public ScraperDataDispatcher(ScraperStateManager<T> scraperStateManager,
                                 ExecutorServiceWrapper executorServiceWrapper) {
        this.scraperStateManager = scraperStateManager;
        this.executorServiceWrapper = executorServiceWrapper;

        setExecutorService();
    }

    private void setExecutorService() {
        executorService = executorServiceWrapper.getCachedThreadPool();
    }

    private void dispatchData() {
        LOGGER.log(Level.INFO, "Started dispatching events");

        try {
            // iterate over all the queues and upsert the data concurrently into db.
            for (Map.Entry<Integer, List<ScraperDataState<T>>> serviceDataStateEntry :
                    scraperStateManager.getServiceDataStateMap().entrySet()) {
                for (ScraperDataState<T> scraperDataState : serviceDataStateEntry.getValue()) {

                    executorService.execute(() -> {
                        // check if the document is still processing
                        while (scraperDataState.getIsActive() || scraperDataState.getDataQueue().size() > 0) {

                            // grab the first data
                            T data = scraperDataState.getDataQueue().poll();

                            // upsert the data
                            if (data != null) {
                                scraperDataState.getService().upsertData(data);
                            }
                        }

                        // release the memory hold by the state and container
                        scraperStateManager.unregisterScraperState(scraperDataState);
                    });
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error occurred while dispatching events data to database");
        }

        LOGGER.log(Level.INFO, "Finished dispatching events");
    }

    private void startDispatch() {
        LOGGER.log(Level.INFO, "Started dispatching the data");

        dispatchData();

        // forced shutdown of the executor service if the processing is not finished within 10 seconds
        executorServiceWrapper.terminate(10, Constants.SCRAPER_DISPATCHER_EXECUTOR_SERVICE);

        // tear down the map
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
        startDispatch();
    }
}
