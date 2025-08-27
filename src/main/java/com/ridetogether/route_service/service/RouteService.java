package com.ridetogether.route_service.service;

import com.ridetogether.route_service.dto.*;
import com.ridetogether.route_service.maps.GoogleMapsApiUrlGenerator;
import com.ridetogether.route_service.maps.GoogleMapsWebUrlGenerator;
import com.ridetogether.route_service.proxy.UserServiceProxy;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

@Service
public class RouteService {

    private final Logger logger = Logger.getLogger(RouteService.class.getName());

    private String apiKey;
    private UserServiceProxy proxy;
    private RestTemplate restTemplate;

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


    public ResponseEntity<RouteDto> buildRouteWithPassengers(Long driverId, List<Long> passengerIds) {
        try {
            DriverDto driver = proxy.getDriverById(driverId);
            List<PassengerDto> passengers = new ArrayList<>();

            for (Long id : passengerIds) {
                passengers.add(proxy.getPassengerById(id));
            }

            RouteDto route = buildRoute(driver, passengers);
            return ResponseEntity.ok(route);

        } catch (FeignException.NotFound e) {
            logger.warning("Driver or passenger not found: " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (FeignException e) {
            logger.warning("User service unavailable: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    /**
     * Core business logic
     */
    private RouteDto buildRoute(DriverDto driver, List<PassengerDto> passengers) {
        List<String> passengerWaypoints = passengers.stream()
                .map(PassengerDto::homeAddress)
                .toList();

        String startAddress = driver.homeAddress();
        String officeAddress = driver.officeAddress();

        GoogleMapsApiUrlGenerator apiWithPassengers = new GoogleMapsApiUrlGenerator(startAddress, officeAddress, passengerWaypoints, apiKey);
        GoogleMapsApiUrlGenerator apiDirectRoute = new GoogleMapsApiUrlGenerator(startAddress, officeAddress, apiKey);
        GoogleMapsWebUrlGenerator webUrl = new GoogleMapsWebUrlGenerator(startAddress, officeAddress, passengerWaypoints);

        String jsonWithWaypoints = restTemplate.getForObject(apiWithPassengers.build(), String.class);
        String jsonDirectRoute = restTemplate.getForObject(apiDirectRoute.build(), String.class);

        int metersWithWaypoints = RouteDataJsonExtractor.extractDistanceInMeters(jsonWithWaypoints);
        int metersDirect = RouteDataJsonExtractor.extractDistanceInMeters(jsonDirectRoute);

        Duration durationWithWaypoints = RouteDataJsonExtractor.extractDuration(jsonWithWaypoints);
        Duration durationDirect = RouteDataJsonExtractor.extractDuration(jsonDirectRoute);

        return new RouteDto(
                webUrl.build(),
                metersWithWaypoints,
                durationWithWaypoints.getSeconds(),
                durationWithWaypoints.minus(durationDirect).getSeconds(),
                metersWithWaypoints - metersDirect
        );
    }

    public ResponseEntity<List<PassengerRouteDto>> getMatchingPassengers(Long driverId) {
        try {
            DriverDto driver = proxy.getDriverById(driverId);
            logger.info("Driver found: " + driver.name());

            List<PassengerDto> passengers = proxy.findMatchingPassengers(driverId);
            logger.info("Found " + passengers.size() + " matching passengers.");

            List<PassengerRouteDto> result = new ArrayList<>();

            for (PassengerDto passenger : passengers) {
                RouteDto route = buildRoute(driver, List.of(passenger));
                result.add(new PassengerRouteDto(passenger, route));
            }

            //ascending order
            result.sort(Comparator.comparingLong(pr -> pr.routeDto().getAddedTimeInSeconds()));

            return ResponseEntity.ok(result);

        } catch (FeignException.NotFound e) {
            logger.warning("Driver or passengers not found: " + e.getMessage());
            return ResponseEntity.notFound().build();

        } catch (FeignException e) {
            logger.warning("User service unavailable: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

        } catch (Exception e) {
            logger.severe("Unexpected error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    public ResponseEntity<List<DriverRouteDto>> getMatchingDrivers(Long passengerId) {
        try {
            PassengerDto passenger = proxy.getPassengerById(passengerId);
            logger.info("Driver found: " + passenger.name());

            List<DriverDto> drivers = proxy.findMatchingDrivers(passengerId);
            logger.info("Found " + drivers.size() + " matching passengers.");

            List<DriverRouteDto> result = new ArrayList<>();

            for (DriverDto driver : drivers) {
                RouteDto route = buildRoute(driver, List.of(passenger));
                result.add(new DriverRouteDto(driver, route));
            }

            //ascending order
            result.sort(Comparator.comparingLong(pr -> pr.routeDto().getAddedTimeInSeconds()));

            return ResponseEntity.ok(result);

        } catch (FeignException.NotFound e) {
            logger.warning("Driver or passengers not found: " + e.getMessage());
            return ResponseEntity.notFound().build();

        } catch (FeignException e) {
            logger.warning("User service unavailable: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

        } catch (Exception e) {
            logger.severe("Unexpected error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
