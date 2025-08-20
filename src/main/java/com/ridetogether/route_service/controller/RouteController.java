package com.ridetogether.route_service.controller;

import com.ridetogether.route_service.dto.RouteDto;
import com.ridetogether.route_service.dto.UserRouteDto;
import com.ridetogether.route_service.model.User;
import com.ridetogether.route_service.service.RouteService;
import com.ridetogether.route_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

// http://localhost:8080/api/matching?driverId=1
// http://localhost:8080/api/route?driverId=1&passengerIds=6&passengerIds=7&passengerIds=8
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class RouteController {
    private static final Logger logger = LoggerFactory.getLogger(RouteController.class);
    private final RouteService routeService;
    private final UserService userService;

    public RouteController(RouteService routeService, UserService userService) {
        this.routeService = routeService;
        this.userService = userService;
    }

    @GetMapping("/api/route")
    public ResponseEntity<RouteDto> getRoute(@RequestParam Long driverId, @RequestParam List<Long> passengerIds){
        logger.info("getRoute called with driverId={}, passengerIds={}", driverId, passengerIds);
        RouteDto routeDto = routeService.getRouteDtoForOneOrMorePassengers(driverId, passengerIds);
        logger.info("getRoute returning RouteDto with mapsLink={}", routeDto.getMapsUrl());
        return ResponseEntity.ok(routeDto);
    }

    @GetMapping("/api/matching")
    public ResponseEntity<List<UserRouteDto>> getMatchingRoutes(@RequestParam Long driverId){
        logger.info("getMatchingRoutes called with driverId={}", driverId);
        List<User> users = userService.findUsersWithMatchingSchedulesAndOppositeRole(driverId);
        List<UserRouteDto> userRouteDtos = new ArrayList<>();
        for (User user : users) {
            UserRouteDto dto = new UserRouteDto(user, routeService.getRouteDtoForOneOrMorePassengers(driverId, List.of(user.getId())));
            logger.info("Matching userId={} added with route mapsLink={}", user.getId(), dto.getRouteDto().getMapsUrl());
            userRouteDtos.add(dto);
        }
        return ResponseEntity.ok(userRouteDtos);
    }
}
