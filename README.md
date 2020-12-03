# seqdb-api

Sequence management services managing laboratory workflows leading to DNA sequences.

seqdb-api is an implementation of the Sequence Module for the [DINA project](https://www.dina-project.net/).

## Required

* Java 11
* Maven 3.2+
* PostgreSQL 10
* Docker

## Run

```
mvn clean spring-boot:run
```

More information on [Running the application](docs/running.adoc).

## Documentation

To generate the complete documentation:
```
mvn clean compile
```

The single HTML page will be available at `target/generated-docs/index.html`

## To Run

For testing purpose or local development a [Docker Compose](https://docs.docker.com/compose/) example file is available in the `local` folder.

Create a new docker-compose.yml file and .env file from the example file in the local directory:

```
cp local/docker-compose.yml.example docker-compose.yml
cp local/*.env .
```

Start the app (default port is 8084):

```
docker-compose up --build
```

Once the services have started you can access the endpoints at http://localhost:8084/api/v1

Cleanup:
```
docker-compose down
```

Automated tests are run using an embedded Docker container.