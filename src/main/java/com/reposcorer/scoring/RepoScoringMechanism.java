package com.reposcorer.scoring;

import com.reposcorer.representation.Repo;

public interface RepoScoringMechanism {
  int computeScore(Repo repository);
}
