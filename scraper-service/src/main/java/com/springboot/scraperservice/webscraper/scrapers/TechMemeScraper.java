package com.springboot.scraperservice.webscraper.scrapers;

import com.springboot.scraperservice.helper.DateConversion;
import com.springboot.scraperservice.model.Events;
import com.springboot.scraperservice.webscraper.Scraper;
import com.springboot.scraperservice.webscraper.ScraperStateHolder;
import com.springboot.scraperservice.webscraper.ScraperEventsState;
import com.springboot.scraperservice.webscraper.ScraperInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class TechMemeScraper implements Scraper, Runnable {
    private final static Logger LOGGER = Logger.getLogger(String.valueOf(TechMemeScraper.class));
    private final ScraperEventsState scraperEventsState;

    @Autowired
    public TechMemeScraper(ScraperStateHolder scraperStateHolder,
                           ScraperEventsState scraperEventsState) {
        this.scraperEventsState = scraperEventsState;

        scraperEventsState.setIsActive(true);
        scraperStateHolder.registerEventsState(scraperEventsState);
    }

    private Date getDateWithYear(String dateStr) {
        if (dateStr.startsWith("N") || dateStr.startsWith("D")) {
            dateStr += ", 2020";
        } else {
            dateStr += ", 2021";
        }
        return DateConversion.convertStrToDate(dateStr, "MMM d, yyyy");
    }

    private void extractAndSetEventDates(String dateStr, Events event) {
        if (dateStr != null && !dateStr.isEmpty()) {
            String[] dateArr = dateStr.split("-");
            if (dateArr.length == 2) {

                // check if there is month available in the date
                if (dateArr[1].length() > 3) {
                    event.setEndDate(getDateWithYear(dateArr[1]));
                } else {
                    event.setEndDate(getDateWithYear(
                            String.format("%s %s", dateArr[0].split(" ")[0],
                                    dateArr[1])));
                }

            }
            event.setStartDate(getDateWithYear(dateArr[0]));
        }
    }

    private void processDocument(Document document) {
        LOGGER.log(Level.INFO, "Started Processing TechMemeScraper document..." + scraperEventsState.hashCode());

        for (Element row : document.select("div#events div.rhov")) {
            String title = row.select("[href] > div:nth-of-type(2)").text();
            String dates = row.select("[href] > div:nth-of-type(1)").text();
            String location = row.select("[href] > div:nth-of-type(3)").text();
            if (!title.equals("")) {
                Events event = new Events();
                event.setTitle(title);
                event.setWebsite(ScraperInfo.TECH_MEME.NAME);

                if (!location.isEmpty()) {
                    event.setLocation(location);
                }
                extractAndSetEventDates(dates, event);
                scraperEventsState.getEventsQueue().add(event);
            }
        }

        scraperEventsState.setIsActive(false);
        LOGGER.log(Level.INFO, "Finished Processing TechMemeScraper document...");
    }

    @Override
    public void startScraper() {

        try {
            final Document document = Jsoup.connect(ScraperInfo.TECH_MEME.URL).get();
            processDocument(document);
        } catch (Exception ex) {
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
