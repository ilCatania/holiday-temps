package it.gcatania.holidaytemps;

import it.gcatania.holidaytemps.model.HolidayTempEntry;
import it.gcatania.holidaytemps.model.TemperatureBounds;
import it.gcatania.holidaytemps.service.HolidayService;
import it.gcatania.holidaytemps.service.TemperatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

@RestController
public class TempsController {

    private static Logger log = LoggerFactory.getLogger(TempsController.class);

    @Autowired
    private HolidayService holidayService;

    @Autowired
    private TemperatureService temperatureService;

    @RequestMapping("/bank-holidays/{city}/temps")
    public List<HolidayTempEntry> temps(@PathVariable("city") String city) {
        log.debug("requested city: {}", city);
        return Arrays.asList(
                new HolidayTempEntry(
                        LocalDate.of(2021, Month.JANUARY, 23),
                        "Mardi gras",
                        new TemperatureBounds(22.3d, 33.5d)
                ),
                new HolidayTempEntry(
                        LocalDate.of(2018, Month.DECEMBER, 25),
                        "Christmas",
                        new TemperatureBounds(20.6d, 30.0d)
                )
        );
    }

}
