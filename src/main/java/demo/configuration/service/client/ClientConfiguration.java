package demo.configuration.service.client;

import static io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.net.URI;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.netty.http.client.HttpClient;

@Configuration
public class ClientConfiguration {

  @NotNull
  private Map<String, String> headers = new HashMap<>();

  @Bean
  DemoClient demoClient() {
    WebClient webClient = WebClient.builder()
        .baseUrl(UriComponentsBuilder
            .fromHttpUrl("http://localhost:9112")
            .path("demo-server-client").toUriString())
        .clientConnector(new ReactorClientHttpConnector(HttpClient.create()
            .keepAlive(true).compress(true).wiretap(true)
            .tcpConfiguration(tcpClient -> tcpClient
                .option(CONNECT_TIMEOUT_MILLIS, 5000)
                .doOnConnected(connection -> connection
                    .addHandlerLast(new ReadTimeoutHandler(5000, MILLISECONDS))
                    .addHandlerLast(new WriteTimeoutHandler(1000, MILLISECONDS))
                )
            )
        ))
        .defaultHeaders(httpHeaders -> headers.forEach(httpHeaders::add))
        .build();

    return new DefaultDemoClient(webClient);
  }
}
