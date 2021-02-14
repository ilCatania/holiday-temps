package it.gcatania.holidaytemps;

import it.gcatania.holidaytemps.impl.HolidayTemperatureController;
import it.gcatania.holidaytemps.model.Holiday;
import it.gcatania.holidaytemps.model.HolidayTemperatureEntry;
import it.gcatania.holidaytemps.model.TemperatureBounds;
import it.gcatania.holidaytemps.service.HolidayService;
import it.gcatania.holidaytemps.service.TemperatureService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = HolidayTempsApplication.class)
public class HolidayTemperatureControllerTest {

    @MockBean
    private HolidayService holidayService;

    @MockBean
    private TemperatureService temperatureService;

    @Autowired
    private HolidayTemperatureController holidayTemperatureController;

    @Test
    public void happyFlow() {
        // set up test data
        String city = "London";
        LocalDate from = LocalDate.parse("2020-12-01");
        LocalDate to = LocalDate.parse("2020-12-31");
        Holiday christmas = new Holiday("Christmas", LocalDate.of(2020, Month.DECEMBER, 25));
        Holiday newYearsDay = new Holiday("New Year's Day", LocalDate.of(2021, Month.JANUARY, 1));
        Map<LocalDate, TemperatureBounds> tempBoundsMap = Map.of(
                christmas.getDate(), new TemperatureBounds(16, 14),
                newYearsDay.getDate(), new TemperatureBounds(22, 20)
        );

        List<HolidayTemperatureEntry> expected = Arrays.asList(
                new HolidayTemperatureEntry(christmas.getDate(), christmas.getTitle(), tempBoundsMap.get(christmas.getDate())),
                new HolidayTemperatureEntry(newYearsDay.getDate(), newYearsDay.getTitle(), tempBoundsMap.get(newYearsDay.getDate()))
        );

        // mock services
        when(holidayService.holidays(city, from, to))
                .thenReturn(Arrays.asList(christmas, newYearsDay));
        when(temperatureService.temperatureBounds(city, Arrays.asList(christmas.getDate(), newYearsDay.getDate())))
                .thenReturn(tempBoundsMap);

        // check results
        List<HolidayTemperatureEntry> actual = holidayTemperatureController.temps(city, from, to);
        assertThat(actual, is(equalTo(expected)));
    }
}
