package demo.configuration.service.handler;

import static org.mockito.ArgumentMatchers.any;

import demo.configuration.service.TestingConfiguration;
import demo.configuration.service.cache.UserCache;
import demo.configuration.service.handler.UserHandlerTests.SupportConfiguration;
import demo.configuration.service.model.dto.UserRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootConfiguration
@SpringBootTest(classes = TestingConfiguration.class, properties = {

})
@Import(SupportConfiguration.class)
public class UserHandlerTests {

  @Autowired
  private UserHandler userHandler;

  @MockBean
  private UserCache userCache;

  @Test
  void whenCreateUser_thenShouldSuccess() {

    Mockito.when(userCache.put(any()))
        .thenReturn(Mono.empty());

    StepVerifier.create(userHandler.create(UserRequest.builder().build()))
        .expectSubscription().thenAwait()
        .expectNextMatches(userResponse -> {

          return true;
        })
        .verifyComplete();
  }

  @TestConfiguration
  static class SupportConfiguration {
    @Bean
    UserHandler userHandler(UserCache userCache) {
      return new DefaultUserHandler(userCache);
    }
  }
}
