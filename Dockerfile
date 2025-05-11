FROM openjdk:17-jdk

WORKDIR /APP

COPY app.jar /app/app.jar

ENV SPRING_PROFILES_ACTIVE=prod

COPY application-prod.yaml /app/config/application-prod.yaml

CMD ["java", "-jar", "/app/app.jar"]