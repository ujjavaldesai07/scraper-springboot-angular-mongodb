package com.springboot.scraperservice.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
public class MongoConfig {
    private final static Logger LOGGER = Logger.getLogger(String.valueOf(MongoConfig.class));

    @Bean
    public MongoClient mongo() {
        // get the connection string from env var
        ConnectionString connectionString = new ConnectionString(System.getenv("MONGODB_URI"));

        // build the settings
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToConnectionPoolSettings(builder -> {

                    try {
                        // set the min & max size of pool so we always have some connection in the pool
                        // and we dont have to create new connection every time we access mongodb.
                        // 3 connection per pool + default 4 connections
                        builder.maxSize(5)
                                .minSize(2)
                                .maxWaitTime(60, TimeUnit.SECONDS);
                    } catch (NumberFormatException e) {
                        LOGGER.log(Level.SEVERE,
                                "Unable to set connection pool settings for mongoDB." +
                                        " Connection pool setting must be in integers.");
                    }
                })
                .build();

        // create mongo client
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {

        // generate MongoTemplate to use the mongoDB advanced APIs like upsert, insertAll etc.
        return new MongoTemplate(mongo(), System.getenv("DB_NAME"));
    }

}
