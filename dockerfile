FROM openjdk:17.0.2-slim
ARG JAR_FILE=build/libs/*.jar

#COPY .aws/config /root/.aws/config
#COPY .aws/credentials /root/.aws/credentials
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar","-Dspring.profiles.active=dev","/app.jar"]

FROM docker.io/binxio/ssm-get-parameter:0.2.3 AS ssm

FROM alpine
COPY --from=ssm /ssm-get-parameter  /usr/local/bin
ENTRYPOINT [ "/usr/local/bin/entrypoint" ]
