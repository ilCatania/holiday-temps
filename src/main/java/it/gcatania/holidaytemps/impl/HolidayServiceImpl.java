package it.gcatania.holidaytemps.impl;

import it.gcatania.holidaytemps.model.Holiday;
import it.gcatania.holidaytemps.service.HolidayService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HolidayServiceImpl implements HolidayService {
    @Override
    public List<Holiday> holidays(String city, LocalDate from, LocalDate to) {
        return null;
    }
}
