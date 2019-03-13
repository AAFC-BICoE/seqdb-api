# seqdb-api [![Build Status](https://travis-ci.org/AAFC-BICoE/seqdb-api.svg?branch=dev)](https://travis-ci.org/AAFC-BICoE/seqdb-api)

Sequence management services managing laboratory workflows leading to DNA sequences.

seqdb-api is an implementation of the Sequence Module for the [DINA project](https://www.dina-project.net/).

## Status
Currently under development, see [Release Notes](RELEASE_NOTES.md) for details.

## Required

* Java 1.8+
* Maven 3.2+

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

The sinlge HTML page will be available at `target/generated-docs/index.hml`
