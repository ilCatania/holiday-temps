package it.gcatania.holidaytemps.impl;

import it.gcatania.holidaytemps.model.Holiday;
import it.gcatania.holidaytemps.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HolidayServiceImpl implements HolidayService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${holiday.ws.host}")
    private String wsHost;

    @Override
    public List<Holiday> holidays(String city, LocalDate from, LocalDate to) {
        Map<String, ?> holidayData = restTemplate.getForObject(wsHost + "/bank-holidays.json", Map.class);
        // TODO figure out correct region from city, improve deserialization, error handling
        Map<String, ?> holidaysForRegion = (Map<String, ?>) holidayData.get("england-and-wales");
        List<Map<String, String>> holidayEntries = (List<Map<String, String>>) holidaysForRegion.get("events");
        List<Holiday> holidays = holidayEntries
                .stream()
                .map(m -> new Holiday(m.get("title"), LocalDate.parse(m.get("date"))))
                .sorted(Comparator.comparing(Holiday::getDate))
                .collect(Collectors.toList());
        return Collections.unmodifiableList(holidays);
    }
}
