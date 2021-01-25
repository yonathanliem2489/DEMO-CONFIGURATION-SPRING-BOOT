package demo.configuration.service.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class UserResponse implements Serializable {

  private String name;
  private int age;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate birthDate;

  @JsonCreator
  @lombok.Builder(builderClassName = "Builder")
  UserResponse(String name, int age, LocalDate birthDate) {
    this.name = name;
    this.age = age;
    this.birthDate = birthDate;
  }
}
