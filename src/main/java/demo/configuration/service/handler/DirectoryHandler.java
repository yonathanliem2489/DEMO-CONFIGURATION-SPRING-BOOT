package demo.configuration.service.handler;

import reactor.core.publisher.Mono;

public interface DirectoryHandler {

  Mono<Void> handle();
}
