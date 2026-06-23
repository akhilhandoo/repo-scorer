package com.reposcorer.remote;

import com.reposcorer.remote.config.RemoteConfiguration;
import com.reposcorer.remote.model.Repository;
import com.reposcorer.remote.model.SearchResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import tools.jackson.databind.ObjectMapper;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class GithubClientTest {

    @InjectMocks
    private GithubClient githubClient;

    @Mock
    private HttpClient httpClient;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private RemoteConfiguration remoteConfiguration;


    @Test
    public void returnsExpectedRepositoriesWithScores() throws Exception {
        //  Given
        var createdInstant = LocalDate.of(2024, 05, 01).atStartOfDay(ZoneId.of("UTC")).toInstant();
        var updatedInstant = LocalDate.of(2026, 05, 01).atStartOfDay(ZoneId.of("UTC")).toInstant();
        Repository repository = new Repository(1, "Mockito Java Library", "http://www.mockito.org", "A library of mocks", 100, 25, "Java", createdInstant, updatedInstant);
        var baseUrl = "https://api.github.com/search/repositories";
        var queryString = "q=Mockito";
        var urlToUse = baseUrl + "?" + queryString + "&per_page=100&page=1";
        var timeoutInSeconds = 5l;
        var githubResponseBody = new ObjectMapper().writeValueAsString(new SearchResponse(1, List.of(repository)));
        var response = getMockResponse(githubResponseBody);
        Mockito.when(remoteConfiguration.getUrl()).thenReturn(baseUrl);
        Mockito.when(remoteConfiguration.getSize()).thenReturn(100);
        Mockito.when(remoteConfiguration.getParams()).thenReturn(Map.of());
        Mockito.when(remoteConfiguration.getTimeout()).thenReturn(timeoutInSeconds);
        Mockito.when(objectMapper.readValue(githubResponseBody, SearchResponse.class)).thenReturn(new SearchResponse(1, List.of(repository)));
        Mockito.when(httpClient.sendAsync(HttpRequest.newBuilder().uri(URI.create(urlToUse))
                        .timeout(Duration.ofSeconds(remoteConfiguration.getTimeout()))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString())).thenReturn(response);

        //  When
        var listOfRepo = githubClient.getAllRepositories("Mockito", Map.of());

        //  Then
        Assertions.assertEquals(List.of(repository), listOfRepo);
    }

    private CompletableFuture<HttpResponse<String>> getMockResponse(String githubResponseBody) {
        HttpResponse<String> response = new HttpResponse<String>() {
            @Override
            public int statusCode() {
                return 200;
            }

            @Override
            public HttpRequest request() {
                return null;
            }

            @Override
            public Optional<HttpResponse<String>> previousResponse() {
                return Optional.empty();
            }

            @Override
            public HttpHeaders headers() {
                return null;
            }

            @Override
            public String body() {
                return githubResponseBody;
            }

            @Override
            public Optional<SSLSession> sslSession() {
                return Optional.empty();
            }

            @Override
            public URI uri() {
                return null;
            }

            @Override
            public HttpClient.Version version() {
                return null;
            }
        };
        return CompletableFuture.completedFuture(response);
    }

}

