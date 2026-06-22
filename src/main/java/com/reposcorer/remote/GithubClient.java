package com.reposcorer.remote;

import com.reposcorer.exception.HttpResponseClientException;
import com.reposcorer.exception.RemoteObjectFetchException;
import com.reposcorer.remote.config.RemoteConfiguration;
import com.reposcorer.remote.model.Repository;
import com.reposcorer.remote.model.SearchResponse;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@Service
public class GithubClient {

  private final HttpClient httpClient;
  private final ObjectMapper objectMapper;
  private final RemoteConfiguration remoteConfiguration;

  //  search is designed with asynchronous http communication and multiple concurrent calls to the Github API in mind.
  public Future<SearchResponse> search(String queryString, int page) {
    String url = remoteConfiguration.getUrl() + "?" + queryString + "&page=" + page;
    System.out.println("URL -> " + url);
    return httpClient
        .sendAsync(
            HttpRequest.newBuilder().uri(URI.create(url))
                .timeout(Duration.ofSeconds(remoteConfiguration.getTimeout()))
                .GET()
                .build(),
            HttpResponse.BodyHandlers.ofString())
        .thenApply(
            response -> {
              if (response.statusCode() >= 400 && response.statusCode() <= 499) {
                throw new HttpResponseClientException(responseToString(response));
              } else if (response.statusCode() >= 500 && response.statusCode() <= 599) {
                throw new RemoteObjectFetchException(responseToString(response));
              }
              return objectMapper.readValue(response.body(), SearchResponse.class);
            });
  }

  //  Sequential for now. Can be altered to fetch several pages concurrently.
  public List<Repository> getAllRepositories(String query, Map<String, String> optionalQueryParams) {
    try {
      List<Repository> repos = new ArrayList<>();
      int page = 1;
      int pages = 0;
      String queryString = buildQueryString(query, optionalQueryParams);
      do {
        SearchResponse response = null;
        try {
          response = search(queryString, page).get();
          page++;
        } catch (ExecutionException ee) {
          ee.printStackTrace();
          System.out.println("Error: " + ee.getMessage());
          if (ee.getCause() instanceof HttpResponseClientException || ee.getCause() instanceof HttpTimeoutException) {
            //  Possibly encountered some restriction (rate limit or timeout) on the Github API
            //  Return early with repositories collected so far.
            if (page > 1) {
              break;
            }
          }
          throw new RemoteObjectFetchException(ee.getMessage());
        }
        repos.addAll(response.items());
        if (pages == 0) {
          pages = response.totalCount() / remoteConfiguration.getSize();
          if (response.totalCount() % remoteConfiguration.getSize() != 0) {
            pages++;
          }
        }
      } while (page <= pages);
      return repos;
    } catch (Throwable t) {
      t.printStackTrace();
      throw new RemoteObjectFetchException(t.getMessage());
    }
  }

  private String buildQueryString(String query, Map<String, String> optionalQueryParams) {
    var queryStringJoiner = new StringJoiner("&");
    var questionLineBuilder = new StringBuilder();
    questionLineBuilder.append(query);
    for (String allowedOptionalParam : remoteConfiguration.getParams().keySet()) {
      if (optionalQueryParams.containsKey(allowedOptionalParam)) {
        String internalQueryParamToUse = remoteConfiguration.getParams().get(allowedOptionalParam);
        if ("created".equals(internalQueryParamToUse)) {
          questionLineBuilder.append(" ").append(internalQueryParamToUse).append(":>=").append(optionalQueryParams.get(allowedOptionalParam));
        } else {
          questionLineBuilder.append(" ").append(internalQueryParamToUse).append(":").append(optionalQueryParams.get(allowedOptionalParam));
        }
      }
    }
    queryStringJoiner.add("q=" + URLEncoder.encode(questionLineBuilder.toString(), StandardCharsets.UTF_8));
    queryStringJoiner.add("per_page=" + remoteConfiguration.getSize());
    return queryStringJoiner.toString();
  }

  private String responseToString(HttpResponse response) {
    return "Github API returned status code: " + response.statusCode() + "\nMessage: " + response.body();
  }
}
