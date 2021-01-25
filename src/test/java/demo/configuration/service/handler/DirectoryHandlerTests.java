package demo.configuration.service.handler;

import demo.configuration.service.TestingConfiguration;
import demo.configuration.service.cache.DirectoryCache;
import demo.configuration.service.handler.DirectoryHandlerTests.SupportConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

@SpringBootTest(classes = TestingConfiguration.class, properties = {

})
@Import(SupportConfiguration.class)
public class DirectoryHandlerTests {

  @Autowired
  private DirectoryHandler directoryHandler;

  @MockBean
  private DirectoryCache directoryCache;

  @Test
  void whenHandle_thenShouldSuccess() {

    StepVerifier.create(directoryHandler.handle())
        .expectSubscription().thenAwait()
        .verifyComplete();
  }

  @TestConfiguration
  static class SupportConfiguration {
    @Bean
    DirectoryHandler directoryHandler(DirectoryCache directoryCache) {
      return new DefaultDirectoryHandler(directoryCache);
    }
  }
}
