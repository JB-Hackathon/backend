# 🚀 JB-Hackathon Backend Service

본 저장소는 프론트엔드(React), AI 서비스(FastAPI) 및 pgvector 데이터베이스와 유기적으로 통신하며 비즈니스 로직을 처리하는 핵심 **Spring Boot 백엔드 애플리케이션**입니다.

MSA(Microservices Architecture)로의 확장성과 컨테이너 기반 격리 보안을 고려하여 설계되었습니다.

---

## 🛠 기술 스택 및 개발 환경 (Tech Stack)

### Core 스택
- **Language**: Java 21 (LTS)
- **Framework**: Spring Boot 4.0.6
- **Build Tool**: Gradle 9.5.1 (Kotlin DSL)
    - 타입 안정성 확보 및 IntelliJ 프로바이더의 자동 완성·오타 검출 극대화를 위해 Kotlin DSL을 채택했습니다.

### Database & Infra
- **Database**: PostgreSQL 16 (`pgvector` 확장 모듈 탑재)
- **Infra / Virtualization**: Docker, Docker Compose
    - 외부 노출이 필요 없는 내부망과 외부망을 분리하는 투티어(2-Tier) 네트워크 아키텍처를 적용했습니다.

### 주요 의존성 (Dependencies)
- **Spring Web**: 내장 톰캣(Tomcat)을 기반으로 RESTful API를 구축하고 HTTP 웹 통신을 처리하는 핵심 엔진
- **Spring Data JPA**: 객체 지향적 도메인 모델 기반 데이터베이스 연동 및 ORM 구현
- **PostgreSQL Driver**: 백엔드 애플리케이션과 PostgreSQL(pgvector) 데이터베이스 간의 물리적 커넥션 연결
- **Spring Security**: React(Frontend) 및 FastAPI(AI) 간의 안전한 리소스 공유를 위한 CORS(Cross-Origin Resource Sharing) 제어 및 기본 접근 통제
- **Spring Boot Actuator**: 애플리케이션 상태 모니터링 및 Docker Compose의 `service_healthy` 신호 연동을 위한 내장 헬스체크 구동
- **Lombok**: 반복적인 가독성 저해 코드(Getter, Setter, Constructor, Logger) 자동화를 통한 생산성 향상
- **Validation**: 클라이언트 측 요청 데이터(DTO)의 무결성 사전 검증 자동화
- **Spring Boot DevTools**: 코드 수정 시 서버 재시작 없이 실시간 리로딩을 지원하는 개발 생산성 도구

---

## 🏗 시스템 아키텍처 및 네트워크 구조

본 프로젝트는 보안 및 데이터 격리를 위해 두 개의 독립된 도커 가상 네트워크를 구성하여 운영됩니다.

1. **`web` 네트워크 (외부망)**: 사용자 브라우저와 소통하는 영역으로, `react-app`과 `backend-app`이 포함됩니다.
2. **`internal-network` (내부망)**: 외부에 노출될 필요가 없는 영역으로, `backend-app`, `ai-app`, `postgres-db`가 포함됩니다.

> 💡 **보안 설계 특징**: AI 서버(`ai-app`)와 데이터베이스(`postgres-db`)는 운영 환경에서의 공격 표면(Attack Surface) 노출을 최소화하기 위해 포트를 외부로 개방하지 않았습니다. 로컬 개발 환경의 디버깅을 위해서만 루프백 주소(`127.0.0.1`)로 안전하게 바인딩되어 있으며, 백엔드는 도커 내부 DNS 서비스 이름(`http://ai-app:8000`, `postgres-db:5432`)을 통해 격리된 내부망 안에서 안전하게 통신합니다.

---

## 🔄 컨테이너 구동 시퀀스 (Healthcheck)

서버가 처음 켜질 때 데이터베이스 인프라가 완전히 준비되지 않아 백엔드가 크래시(Connection Refused)되는 현상을 방지하기 위해, Docker `healthcheck`와 `depends_on` 구조를 유기적으로 연동했습니다.

---

## 💻 로컬 개발 환경 시작 가이드

본 백엔드 애플리케이션은 전체 서비스 루트 폴더에 위치한 `docker-compose.yml`을 통해 컨테이너 환경에서 통합 구동됩니다.

### 1. 사전 요구사항
- 로컬 PC에 **Docker Desktop**이 설치 및 구동 중이어야 합니다.
- 백엔드 코드 수정을 위해서는 **IntelliJ IDEA** 사용을 권장합니다.

### 2. IntelliJ 초기 세팅 (최초 1회 필수)
Lombok 정상 작동 및 컴파일 에러 방지를 위해 IDE 설정을 변경해 주세요.
1. **Annotation Processors 활성화**: `Settings` ➡️ `Build, Execution, Deployment` ➡️ `Compiler` ➡️ `Annotation Processors` 이동 후 **[Enable annotation processing]** 체크
2. **빌드 속도 최적화**: `Settings` ➡️ `Build Tools` ➡️ `Gradle` 이동 후 **Build and run using**과 **Run tests using**을 모두 `Gradle`에서 **`IntelliJ IDEA`**로 변경

### 3. 애플리케이션 실행 명령어
전체 프로젝트의 루트 디렉토리(JB-HACKATHON)에서 아래 명령어를 실행합니다.

```bash
# 전체 서비스 컨테이너 빌드 및 백그라운드 구동
docker-compose up --build -d

# 백엔드 실시간 로그 확인
docker compose logs -f backend-app

# 서비스 완전 정지 및 컨테이너 삭제 (데이터는 볼륨 마운트로 보존됨)
docker-compose down