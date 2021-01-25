package demo.configuration.service.model.entity;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@ToString
@EqualsAndHashCode
@Document(collection = "Directory")
public class Directory implements Serializable {

  @Id
  private String id;
  private String type;
  private String label;

  @lombok.Builder(builderClassName = "Builder")
  Directory(String id, String type, String label) {
    this.id = id;
    this.type = type;
    this.label = label;
  }
}
