package com.springboot.scraperservice.webscraper;

import com.springboot.scraperservice.constants.Constants;
import com.springboot.scraperservice.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.logging.Logger;

@Component
public class ScraperEngine {
    private final static Logger LOGGER = Logger.getLogger(String.valueOf(ScraperEngine.class));
    private final EventService eventService;
    private final ScraperFactory scraperFactory;
    private final ExecutorServiceWrapper executorServiceWrapper;
    private final ScraperDataDispatcher scraperDataDispatcher;

    @Autowired
    public ScraperEngine(EventService eventService,
                         ScraperFactory scraperFactory,
                         ExecutorServiceWrapper executorServiceWrapper,
                         ScraperDataDispatcher scraperDataDispatcher) {
        this.eventService = eventService;
        this.scraperFactory = scraperFactory;
        this.executorServiceWrapper = executorServiceWrapper;
        this.scraperDataDispatcher = scraperDataDispatcher;
    }

    public void begin() {
        ExecutorService executorService = executorServiceWrapper
                .getNewFixedThreadPool(System.getenv("MAX_SCRAPER_THREAD_COUNT"));


        // cleanup the db if we dont need to maintain the history.
//         eventService.dropCollection(Constants.MONGO_EVENT_COLLECTION);

        for (ScraperInfo scraperInfo : ScraperInfo.values()) {
            executorService.execute((Runnable) scraperFactory.createScraper(scraperInfo));
        }
        executorService.execute(scraperDataDispatcher);

        executorServiceWrapper.prepareToShutDown(20, "SCRAPER_EXECUTOR_SERVICE");

    }
}
