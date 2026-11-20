# AGENTS.md

## Project overview

`seqdb-api` is the DINA Sequence Module API, implemented as a Spring Boot
application. The main Java package is
`ca.gc.aafc.seqdb.api`.

Important directories:

- `src/main/java/ca/gc/aafc/seqdb/api/entities` - persistence entities
- `src/main/java/ca/gc/aafc/seqdb/api/repository` - data repositories
- `src/main/java/ca/gc/aafc/seqdb/api/service` - application services
- `src/main/java/ca/gc/aafc/seqdb/api/dto` - API data-transfer objects
- `src/main/java/ca/gc/aafc/seqdb/api/mapper` - entity/DTO mappings
- `src/main/java/ca/gc/aafc/seqdb/api/validation` - domain validation
- `src/main/resources` - Spring configuration, Liquibase changelogs, and
  vocabularies
- `src/test/java` - unit and integration tests
- `docs` - AsciiDoc project and run documentation
- `local` - Docker Compose examples and local environment templates

Keep changes focused on the relevant layer and follow existing patterns in
nearby classes. Do not edit generated or build output under `target/`.

## Prerequisites

- Java 25
- Maven 3.8+
- PostgreSQL 18 for the full integration-test workflow
- Docker for the container-backed tests and local services

## Build and test

Run unit tests:

```bash
mvn clean test
```

Run unit and integration tests:

```bash
mvn clean verify -Dspring.config.additional-location=src/test/resources/test-db-configuration.yml
```

Integration tests use an embedded Docker container; ensure Docker is available
before running them. Package the application with:

```bash
mvn clean install
```

Run the configured quality and documentation reports with:

```bash
mvn clean verify site
```

This includes Checkstyle, SpotBugs, OWASP dependency checking, and JaCoCo
reporting. The OWASP configuration fails the build for CVSS 8 or higher unless
an issue is covered by `owasp-suppression.xml`.

## Running locally

Start the application directly with:

```bash
mvn clean spring-boot:run
```

For the local service stack, copy the templates from `local` before starting
Docker Compose:

```bash
cp local/docker-compose.yml.example docker-compose.yml
cp local/*.env .
docker-compose up
```

The API is normally available at `http://localhost:8084/api/`. Stop the local
stack with `docker-compose down`.

## Database and configuration

Liquibase manages database changes. Add schema changes through the existing
changelog structure in `src/main/resources/db`, rather than modifying an
already-applied migration. Keep credentials and machine-specific settings in
local configuration files; do not commit secrets.

When adding or changing API resources, update the corresponding entity,
repository, service, mapper, DTO, validation, and tests as applicable. Preserve
the existing JSON:API and OpenAPI conventions used by neighboring resources.

## Documentation

Update the relevant AsciiDoc files in `docs` when changing setup, runtime, or
developer workflows. The root `README.md` contains the short project overview
and links to the detailed running instructions in `docs/running.adoc`.
