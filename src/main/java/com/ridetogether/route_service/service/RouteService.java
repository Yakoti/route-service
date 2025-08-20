package com.ridetogether.route_service.service;

import com.ridetogether.ridetogether.dto.RouteDto;
import com.ridetogether.ridetogether.maps.GoogleMapsApiUrlGenerator;
import com.ridetogether.ridetogether.maps.GoogleMapsWebUrlGenerator;
import com.ridetogether.ridetogether.model.User;
import com.ridetogether.ridetogether.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

//TODO: Add proper logging

@Service
public class RouteService {
    private final String apiKey;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    public static final Logger logger = Logger.getLogger(RouteService.class.getName());

    public RouteService(@Value("${google.api.key}") String apiKey,
                        UserRepository userRepository,
                        UserService userService,
                        RestTemplate restTemplate) {
        this.apiKey = apiKey;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
    }

    public RouteDto getRouteDtoForOneOrMorePassengers(Long driverId, List<Long> passengerIds){
        logger.info("CALLED: RouteService.getRouteDtoForOneOrMorePassengers");

        User driver = userRepository.findById(driverId).orElseThrow();
        List<String> waypoints = passengerIds.stream()
                .map(id -> userRepository.findById(id).orElseThrow().getHomeAddress())
                .toList();

        String startAddress = driver.getHomeAddress();
        String endAddress = driver.getOfficeAddress();

        GoogleMapsApiUrlGenerator apiUrl = new GoogleMapsApiUrlGenerator(startAddress, endAddress, waypoints, apiKey);
        GoogleMapsWebUrlGenerator webUrl = new GoogleMapsWebUrlGenerator(startAddress, endAddress, waypoints);

        String jsonWithWaypoints = restTemplate.getForObject(apiUrl.build(), String.class);

        int metersWithWaypoints = RouteDataJsonExtractor.extractDistanceInMeters(jsonWithWaypoints);
        Duration durationWithWaypoints = RouteDataJsonExtractor.extractDuration(jsonWithWaypoints);

        GoogleMapsApiUrlGenerator directApiUrl = new GoogleMapsApiUrlGenerator(startAddress, endAddress, apiKey);
        String jsonDirectRoute = restTemplate.getForObject(directApiUrl.build(), String.class);

        int metersDirectRoute = RouteDataJsonExtractor.extractDistanceInMeters(jsonDirectRoute);
        Duration durationDirectRoute = RouteDataJsonExtractor.extractDuration(jsonDirectRoute);

        return new RouteDto(
                webUrl.build(),
                metersWithWaypoints,
                durationWithWaypoints.getSeconds(),
                durationWithWaypoints.minus(durationDirectRoute).getSeconds(),
                metersWithWaypoints - metersDirectRoute
        );
    }
}
