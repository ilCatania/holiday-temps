package it.gcatania.holidaytemps;

import lombok.Value;

import java.time.LocalDate;

@Value
public class HolidayTempEntry {
    LocalDate date;
    String title;
    Temperatures temps;
}
