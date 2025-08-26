package com.ridetogether.route_service.service;

import com.ridetogether.route_service.dto.RouteDto;
import com.ridetogether.route_service.maps.GoogleMapsApiUrlGenerator;
import com.ridetogether.route_service.maps.GoogleMapsWebUrlGenerator;
import com.ridetogether.route_service.dto.DriverDto;
import com.ridetogether.route_service.dto.PassengerDto;
import com.ridetogether.route_service.proxy.UserServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

//TODO: Add proper logging

@Service
public class RouteService {

    private String apiKey;
    private UserServiceProxy proxy;
    private RestTemplate restTemplate;
    private static final Logger logger = Logger.getLogger(RouteService.class.getName());

    @Value("${google.api.key}")
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Autowired
    public void setProxy(UserServiceProxy proxy) {
        this.proxy = proxy;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public RouteDto buildRouteWithPassengers(DriverDto driver, List<PassengerDto> passengers) {
        List<String> passengerWaypoints = passengers.stream()
                .map(PassengerDto::homeAddress)
                .toList();

        String startAddress = driver.homeAddress();
        String officeAddress = driver.officeAddress();

        GoogleMapsApiUrlGenerator apiUrlWithPassengers = new GoogleMapsApiUrlGenerator(startAddress, officeAddress, passengerWaypoints, apiKey);
        GoogleMapsApiUrlGenerator apiUrlWithoutPassengers = new GoogleMapsApiUrlGenerator(startAddress, officeAddress, apiKey);
        GoogleMapsWebUrlGenerator webUrl = new GoogleMapsWebUrlGenerator(startAddress, officeAddress, passengerWaypoints);

        String googleJsonWithWaypoints = restTemplate.getForObject(apiUrlWithPassengers.build(), String.class);
        String googleJsonDirectRoute = restTemplate.getForObject(apiUrlWithoutPassengers.build(), String.class);

        int metersWithWaypoints = RouteDataJsonExtractor.extractDistanceInMeters(googleJsonWithWaypoints);
        int metersDirectRoute = RouteDataJsonExtractor.extractDistanceInMeters(googleJsonDirectRoute);

        Duration durationWithWaypoints = RouteDataJsonExtractor.extractDuration(googleJsonWithWaypoints);
        Duration durationDirectRoute = RouteDataJsonExtractor.extractDuration(googleJsonDirectRoute);

        return new RouteDto(
                webUrl.build(),
                metersWithWaypoints,
                durationWithWaypoints.getSeconds(),
                durationWithWaypoints.minus(durationDirectRoute).getSeconds(),
                metersWithWaypoints - metersDirectRoute
        );
    }
}
