package com.project.postex.routes;

import com.project.postex.handlers.AccountHandler;
import com.project.postex.handlers.AuthenticationHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
@AllArgsConstructor
public class RouterConfig {
    private final AccountHandler accountHandler;
    private final AuthenticationHandler authHandler;

    @Bean
    public RouterFunction<ServerResponse> authRoutes() {
        return RouterFunctions
                .route(POST("/signup").and(accept(APPLICATION_JSON)), authHandler::signUp)
                .andRoute(POST("/signin").and(accept(APPLICATION_JSON)), authHandler::signIn);
    }

    @Bean
    public RouterFunction<ServerResponse> accountRoutes() {
        return RouterFunctions
                .route(GET("/profile"), accountHandler::getProfile);
    }

}
