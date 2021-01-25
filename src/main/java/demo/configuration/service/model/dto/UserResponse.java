package demo.configuration.service.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@SuppressWarnings("serial")
public class UserResponse implements Serializable {

  private String name;
  private int age;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate birthDate;

  @JsonCreator
  @lombok.Builder(builderClassName = "Builder")
  UserResponse(@JsonProperty("name") String name,
      @JsonProperty("age") int age,
      @JsonProperty("birthDate")
      @JsonFormat(pattern = "yyyy-MM-dd")
          LocalDate birthDate) {
    this.name = name;
    this.age = age;
    this.birthDate = birthDate;
  }
}
