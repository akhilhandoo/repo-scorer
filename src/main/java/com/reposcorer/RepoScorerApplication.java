package com.reposcorer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.reposcorer.**"})
public class RepoScorerApplication {
  public static void main(String[] args) {
    SpringApplication.run(RepoScorerApplication.class, args);
  }
}
