FROM maven:3.6.3-jdk-11-slim
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
USER user
COPY --from=0 --chown=644 /project/target/seqdb.api-*.jar /seqdb-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/seqdb-api.jar"]
