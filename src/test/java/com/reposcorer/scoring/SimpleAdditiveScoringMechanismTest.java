package com.reposcorer.scoring;

import com.reposcorer.representation.Repo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@SpringBootTest(classes = SimpleAdditiveScoringMechanism.class)
@ActiveProfiles("test")
class SimpleAdditiveScoringMechanismTest {

    @Autowired
    private SimpleAdditiveScoringMechanism scoringMechanism;

    @Test
    void shouldCalculateASaneScore() {
        // Given
        Repo repo = Repo.builder().stargazersCount(100).forksCount(50).updatedAt(Instant.now().minus(10, ChronoUnit.DAYS)).build();

        // When
        int score = scoringMechanism.computeScore(repo);

        // Then
        Assertions.assertTrue(score > 0);
    }

    @Test
    void shouldCalculateExactScoreBasedOnStarsForksAndRecency() {
        // Given
        Repo repo = Repo.builder().stargazersCount(1000).forksCount(1000).updatedAt(Instant.now().minus(100, ChronoUnit.DAYS)).build();

        // When
        int score = scoringMechanism.computeScore(repo);

        // Then
        Assertions.assertEquals(2010, score);
    }

    @Test
    void shouldCalculateExactScoreBasedOnlyOnRecency() {
        // Given
        Repo repo = Repo.builder().stargazersCount(0).forksCount(0).updatedAt(Instant.now().minus(100, ChronoUnit.DAYS)).build();

        // When
        int score = scoringMechanism.computeScore(repo);

        // Then
        Assertions.assertEquals(10, score);
    }
}