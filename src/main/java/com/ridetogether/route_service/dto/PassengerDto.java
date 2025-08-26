package com.ridetogether.route_service.dto;
import java.time.LocalTime;

public record PassengerDto(
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
        UserRole role
) {}


