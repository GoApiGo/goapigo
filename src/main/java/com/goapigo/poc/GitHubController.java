package com.goapigo.poc;

import com.goapigo.poc.adapter.GoApiGoService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController("/api/github")
public class GitHubController {

  @Autowired private GoApiGoService service;

  @GetMapping
  public ResponseEntity<RepositoryListDTO> getRepositories(@RequestParam String user)
      throws IOException {
    Document document = Jsoup.connect("https://github.com/" + user + "?tab=repositories").get();
    return ResponseEntity.ok(service.process(document.html(), RepositoryListDTO.class));
  }
}
