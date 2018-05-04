# seqdb-api

Sequence management services managing laboratory workflows leading to DNA sequences.

seqdb-api is an implementation of the Sequence Module for the [DINA project](https://www.dina-project.net/).

## Required:

Java 1.8+

Maven 3.2+

## To launch:
mvn spring-boot:run

## Configuration:
This application is configured using Spring Boot, with default properties stored in src/main/resources/application.yml. It can be [configured externally using properties files, YAML files, environment variables, and command-line arguments](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html).

[Common Spring properties](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html)

[Crnk properties](http://www.crnk.io/releases/stable/documentation/#_integration_with_spring_and_string_boot)