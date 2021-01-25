package demo.configuration;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import demo.configuration.service.handler.DirectoryHandler;
import demo.configuration.service.handler.UserHandler;
import demo.configuration.service.model.dto.UserRequest;
import java.util.Objects;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class RestEndpointConfiguration {

  @Bean
  RouterFunction<ServerResponse> userEndpoint(
    ObjectProvider<UserHandler> userHandlerObjectProvider) {

    /**
     * CRUD Endpoints
     */
    RequestPredicate createPredicate = RequestPredicates
        .method(HttpMethod.POST)
        .and(RequestPredicates.path("demo-configuration/user"));
    HandlerFunction<ServerResponse> createFunction =
        serverRequest -> serverRequest.bodyToMono(UserRequest.class)
            .flatMap(userRequest -> Objects
                .requireNonNull(userHandlerObjectProvider.getIfUnique())
                  .create(userRequest))
            .flatMap(userResponse -> ok().bodyValue(userResponse));

    RequestPredicate existPredicate = RequestPredicates
        .method(HttpMethod.GET)
        .and(RequestPredicates.path("demo-configuration/user/exist/{name}"));
    HandlerFunction<ServerResponse> existFunction =
        serverRequest -> Mono.fromCallable(() -> serverRequest.pathVariable("name"))
            .flatMap(name -> Objects
                .requireNonNull(userHandlerObjectProvider.getIfUnique())
                .exist(name))
            .flatMap(result -> ok().bodyValue(result));

    RequestPredicate retrievePredicate = RequestPredicates
        .method(HttpMethod.GET)
        .and(RequestPredicates.path("demo-configuration/user/{name}"));
    HandlerFunction<ServerResponse> retrieveFunction =
        serverRequest -> Mono.fromCallable(() -> serverRequest.pathVariable("name"))
            .flatMap(name -> Objects
                .requireNonNull(userHandlerObjectProvider.getIfUnique())
                .retrieve(name))
            .flatMap(result -> ok().bodyValue(result));

    return RouterFunctions.route()
        .route(createPredicate, createFunction)
        .route(existPredicate, existFunction)
        .route(retrievePredicate, retrieveFunction)
        .build();
  }

  @Bean
  RouterFunction<ServerResponse> directoryEndpoint(
      ObjectProvider<DirectoryHandler> directoryHandlerObjectProvider) {
    RequestPredicate handlePredicate = RequestPredicates
        .method(HttpMethod.GET)
        .and(RequestPredicates.path("demo-configuration/directory"));
    HandlerFunction<ServerResponse> handleFunction =
        serverRequest -> Objects.requireNonNull(directoryHandlerObjectProvider.getIfUnique()).handle()
            .then(ok().build());

    return RouterFunctions.route(handlePredicate, handleFunction);
  }
}
