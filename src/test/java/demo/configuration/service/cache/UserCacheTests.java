package demo.configuration.service.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.configuration.service.cache.UserCacheTests.TestingConfiguration;
import demo.configuration.service.model.entity.User;
import demo.configuration.service.support.embedded.redis.EmbeddedRedisExtension;
import demo.configuration.service.support.embedded.redis.EmbeddedRedisServer;
import demo.configuration.service.support.embedded.redis.RedisFlushAll;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import reactor.test.StepVerifier;

@SpringBootConfiguration
@SpringBootTest(classes = TestingConfiguration.class, webEnvironment = WebEnvironment.NONE,
    properties = {
        "spring.redis.port=" + EmbeddedRedisExtension.DEFAULT_PORT,
        "service.adapt.profile.cache.ttl=10s"
    })
@EmbeddedRedisServer
@ImportAutoConfiguration({
    RedisAutoConfiguration.class,
    RedisReactiveAutoConfiguration.class,
    JacksonAutoConfiguration.class})
@Import(TestingConfiguration.class)
public class UserCacheTests {

  @Autowired
  private UserCache cache;

  @Test
  @RedisFlushAll
  void whenPutUserAndCheckExist_thenShouldSuccess() {

    StepVerifier.create(cache.put(User.builder()
          .name("yonathan")
          .age(30)
          .birthDate(LocalDate.now().minusYears(30))
          .build()).then(cache.exist("yonathan")))
        .expectSubscription().thenAwait()
        .expectNextMatches(exist -> {
          assertEquals(true, exist);
          return true;
        })
        .verifyComplete();
  }

  @TestConfiguration
  static class TestingConfiguration {
    @Bean
    UserCache userCache(ReactiveRedisTemplate reactiveRedisTemplate,
        ObjectMapper objectMapper) {
      return new DefaultUserCache(reactiveRedisTemplate, objectMapper);
    }
  }
}
