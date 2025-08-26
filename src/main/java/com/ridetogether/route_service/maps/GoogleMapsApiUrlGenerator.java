package com.ridetogether.route_service.maps;

import java.util.List;
import java.util.logging.Logger;

//TODO: Move the logger above the adding of the API key
public class GoogleMapsApiUrlGenerator extends GoogleMapsUrlGenerator {
    private String apiKey;
    public static final Logger logger = Logger.getLogger(GoogleMapsApiUrlGenerator.class.getName());


    public GoogleMapsApiUrlGenerator(String origin, String destination, String apiKey) {
        super(origin, destination);
        if(apiKey == null || apiKey.isEmpty()) throw new IllegalArgumentException("apiKey cannot be null or empty");
        this.apiKey = apiKey;
    }

    public GoogleMapsApiUrlGenerator(String origin, String destination, List<String> waypoints, String apiKey) {
        super(origin, destination, waypoints);
        if(apiKey == null || apiKey.isEmpty()) throw new IllegalArgumentException("apiKey cannot be null or empty");
        this.apiKey = apiKey;
    }

    @Override
    public String build() {
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        sb.append("origin=").append(encode(origin));
        sb.append("&destination=").append(encode(destination));
        if (!waypoints.isEmpty()) {
            sb.append("&waypoints=").append(String.join("|", waypoints.stream().map(this::encode).toList()));
        }
        sb.append("&key=").append(apiKey);

        logger.info("CALLED GoogleMapsApiUrlGenerator.build builded url: " + sb.toString());
        return sb.toString();
    }
}

