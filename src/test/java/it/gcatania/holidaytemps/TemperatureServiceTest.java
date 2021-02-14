package it.gcatania.holidaytemps;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import it.gcatania.holidaytemps.model.TemperatureBounds;
import it.gcatania.holidaytemps.service.TemperatureService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = HolidayTempsApplication.class, initializers = WireMockInitializer.class)
public class TemperatureServiceTest {

    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private TemperatureService temperatureService;

    @Test
    public void testGetTemperatures() {
        // set up mock webservice
        String city = "Gotham City";
        LocalDate date = LocalDate.parse("2020-12-25");
        wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/api/location/search/"))
                .withQueryParam("query", equalTo(city))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("woeid-response.json")));
        wireMockServer.stubFor(WireMock.get("/api/location/42/2020/12/25/")
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("temperatures-response.json")));

        //verify service call
        Map<LocalDate, TemperatureBounds> expected = Collections
                .singletonMap(date, new TemperatureBounds(8.665, 1.37));
        Map<LocalDate, TemperatureBounds> actual = temperatureService
                .temperatureBounds(city, Collections.singletonList(date));
        assertThat(actual, is(expected));
    }
}
