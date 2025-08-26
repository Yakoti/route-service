package com.ridetogether.route_service.controller;

import com.ridetogether.route_service.dto.DriverDto;
import com.ridetogether.route_service.dto.PassengerDto;
import com.ridetogether.route_service.dto.RouteDto;
import com.ridetogether.route_service.proxy.UserServiceProxy;
import com.ridetogether.route_service.service.RouteService;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.ServiceUnavailableException;

@RestController("/routes")
public class RouteController {
    private static final Logger logger = LoggerFactory.getLogger(RouteController.class);
    private final RouteService routeService;
    private final UserServiceProxy proxy;

    public RouteController(RouteService routeService, UserServiceProxy proxy) {
        this.routeService = routeService;
        this.proxy = proxy;
    }

//    @GetMapping("/matches")
//    public ResponseEntity<List<UserRouteDto>> getMatchingRoutes(@RequestParam String role, @RequestParam Long Id){
//
//        logger.info("getMatchingRoutes called with driverId={}", driverId);
//        List<User> users = userService.findUsersWithMatchingSchedulesAndOppositeRole(driverId);
//        List<UserRouteDto> userRouteDtos = new ArrayList<>();
//        for (User user : users) {
//            UserRouteDto dto = new UserRouteDto(user, routeService.getRouteDtoForOneOrMorePassengers(driverId, List.of(user.getId())));
//            logger.info("Matching userId={} added with route mapsLink={}", user.getId(), dto.getRouteDto().getMapsUrl());
//            userRouteDtos.add(dto);
//        }
//        return ResponseEntity.ok(userRouteDtos);
//    }

    //GET /routes/drivers/{driverId}/withPassengers?ids=1,2,3,4,5
    @GetMapping("/drivers/{driverId}/withPassengers")
    public ResponseEntity<RouteDto> getRouteWithPassengers(
            @PathVariable Long driverId,
            @RequestParam List<Long> passengersIds) {

        DriverDto driver;
        try {
            driver = proxy.getDriverById(driverId);
        } catch (FeignException.NotFound e) {
            return ResponseEntity.notFound().build();
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

        List<PassengerDto> passengers = new ArrayList<>();
        for (long id : passengersIds) {
            try {
                passengers.add(proxy.getPassengerById(id));
            } catch (FeignException.NotFound e) {
                return ResponseEntity.notFound().build();
            } catch (FeignException e) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
            }
        }

        RouteDto route = routeService.buildRouteWithPassengers(driver, passengers);


        try {
            RouteDto route = routeService.buildRouteWithPassengers(driverId, passengersIds);
            return ResponseEntity.ok(route);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ServiceUnavailableException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }


        return ResponseEntity.ok(route);
    }


}
