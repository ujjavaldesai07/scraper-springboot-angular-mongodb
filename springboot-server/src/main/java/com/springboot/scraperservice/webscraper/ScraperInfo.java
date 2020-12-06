package com.springboot.scraperservice.webscraper;

/**
 * Enum to maintain scrapper information and registered for scraping.
 * Scraper Engine directly iterates over the values of this enum.
 */
public enum ScraperInfo {
    TECH_MEME("https://www.techmeme.com/events", "Techmeme", 1),
    COMPUTER_WORLD(
            "https://www.computerworld.com/article/3313417/tech-event-calendar-shows-conferences-and-it-expos-updated.html"
            , "ComputerWorld", 2);


    public final String URL;
    public final Integer ID;

    // website name
    public final String NAME;

    ScraperInfo(String URL, String NAME, Integer ID) {
        this.URL = URL;
        this.NAME = NAME;
        this.ID = ID;
    }
}
