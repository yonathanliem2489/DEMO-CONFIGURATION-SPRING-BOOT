package demo.configuration.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import demo.configuration.service.TestingConfiguration;
import demo.configuration.service.model.dto.UserRequest;
import demo.configuration.service.model.dto.UserResponse;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.codec.CodecsAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.ClientHttpConnectorAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import reactor.test.StepVerifier;

@SpringBootTest(classes = TestingConfiguration.class, webEnvironment = WebEnvironment.NONE,
    properties = {

    })
@ImportAutoConfiguration({JacksonAutoConfiguration.class, CodecsAutoConfiguration.class,
    ClientHttpConnectorAutoConfiguration.class, WebClientAutoConfiguration.class,
    ClientConfiguration.class
})
@MockServerSettings(ports = 9112)
public class DemoClientTests {

  @Autowired
  private DemoClient client;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void whenSendRequest_thenShouldSuccess(MockServerClient mockServerClient)
      throws JsonProcessingException {

    UserRequest userRequest = UserRequest.builder()
        .age(30)
        .birthDate(LocalDate.now().minusYears(30))
        .name("yonathan")
        .build();

    byte[] requestByte = objectMapper.writeValueAsBytes(userRequest);

    byte[] responseByte = objectMapper.writeValueAsBytes(UserResponse.builder()
        .age(30)
        .birthDate(LocalDate.now().minusYears(30))
        .name("yonathan")
        .build());

    mockServerClient.when(HttpRequest.request()
      .withMethod("POST")
      .withBody(requestByte)
    ).respond(HttpResponse.response().withStatusCode(200)
    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    .withBody(responseByte));

    StepVerifier.create(client.handle(userRequest))
        .expectSubscription().thenAwait()
        .expectNextMatches(userResponse -> {

          Assert.notNull(userResponse, "userResponse must not null");

          return true;
        })
        .verifyComplete();

  }
}
