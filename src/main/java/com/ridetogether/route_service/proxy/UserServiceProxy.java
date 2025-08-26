package com.ridetogether.route_service.proxy;

import com.ridetogether.route_service.dto.DriverDto;
import com.ridetogether.route_service.dto.PassengerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserServiceProxy {

    // === DRIVER endpoints ===
    @GetMapping("/users/drivers/{id}")
    DriverDto getDriverById(@PathVariable("id") Long id);

    @GetMapping("/users/drivers")
    List<DriverDto> getAllDrivers();

    @GetMapping("/users/drivers/{id}/matching-drivers")
    List<PassengerDto> findMatchingPassengers(@PathVariable("id") Long id);


    // === PASSENGER endpoints ===
    @GetMapping("/users/passengers/{id}")
    PassengerDto getPassengerById(@PathVariable("id") Long id);

    @GetMapping("/users/passengers")
    List<PassengerDto> getAllPassengers();

    @GetMapping("/users/passengers/{id}/matching-drivers")
    List<DriverDto> findMatchingDrivers(@PathVariable("id") Long id);
}
