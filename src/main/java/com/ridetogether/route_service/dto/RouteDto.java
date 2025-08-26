package com.ridetogether.route_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteDto {
    String mapsUrl;
    double totalDistanceInMeters;
    double totalTimeInSeconds;
    double addedTimeInSeconds;
    double addedDistanceInMeters;
}
