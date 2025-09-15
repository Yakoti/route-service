//package com.ridetogether.route_service.proxy;
//
//import com.ridetogether.route_service.dto.DriverDto;
//import com.ridetogether.route_service.dto.PassengerDto;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
////userservice + eureka have to be running +
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class UserServiceProxyIntegrationTest {
//
//    @Autowired
//    private UserServiceProxy userServiceProxy;
//
//    @Test
//    void testGetDriverById() {
//        DriverDto driver = userServiceProxy.getDriverById(9L);
//        System.out.println("Driver ID 9: " + driver.id() + ", Name: " + driver.name());
//    }
//
//    @Test
//    void testGetAllDrivers() {
//        List<DriverDto> drivers = userServiceProxy.getAllDrivers();
//        System.out.println("All Drivers count: " + drivers.size());
//    }
//
//    @Test
//    void testFindMatchingPassengers() {
//        List<PassengerDto> passengers = userServiceProxy.findMatchingPassengers(9L);
//        System.out.println("Matching passengers for Driver 9: " + passengers.size());
//    }
//
//    @Test
//    void testGetPassengerById() {
//        PassengerDto passenger = userServiceProxy.getPassengerById(1L);
//        System.out.println("Passenger ID 1: " + passenger.id() + ", Name: " + passenger.name());
//    }
//
//    @Test
//    void testGetAllPassengers() {
//        List<PassengerDto> passengers = userServiceProxy.getAllPassengers();
//        System.out.println("All Passengers count: " + passengers.size());
//    }
//
//    @Test
//    void testFindMatchingDrivers() {
//        List<DriverDto> drivers = userServiceProxy.findMatchingDrivers(1L);
//        System.out.println("Matching drivers for Passenger 1: " + drivers.size());
//    }
//}
