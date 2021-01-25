package demo.configuration.service.cache;

import demo.configuration.service.model.entity.Directory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

public class DefaultDirectoryCache implements DirectoryCache {

  private final ReactiveRedisTemplate<Object, Object> redisTemplate;

  public DefaultDirectoryCache(
      ReactiveRedisTemplate<Object, Object> redisTemplate) {
    Assert.notNull(redisTemplate, "redisTemplate must be provide");
    this.redisTemplate = redisTemplate;
  }

  @Override
  public Mono<Boolean> exist(Directory directory) {
    return Mono.empty();
  }
}
