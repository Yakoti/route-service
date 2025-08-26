package com.ridetogether.route_service.dto;

import java.time.LocalTime;

public record DriverDto(
        Long id,
        String name,
        String email,
        String phone,
        String homeAddress,
        String officeAddress,
        LocalTime preferredArrivalStart,
        LocalTime preferredArrivalEnd,
        int flexibilityMinutes,
        double flexibilityKm,
        UserRole role,
        int availableSeats,
        double costPer100KmEUR
) {}
