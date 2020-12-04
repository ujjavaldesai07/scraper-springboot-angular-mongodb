package com.springboot.scraperservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * To take the query string parameters from URL.
 */

@Getter
@Setter
@NoArgsConstructor
@ToString
public class QueryPropertiesDTO {

    private String location;
    private String website;
    private Date startDate;
    private Date endDate;
    private String sort;
}
