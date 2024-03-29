FROM openjdk:17.0.2-slim
ARG JAR_FILE=build/libs/som-0.0.1-SNAPSHOT.jar

COPY .aws/config /root/.aws/config
COPY .aws/credentials /root/.aws/credentials
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar","-Dspring.profiles.active=dev","/app.jar"]
