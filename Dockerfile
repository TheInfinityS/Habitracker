FROM openjdk:17
EXPOSE 8080
ARG APP_NAME="habittracker"
ARG APP_VERSION="0.0.1"
ARG JAR_FILE="/target/habittracker-0.0.1-SNAPSHOT.jar"

ENV googleClientSecret=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
ENV emailPassword=qneozuhjxlxhlmpj

COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]