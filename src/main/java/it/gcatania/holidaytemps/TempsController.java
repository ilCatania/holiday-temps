package it.gcatania.holidaytemps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @RequestMapping("/bank-holidays/{city}/temps")
    public List<HolidayTempEntry> temps(@PathVariable("city") String city) {
        log.debug("requested city: {}", city);
        return Arrays.asList(
                new HolidayTempEntry(
                        LocalDate.of(2021, Month.JANUARY, 23),
                        "Mardi gras",
                        new Temperatures(22.3d, 33.5d)
                ),
                new HolidayTempEntry(
                        LocalDate.of(2018, Month.DECEMBER, 25),
                        "Christmas",
                        new Temperatures(20.6d, 30.0d)
                )
        );
    }

}
