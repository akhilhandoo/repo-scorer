package com.reposcorer.remote.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record SearchResponse(
    @JsonProperty("total_count") Integer totalCount, List<Repository> items) {}
