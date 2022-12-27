package com.cg.microservices.apigateway.routes;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route("USER-SERVICE",p->p.path("/users/**").uri("lb://USER-SERVICE"))
                .route("RATING-SERVICE",p->p.path("/ratings/**").uri("lb://RATING-SERVICE"))
                .route("HOTEL-SERVICE",p->p.path("/hotels/**").uri("lb://HOTEL-SERVICE"))
                .build();
    }

    //This way or write data in application.yml
    //PATH = URL of service to call
    //can add multiple filter in between
    //rewrite the url also
    //URI = Name of service to call
}
