# --- 빌드 스테이지 ---
FROM gradle:9.5.1-jdk21 AS builder
WORKDIR /backend

# 의존성 파일을 먼저 복사해 캐싱을 타게 만들기
COPY build.gradle.kts settings.gradle.kts ./
RUN gradle build -x test -x bootJar -x jar --no-daemon || true

# 나머지 소스 코드를 복사하고 진짜 실 배포용 jar 빌드
COPY . .
RUN gradle bootJar --no-daemon -x test

# --- 실행 스테이지 ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /backend
# 빌드 스테이지에서 생성된 jar 파일 사용
COPY --from=builder /backend/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]