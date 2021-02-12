package it.gcatania.holidaytemps;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class HolidayTempsApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void someTempsAreReturned() throws Exception {
        mockMvc.perform(get("/bank-holidays/london/temps", String.class))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[1].title", is("Christmas")));
    }
}
