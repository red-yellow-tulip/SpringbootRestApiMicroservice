package app.route;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class DataRouter {

    @Autowired
    private DataHandler dataHandler;

    @Bean
    public RouterFunction<ServerResponse> routeRequest(DataHandler dataHandler) {

        RequestPredicate getId  = GET("/route-data/{id}").and(accept(APPLICATION_JSON));

        RequestPredicate getAll = GET("/route-data/all/{count}").and(accept(APPLICATION_JSON));

        RequestPredicate postId = POST("/route-data/{id}").and(accept(APPLICATION_JSON)).and(contentType(APPLICATION_JSON));

        return RouterFunctions
                .route(getId, dataHandler::getById)
                .andRoute(getAll, dataHandler::getAll)
                .andRoute(postId, dataHandler::post);
    }



}
