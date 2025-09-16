package com.ridetogether.route_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ridetogether.route_service.dto.PassengerRouteDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
@SpringBootTest → starts the full Spring Boot application context for the test,
loading all beans (controllers, services, proxies, etc.)
@AutoConfigureMockMvc → sets up MockMvc so we can simulate HTTP requests
without starting a real server.
*/
@SpringBootTest
@AutoConfigureMockMvc
public class RouteControllerTest {

    @Autowired
    private MockMvc mockMvc;
    // MockMvc allows performing HTTP requests to your controllers in tests
    // without starting a real HTTP server.

    @Autowired
    private ObjectMapper objectMapper;
    // ObjectMapper is used for JSON serialization/deserialization and pretty printing.

    // ===== Test 1: GET /routes/drivers/{driverId}/matchingPassengers =====
    @Test
    void testGetMatchingPassengers() throws Exception {
        Long driverId = 11L; // example driver ID

        // Perform a GET request to the endpoint and expect HTTP 200 OK
        MvcResult result = mockMvc.perform(get("/routes/drivers/" + driverId + "/matchingPassengers"))
                .andExpect(status().isOk())
                .andReturn(); // returns the response content and metadata

        String json = result.getResponse().getContentAsString();
        // Get the raw JSON response as a String

        // Convert JSON to List<PassengerRouteDto> so it can be processed in Java
        List<PassengerRouteDto> passengers = objectMapper.readValue(
                json, objectMapper.getTypeFactory().constructCollectionType(List.class, PassengerRouteDto.class)
        );

        // Pretty-print the JSON so it’s readable in the console
        String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(passengers);
        System.out.println(prettyJson);
    }

    // ===== Test 2: GET /routes/passengers/{passengerId}/matchingDrivers =====
    @Test
    void testGetMatchingDrivers() throws Exception {
        Long passengerId = 9L; // example passenger ID

        MvcResult result = mockMvc.perform(get("/routes/passengers/" + passengerId + "/matchingDrivers"))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();

        // Convert JSON response to List<PassengerRouteDto> for processing
        List<PassengerRouteDto> drivers = objectMapper.readValue(
                json, objectMapper.getTypeFactory().constructCollectionType(List.class, PassengerRouteDto.class)
        );

        // Pretty print JSON for easy reading
        String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(drivers);
        System.out.println(prettyJson);
    }

    // ===== Test 3: GET /routes/drivers/{driverId}/withPassengers?passengersIds=1,2,3 =====
    @Test
    void testGetRouteWithPassengers() throws Exception {
        Long driverId = 11L; // example driver ID

        // Perform GET request with query parameters (passengersIds list)
        // MockMvc will automatically serialize multiple values properly for List<Long> @RequestParam
        MvcResult result = mockMvc.perform(get("/routes/drivers/" + driverId + "/withPassengers")
                        .param("passengersIds", "1", "2", "3", "4", "5"))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();

        // Deserialize the JSON to a list of PassengerRouteDto objects
        List<PassengerRouteDto> passengers = objectMapper.readValue(
                json, objectMapper.getTypeFactory().constructCollectionType(List.class, PassengerRouteDto.class)
        );

        // Pretty print JSON
        String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(passengers);
        System.out.println(prettyJson);
    }
}
