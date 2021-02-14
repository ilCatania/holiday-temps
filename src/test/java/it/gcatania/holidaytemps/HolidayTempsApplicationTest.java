package it.gcatania.holidaytemps;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.util.Arrays;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = HolidayTempsApplication.class, initializers = WireMockInitializer.class)
class HolidayTempsApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private WireMockServer wireMockServer;

    /**
     * run integration test on
     * http://localhost:8080/bank-holidays/birmingham/temps?from=2020-12-20&to=2020-12-31
     *
     * @throws Exception on failure
     */
    @Test
    void happyFlow() throws Exception {
        //parameters
        String city = "birmingham";
        String from = "2020-12-20";
        String to = "2020-12-31";

        // set up mock webservice
        wireMockServer.stubFor(WireMock.get("/bank-holidays.json")
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("integration/bank-holidays-response.json")));
        wireMockServer.stubFor(WireMock.get(urlPathEqualTo("/api/location/search/"))
                .withQueryParam("query", equalTo(city))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("integration/woeid-response.json")));
        for (String day : Arrays.asList("25", "28")) { // Christmas, Boxing Day
            wireMockServer.stubFor(WireMock.get("/api/location/12723/2020/12/" + day + "/")
                    .willReturn(aResponse()
                            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                            .withBodyFile("integration/temperature-response-2020-12-" + day + ".json")));
        }

        // check service output against expected
        String expectedContent = Files.readString(resourceLoader
                .getResource("classpath:__files/integration/expected-result.json")
                .getFile()
                .toPath());
        mockMvc.perform(
                get("/bank-holidays/" + city + "/temps", String.class)
                        .param("startDate", from)
                        .param("endDate", to))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(expectedContent));

    }
}