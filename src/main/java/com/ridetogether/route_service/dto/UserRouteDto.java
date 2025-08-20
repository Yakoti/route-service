package com.ridetogether.route_service.dto;

import com.ridetogether.ridetogether.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRouteDto {
    User user;
    RouteDto routeDto;
}
