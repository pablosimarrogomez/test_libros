package org.cenriquesz.lab.ta.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
public class MailService {

  private final SesClient sesClient;

  @Value("${aws.ses.source-email}")
  private String sourceEmail;

  @Autowired
  public MailService(SesClient sesClient) {
    this.sesClient = sesClient;
  }

  public boolean sendEmail(String to, String subject, String body) {
    return sendEmail(List.of(to), List.of(), List.of(), List.of(), subject, body);
  }

  public boolean sendEmail(List<String> to, List<String> cc, List<String>bcc, List<String>replyTo,
      String subject, String body) {
    return sendEmail(to, cc, bcc, replyTo, subject, body, body);
  }

  private boolean sendEmail(
      List<String> to, List<String> cc, List<String>bcc, List<String>replyTo,
      String subject, String bodyText, String bodyHtml) {
    SendEmailResponse response = sesClient.sendEmail(SendEmailRequest.builder()
        .source(sourceEmail)
        .destination(Destination.builder()
            .toAddresses(to)
            .ccAddresses(cc)
            .bccAddresses(bcc)
            .build())
        .replyToAddresses(replyTo)
        .message(Message.builder()
            .subject(Content.builder()
                .data(subject)
                .charset("UTF-8")
                .build())
            .body(Body.builder()
                .text(Content.builder()
                    .data(bodyText)
                    .charset("UTF-8")
                    .build())
                .html(Content.builder()
                    .data(bodyHtml)
                    .charset("UTF-8")
                    .build())
                .build())
            .build())
        .build());
     return response.sdkHttpResponse().isSuccessful();
  }
}
