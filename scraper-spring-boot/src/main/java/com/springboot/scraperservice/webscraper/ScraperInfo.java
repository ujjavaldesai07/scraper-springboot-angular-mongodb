package com.springboot.scraperservice.webscraper;

/**
 * Enum to maintain scrapper information and registered for scraping.
 * Scraper Engine directly iterates over the values of this enum.
 */
public enum ScraperInfo {
    TECH_MEME("https://www.techmeme.com/events", "Techmeme"),
    COMPUTER_WORLD(
            "https://www.computerworld.com/article/3313417/tech-event-calendar-shows-conferences-and-it-expos-updated.html"
            , "ComputerWorld");

    public final String URL;

    // website name
    public final String NAME;

    ScraperInfo(String URL, String NAME) {
        this.URL = URL;
        this.NAME = NAME;
    }
}
