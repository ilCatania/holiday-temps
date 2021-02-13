package it.gcatania.holidaytemps;

import it.gcatania.holidaytemps.model.Holiday;
import it.gcatania.holidaytemps.model.HolidayTempEntry;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = HolidayTempsApplication.class)
public class TempsControllerTests {

    @MockBean
    private HolidayService holidayService;

    @MockBean
    private TemperatureService temperatureService;

    @Autowired
    private TempsController tempsController;

    @Test
    public void happyFlow() {
        String city = "London";
        Holiday christmas = new Holiday("Christmas", LocalDate.of(2020, Month.DECEMBER, 25));
        Holiday newYearsDay = new Holiday("New Year's Day", LocalDate.of(2021, Month.JANUARY, 1));
        Map<LocalDate, TemperatureBounds> tempBoundsMap = new HashMap<>();
        tempBoundsMap.put(christmas.getDate(), new TemperatureBounds(16, 14));
        tempBoundsMap.put(newYearsDay.getDate(), new TemperatureBounds(22, 20));

        List<HolidayTempEntry> expected = Arrays.asList(
                new HolidayTempEntry(christmas.getDate(), christmas.getTitle(), tempBoundsMap.get(christmas.getDate())),
                new HolidayTempEntry(newYearsDay.getDate(), newYearsDay.getTitle(), tempBoundsMap.get(newYearsDay.getDate()))
        );

        when(holidayService.holidays(city, null, null))
                .thenReturn(Arrays.asList(christmas, newYearsDay));
        when(temperatureService.temperatureBounds(Arrays.asList(christmas.getDate(), newYearsDay.getDate())))
                .thenReturn(tempBoundsMap);

        List<HolidayTempEntry> actual = tempsController.temps(city);
        assertThat(actual, is(equalTo(expected)));
        verify(holidayService);
        verify(temperatureService);
    }
}