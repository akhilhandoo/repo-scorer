package com.reposcorer.scoring;

import com.reposcorer.representation.Repo;
import java.time.Duration;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("Advanced")
public class AdvancedWeightedScoringMechanism implements RepoScoringMechanism {

  private static final float RECENCY_DECAY = 0.01f;
  @Value("${github.scoring.stars-weight}")
  private Float scoringStarsWeight;
  @Value("${github.scoring.forks-weight}")
  private Float scoringForksWeight;
  @Value("${github.scoring.recency-weight}")
  private Float scoringRecencyWeight;

  /*
      Methodology -
        Weights for each factor.
        Taking log(base 10) of stars-count and forks-count, since they tend to have wide range (0 to thousands)
        Logarithms naturally provide diminishing returns as these values grow larger.
        Recency-decay multiplier ensures smoother approach to 0 for repositories with less activity.
   */
  @Override
  public int computeScore(Repo repository) {
    int daysPassedSinceLastUpdate =
        ((int) Duration.between(repository.getUpdatedAt(), Instant.now()).toDays());
    return (int) (scoringStarsWeight * Math.log10(1 + repository.getStargazersCount()))
        + (int) (scoringForksWeight * Math.log10(1 + repository.getForksCount()))
        + (int) (scoringRecencyWeight / (1 + (RECENCY_DECAY * daysPassedSinceLastUpdate)));
  }
}
