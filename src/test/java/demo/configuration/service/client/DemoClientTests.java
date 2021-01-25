package demo.configuration.service.client;

import static org.mockserver.model.HttpRequest.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.configuration.service.TestingConfiguration;
import demo.configuration.service.cache.DirectoryCache;
import demo.configuration.service.client.DemoClientTests.SupportConfiguration;
import demo.configuration.service.handler.DefaultDirectoryHandler;
import demo.configuration.service.handler.DirectoryHandler;
import demo.configuration.service.model.dto.UserRequest;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerSettings;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.codec.CodecsAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.ClientHttpConnectorAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

@SpringBootTest(classes = TestingConfiguration.class, webEnvironment = WebEnvironment.NONE,
    properties = {

    })
@ImportAutoConfiguration({JacksonAutoConfiguration.class, CodecsAutoConfiguration.class,
    ClientHttpConnectorAutoConfiguration.class, WebClientAutoConfiguration.class,
    })
@MockServerSettings(ports = 1010)
@Import(SupportConfiguration.class)
public class DemoClientTests {

  @Autowired
  private DemoClient client;

  @Autowired
  private ObjectMapper objectMapper;

  private final MockServerClient mockServerClient;

  public DemoClientTests() {
    this.mockServerClient = new MockServerClient("localhost", 1010);
  }

  @Test
  void whenSendRequest_thenShouldSuccess() {

//    mockServerClient.when(request()
//      .withMethod()).respond(HttpResponse.response().withStatusCode(200)
//    .withBody());

    StepVerifier.create(client.handle(UserRequest.builder()
        .build()))
        .expectSubscription().thenAwait()
        .expectNextMatches(userResponse -> {

          return true;
        })
        .verifyComplete();

  }

  @TestConfiguration
  static class SupportConfiguration {
    @Bean
    DemoClient demoClient(WebClient webClient) {
      return new DefaultDemoClient(webClient);
    }
  }
}
