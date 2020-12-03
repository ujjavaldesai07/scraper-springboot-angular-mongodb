package com.springboot.scraperservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class EventsDTO {

    private String title;

    private String location;

    private String website;

    private String startDate;

    private String endDate;
}
