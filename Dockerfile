FROM openjdk:8-jre-slim
VOLUME /tmp
COPY target/seqdb.api-*.jar /seqdb-api.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/seqdb-api.jar"]
