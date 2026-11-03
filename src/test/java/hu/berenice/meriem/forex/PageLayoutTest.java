package hu.berenice.meriem.forex;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The task says the creators' names and Neptun codes must be at the bottom of every page.
 * These pages need no OANDA call when opened without parameters, so no network is used.
 */
@SpringBootTest
@AutoConfigureMockMvc
class PageLayoutTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void homePageHasNavbarAndFooter() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ForexVision")))
                .andExpect(content().string(containsString("Neptun codes")));
    }

    @Test
    void calculatorPageHasFooter() throws Exception {
        mockMvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Neptun codes")));
    }

    @Test
    void actualPricePageShowsInstrumentDropdownAndFooter() throws Exception {
        mockMvc.perform(get("/forex/actual-price"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("EUR_USD")))
                .andExpect(content().string(containsString("Neptun codes")));
    }

    @Test
    void historicalPricePageShowsGranularityDropdown() throws Exception {
        mockMvc.perform(get("/forex/historical-price"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("D – daily")))
                .andExpect(content().string(containsString("Neptun codes")));
    }

    @Test
    void unknownInstrumentIsRejectedWithoutCallingOanda() throws Exception {
        mockMvc.perform(get("/forex/actual-price").param("instrument", "FOO_BAR"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Unknown instrument")));
    }
}
