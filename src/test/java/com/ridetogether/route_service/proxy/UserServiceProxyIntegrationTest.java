package com.ridetogether.route_service.proxy;

import com.ridetogether.route_service.dto.DriverDto;
import com.ridetogether.route_service.dto.PassengerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserServiceProxyIntegrationTest {

    @Autowired
    private UserServiceProxy userServiceProxy;

    @Test
    void testGetDriverById() {
        DriverDto driver = userServiceProxy.getDriverById(11L);
        System.out.println("Driver by ID 1: " + driver);
    }

    @Test
    void testGetAllDrivers() {
        List<DriverDto> allDrivers = userServiceProxy.getAllDrivers();
        System.out.println("All drivers: " + allDrivers);
    }

    @Test
    void testFindMatchingPassengers() {
        List<PassengerDto> matchingPassengers = userServiceProxy.findMatchingPassengers(1L);
        System.out.println("Matching passengers for driver 1: " + matchingPassengers);
    }

    @Test
    void testGetPassengerById() {
        PassengerDto passenger = userServiceProxy.getPassengerById(1L);
        System.out.println("Passenger by ID 1: " + passenger);
    }

    @Test
    void testGetAllPassengers() {
        List<PassengerDto> allPassengers = userServiceProxy.getAllPassengers();
        System.out.println("All passengers: " + allPassengers);
    }

    @Test
    void testFindMatchingDrivers() {
        List<DriverDto> matchingDrivers = userServiceProxy.findMatchingDrivers(1L);
        System.out.println("Matching drivers for passenger 1: " + matchingDrivers);
    }
}
