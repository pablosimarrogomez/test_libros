package org.cenriquesz.lab.ta.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MailServiceTests {

  @Autowired
  private MailService service;

  @Test
  public void testSendMail() {
    boolean sent = service.sendEmail(
        "test@gmail.com",
        "test-subject",
        "test-body");
    Assertions.assertTrue(sent);
  }

}
