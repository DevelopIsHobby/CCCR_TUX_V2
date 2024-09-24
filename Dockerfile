# 베이스 이미지 설정
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /home/user

# 현재 디렉토리의 .jar 파일을 파드 안으로 복사
COPY build/libs/chattest-0.0.1-SNAPSHOT.jar /home/user/tuxtalk.jar

# /upload/default 디렉토리 생성 및 권한 설정
RUN mkdir -p /upload/default


## 로컬 시스템의 파일을 업로드 디렉토리로 복사 (선택 사항)
#COPY /upload/2024/default/관리자님.jpg /hoem/user/upload/관리자님.jpg
#COPY /upload/2024/default/남석진우뉨.jpg /home/user/upload/남석진우님.jpg
#COPY /upload/2024/default/민정님.jpg /app/민정님.jpg
#COPY /upload/2024/default/카카오톡_프사.jpg /app/카카오톡_프사.jpg

# 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "/home/user/tuxtalk.jar"]
