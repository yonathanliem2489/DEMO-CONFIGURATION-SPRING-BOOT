package demo.configuration.service.handler;

import demo.configuration.service.cache.DirectoryCache;
import demo.configuration.service.cache.UserCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(HandlerProperties.class)
public class HandlerConfiguration {

  @Bean
  @ConditionalOnBean(value = UserCache.class)
  UserHandler userHandler(UserCache userCache, HandlerProperties properties) {
    return new DefaultUserHandler(userCache, properties);
  }

  @Bean
  @ConditionalOnBean(value = DirectoryCache.class)
  DirectoryHandler directoryHandler(
      DirectoryCache directoryCache) {
    return new DefaultDirectoryHandler(directoryCache);
  }

}
