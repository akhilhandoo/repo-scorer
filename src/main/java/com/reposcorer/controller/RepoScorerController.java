package com.reposcorer.controller;

import com.reposcorer.representation.Repo;
import com.reposcorer.service.RepoScorerService;

import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/github")
public class RepoScorerController {

  private final RepoScorerService repoScorerService;

  @GetMapping(value = "/repositories/{query}/score", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<Repo>> scoreRepos(
          @PathVariable("query") String query,
          @RequestParam Map<String, String> queryParams) {
    return ResponseEntity.ok(repoScorerService.findRepositoryAndComputeScore(query, queryParams));
  }
}
