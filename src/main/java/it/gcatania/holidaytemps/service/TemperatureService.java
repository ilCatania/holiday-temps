package it.gcatania.holidaytemps.service;

import it.gcatania.holidaytemps.model.TemperatureBounds;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TemperatureService {

    /**
     * returns temperature bounds for the input dates
     *
     * @param city  the city to query for
     * @param dates the list of dates to query for
     * @return a list of temperature bounds
     */
    public Map<LocalDate, TemperatureBounds> temperatureBounds(String city, List<LocalDate> dates);
}
