package com.reposcorer.service;


import com.reposcorer.remote.GithubClient;
import com.reposcorer.remote.model.Repository;
import com.reposcorer.representation.Repo;
import com.reposcorer.representation.mapper.RepositoryMapper;
import com.reposcorer.scoring.RepoScoringMechanism;
import com.reposcorer.scoring.RepoScoringMechanismProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class RepoScorerServiceTest {

    @Mock
    private GithubClient githubClient;

    @Mock
    private RepoScoringMechanismProvider repoScoringMechanismProvider;

    @Mock
    private RepoScoringMechanism repoScoringMechanism;

    @Mock
    private RepositoryMapper repositoryMapper;

    @InjectMocks
    private RepoScorerService repoScorerService;


    @Test
    public void returnsExpectedRepositoriesWithScores() {
        //  Given
        var createdInstant = LocalDate.of(2024, 05, 01).atStartOfDay(ZoneId.of("UTC")).toInstant();
        var updatedInstant = LocalDate.of(2026, 05, 01).atStartOfDay(ZoneId.of("UTC")).toInstant();
        Repository repository = new Repository(1, "Mockito Java Library", "http://www.mockito.org", "A library of mocks", 100, 25, "Java", createdInstant, updatedInstant);
        Repo repo = Repo.builder().id(1).fullName("Mockito Java Library").description("A library of mocks").htmlUrl("http://www.mockito.org").stargazersCount(100).forksCount(25).language("Java").createdAt(createdInstant).updatedAt(updatedInstant).build();
        Mockito.when(githubClient.getAllRepositories("query", Map.of("scoringMechanism", "Advanced"))).thenReturn(List.of(repository));
        Mockito.when(repositoryMapper.fromRepository(repository)).thenReturn(repo);
        Mockito.when(repoScoringMechanismProvider.getRepoScoringMechanism("Advanced")).thenReturn(repoScoringMechanism);
        Mockito.when(repoScoringMechanism.computeScore(repo)).thenReturn(10);

        //  When
        var listOfRepo = repoScorerService.findRepositoryAndComputeScore("query", Map.of("scoringMechanism", "Advanced"));

        //  Then
        Assertions.assertEquals(List.of(repo.toBuilder().score(10).build()), listOfRepo);
    }

}
