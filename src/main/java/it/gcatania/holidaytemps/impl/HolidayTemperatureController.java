package it.gcatania.holidaytemps.impl;

import it.gcatania.holidaytemps.model.Holiday;
import it.gcatania.holidaytemps.model.HolidayTemperatureEntry;
import it.gcatania.holidaytemps.model.TemperatureBounds;
import it.gcatania.holidaytemps.service.HolidayService;
import it.gcatania.holidaytemps.service.TemperatureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class HolidayTemperatureController {

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private TemperatureService temperatureService;

    @RequestMapping("/bank-holidays/{location}/temps")
    public List<HolidayTemperatureEntry> temps(
            @PathVariable String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        // if dates unspecified or invalid, default to last year
        if (endDate == null || endDate.isAfter(LocalDate.now())) {
            endDate = LocalDate.now();
        }
        if (startDate == null) {
            startDate = endDate.minusYears(1);
        }
        log.info("Retrieving holidays and temperatures for {} between {} and {}", location, startDate, endDate);
        // TODO check outputs of holiday / temp services for missing entries, handle
        List<Holiday> holidays = holidayService.holidays(location, startDate, endDate);
        List<LocalDate> dates = holidays.stream().map(Holiday::getDate).collect(Collectors.toList());
        Map<LocalDate, TemperatureBounds> temperatureBounds = temperatureService.temperatureBounds(location, dates);
        List<HolidayTemperatureEntry> results = holidays.stream()
                .map(h -> new HolidayTemperatureEntry(h.getDate(), h.getTitle(), temperatureBounds.get(h.getDate())))
                .collect(Collectors.toUnmodifiableList());
        return results;
    }

}
