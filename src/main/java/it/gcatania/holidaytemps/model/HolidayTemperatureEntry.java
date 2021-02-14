package it.gcatania.holidaytemps.model;

import lombok.Value;

import java.time.LocalDate;

@Value
public class HolidayTemperatureEntry {
    LocalDate date;
    String title;
    TemperatureBounds temps;
}
