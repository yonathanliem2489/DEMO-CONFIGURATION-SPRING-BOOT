package demo.configuration.service.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import demo.configuration.service.handler.UserHandler;
import demo.configuration.service.model.dto.UserRequest;
import demo.configuration.service.model.dto.UserResponse;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserEndpointTests {

  @Autowired
  private WebTestClient testClient;

  @MockBean
  private UserHandler userHandler;

  @Test
  void whenCreateUser_thenSuccess() {

    UserRequest request = UserRequest.builder()
        .name("yonathan")
        .age(30)
        .birthDate(LocalDate.now().minusYears(30))
        .build();
    UserResponse response = UserResponse.builder()
        .name("yonathan")
        .age(30)
        .birthDate(LocalDate.now().minusYears(30))
        .build();

    Mockito.when(userHandler.create(request))
        .thenReturn(Mono.just(response));

    Flux<UserResponse> test = testClient.post()
        .uri(uriBuilder -> uriBuilder
            .path("/demo-configuration/user")
            .build())
        .accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
        .bodyValue(request)
        .exchange()
        .expectStatus().isEqualTo(HttpStatus.OK)
        .returnResult(UserResponse.class)
        .getResponseBody();

    StepVerifier.create(test)
        .expectSubscription().thenAwait()
        .expectNextMatches(userResponse -> {
          Assertions.assertEquals(response, userResponse);
          return true;
        })
        .verifyComplete();
  }

  @Test
  void whenRetrieveUser_thenSuccess() {
    UserResponse response = UserResponse.builder()
        .name("yonathan")
        .age(30)
        .birthDate(LocalDate.now().minusYears(30))
        .build();

    Mockito.when(userHandler.retrieve("yonathan"))
        .thenReturn(Mono.just(response));

    Flux<UserResponse> test = testClient.get()
        .uri(uriBuilder -> uriBuilder
            .path("/demo-configuration/user/yonathan")
            .build())
        .accept(APPLICATION_JSON)
        .exchange()
        .expectStatus().isEqualTo(HttpStatus.OK)
        .returnResult(UserResponse.class)
        .getResponseBody();

    StepVerifier.create(test)
        .expectSubscription().thenAwait()
        .expectNextMatches(userResponse -> {

          Assertions.assertEquals(response, userResponse);

          return true;
        })
        .verifyComplete();
  }
}
