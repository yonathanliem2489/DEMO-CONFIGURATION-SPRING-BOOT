package demo.configuration.service.client;

import demo.configuration.service.model.dto.UserRequest;
import demo.configuration.service.model.dto.UserResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class DefaultDemoClient implements DemoClient {

  private final WebClient client;

  public DefaultDemoClient(WebClient client) {
    this.client = client;
  }

  @Override
  public Mono<UserResponse> handle(UserRequest request) {
    return client.post()
        .bodyValue(request)
        .retrieve()
        .bodyToMono(UserResponse.class);
  }
}
