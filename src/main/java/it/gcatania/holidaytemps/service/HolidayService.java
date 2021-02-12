package it.gcatania.holidaytemps.service;

import it.gcatania.holidaytemps.model.Holiday;

import java.time.LocalDate;
import java.util.List;

public interface HolidayService {

    /**
     * return all holidays in a given city between dates
     *
     * @param city the city whose holidays will be returned
     * @param from from date (inclusive), null for no limits
     * @param to   to date (inclusive), null for no limits
     * @return a list of holidays
     */
    public List<Holiday> holidays(String city, LocalDate from, LocalDate to);

}
