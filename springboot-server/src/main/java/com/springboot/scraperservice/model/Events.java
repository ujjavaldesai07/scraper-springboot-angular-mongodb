package com.springboot.scraperservice.model;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Entity class to insert the document in the collection.
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(collection = "events")
public class Events {

    private String title;

    private String location;

    private String website;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date endDate;
}
