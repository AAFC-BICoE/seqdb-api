FROM maven:3.6.3-jdk-11-slim
RUN useradd -s /bin/bash user
USER user
COPY --chown=644 target/seqdb.api-*.jar /seqdb-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/seqdb-api.jar"]
