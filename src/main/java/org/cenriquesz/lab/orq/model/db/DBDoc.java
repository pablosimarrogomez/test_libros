package org.cenriquesz.lab.orq.model.db;

import java.time.Instant;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@SuperBuilder(toBuilder = true)
public abstract class DBDoc {

  @Id
  private String id;

  @Builder.Default
  @Field(name = "@timestamp", type = FieldType.Date)
  private Instant timestamp = Instant.now();

  public abstract String getIndexName();
}
