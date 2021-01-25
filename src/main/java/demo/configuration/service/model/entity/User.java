package demo.configuration.service.model.entity;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class User implements Serializable {

  private String name;
  private int age;
  private LocalDate birthDate;

  @lombok.Builder(builderClassName = "Builder")
  User(String name, int age, LocalDate birthDate) {
    this.name = name;
    this.age = age;
    this.birthDate = birthDate;
  }
}
