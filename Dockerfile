FROM maven:3.6.3-jdk-11 as build-stage

RUN mkdir /project
WORKDIR /project

# Cache maven dependencies
ADD pom.xml /project
RUN mvn clean install -Dmaven.test.skip=true -Dspring-boot.repackage.skip

# Stage 1: build jar
ADD . /project

# Integration Tests will be skipped as they require a database
RUN mvn test
RUN mvn clean install -Dmaven.test.skip=true

# Stage 2: extract jar and set entrypoint
FROM openjdk:11-jre-slim
RUN useradd -s /bin/bash user
USER root

WORKDIR /app
COPY --from=build-stage --chown=user /project/target/seqdb.api-*.jar /app/
COPY --chown=user scripts/*.sh /app/
COPY --chown=user scripts/*.awk /app/
COPY --chown=user pom.xml /app/
RUN chmod +x *.sh

USER user
EXPOSE 8080

WORKDIR /app

ENV APPLICATION=dina
ENV POSTGRES_DB=postgres
ENV POSTGRES_HOST=localhost
ENV minio.host=localhost
ENV minio.port=9000
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=databasecreds
ENV spring.datasource.username=springuser
ENV spring.datasource.password=springcreds
ENV spring.datasource.database=object_store
ENV spring.liquibase.user=liquibaseuser
ENV spring.liquibase.password=liquibasecreds
ENV minio.accessKey=minio
ENV minio.secretKey=minio123
ENV spring.http.log-request-details=true
ENV spring.servlet.multipart.max-file-size: 1GB
ENV spring.servlet.multipart.max-request-size: 1GB

ENTRYPOINT ["java", "-jar", "/seqdb-api.jar"]
