package demo.configuration.service.handler;

import demo.configuration.service.cache.UserCache;
import demo.configuration.service.model.dto.UserRequest;
import demo.configuration.service.model.dto.UserResponse;
import demo.configuration.service.model.entity.User;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

public class DefaultUserHandler implements UserHandler {

  private final UserCache userCache;
  private final HandlerProperties properties;

  public DefaultUserHandler(UserCache userCache,
      HandlerProperties properties) {
    Assert.notNull(userCache, "userCache must be provide");
    this.userCache = userCache;
    this.properties = properties;
  }

  @Override
  public Mono<UserResponse> create(UserRequest userRequest) {
    return userCache.put(User.builder()
          .name(userRequest.getName())
          .age(userRequest.getAge())
          .birthDate(userRequest.getBirthDate())
        .build())
      .thenReturn(UserResponse.builder()
          .name(userRequest.getName())
          .age(userRequest.getAge())
          .birthDate(userRequest.getBirthDate())
          .build());
  }

  @Override
  public Mono<Boolean> exist(String name) {
    return userCache.exist(name);
  }

  @Override
  public Mono<UserResponse> retrieve(String name) {
    return userCache.retrieve(name)
        .map(user -> UserResponse.builder()
            .name(user.getName())
            .age(user.getAge())
            .birthDate(user.getBirthDate())
            .build());
  }
}
