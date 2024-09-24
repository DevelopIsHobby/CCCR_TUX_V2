# 베이스 이미지 설정
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# 현재 디렉토리의 .jar파일을 파드 안으로 복사(build/libs 아닌 다른 경로 입력하면 빌드 안됨)
COPY build/libs/chattest-0.0.1-SNAPSHOT.jar tuxtalk.jar

# 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "/tuxtalk.jar"]

