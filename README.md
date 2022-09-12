# seqdb-api

Sequence management services managing laboratory workflows leading to DNA sequences.

seqdb-api is an implementation of the Sequence Module for the [DINA project](https://www.dina-project.net/).

## Required

* Java 17
* Maven 3.8+
* PostgreSQL 10
* Docker

## Run

```
mvn clean spring-boot:run
```

More information on [Running the application](docs/running.adoc).

## Documentation

See [DINA Developer Guide](https://aafc-bicoe.github.io/dina-documentation/developer/)

## To Run

For testing purpose a [Docker Compose](https://docs.docker.com/compose/) example file is available in the `local` folder.

Create a new docker-compose.yml file and .env file from the example file in the local directory:

```
cp local/docker-compose.yml.example docker-compose.yml
cp local/*.env .
```

Start the app (default port is 8084):

```
docker-compose up
```

Once the services have started you can access the endpoints at http://localhost:8084/api/

Cleanup:
```
docker-compose down
```

Automated tests are run using an embedded Docker container.
