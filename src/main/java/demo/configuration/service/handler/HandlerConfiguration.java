package demo.configuration.service.handler;

import demo.configuration.service.cache.DirectoryCache;
import demo.configuration.service.cache.UserCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfiguration {

  @Bean
  UserHandler userHandler(UserCache userCache) {
    return new DefaultUserHandler(userCache);
  }

  @Bean
  DirectoryHandler directoryHandler(
      DirectoryCache directoryCache) {
    return new DefaultDirectoryHandler(directoryCache);
  }

}
