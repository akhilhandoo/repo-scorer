package com.reposcorer.scoring;

import com.reposcorer.representation.Repo;
import java.time.Duration;
import java.time.Instant;
import org.springframework.stereotype.Component;

@Component("Simple")
public class SimpleAdditiveScoringMechanism implements RepoScoringMechanism {

    @Override
    public int computeScore(Repo repo) {
        return repo.getForksCount() + repo.getStargazersCount() + ((int) (Duration.between(repo.getUpdatedAt(), Instant.now()).toDays() / 10));
    }
}
