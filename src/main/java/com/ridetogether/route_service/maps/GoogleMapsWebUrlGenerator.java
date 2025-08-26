package com.ridetogether.route_service.maps;

import java.util.List;
import java.util.logging.Logger;

public class GoogleMapsWebUrlGenerator extends GoogleMapsUrlGenerator {

    public static final Logger logger = Logger.getLogger(GoogleMapsWebUrlGenerator.class.getName());

    public GoogleMapsWebUrlGenerator(String origin, String destination) {
        super(origin, destination);
    }

    public GoogleMapsWebUrlGenerator(String origin, String destination, List<String> waypoints) {
        super(origin, destination, waypoints);
    }

    @Override
    public String build() {
        StringBuilder sb = new StringBuilder("https://www.google.com/maps/dir/");
        sb.append(encode(origin)).append("/");

        for (String waypoint : waypoints) {
            sb.append(encode(waypoint)).append("/");
        }
        sb.append(encode(destination));

        logger.info(sb.toString());
        return sb.toString();
    }
}