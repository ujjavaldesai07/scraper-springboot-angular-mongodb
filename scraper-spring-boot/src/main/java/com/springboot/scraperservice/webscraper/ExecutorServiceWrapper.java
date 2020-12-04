package com.springboot.scraperservice.webscraper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Executor service wrapper is use to maintain the state, functionalities and handle exceptions.
 * This also need as the executor service is used twice i.e. in ScraperEngine and ScraperDataDispatcher.
 * This is prototype scope as it is used on two different places at the same time.
 *
 */

@Setter
@Getter
@Component(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExecutorServiceWrapper {
    private final static Logger LOGGER = Logger.getLogger(String.valueOf(ExecutorServiceWrapper.class));
    private ExecutorService executorService;

    /**
     * This will be used for CPU bound operations
     * @param maxThreadCount: Max number of threads assigned to the pool.
     * @return
     */
    public ExecutorService getNewFixedThreadPool(String maxThreadCount) {
        try {
            // start the executor service with MAX_SCRAPER_THREAD_COUNT
            executorService = Executors.newFixedThreadPool(Integer.parseInt(maxThreadCount));
            setExecutorService(executorService);
            return executorService;

        } catch (NumberFormatException ex) {

            LOGGER.log(Level.SEVERE,
                    String.format("Provided MAX_SCRAPER_THREAD_COUNT [value=%s] is not an integer: %s ",
                            System.getenv("MAX_SCRAPER_THREAD_COUNT"), ex));
        } catch (Exception ex) {

            // catch any exceptions
            LOGGER.log(Level.SEVERE, String.format("Failed to getNewFixedThreadPool executor service. Reason: %s", ex));
        }
        return null;
    }

    /**
     * This is cached pool used for IO bound operation, where we dont know
     * how many threads are needed.
     * @return
     */
    public ExecutorService getCachedThreadPool() {
        try {
            executorService = Executors.newCachedThreadPool();
            setExecutorService(executorService);
            return executorService;

        } catch (Exception ex) {

            LOGGER.log(Level.SEVERE, String.format("Failed to getCachedThreadPool executor service. Reason: %s", ex));
        }
        return null;
    }

    /**
     * Shutdown the service after sometime as this will not get automatically terminated.
     * @param seconds: wait for number of given seconds and then terminate
     * @param executorServiceName: service name to check which if exceptions occurred.
     */
    public void terminate(int seconds, String executorServiceName) {
        try {
            if (getExecutorService().awaitTermination(seconds, TimeUnit.SECONDS)) {
                LOGGER.log(Level.INFO, String.format("Task completed for executive service %s", executorServiceName));
            } else {
                LOGGER.log(Level.INFO, String.format("Forced shutdown for executive service %s", executorServiceName));
                getExecutorService().shutdownNow();
            }
        } catch (InterruptedException ex) {
            LOGGER.log(Level.SEVERE,
                    String.format("Executor service is interrupted for for executive service %s", executorServiceName));
        }
    }
}
