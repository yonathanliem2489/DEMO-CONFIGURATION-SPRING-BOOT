package demo.configuration.service.handler;

import reactor.core.publisher.Mono;

public interface AutoConfigurationHandlerA {

  Mono<Void> handle();
}
