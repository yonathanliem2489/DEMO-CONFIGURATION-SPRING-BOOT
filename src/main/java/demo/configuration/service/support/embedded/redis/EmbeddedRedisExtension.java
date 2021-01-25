package demo.configuration.service.support.embedded.redis;

import static org.junit.platform.commons.util.AnnotationUtils.findAnnotation;

import demo.configuration.service.support.embedded.redis.RedisFlushAll.FlushTime;
import java.util.Optional;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;
import org.junit.platform.commons.util.AnnotationUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;
import redis.embedded.RedisExecProvider;
import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;
import redis.embedded.util.OS;

@Slf4j
public class EmbeddedRedisExtension implements BeforeAllCallback, BeforeEachCallback,
    AfterEachCallback, AfterAllCallback {

  public static final int DEFAULT_PORT = 13679;

  private static final Namespace STORE_NAMESPACE = Namespace.create(EmbeddedRedisExtension.class);
  private static final String UNIX_EXECUTABLE = "/usr/bin/redis-server";
  private static final String WINDOWS_EXECUTABLE = "D:\\JAVA\\redis\\x64\\Release\\redis-server.exe";

  private final Function<Integer, InstanceContainer> serverFactory = boundPort -> {
    RedisServerBuilder serverBuilder = RedisServer.builder()
        .port(boundPort);

    Resource unixResource = new FileSystemResource(UNIX_EXECUTABLE);
    Resource windowsResource = new FileSystemResource(WINDOWS_EXECUTABLE);

    if(unixResource.exists()) {
      log.info("Using custom unix redis executable located on '{}'", UNIX_EXECUTABLE);
      RedisExecProvider redisExecProvider = RedisExecProvider.defaultProvider()
          .override(OS.UNIX, UNIX_EXECUTABLE);
      serverBuilder.redisExecProvider(redisExecProvider);
    }
    else if (windowsResource.exists()) {
      log.info("Using custom windows redis executable located on '{}'", WINDOWS_EXECUTABLE);
      RedisExecProvider redisExecProvider = RedisExecProvider.defaultProvider()
          .override(OS.WINDOWS, WINDOWS_EXECUTABLE);
      serverBuilder.redisExecProvider(redisExecProvider);
    }

    RedisServer server = serverBuilder.build();
    server.start();

    Jedis jedis = new Jedis("localhost", boundPort);
    jedis.connect();

    return InstanceContainer.forInstance(server, jedis);
  };

  @Override
  public void beforeAll(ExtensionContext extensionContext) throws Exception {
    Integer boundPort = resolvePort(extensionContext);

    log.debug("Starting embedded redis server, bound on port {}", boundPort);
    InstanceContainer container = getStore(extensionContext).getOrComputeIfAbsent(boundPort, serverFactory, InstanceContainer.class);

    Optional<RedisFlushAll> flush = AnnotationUtils.findAnnotation(extensionContext.getRequiredTestClass(), RedisFlushAll.class);
    if(flush.isPresent() && FlushTime.BEFORE.equals(flush.get().value())) {
      container.client.flushAll();
    }
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    Optional<RedisFlushAll> flush = AnnotationUtils.findAnnotation(context.getRequiredTestMethod(), RedisFlushAll.class);
    if(flush.isPresent() && FlushTime.BEFORE.equals(flush.get().value())) {
      getStore(context).get(resolvePort(context), InstanceContainer.class).client.flushAll();
    }
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    Optional<RedisFlushAll> flush = AnnotationUtils.findAnnotation(context.getRequiredTestMethod(), RedisFlushAll.class);
    if(flush.isPresent() && FlushTime.AFTER.equals(flush.get().value())) {
      getStore(context).get(resolvePort(context), InstanceContainer.class).client.flushAll();
    }
  }

  @Override
  public void afterAll(ExtensionContext  context) throws Exception {
    Optional<RedisFlushAll> flush = AnnotationUtils.findAnnotation(context.getRequiredTestClass(), RedisFlushAll.class);
    if(flush.isPresent() && FlushTime.BEFORE.equals(flush.get().value())) {
      getStore(context).get(resolvePort(context), InstanceContainer.class).client.flushAll();
    }
  }

  /**
   * Resolve on which port redis server has been binding for current test.
   *
   * @param extensionContext
   * @return
   */
  private Integer resolvePort(ExtensionContext extensionContext) {
    Class<?> testClass = extensionContext.getRequiredTestClass();
    EmbeddedRedisServer serverSettings = findAnnotation(testClass, EmbeddedRedisServer.class)
        .orElse(null);

    Integer boundPort = DEFAULT_PORT;
    if(serverSettings != null && serverSettings.start()) {
      boundPort = serverSettings.port();
    }
    return boundPort;
  }

  private static Store getStore(ExtensionContext context) {
    return context.getRoot().getStore(STORE_NAMESPACE);
  }

  /**
   * {@link RedisServer} instance container.
   */
  @Slf4j
  private static class InstanceContainer implements CloseableResource {
    private final RedisServer server;
    private final Jedis client;

    InstanceContainer(RedisServer server, Jedis client) {
      Assert.notNull(server, "Redis server instance must be provided");
      Assert.notNull(client, "Jedis client must be provided");

      this.server = server;
      this.client = client;
    }

    @Override
    public void close() throws Throwable {
      log.debug("Stopping embedded redis server which bound on port {}", server.ports().get(0));
      client.disconnect();
      server.stop();
    }

    static InstanceContainer forInstance(RedisServer redisServer, Jedis client) {
      return new InstanceContainer(redisServer, client);
    }
  }
}
