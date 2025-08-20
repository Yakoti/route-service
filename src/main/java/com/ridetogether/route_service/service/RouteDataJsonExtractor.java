package com.ridetogether.route_service.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class RouteDataJsonExtractor {
    private static final Logger logger = LoggerFactory.getLogger(RouteDataJsonExtractor.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static int extractDistanceInMeters(String json) {
        logger.info("CALLED JsonExtractor.fetchDistanceInMeters");
        try {
            JsonNode node = mapper.readTree(json);
            return node.get("routes").get(0).get("legs").get(0).get("distance").get("value").asInt();
        } catch (Exception e) {
            logger.error("ERROR extracting distance in JsonExtractor.fetchDistanceInMeters", e);
            return -1;
        }
    }

    public static Duration extractDuration(String json) {
        logger.info("CALLED JsonExtractor.fetchDuration");
        try {
            JsonNode node = mapper.readTree(json);
            int seconds = node.get("routes").get(0).get("legs").get(0).get("duration").get("value").asInt();
            return Duration.ofSeconds(seconds);
        } catch (Exception e) {
            logger.error("ERROR extracting duration in JsonExtractor.fetchDuration", e);
            return Duration.ZERO;
        }
    }

//    public static int DEBUGfetchDistanceInMetersDEBUG(String json) {
//        logger.info("CALLED JsonExtractor.DEBUGfetchDistanceInMeters");
//
//        try {
//            JsonNode root = mapper.readTree(json);
//
//            // Check for API status
//            String status = root.path("status").asText();
//            if (!"OK".equals(status)) {
//                logger.error("Google API returned status: {}", status);
//                return 0;
//            }
//
//            JsonNode routes = root.path("routes");
//            if (!routes.isArray() || routes.isEmpty()) {
//                logger.error("No routes found in JSON.");
//                return 0;
//            }
//
//            JsonNode distanceValue = routes.get(0)
//                    .path("legs").get(0)
//                    .path("distance").path("value");
//
//            if (!distanceValue.isInt()) {
//                logger.error("Distance value is missing or not an integer: {}", distanceValue);
//                return 0;
//            }
//
//            int meters = distanceValue.asInt();
//            logger.info("Extracted distance: {} meters", meters);
//            return meters;
//
//        } catch (Exception e) {
//            logger.error("ERROR extracting distance in JsonExtractor.fetchDistanceInMeters", e);
//            return 0;
//        }
//    }
}
