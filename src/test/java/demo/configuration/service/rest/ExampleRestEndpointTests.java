package demo.configuration.service.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import demo.configuration.ExampleRestEndpointConfiguration;
import demo.configuration.RestEndpointConfiguration;
import demo.configuration.service.handler.AutoConfigurationHandlerA;
import demo.configuration.service.handler.AutoConfigurationHandlerB;
import demo.configuration.service.handler.UserHandler;
import demo.configuration.service.model.dto.UserRequest;
import demo.configuration.service.model.dto.UserResponse;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.codec.CodecsAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;



@SpringBootTest
@AutoConfigureWebTestClient
@AutoConfigureRestEndpoints
@ImportAutoConfiguration(ExampleRestEndpointConfiguration.class)
public class ExampleRestEndpointTests {

  @Autowired
  private WebTestClient testClient;

  @MockBean
  private AutoConfigurationHandlerA handlerA;

  @MockBean
  private AutoConfigurationHandlerB handlerB;

  @Test
  void whenExampleA_thenSuccess() {

    Mockito.when(handlerA.handle())
        .thenReturn(Mono.empty());

    Flux<String> test = testClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/demo-configuration/example-a")
            .build())
        .accept(APPLICATION_JSON)
        .exchange()
        .expectStatus().isEqualTo(HttpStatus.OK)
        .returnResult(String.class)
        .getResponseBody();

    StepVerifier.create(test)
        .expectSubscription().thenAwait()
        .expectNextMatches(result -> {
          Assertions.assertEquals("service A success", result);
          return true;
        })
        .verifyComplete();
  }

  @Test
  void whenExampleB_thenSuccess() {

    Mockito.when(handlerB.handle())
        .thenReturn(Mono.empty());

    Flux<String> test = testClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/demo-configuration/example-b")
            .build())
        .accept(APPLICATION_JSON)
        .exchange()
        .expectStatus().isEqualTo(HttpStatus.OK)
        .returnResult(String.class)
        .getResponseBody();

    StepVerifier.create(test)
        .expectSubscription().thenAwait()
        .expectNextMatches(result -> {
          Assertions.assertEquals("service B success", result);
          return true;
        })
        .verifyComplete();
  }

}
