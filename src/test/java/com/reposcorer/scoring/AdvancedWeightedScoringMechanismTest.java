package com.reposcorer.scoring;

import com.reposcorer.representation.Repo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@SpringBootTest(classes = AdvancedWeightedScoringMechanism.class)
@ActiveProfiles("test")
class AdvancedWeightedScoringMechanismTest {

    @Autowired
    private AdvancedWeightedScoringMechanism scoringMechanism;

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
        // With star-weight = 4 & forks-weight = 6 and log 1000 giving 3
        // stars should add 12 and forks should add 18 to the score giving 30
        // A recency-weight of 10 divided by (1 + 100 day old repo * 0.01 recency decay) gives 5
        // Total 35.
        Assertions.assertEquals(35, score);
    }
}
