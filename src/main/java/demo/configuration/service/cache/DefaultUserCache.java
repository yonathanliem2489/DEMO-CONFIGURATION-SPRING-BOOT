package demo.configuration.service.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.configuration.service.model.entity.User;
import java.nio.ByteBuffer;
import java.time.Duration;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

public class DefaultUserCache implements UserCache {

  private final ReactiveRedisTemplate<Object, Object> redisTemplate;
  private final ObjectMapper objectMapper;

  public DefaultUserCache(
      ReactiveRedisTemplate<Object, Object> redisTemplate,
      ObjectMapper objectMapper) {
    Assert.notNull(redisTemplate, "redisTemplate must be provide");
    Assert.notNull(objectMapper, "objectMapper must be provide");
    this.redisTemplate = redisTemplate;
    this.objectMapper = objectMapper;
  }

  @Override
  public Mono<Void> put(User user) {
    return Mono.fromCallable(() ->
          objectMapper.writeValueAsBytes(user))
        .flatMap(value ->
            redisTemplate
                .createMono(reactiveRedisConnection ->
                    reactiveRedisConnection.stringCommands()
                        .setEX(ByteBuffer.wrap("demo.configuration.user."
                                .concat(user.getName()).getBytes()),
                            ByteBuffer.wrap(value), Expiration.from(Duration.ofMinutes(10)))))
        .then();
  }

  @Override
  public Mono<Boolean> exist(String name) {
    return redisTemplate.createMono(reactiveRedisConnection ->
        reactiveRedisConnection.keyCommands()
            .exists(ByteBuffer.wrap("demo.configuration.user."
                .concat(name).getBytes())));
  }

  @Override
  public Mono<User> retrieve(String name) {
    return redisTemplate.createMono(reactiveRedisConnection ->
        reactiveRedisConnection.stringCommands()
            .get(ByteBuffer.wrap("demo.configuration.user."
                .concat(name).getBytes()))
            .flatMap(value ->
                Mono.fromCallable(() -> objectMapper.readValue(value.array(), User.class)))
    );
  }
}
