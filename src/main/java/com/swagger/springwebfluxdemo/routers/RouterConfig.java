package com.swagger.springwebfluxdemo.routers;

import com.swagger.springwebfluxdemo.handlers.CustomerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

import static org.springframework.http.MediaType.APPLICATION_JSON;

/**
 * Refer for more: https://medium.com/@augusto.klaic/functional-endpoints-695f3cc87491
 */
@Configuration
public class RouterConfig {

    @Autowired
    private CustomerHandler customerHandler;

    @Bean
    public RouterFunction<ServerResponse> getRouting() {
        return RouterFunctions.route()
                .path("/router/customers",
                        builder -> builder.nest(RequestPredicates.accept(APPLICATION_JSON),
                                routerBuilder -> routerBuilder
                                        .GET("", customerHandler::loadCustomers)
                                        .GET("/stream", customerHandler::loadCustomersStream)
                                        .GET("/{customerId}", customerHandler::findCustomer)
                                        .POST("/create", customerHandler::createCustomer)
                                ))
                .path("/router/products", builder -> builder.nest(RequestPredicates.accept(APPLICATION_JSON),
                        routerBuilder -> routerBuilder.GET("", customerHandler::loadCustomers)))
                .build();
    }
}
