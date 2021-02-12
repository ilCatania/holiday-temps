package it.gcatania.holidaytemps.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Value;

import java.time.LocalDate;

@Value

@JsonIgnoreProperties(ignoreUnknown = true)
public class Holiday {
    String title;
    LocalDate date;
}
