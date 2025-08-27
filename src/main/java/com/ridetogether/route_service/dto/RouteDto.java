package com.ridetogether.route_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteDto {
    String mapsUrl;
    int totalDistanceInMeters;
    long totalTimeInSeconds;
    long addedTimeInSeconds;
    int addedDistanceInMeters;
}
