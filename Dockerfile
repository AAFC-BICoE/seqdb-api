FROM openjdk:11-jre-slim

RUN useradd -s /bin/bash user
USER user
COPY --chown=644 target/seqdb.api-*.jar /seqdb-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/seqdb-api.jar"]
