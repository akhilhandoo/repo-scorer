package com.reposcorer.config;

import java.net.http.HttpClient;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RestClientProvider {

  //  Single Thread executor limits http connection concurrency to Github to 1. Eliminates errors
  // due to rate limiting.
  //  More nuanced http connection concurrency can be easily achieved if rate limits are known, by
  // customizing the executor thread-pool.
  @Bean
  public HttpClient httpClient() {
    return HttpClient.newBuilder()
        .executor(Executors.newSingleThreadExecutor())
        .followRedirects(HttpClient.Redirect.ALWAYS)
        .build();
  }
}
