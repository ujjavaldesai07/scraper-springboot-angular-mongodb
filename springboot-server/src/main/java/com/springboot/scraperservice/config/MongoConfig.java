package com.springboot.scraperservice.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.springboot.scraperservice.constants.Constants;
import com.springboot.scraperservice.model.Events;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
public class MongoConfig {
    private final static Logger LOGGER = Logger.getLogger(String.valueOf(MongoConfig.class));
    private MongoTemplate mongoTemplate;

    @Bean
    public MongoClient mongoClient() {
        try {
            // get the connection string from env var
            ConnectionString connectionString = new ConnectionString(System.getenv("MONGODB_URI"));

            // build the settings
            MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .applyToConnectionPoolSettings(builder -> {

                        try {
                            // set the min & max size of pool so we always have some connection in the pool
                            // and we dont have to create new connection every time we access mongodb.
                            builder.maxSize(20).minSize(10).maxWaitTime(60, TimeUnit.SECONDS);
                        } catch (NumberFormatException e) {
                            LOGGER.log(Level.SEVERE,
                                    "Unable to set connection pool settings for mongoDB." +
                                            " Connection pool setting must be in integers.");
                        }
                    })
                    .build();

            // create mongo client
            return MongoClients.create(mongoClientSettings);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Unable to connect mongo client. Reason: " + ex);
            LOGGER.log(Level.SEVERE, "Shutting down the application...");
            System.exit(1);
        }
        return null;
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        try {

            // generate MongoTemplate to use the mongoDB advanced APIs like upsert, insertAll etc.
            mongoTemplate = new MongoTemplate(mongoClient(), "events");

            // adding indexing
            addASCIndexing(Constants.EVENTS_INDEX_ASC_ATTRIBUTES, Events.class);

            return mongoTemplate;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Unable to connect mongo template. Reason: " + ex);
            LOGGER.log(Level.SEVERE, "Shutting down the application...");
            System.exit(1);
        }
        return null;
    }

    private void addASCIndexing(String[] attributesNames, Class<?> documentName) {
        for (String attributeName : attributesNames) {
            mongoTemplate.indexOps(documentName)
                    .ensureIndex(new Index().on(attributeName, Sort.Direction.ASC));
        }
    }

}
