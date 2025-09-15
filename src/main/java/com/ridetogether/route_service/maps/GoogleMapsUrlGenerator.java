package com.ridetogether.route_service.maps;

import lombok.Getter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class GoogleMapsUrlGenerator {

    protected final String origin;
    protected final String destination;
    protected final List<String> waypoints = new ArrayList<>();

    public GoogleMapsUrlGenerator(String origin, String destination) {
        this.origin = validateAddressFormat(origin);
        this.destination = validateAddressFormat(destination);
    }

    public GoogleMapsUrlGenerator(String origin, String destination, List<String> waypoints) {
        this.origin = validateAddressFormat(origin);
        this.destination = validateAddressFormat(destination);
        validateAddressFormat(waypoints);
        this.waypoints.addAll(waypoints);
    }

    public GoogleMapsUrlGenerator addWaypoint(String waypoint) {
        this.waypoints.add(validateAddressFormat(waypoint));
        return this;
    }

    public static String validateAddressFormat(String address) {
        if (address == null || address.trim().isEmpty())
            throw new IllegalArgumentException("Address cannot be null or empty.");
        String trimmed = address.trim();
        if (!trimmed.matches(".*[a-zA-Zа-яА-Я0-9].*"))
            throw new IllegalArgumentException("Address must contain readable characters.");
        return trimmed;
    }

    public static List<String> validateAddressFormat(List<String> addresses) {
        List<String> result = new ArrayList<>();
        for (String address : addresses) {
            result.add(validateAddressFormat(address));
        }

        return result;
    }

    protected String encode(String location) {
        return URLEncoder.encode(location, StandardCharsets.UTF_8);
    }

    public abstract String build();
}