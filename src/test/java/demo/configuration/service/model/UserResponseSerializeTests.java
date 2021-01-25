package demo.configuration.service.model;

import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import demo.configuration.service.model.dto.UserResponse;
import java.io.IOException;
import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "spring.jackson.serialization.write-dates-as-timestamps=true",
    "spring.jackson.serialization.write-date-timestamps-as-nanoseconds=false",
    "spring.jackson.parser.allow-numeric-leading-zeros=true",
    "spring.jackson.mapper.accept-case-insensitive-enums=true"

    })
@ImportAutoConfiguration({
    JacksonAutoConfiguration.class
})
public class UserResponseSerializeTests {

  @Autowired
  private ObjectMapper mapper;

  @Test
  void userResponseSerialize_thenShouldSuccess() throws IOException {
    UserResponse userResponse = UserResponse.builder()
        .age(30)
        .birthDate(LocalDate.now().minusYears(30))
        .name("yonathan")
        .build();
    String responseString = mapper.writeValueAsString(userResponse);
    Assertions.assertEquals(userResponse, mapper.readValue(responseString, UserResponse.class));
  }
}
