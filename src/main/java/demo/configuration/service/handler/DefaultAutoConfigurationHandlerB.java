package demo.configuration.service.handler;

import demo.configuration.service.cache.DirectoryCache;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

public class DefaultAutoConfigurationHandlerB implements AutoConfigurationHandlerB {

  private final DirectoryCache directoryCache;

  public DefaultAutoConfigurationHandlerB(DirectoryCache directoryCache) {
    Assert.notNull(directoryCache, "directoryCache must be provide");
    this.directoryCache = directoryCache;
  }

  @Override
  public Mono<Void> handle() {
    return Mono.empty();
  }
}
