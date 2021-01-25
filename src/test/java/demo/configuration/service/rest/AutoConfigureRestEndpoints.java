package demo.configuration.service.rest;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.http.codec.CodecsAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.ReactiveWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.reactive.WebFluxAutoConfiguration;

/**
 * Base setup Auto Configuration for unit test in {@link ImportAutoConfiguration}
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@ImportAutoConfiguration({
    ReactiveWebServerFactoryAutoConfiguration.class,
    JacksonAutoConfiguration.class,
    CodecsAutoConfiguration.class,
    WebFluxAutoConfiguration.class
})
public @interface AutoConfigureRestEndpoints {


}
