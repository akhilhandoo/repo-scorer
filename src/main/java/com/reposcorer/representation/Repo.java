package com.reposcorer.representation;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Repo {

  private Integer id;

  @JsonProperty("full_name")
  private String fullName;

  @JsonProperty("html_url")
  private String htmlUrl;

  private String description;

  @JsonProperty("stargazers_count")
  private Integer stargazersCount;

  @JsonProperty("forks_count")
  private Integer forksCount;

  private String language;

  @JsonProperty("created_at")
  private Instant createdAt;

  @JsonProperty("updated_at")
  private Instant updatedAt;

  private int score;
}
