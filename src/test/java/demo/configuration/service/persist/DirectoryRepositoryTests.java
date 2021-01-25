package demo.configuration.service.persist;

import demo.configuration.service.model.entity.Directory;
import demo.configuration.service.persist.config.MongoServerConfiguration;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.test.context.TestConstructor;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@Slf4j
@AllArgsConstructor
@SpringBootConfiguration
@SpringBootTest(classes = MongoServerConfiguration.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@EnableReactiveMongoRepositories
@ImportAutoConfiguration(MongoReactiveAutoConfiguration.class)
public class DirectoryRepositoryTests {
  private final ReactiveMongoTemplate reactiveMongoTemplate;

  private final DirectoryRepository repository;

  @BeforeEach
  public void setup() {
    this.reactiveMongoTemplate.remove(DirectoryRepository.class).all()
        .subscribe(r -> log.debug("delete all posts: " + r),
            e -> log.debug("error: " + e), () -> log.debug("done"));
  }

  @Test
  void saveAndFind() {
      Mono<Directory> directoryMono = repository
          .save(Directory.builder()
              .label("Makanan Enak")
              .type("Food")
              .build())
          .flatMap(directory ->
              repository.findById(directory.getId()));

    StepVerifier.create(directoryMono)
      .expectSubscription().thenAwait()
      .expectNextMatches(directory -> {
        Assert.notNull(directory, "directory must not be null");
        return true;
      })
      .verifyComplete();
  }
}
