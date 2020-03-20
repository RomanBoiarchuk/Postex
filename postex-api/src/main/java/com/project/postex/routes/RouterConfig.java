package com.project.postex.routes;

import com.project.postex.handlers.AccountHandler;
import com.project.postex.handlers.AuthenticationHandler;
import com.project.postex.handlers.PostHandler;
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
    private final PostHandler postHandler;

    @Bean
    public RouterFunction<ServerResponse> authRoutes() {
        return RouterFunctions
                .route(POST("/signup").and(accept(APPLICATION_JSON)), authHandler::signUp)
                .andRoute(POST("/signin").and(accept(APPLICATION_JSON)), authHandler::signIn);
    }

    @Bean
    public RouterFunction<ServerResponse> accountRoutes() {
        return RouterFunctions
                .route(GET("/profile"), accountHandler::getProfile)
                .andRoute(GET("/profiles"), accountHandler::searchProfiles)
                .andRoute(GET("/profile/{id}"), accountHandler::getProfileById)
                // friends, subscribers
                .andRoute(GET("/profile/{id}/friends"), accountHandler::getFriends)
                .andRoute(GET("/profile/{id}/subscribers"), accountHandler::getSubscribers)
                .andRoute(GET("/friends/check/{id}"), accountHandler::checkIfMyFriend)
                .andRoute(POST("/friends/{id}"), accountHandler::addFriend)
                .andRoute(DELETE("/friends/{id}"), accountHandler::removeFriend);
    }

    @Bean
    public RouterFunction<ServerResponse> postRoutes() {
        return RouterFunctions
                // posts
                .route(GET("/feed"), postHandler::getPosts)
                .andRoute(GET("/profile/me/posts"), postHandler::getMyPosts)
                .andRoute(GET("/profile/{id}/posts"), postHandler::getPostsByAuthor)
                .andRoute(GET("/posts/{id}"), postHandler::getPost)
                .andRoute(GET("/posts"), postHandler::findPosts)
                .andRoute(POST("/posts").and(accept(APPLICATION_JSON)), postHandler::createPost)
                .andRoute(PUT("/posts/{id}").and(accept(APPLICATION_JSON)), postHandler::updatePost)
                .andRoute(DELETE("/posts/{id}"), postHandler::deletePost)
                // likes
                .andRoute(POST("/posts/{id}/likes"), postHandler::setLike)
                .andRoute(DELETE("/posts/{id}/likes"), postHandler::removeLike)
                // comments
                .andRoute(GET("/posts/{postId}/comments"), postHandler::getCommentsByPostId)
                .andRoute(POST("/posts/{postId}/comments")
                        .and(accept(APPLICATION_JSON)), postHandler::createComment);
    }

}
