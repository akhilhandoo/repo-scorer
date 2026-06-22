package com.reposcorer.config;

import com.reposcorer.exception.RemoteObjectFetchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

  @ResponseStatus(HttpStatus.BAD_GATEWAY)
  @ExceptionHandler({RemoteObjectFetchException.class})
  public ResponseEntity<String> handleBadGateway(RemoteObjectFetchException exception) {
    return ResponseEntity.of(
            ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(HttpStatus.BAD_GATEWAY.value()), exception.getMessage()))
        .build();
  }
}
