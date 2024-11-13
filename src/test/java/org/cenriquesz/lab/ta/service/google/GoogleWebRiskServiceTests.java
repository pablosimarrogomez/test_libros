package org.cenriquesz.lab.ta.service.google;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GoogleWebRiskServiceTests {

  @Autowired
  private GoogleWebRiskService service;

  @Test
  public void submissionTest() throws IOException {
    service.submitUrl("https://test-app.com");
  }

}
