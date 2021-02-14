package it.gcatania.holidaytemps;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import it.gcatania.holidaytemps.model.Holiday;
import it.gcatania.holidaytemps.service.HolidayService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = HolidayTempsApplication.class, initializers = WireMockInitializer.class)
public class HolidayServiceTest {

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private HolidayService holidayService;

    @Test
    public void testGetHolidays() {
        // set up mock webservice
        wireMockServer.stubFor(WireMock.get("/bank-holidays.json")
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("bank-holidays-response.json")));

        //verify service call
        List<Holiday> expected = Arrays.asList(
                new Holiday("Christmas", LocalDate.parse("2020-12-25")),
                new Holiday("Boxing Day", LocalDate.parse("2020-12-26")),
                new Holiday("New Year's Day", LocalDate.parse("2021-01-01"))
        );
        List<Holiday> actual = holidayService.holidays(
                "London",
                LocalDate.parse("2020-12-01"),
                LocalDate.parse("2021-01-01"));
        assertThat(actual, is(equalTo(expected)));
    }
}
