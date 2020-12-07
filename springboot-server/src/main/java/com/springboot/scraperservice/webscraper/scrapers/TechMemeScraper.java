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
public class TechMemeScraper implements Scraper, Runnable {
    private final static Logger LOGGER = Logger.getLogger(String.valueOf(TechMemeScraper.class));
    private final ScraperDataState<Events> scraperEventsDataState;

    @Autowired
    public TechMemeScraper(ScraperStateManager<Events> scraperStateManager,
                           ScraperDataState<Events> scraperEventsDataState,
                           ServiceProvider serviceProvider) {
        this.scraperEventsDataState = scraperEventsDataState;

        // register & initialize the the state in scraperStateHolder
        scraperEventsDataState.setScraperId(ScraperInfo.TECH_MEME.ID);

        // pass the database operation from which data service needs to get executed
        // lambda fn is needed because right now I dont know on which index our state
        // is getting registered by scraperStateManager. So manage iterate the list and
        // call the function with appropriate data
        scraperEventsDataState.setConsumer(t -> serviceProvider.getEventsService().upsert(t));
        scraperStateManager.registerScraperState(scraperEventsDataState);
    }

    /**
     * Get the date by appending year based on the current month.
     *
     * @param dateStr: date in string format
     * @return : Date
     */
    private Date getDateWithYear(String dateStr) {

        // year is not present on the website so
        // I am checking whether its month is november or december
        // based on that append the year.
        // eg: Dec 1 => Dec 1, 2020 or Jan 1 => Jan 1, 2021
        if (dateStr.startsWith("N") || dateStr.startsWith("D")) {
            dateStr += ", 2020";
        } else {
            dateStr += ", 2021";
        }

        // convert string to date
        return DateConversion.convertStrToDate(dateStr, "MMM d, yyyy");
    }

    /**
     * Start date and end date are combined and doesn't follow specific format.
     * so this function will try to process the dates in couple of ways.
     *
     * @param dateStr: date string use to process and extract dates
     * @param event:   event reference is used to set the start and end date.
     */
    private void extractAndSetEventDates(String dateStr, Events event) {

        // check of date is not empty
        if (dateStr != null && !dateStr.isEmpty()) {

            try {
                // split the date as I am expecting to get in two formats.
                // for eg: 1) Dec 1-Dec 30 ===> Dec 1 and Dec 30
                // for eg: 2) Dec 1-30 ===> Dec 1 and Dec 30
                String[] dateArr = dateStr.split("-");
                if (dateArr.length == 2) {

                    // check if there is month available in the date
                    if (dateArr[1].length() > 3) {

                        // first way, simple grab the date as we just needs to split
                        event.setEndDate(getDateWithYear(dateArr[1]));
                    } else {

                        // second way, extract the month from the first half of the string
                        // and append it to the end date string.
                        event.setEndDate(getDateWithYear(
                                String.format("%s %s", dateArr[0].split(" ")[0],
                                        dateArr[1])));
                    }

                }

                // start date has fix format
                event.setStartDate(getDateWithYear(dateArr[0]));
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Error occurred while processing dates.");
            }

        }
    }

    /**
     * Processes and extract the document using CSS query selectors
     *
     * @param document: HTML document
     */
    private void processDocument(Document document) {
        LOGGER.log(Level.INFO, "Started Processing TechMeme document");

        // make this active as we are continuously scraping
        scraperEventsDataState.setIsActive(true);

        try {
            // iterate over the document
            for (Element row : document.select("div#events div.rhov")) {

                // query selectors to scrape the data.
                String title = row.select("[href] > div:nth-of-type(2)").text();
                String dates = row.select("[href] > div:nth-of-type(1)").text();
                String location = row.select("[href] > div:nth-of-type(3)").text();

                // check if title is available as this will unique and required parameter.
                if (!title.equals("")) {

                    Events event = new Events();
                    event.setTitle(title);
                    event.setWebsite(ScraperInfo.TECH_MEME.NAME);

                    // optional
                    if (!location.isEmpty()) {
                        event.setLocation(location);
                    }

                    // process date string
                    extractAndSetEventDates(dates, event);

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

        LOGGER.log(Level.INFO, "Finished Processing TechMeme document");
    }

    @Override
    public void startScraper() {
        try {

            // Jsoup is used to parse the HTML.
            final Document document = Jsoup.connect(ScraperInfo.TECH_MEME.URL).get();

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
        LOGGER.log(Level.INFO, String.format("[**Scraper TechMemeScraper Thread**]: %s", Thread.currentThread().getName()));
        startScraper();
    }
}
