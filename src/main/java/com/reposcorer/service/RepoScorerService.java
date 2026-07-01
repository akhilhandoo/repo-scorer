package com.reposcorer.service;

import com.reposcorer.remote.GithubClient;
import com.reposcorer.representation.Repo;
import com.reposcorer.representation.mapper.RepositoryMapper;
import com.reposcorer.scoring.RepoScoringMechanismProvider;

import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RepoScorerService {

  private final GithubClient githubClient;
  private final RepoScoringMechanismProvider repoScoringMechanismProvider;
  private final RepositoryMapper repositoryMapper;

  @Value("${github.scoring.mechanism}")
  private String configuredScoringMechanism;

  public List<Repo> findRepositoryAndComputeScore(String query, Map<String, String> queryParams) {
    var scoringMechanismToUse =
        Optional.ofNullable(
                repoScoringMechanismProvider.getRepoScoringMechanism(
                    queryParams.get("scoringMechanism")))
            .orElse(
                repoScoringMechanismProvider.getRepoScoringMechanism(configuredScoringMechanism));
    var repositories = githubClient.getAllRepositories(query, queryParams);
    var enrichedRepoList =
        repositories.stream()
            .map(
                repository -> {
                  var repo = repositoryMapper.fromRepository(repository);
                  return repo.toBuilder().score(scoringMechanismToUse.computeScore(repo)).build();
                })
            .collect(Collectors.toList());
    Collections.sort(enrichedRepoList, Comparator.comparingInt(Repo::getScore).reversed());
    return enrichedRepoList;
  }
}
