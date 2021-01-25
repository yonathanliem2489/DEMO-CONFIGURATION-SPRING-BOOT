package demo.configuration.service.client;

import demo.configuration.service.model.dto.UserRequest;
import demo.configuration.service.model.dto.UserResponse;
import reactor.core.publisher.Mono;

public interface DemoClient {

  Mono<UserResponse> handle(UserRequest request);
}
