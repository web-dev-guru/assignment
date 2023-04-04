package com.college.assignment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TrainScheduleApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("/schedule works")
    void basicEndpointWorks() throws Exception {
        mockMvc.perform(get("/schedule"))
                .andExpect(status().isOk());
    }

    @Nested
    @DisplayName("Q1 - query by line and departure time")
    class Question1 {

        @Test
        @DisplayName("Happy path - query by line and time works")
        void q1HappyPath() throws Exception {
            mockMvc.perform(get("/schedule/Lakeshore?departure=800"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].line").value("Lakeshore"))
                    .andExpect(jsonPath("$[0].departure").value("800"));
        }

        @Test
        @DisplayName("Departure time is optional")
        void q1OptionalDeparture() throws Exception {
            mockMvc.perform(get("/schedule/Barrie"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$", hasSize(7)))
                    .andExpect(jsonPath("$[0].line").value("Barrie"));
        }

        @Test
        @DisplayName("Returns empty array for a valid line and time that does not exist")
        void q1EmptyResultForNonExistingTime() throws Exception {
            mockMvc.perform(get("/schedule/Kitchener?departure=600")) // does not exist
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$", hasSize(0)));
        }
    }

    @Nested
    @DisplayName("Q2 - HTTP response improvements")
    class Question2 {

        @Test
        @DisplayName("Returns HTTP 404 for unknown train line")
        void q2returns404ForUnknownTrainLines() throws Exception {
            mockMvc.perform(get("/schedule/CrazyTrain")) // does not exist
                    .andExpect(status().isNotFound());
        }

        @ParameterizedTest
        @DisplayName("Returns HTTP 400 for invalid time")
        @ValueSource(strings = {"-100", "2500", "1299", "banana"})
        void q2returns400forInvalidDepartureTime(String invalidTime) throws Exception {
            mockMvc.perform(get("/schedule/Barrie?departure=" + invalidTime))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Q3 - Human-readable times")
    class Question3 {
        @ParameterizedTest
        @DisplayName("Accepts human readable departure times")
        @CsvSource({
                "Lakeshore,10:00am,1000",
                "Barrie,7:30am,730",
                "Barrie,07:30am,730",
                "Barrie,4:30pm,1630",
                "Barrie,04:30pm,1630",
        })
        void q3convertsHumanReadableTime(String line, String time, int correctTime) throws Exception {
            mockMvc.perform(get("/schedule/" + line + "?departure=" + time))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].line").value(line))
                    .andExpect(jsonPath("$[0].departure").value(correctTime));
        }

        @ParameterizedTest
        @DisplayName("Rejects invalid human readable departure times")
        @ValueSource(strings = {"10am", "23:00", "16:00pm", "16:00am", "1200am", "3:88am", "57:00am"})
        void q3returns400forInvalidDepartureTime(String invalidTime) throws Exception {
            mockMvc.perform(get("/schedule/Lakeshore?departure=" + invalidTime))
                    .andExpect(status().isBadRequest());
        }
    }
}
