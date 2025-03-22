FROM openjdk:17-jdk

WORKDIR /APP

COPY app.jar /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]