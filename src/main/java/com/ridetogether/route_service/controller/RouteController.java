package com.ridetogether.route_service.controller;

import com.ridetogether.route_service.dto.DriverRouteDto;
import com.ridetogether.route_service.dto.PassengerRouteDto;
import com.ridetogether.route_service.dto.RouteDto;
import com.ridetogether.route_service.proxy.UserServiceProxy;
import com.ridetogether.route_service.service.RouteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routes")
public class RouteController {
    private static final Logger logger = LoggerFactory.getLogger(RouteController.class);
    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping("/drivers/{driverId}/matchingPassengers")
    public ResponseEntity<List<PassengerRouteDto>> getMatchingPassengers(@PathVariable Long driverId){
        logger.debug("Fetching matching passengers for driverId={}", driverId);
        return routeService.getMatchingPassengers(driverId);
    }

    @GetMapping("/passengers/{passengerId}/matchingDrivers")
    public ResponseEntity<List<DriverRouteDto>> getMatchingDrivers(@PathVariable Long passengerId){
        logger.debug("Fetching matching drivers for passengerId={}", passengerId);
        return routeService.getMatchingDrivers(passengerId);
    }

    // GET : /routes/drivers/11/withPassengers?passengersIds=1,2,3,4,5
    @GetMapping("/drivers/{driverId}/withPassengers")
    public ResponseEntity<RouteDto> getRouteWithPassengers(
            @PathVariable Long driverId,
            @RequestParam List<Long> passengersIds) {
        logger.debug("Building route for driverId={} with passengerIds={}", driverId, passengersIds);
        return routeService.buildRouteWithPassengers(driverId, passengersIds);
    }
}

