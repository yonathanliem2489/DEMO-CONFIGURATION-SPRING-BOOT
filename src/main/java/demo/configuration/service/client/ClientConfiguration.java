package demo.configuration.service.client;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class ClientConfiguration {

  @NotNull
  private Map<String, String> headers = new HashMap<>();

  @Bean
  DemoClient demoClient() {
    WebClient webClient = WebClient.builder()
        .baseUrl(UriComponentsBuilder
            .fromUri(URI.create("http://localhost:1010"))
            .path("demo-server-client").toUriString())
        .defaultHeaders(httpHeaders -> headers.forEach(httpHeaders::add))
        .build();

    return new DefaultDemoClient(webClient);
  }
}
