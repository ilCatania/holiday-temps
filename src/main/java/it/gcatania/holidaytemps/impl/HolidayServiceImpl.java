package it.gcatania.holidaytemps.impl;

import it.gcatania.holidaytemps.model.Holiday;
import it.gcatania.holidaytemps.service.HolidayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HolidayServiceImpl implements HolidayService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${holiday.ws.root}")
    private String wsRoot;

    @Override
    public List<Holiday> holidays(String location, LocalDate from, LocalDate to) {
        log.info("Retrieving holidays for {} between {} and {}", location, from, to);
        Map<String, ?> holidayData = restTemplate.getForObject(wsRoot + "/bank-holidays.json", Map.class);
        // TODO figure out correct region from city, improve deserialization, error handling
        Map<String, ?> holidaysForRegion = (Map<String, ?>) holidayData.get("england-and-wales");
        List<Map<String, String>> holidayEntries = (List<Map<String, String>>) holidaysForRegion.get("events");
        List<Holiday> holidayResults = holidayEntries
                .stream()
                .map(m -> new Holiday(m.get("title"), LocalDate.parse(m.get("date"))))
                .filter(h -> !h.getDate().isBefore(from) && !h.getDate().isAfter(to))
                .sorted(Comparator.comparing(Holiday::getDate))
                .collect(Collectors.toUnmodifiableList());
        log.info("Found {} holidays", holidayResults.size());
        return holidayResults;
    }
}
