package it.gcatania.holidaytemps;

import it.gcatania.holidaytemps.model.Holiday;
import it.gcatania.holidaytemps.model.HolidayTempEntry;
import it.gcatania.holidaytemps.model.TemperatureBounds;
import it.gcatania.holidaytemps.service.HolidayService;
import it.gcatania.holidaytemps.service.TemperatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class TempsController {

    private static Logger log = LoggerFactory.getLogger(TempsController.class);

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private TemperatureService temperatureService;

    @RequestMapping("/bank-holidays/{location}/temps")
    public List<HolidayTempEntry> temps(
            @PathVariable String location,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        if (to == null || to.isAfter(LocalDate.now())) {
            to = LocalDate.now();
        }
        if (from == null) {
            from = to.minusYears(1);
        }
        log.debug("requested holidays: {}", location);
        // TODO check outputs of holiday / temp services for missing entries, handle
        List<Holiday> holidays = holidayService.holidays(location, from, to);
        List<LocalDate> dates = holidays.stream().map(Holiday::getDate).collect(Collectors.toList());
        Map<LocalDate, TemperatureBounds> temperatureBounds = temperatureService.temperatureBounds(location, dates);
        List<HolidayTempEntry> results = holidays.stream()
                .map(h -> new HolidayTempEntry(h.getDate(), h.getTitle(), temperatureBounds.get(h.getDate())))
                .collect(Collectors.toUnmodifiableList());
        return results;
    }

}
