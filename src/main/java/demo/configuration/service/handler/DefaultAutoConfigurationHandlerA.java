package demo.configuration.service.handler;

import demo.configuration.service.cache.UserCache;
import demo.configuration.service.model.entity.User;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

public class DefaultAutoConfigurationHandlerA implements AutoConfigurationHandlerA {

  private final UserCache userCache;

  public DefaultAutoConfigurationHandlerA(UserCache userCache) {
    Assert.notNull(userCache, "userCache must be provide");
    this.userCache = userCache;
  }

  @Override
  public Mono<Void> handle() {
    return userCache.put(User.builder().build());
  }
}
