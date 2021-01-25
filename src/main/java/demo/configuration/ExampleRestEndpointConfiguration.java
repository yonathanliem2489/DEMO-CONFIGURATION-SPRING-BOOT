package demo.configuration;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import demo.configuration.service.handler.AutoConfigurationHandlerA;
import demo.configuration.service.handler.AutoConfigurationHandlerB;
import demo.configuration.service.handler.DirectoryHandler;
import demo.configuration.service.handler.UserHandler;
import demo.configuration.service.model.dto.UserRequest;
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
public class ExampleRestEndpointConfiguration {

  @Bean
  RouterFunction<ServerResponse> exampleEndpoint(
      AutoConfigurationHandlerA handlerA, AutoConfigurationHandlerB handlerB) {
    RequestPredicate handlePredicate = RequestPredicates
        .method(HttpMethod.GET)
        .and(RequestPredicates.path("demo-configuration/example-a"));
    HandlerFunction<ServerResponse> handleFunction =
        serverRequest -> handlerA.handle()
            .then(ok().bodyValue("service A success"));

    RequestPredicate handlePredicateB = RequestPredicates
        .method(HttpMethod.GET)
        .and(RequestPredicates.path("demo-configuration/example-b"));
    HandlerFunction<ServerResponse> handleFunctionB =
        serverRequest -> handlerB.handle()
            .then(ok().bodyValue("service B success"));

    return RouterFunctions.route()
        .route(handlePredicate, handleFunction)
        .route(handlePredicateB, handleFunctionB)
        .build();
  }
}
