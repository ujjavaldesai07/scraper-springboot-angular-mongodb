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
public class ComputerWorldScraper implements Scraper, Runnable {

    private final static Logger LOGGER = Logger.getLogger(String.valueOf(ComputerWorldScraper.class));
    private final static String dateFormat = "yyyy-MM-dd";
    private final ScraperEventsState scraperEventsState;

    @Autowired
    public ComputerWorldScraper(ScraperStateHolder scraperStateHolder,
                                ScraperEventsState scraperEventsState) {
        this.scraperEventsState = scraperEventsState;

        scraperEventsState.setIsActive(true);
        scraperStateHolder.registerEventsState(scraperEventsState);
    }

    private void processDocument(Document document) {
        LOGGER.log(Level.INFO, "Started Processing ComputerWorldScraper document..." + scraperEventsState.hashCode());

        for (Element row : document.select("table.tablesorter tbody tr")) {

            String title = row.select("th").text();
            String startDate = row.select("td:nth-of-type(2)").text();
            String endDate = row.select("td:nth-of-type(3)").text();
            String location = row.select("td:nth-of-type(4)").text();

            if (!title.equals("")) {
                Events event = new Events();
                event.setTitle(title);
                event.setWebsite(ScraperInfo.COMPUTER_WORLD.NAME);

                if (!location.isEmpty()) {
                    event.setLocation(location);
                }

                Date startDateWithYear = DateConversion.convertStrToDate(startDate, dateFormat);
                if (startDateWithYear != null) {
                    event.setStartDate(startDateWithYear);
                }

                Date endDateWithYear = DateConversion.convertStrToDate(endDate, dateFormat);
                if (endDateWithYear != null) {
                    event.setEndDate(endDateWithYear);
                }

                scraperEventsState.getEventsQueue().add(event);
            }
        }
        LOGGER.log(Level.INFO, "Finished Processing ComputerWorldScraper document...");
    }

    @Override
    public void startScraper() {

        try {
            final Document document = Jsoup.connect(ScraperInfo.COMPUTER_WORLD.URL).get();
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
