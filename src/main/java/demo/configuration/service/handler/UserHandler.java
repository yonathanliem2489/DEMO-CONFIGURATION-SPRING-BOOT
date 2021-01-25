package demo.configuration.service.handler;

import demo.configuration.service.model.dto.UserRequest;
import demo.configuration.service.model.dto.UserResponse;
import reactor.core.publisher.Mono;

public interface UserHandler {

  Mono<UserResponse> create(UserRequest userRequest);

  Mono<Boolean> exist(String name);

  Mono<UserResponse> retrieve(String name);
}
