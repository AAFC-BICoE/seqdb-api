# seqdb-api


##  Description (en)

Molecular Analysis services - Supported Workflows
 * Sanger
 * NGS - Whole Genome
 * Metagenomics
 * Generic workflow

seqdb-api is an implementation of the Sequence Module for the [DINA project](https://www.dina-project.net/).

## Description (fr)

Services d'analyse moléculaire - Flux de travail supportés

* Sanger
* NGS - Génome entier
* Métagénomique
* Flux de travail générique

seqdb-api est une implémentation du module de sequence pour le [DINA project](https://www.dina-project.net/) (anglais seulement).

## Required

* Java 21
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
