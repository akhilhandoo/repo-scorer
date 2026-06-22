package com.reposcorer.service;

import com.reposcorer.remote.GithubClient;
import com.reposcorer.representation.Repo;
import com.reposcorer.representation.mapper.RepositoryMapper;
import com.reposcorer.scoring.RepoScoringMechanismProvider;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    String scoringMechanismToUse =
        Optional.ofNullable(queryParams.get("scoringMechanism")).orElse(configuredScoringMechanism);
    var repositories = githubClient.getAllRepositories(query, queryParams);
    return repositories.stream()
        .map(
            repository -> {
              var repo = repositoryMapper.fromRepository(repository);
              return repo.toBuilder()
                  .score(
                      repoScoringMechanismProvider
                          .getRepoScoringMechanism(scoringMechanismToUse)
                          .computeScore(repo))
                  .build();
            })
        .collect(Collectors.toUnmodifiableList());
  }
}
