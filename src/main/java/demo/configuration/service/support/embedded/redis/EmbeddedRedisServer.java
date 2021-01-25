package demo.configuration.service.support.embedded.redis;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
@ExtendWith(EmbeddedRedisExtension.class)
public @interface EmbeddedRedisServer {
  /**
   * On which port redis server should be bound.
   *
   * @return
   */
  int port() default EmbeddedRedisExtension.DEFAULT_PORT;

  /**
   * Whether to start or not.
   *
   * @return
   */
  boolean start() default true;
}
