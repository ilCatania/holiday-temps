package it.gcatania.holidaytemps.impl;

import it.gcatania.holidaytemps.model.TemperatureBounds;
import it.gcatania.holidaytemps.service.TemperatureService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class TemperatureServiceImpl implements TemperatureService {
    @Override
    public Map<LocalDate, TemperatureBounds> temperatureBounds(List<LocalDate> dates) {
        return null;
    }
}
