package com.reposcorer.scoring;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepoScoringMechanismProvider {

  private final Map<String, RepoScoringMechanism> repoScoringMechanisms;

  @Autowired
  public RepoScoringMechanismProvider(Map<String, RepoScoringMechanism> repoScoringMechanisms) {
    this.repoScoringMechanisms = repoScoringMechanisms;
  }

  public RepoScoringMechanism getRepoScoringMechanism(String qualifier) {
    return repoScoringMechanisms.get(qualifier);
  }
}
