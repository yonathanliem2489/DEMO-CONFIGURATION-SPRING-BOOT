package demo.configuration.service.handler;

import demo.configuration.service.TestingConfiguration;
import demo.configuration.service.cache.DirectoryCache;
import demo.configuration.service.cache.UserCache;
import demo.configuration.service.handler.AutoConfigurationHandlerA2Tests.SupportConfiguration;
import demo.configuration.service.model.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(classes = TestingConfiguration.class, properties = {})
@ImportAutoConfiguration({
//    ExampleAutoConfiguration.class
})
@Import(SupportConfiguration.class)
public class AutoConfigurationHandlerA2Tests {

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


  @TestConfiguration
  static class SupportConfiguration {
    @Bean
    AutoConfigurationHandlerA autoConfigurationHandlerA(UserCache userCache) {
      return new DefaultAutoConfigurationHandlerA(userCache);
    }
  }
}
