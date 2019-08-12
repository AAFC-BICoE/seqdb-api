FROM openjdk:8-jre-slim
RUN useradd -s /bin/bash user
USER user
COPY --chown=644 target/seqdb.api-*.jar /seqdb-api.jar
EXPOSE 8080
ENTRYPOINT ["java","-XX:+UnlockExperimentalVMOptions","-XX:+UseCGroupMemoryLimitForHeap","-jar","/seqdb-api.jar"]
