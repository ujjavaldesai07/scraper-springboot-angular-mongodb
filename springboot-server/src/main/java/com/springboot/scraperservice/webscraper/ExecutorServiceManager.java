package com.springboot.scraperservice.webscraper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Executor service manager is use to manage the state, functionalities and handle exceptions.
 * This also need as the executor service is used twice i.e. in ScraperEngine and ScraperDataDispatcher.
 * This is prototype scope as it is used on two different places at the same time.
 */

@Setter
@Getter
@Component
public class ExecutorServiceManager {
    private final static Logger LOGGER = Logger.getLogger(String.valueOf(ExecutorServiceManager.class));

    /**
     * This will be used for CPU bound operations
     *
     * @param maxThreadCount: Max number of threads assigned to the pool.
     * @return new executorService with fixed thread pool
     */
    public ExecutorService getNewFixedThreadPool(int maxThreadCount, String executorServiceName) {
        try {
            // start the executor service with MAX_SCRAPER_THREAD_COUNT
            return Executors.newFixedThreadPool(maxThreadCount);

        } catch (NumberFormatException ex) {

            LOGGER.log(Level.SEVERE,
                    String.format("Provided maxThreadCount [value=%s] is not an integer: %s ",
                            maxThreadCount, ex));
        } catch (Exception ex) {

            // catch any exceptions
            LOGGER.log(Level.SEVERE,
                    String.format("Failed to get NewFixedThreadPool %s executor service. Reason: %s",
                            executorServiceName, ex));
        }
        return null;
    }

    /**
     * This is cached pool used for IO bound operation, threads are created by
     * executor server as per our number of data in the queue.
     *
     * @return new executorService with fixed thread pool
     */
    public ExecutorService getCachedThreadPool(String executorServiceName) {
        try {
            return Executors.newCachedThreadPool();

        } catch (Exception ex) {

            LOGGER.log(Level.SEVERE,
                    String.format("Failed to get CachedThreadPool for %s executor service. Reason: %s",
                            executorServiceName, ex));
        }
        return null;
    }

    /**
     * This function shutdown the executionService upon the tasks completion.
     *
     * @param futureList: takes the list of futures from the tasks getting executed.
     */
    public void shutdownUponTaskCompletion(List<Future<?>> futureList, ExecutorService executorService) {
        for (Future<?> future : futureList) {
            try {
                // check whether we have value else wait as get() is a blocking call
                future.get();
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Unable to get the value from the future.");
            }
        }

        // now we are sure that all the tasks are completed at this point.
        executorService.shutdown();
    }

    /**
     * Shutdown the service after sometime as this will not get automatically terminated.
     *
     * @param seconds:             wait for number of given seconds and then terminate
     * @param executorServiceName: service name to check which if exceptions occurred.
     */
    public void scheduleTermination(int seconds, String executorServiceName, ExecutorService executorService) {
        try {
            if (executorService.awaitTermination(seconds, TimeUnit.SECONDS)) {
                LOGGER.log(Level.INFO, String.format("Task completed for executive service %s", executorServiceName));
            } else {
                LOGGER.log(Level.INFO, String.format("Forced shutdown for executive service %s", executorServiceName));
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            LOGGER.log(Level.SEVERE,
                    String.format("Executor service is interrupted for for executive service %s", executorServiceName));
        }
    }
}
