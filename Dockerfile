FROM openjdk:8-jre-slim
RUN useradd -s /bin/bash user
USER root

WORKDIR /app
COPY --chown=user target/seqdb.api-*.jar /app/
COPY --chown=user scripts/*.sh /app/
COPY --chown=user scripts/*.sql /app/
COPY --chown=user scripts/*.awk /app/
COPY --chown=user pom.xml /app/

RUN apt-get update && apt-get install -y postgresql-client-11
RUN apt-get install -y curl
RUN mkdir -p /home/user

USER user

EXPOSE 8080
WORKDIR /app

ENV spring.datasource.url=jdbc:postgresql://localhost/object_store?currentSchema=seqdb
ENV spring.datasource.username=springuser
ENV spring.datasource.password=springcreds
ENV spring.liquibase.user=liquibaseuser
ENV spring.liquibase.password=liquibasecreds
ENV spring.liquibase.defaultSchema=seqdb
ENV POSTGRES_DB=object_store
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=databasecreds
ENV POSTGRES_HOST=localhost
ENV spring.http.log-request-details=true

ENTRYPOINT ["bash","/app/launch.sh","seqdb.api"]

