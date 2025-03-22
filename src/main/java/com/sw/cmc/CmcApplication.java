package com.sw.cmc;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class CmcApplication {

	public static void main(String[] args) {

		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		// .env 파일 로드
		Dotenv dotenv = Dotenv.configure().directory("./").ignoreIfMissing().load();
		// 시스템 환경 변수에 추가
		System.setProperty("DB_HOST", dotenv.get("DB_HOST"));
		System.setProperty("DB_PORT", dotenv.get("DB_PORT"));
		System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
		System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
		System.setProperty("SMTP_USERNAME", dotenv.get("SMTP_USERNAME"));
		System.setProperty("SMTP_PASSWORD", dotenv.get("SMTP_PASSWORD"));
		System.setProperty("AI_KEY", dotenv.get("AI_KEY"));
		System.setProperty("AI_MODEL", dotenv.get("AI_MODEL"));
		System.setProperty("JWT_SECRET_KEY", dotenv.get("JWT_SECRET_KEY"));
		System.setProperty("JWT_ENCRYPTION_KEY", dotenv.get("JWT_ENCRYPTION_KEY"));
		SpringApplication.run(CmcApplication.class, args);
	}

}
