package org.cenriquesz.lab.ta.service.google;

import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
public class GoogleWebRiskService {

  @Value("${google.credentials.file-path}")
  private Resource credentialsFilePath;

  @Value("${google.project.id}")
  private String projectId;

  public void submitUrl(String urlToSubmit) throws IOException {
    GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsFilePath.getInputStream())
        .createScoped(Collections.singleton("https://www.googleapis.com/auth/cloud-platform"));

    credentials.refreshIfExpired();
    AccessToken token = credentials.getAccessToken();

    String submitUrl = String.format("https://webrisk.googleapis.com/v1/projects/%s/uris:submit", projectId);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + token.getTokenValue());
    headers.set("Content-Type", "application/json");

    JsonObject requestBody = new JsonObject();
    JsonObject submission = new JsonObject();
    submission.addProperty("uri", urlToSubmit);
    requestBody.add("submission", submission);

    HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

    // RestTemplate restTemplate = new RestTemplate();
    // ResponseEntity<String> response = restTemplate.exchange(submitUrl, HttpMethod.POST, entity, String.class);
    // response.getBody();
  }
}