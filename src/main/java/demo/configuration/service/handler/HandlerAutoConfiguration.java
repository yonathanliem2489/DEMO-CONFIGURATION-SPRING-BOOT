package demo.configuration.service.handler;

import demo.configuration.service.cache.DirectoryCache;
import demo.configuration.service.cache.UserCache;
import demo.configuration.service.model.entity.User;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

public class HandlerAutoConfiguration {

  @Bean
  AutoConfigurationHandlerA autoConfigurationService(ObjectProvider<UserCache> userCache) {
    return new DefaultAutoConfigurationHandlerA(userCache
        .getIfUnique(() -> new UserCache() {
          @Override
          public Mono<Void> put(User user) {
            return illegalAccess();
          }

          @Override
          public Mono<Boolean> exist(String name) {
            return illegalAccess();
          }

          @Override
          public Mono<User> retrieve(String name) {
            return illegalAccess();
          }
        }));
  }

  @Bean
  AutoConfigurationHandlerB autoConfigurationServiceB(
      ObjectProvider<DirectoryCache> directoryCache) {
    return new DefaultAutoConfigurationHandlerB(
        directoryCache.getIfUnique(() -> directory -> illegalAccess()));
  }

  private <T> Mono<T> illegalAccess() {
    return Mono.error(new IllegalAccessException());
  }
}
