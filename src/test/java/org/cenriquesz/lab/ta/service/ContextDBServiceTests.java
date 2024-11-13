package org.cenriquesz.lab.ta.service;

import java.time.Instant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.cenriquesz.lab.ta.model.entity.Context;
import org.cenriquesz.lab.ta.model.entity.Context.Details;
import org.cenriquesz.lab.ta.model.entity.Context.Metadata;

@SpringBootTest
public class ContextDBServiceTests {

  @Autowired
  private ContextDBService service;

  @Test
  public void test() {

    Context context = Context.builder()
        .openedAt(Instant.now())
        .message("Contexto de prueba")
        .details(Details.builder()
            .description("Descripcion del context")
            .metadata(Metadata.builder()
                .createdAt(Instant.now())
                .createdBy("cenriquesz")
                .build())
            .build())
        .build();

    service.save(context);
    Assertions.assertNotNull(context.getId());
  }
}
