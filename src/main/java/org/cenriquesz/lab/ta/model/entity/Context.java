package org.cenriquesz.lab.ta.model.entity;

import java.time.Instant;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import org.cenriquesz.lab.orq.model.db.DBDoc;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Value
@Document(indexName = "context-*", createIndex = false)
public class Context extends DBDoc {

  String message;
  Details details;

  @Field(type = FieldType.Date, format = DateFormat.epoch_millis)
  Instant openedAt;


  @Override
  public String getIndexName() {
    return "context";
  }

  @Builder
  @Value
  public static class Details {

    String description;

    @Field(type = FieldType.Object, index = false)
    Metadata metadata;

  }

  @Builder
  @Value
  public static class Metadata {

    Instant createdAt;
    String createdBy;

  }
}
