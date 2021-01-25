package demo.configuration.service.cache;

import demo.configuration.service.model.entity.Directory;
import reactor.core.publisher.Mono;

public interface DirectoryCache {

  Mono<Boolean> exist(Directory directory);
}
