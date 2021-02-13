package it.gcatania.holidaytemps;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

//@WebMvcTest
class HolidayTempsApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void someTempsAreReturned() throws Exception {
        /*
        mockMvc.perform(get("/bank-holidays/london/temps", String.class))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[1].title", is("Christmas")));
         */
    }
}
