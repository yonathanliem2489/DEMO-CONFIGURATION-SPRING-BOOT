package demo.configuration.service.handler;

import reactor.core.publisher.Mono;

public interface AutoConfigurationHandlerB {

  Mono<Void> handle();
}
