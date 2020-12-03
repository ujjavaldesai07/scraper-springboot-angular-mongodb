package com.springboot.scraperservice.webscraper;

public enum ScraperInfo {
    TECH_MEME("https://www.techmeme.com/events", "Techmeme"),
    COMPUTER_WORLD(
            "https://www.computerworld.com/article/3313417/tech-event-calendar-shows-conferences-and-it-expos-updated.html"
            , "ComputerWorld");

    public final String URL;
    public final String NAME;

    ScraperInfo(String URL, String NAME) {
        this.URL = URL;
        this.NAME = NAME;
    }
}
