package com.springboot.scraperservice.webscraper;

import com.springboot.scraperservice.webscraper.scrapers.ComputerWorldScraper;
import com.springboot.scraperservice.webscraper.scrapers.TechMemeScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ScraperFactory {
    private final static Logger LOGGER = Logger.getLogger(String.valueOf(ScraperFactory.class));

    private final TechMemeScraper techMemeScraper;
    private final ComputerWorldScraper computerWorldScraper;

    @Autowired
    public ScraperFactory(TechMemeScraper techMemeScraper,
                          ComputerWorldScraper computerWorldScraper) {
        this.techMemeScraper = techMemeScraper;
        this.computerWorldScraper = computerWorldScraper;
    }

    public Scraper createScraper(ScraperInfo scraperInfo) {
        switch (scraperInfo) {
            case TECH_MEME:
                return techMemeScraper;

            case COMPUTER_WORLD:
                return computerWorldScraper;

            default:
                LOGGER.log(Level.SEVERE, "Unsupported scraper " + scraperInfo);
        }
        return null;
    }
}
