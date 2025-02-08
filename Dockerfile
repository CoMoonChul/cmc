# Step 1: 베이스 이미지 선택 (JDK 17 사용)
FROM openjdk:17-jdk

# Step 2: 작업 디렉토리 설정
WORKDIR /app

# Step 3: 애플리케이션 JAR 파일 복사
COPY app.jar /app/app.jar

# Step 4: 실행할 명령어 지정
CMD ["java", "-jar", "/app/app.jar"]
