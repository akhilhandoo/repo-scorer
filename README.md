<h3 style="text-align: center">
    A GitHub Repository Scorer API
</h3>

## Tech Stack

* Java 21
* Spring Boot
* Gradle

## Features
- Search any github repository based upon a keyword.
- API response includes a score computed for every repository.
- Score calculation formula is configurable and new ones can be easily added.
- API facilitates optional filtering by language and lastCreatedDate.
- Protection from throttling of requests and timeouts - partial data is processed and returned in case of failures.
- Support for asynchronous http communication.


## Getting started

Open a terminal and cd into the project root.
```shell script
# Build the application on your local machine. 
./gradlew clean build

# Move into the built artifact
cd build/libs

# Run the spring-boot java app on your local machine.
java -jar repo-scorer-1.0-SNAPSHOT.jar


# Example API calls

# Single API to search for github repositories and compute their score.
curl localhost:8080/github/repositories/{your-query}/score

# Support for filtering based on language of the repository
curl localhost:8080/github/repositories/{your-query}/score?language=Java

# Support for filtering based on creation-date of the repository
# You can specify the earliestCreatedDate=2026-06-01
curl localhost:8080/github/repositories/{your-query}/score?language=Java&earliestCreatedDate=2026-06-01

```

### A note about use of AI
Scoring logic encapsulated in <code>AdvancedWeightedScoringMechanism</code> was enhanced by the use of Gemini AI.
