package demo.configuration.service.handler;

import demo.configuration.service.TestingConfiguration;
import demo.configuration.service.cache.UserCache;
import demo.configuration.service.model.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(classes = TestingConfiguration.class, properties = {})
@ImportAutoConfiguration({
    ExampleAutoConfiguration.class
})
public class AutoConfigurationHandlerATests {

  @Autowired
  private AutoConfigurationHandlerA serviceA;

  @MockBean
  private UserCache userCache;

  @Test
  void whenHandle_thenShouldSuccess() {
    Mockito.when(userCache.put(User.builder().build()))
      .thenReturn(Mono.empty());

    StepVerifier.create(serviceA.handle())
        .expectSubscription().thenAwait()
        .verifyComplete();
  }

}
