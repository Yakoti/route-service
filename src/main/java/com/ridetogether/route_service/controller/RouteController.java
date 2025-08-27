package com.ridetogether.route_service.controller;

import com.ridetogether.route_service.dto.DriverRouteDto;
import com.ridetogether.route_service.dto.PassengerRouteDto;
import com.ridetogether.route_service.dto.RouteDto;
import com.ridetogether.route_service.proxy.UserServiceProxy;
import com.ridetogether.route_service.service.RouteService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController("/routes")
public class RouteController {
    private static final Logger logger = LoggerFactory.getLogger(RouteController.class);
    private final RouteService routeService;

    public RouteController(RouteService routeService, UserServiceProxy proxy) {
        this.routeService = routeService;
    }

    @GetMapping("/drivers/{driverId}/matchingPassengers")
    public ResponseEntity<List<PassengerRouteDto>> getMatchingPassengers(@PathVariable Long driverId){
        return routeService.getMatchingPassengers(driverId);
    }

    @GetMapping("/passengers/{passengerId}/matchingDrivers")
    public ResponseEntity<List<DriverRouteDto>> getMatchingDrivers(@PathVariable Long passengerId){
        return routeService.getMatchingDrivers(passengerId);
    }

    //GET /routes/drivers/{driverId}/withPassengers?ids=1,2,3,4,5
    @GetMapping("/drivers/{driverId}/withPassengers")
    public ResponseEntity<RouteDto> getRouteWithPassengers(
            @PathVariable Long driverId,
            @RequestParam List<Long> passengersIds) {
          return  routeService.buildRouteWithPassengers(driverId, passengersIds);
    }


}
