# --------------------------------------------------
# 1) Build 단계: Gradle 8 이미지로 빌드
# --------------------------------------------------
FROM gradle:8.13-jdk17 AS builder

WORKDIR /app

# 설정 파일만 먼저 복사해서 의존성 캐시 활용
COPY build.gradle settings.gradle ./

# 의존성 다운로드 (빌드 속도 최적화)
RUN gradle dependencies --no-daemon

# 소스 코드 복사
COPY . .

# 테스트 제외하고 JAR 생성
RUN gradle clean build --no-daemon -x test

# --------------------------------------------------
# 2) Runtime 단계: 슬림한 JRE 이미지
# --------------------------------------------------
FROM openjdk:17-jdk-slim

WORKDIR /app

# 빌드된 JAR을 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# Spring Boot 기본 포트
EXPOSE 8080

# JVM 옵션 (필요 시 조정)
ENV JAVA_OPTS="-Xms256m -Xmx512m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
