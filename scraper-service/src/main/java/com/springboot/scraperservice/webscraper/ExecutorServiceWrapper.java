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

@Setter
@Getter
@Component(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExecutorServiceWrapper {
    private final static Logger LOGGER = Logger.getLogger(String.valueOf(ExecutorServiceWrapper.class));
    private ExecutorService executorService;

    public ExecutorService getNewFixedThreadPool(String maxThreadCount) {
        try {
            // start the executor service with MAX_SCRAPER_THREAD_COUNT
            executorService = Executors.newFixedThreadPool(Integer.parseInt(
                    System.getenv("MAX_SCRAPER_THREAD_COUNT")
            ));
            setExecutorService(executorService);
            return executorService;

        } catch (NumberFormatException ex) {

            LOGGER.log(Level.SEVERE,
                    String.format("Provided MAX_SCRAPER_THREAD_COUNT [value=%s] is not an integer: %s ",
                            System.getenv("MAX_SCRAPER_THREAD_COUNT"), ex));
        } catch (Exception ex) {

            LOGGER.log(Level.SEVERE, String.format("Failed to getNewFixedThreadPool executor service. Reason: %s", ex));
        }
        return null;
    }

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

    public void prepareToShutDown(int seconds, String executorServiceName) {
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
