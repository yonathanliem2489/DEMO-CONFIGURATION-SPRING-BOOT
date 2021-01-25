package demo.configuration.service.cache;

import demo.configuration.service.model.entity.User;
import reactor.core.publisher.Mono;

public interface UserCache {

  Mono<Void> put(User user);

  Mono<Boolean> exist(String name);

  Mono<User> retrieve(String name);
}
