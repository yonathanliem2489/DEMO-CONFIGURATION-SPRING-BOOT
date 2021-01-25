package demo.configuration.service.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveRedisTemplate;

@Configuration
public class CacheConfiguration {

  @Bean
  UserCache userCache(ObjectMapper objectMapper,
      ReactiveRedisTemplate<Object, Object> reactiveRedisTemplate) {
    return new DefaultUserCache(reactiveRedisTemplate, objectMapper);
  }

  @Bean
  DirectoryCache directoryCache(
      ReactiveRedisTemplate<Object, Object> reactiveRedisTemplate) {
    return new DefaultDirectoryCache(reactiveRedisTemplate);
  }
}
