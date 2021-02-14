package it.gcatania.holidaytemps.impl;

import it.gcatania.holidaytemps.model.TemperatureBounds;
import it.gcatania.holidaytemps.service.TemperatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TemperatureServiceImpl implements TemperatureService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    @Autowired
    private RestTemplate restTemplate;

    @Value("${temperature.ws.root}")
    private String wsRoot;

    @Override
    public Map<LocalDate, TemperatureBounds> temperatureBounds(String location, List<LocalDate> dates) {
        List<Map<String, ?>> woeidResults = restTemplate
                .getForObject(wsRoot + "/api/location/search/?query={location}", List.class, location);
        // TODO better error handling, data parsing
        if (woeidResults.isEmpty()) {
            throw new IllegalArgumentException("Location not recognized: " + location);
        }
        int woeid = (int) woeidResults.get(0).get("woeid");
        Map<LocalDate, TemperatureBounds> temps = new HashMap<>(dates.size());
        for (LocalDate d : dates) {
            String dateStr = DATE_FORMATTER.format(d);
            List<Map<String, ?>> tempsForDate = restTemplate
                    .getForObject(wsRoot + "/api/location/{woeid}/" + dateStr + "/", List.class, woeid);
            if (tempsForDate.isEmpty()) {
                continue; // TODO better handling?
            }
            double min = Double.POSITIVE_INFINITY;
            double max = Double.NEGATIVE_INFINITY;
            for (Map<String, ?> data : tempsForDate) {
                min = Math.min(min, (double) data.get("min_temp"));
                max = Math.max(max, (double) data.get("max_temp"));
            }
            temps.put(d, new TemperatureBounds(max, min));
        }
        return Collections.unmodifiableMap(temps);
    }
}
