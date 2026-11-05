package hu.berenice.meriem.forex;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ForexTradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /forex/open shows the instrument list and the footer")
    void openFormIsShown() throws Exception {
        mockMvc.perform(get("/forex/open"))
                .andExpect(status().isOk())
                .andExpect(view().name("forex/open"))
                .andExpect(content().string(containsString("EUR_USD")))
                .andExpect(content().string(containsString("Neptun codes")));
    }

    @Test
    @DisplayName("POST /forex/open with 0 units is refused before calling OANDA")
    void zeroUnitsAreRefused() throws Exception {
        mockMvc.perform(post("/forex/open").param("instrument", "EUR_USD").param("units", "0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forex/open"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    @DisplayName("POST /forex/open with too many units is refused")
    void tooManyUnitsAreRefused() throws Exception {
        mockMvc.perform(post("/forex/open").param("instrument", "EUR_USD").param("units", "101"))
                .andExpect(redirectedUrl("/forex/open"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    @DisplayName("POST /forex/open with an unknown instrument is refused")
    void unknownInstrumentIsRefused() throws Exception {
        mockMvc.perform(post("/forex/open").param("instrument", "FOO_BAR").param("units", "5"))
                .andExpect(redirectedUrl("/forex/open"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    @DisplayName("GET /forex/close shows the trade ID form")
    void closeFormIsShown() throws Exception {
        mockMvc.perform(get("/forex/close"))
                .andExpect(status().isOk())
                .andExpect(view().name("forex/close"))
                .andExpect(content().string(containsString("Trade ID")));
    }

    @Test
    @DisplayName("POST /forex/close with a negative trade ID is refused")
    void negativeTradeIdIsRefused() throws Exception {
        mockMvc.perform(post("/forex/close").param("tradeId", "-4"))
                .andExpect(redirectedUrl("/forex/close"))
                .andExpect(flash().attributeExists("error"));
    }
}