package com.springboot.scraperservice.webscraper.scrapers;

import com.springboot.scraperservice.helper.DateConversion;
import com.springboot.scraperservice.model.Events;
import com.springboot.scraperservice.service.ServiceProvider;
import com.springboot.scraperservice.webscraper.Scraper;
import com.springboot.scraperservice.webscraper.ScraperStateManager;
import com.springboot.scraperservice.webscraper.ScraperDataState;
import com.springboot.scraperservice.webscraper.ScraperInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class scrapes the data from the website and add in the queue.
 * It will also run on the separate thread other then the main thread.
 */

@Component
public class ComputerWorldScraper implements Scraper, Runnable {

    private final static Logger LOGGER = Logger.getLogger(String.valueOf(ComputerWorldScraper.class));
    private final static String dateFormat = "yyyy-MM-dd";
    private final ScraperDataState<Events> scraperEventsDataState;

    @Autowired
    public ComputerWorldScraper(ScraperStateManager<Events> scraperStateManager,
                                ScraperDataState<Events> scraperEventsDataState,
                                ServiceProvider serviceProvider) {
        this.scraperEventsDataState = scraperEventsDataState;

        // register & initialize the the state in scraperStateHolder
        scraperEventsDataState.setIsActive(true);
        scraperEventsDataState.setDataService(serviceProvider.getEventsService());
        scraperEventsDataState.setScraperId(ScraperInfo.COMPUTER_WORLD.ID);
        scraperStateManager.registerScraperState(scraperEventsDataState);
    }

    /**
     * Processes and extract the document using CSS query selectors
     *
     * @param document: HTML document
     */
    private void processDocument(Document document) {
        LOGGER.log(Level.INFO, "Started Processing ComputerWorldScraper document");

        try {
            // iterate over the document
            for (Element row : document.select("table.tablesorter tbody tr")) {

                // query selectors to scrape the data.
                String title = row.select("th").text();
                String startDate = row.select("td:nth-of-type(2)").text();
                String endDate = row.select("td:nth-of-type(3)").text();
                String location = row.select("td:nth-of-type(4)").text();

                // check if title is available as this will unique and required parameter.
                if (!title.equals("")) {

                    Events event = new Events();
                    event.setTitle(title);
                    event.setWebsite(ScraperInfo.COMPUTER_WORLD.NAME);

                    // optional
                    if (!location.isEmpty()) {
                        event.setLocation(location);
                    }

                    // optional
                    Date startDateWithYear = DateConversion.convertStrToDate(startDate, dateFormat);
                    if (startDateWithYear != null) {
                        event.setStartDate(startDateWithYear);
                    }

                    // optional
                    Date endDateWithYear = DateConversion.convertStrToDate(endDate, dateFormat);
                    if (endDateWithYear != null) {
                        event.setEndDate(endDateWithYear);
                    }

                    // add event in the queue
                    scraperEventsDataState.getDataQueue().add(event);
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error occurred while processing the document");
        }


        // reset the status to false in order to release the ScraperDataDispatcher thread
        // which is used to insert data in the database.
        scraperEventsDataState.setIsActive(false);
        LOGGER.log(Level.INFO, "Finished Processing ComputerWorldScraper document.");
    }

    @Override
    public void startScraper() {

        try {
            // Jsoup is used to parse the HTML.
            final Document document = Jsoup.connect(ScraperInfo.COMPUTER_WORLD.URL).get();

            // process document
            processDocument(document);
        } catch (Exception ex) {

            // release the ScraperDataDispatcher thread if exception occurs.
            scraperEventsDataState.setIsActive(false);
            ex.printStackTrace();
        }
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
        startScraper();
    }
}
