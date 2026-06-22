package com.reposcorer.remote.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public record Repository(
    Integer id,
    @JsonProperty("full_name") String fullName,
    @JsonProperty("html_url") String htmlUrl,
    String description,
    @JsonProperty("stargazers_count") Integer stargazersCount,
    @JsonProperty("forks_count") Integer forksCount,
    String language,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("updated_at") Instant updatedAt) {}
