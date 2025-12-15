# Certifolio

Certifolio는 개발자들의 경력 관리를 돕고, 기업과 소통하며, 함께 성장할 수 있는 포트폴리오 기반의 커리어 플랫폼입니다.

## ✨ 주요 기능

- **회원 및 프로필 관리**: 사용자 정보, 학력, 경력, 프로젝트 이력 등을 관리합니다.
- **기업 및 채용**: 기업 정보를 등록하고, 채용 공고를 게시 및 관리합니다.
- **프로젝트/스터디**: 사이드 프로젝트나 스터디를 위한 팀원을 모집하고 관리합니다.
- **멘토링**: 경험이 풍부한 멘토와 성장을 원하는 멘티를 연결합니다.
- **커뮤니티**: 개발 관련 지식을 묻고 답할 수 있는 Q&A 게시판을 제공합니다.

## 🛠️ 기술 스택

- **Language**: Java 21
- **Framework**: Spring Boot
- **Database**: MySQL (Production), H2 (Local)
- **Data Access**: Spring Data JPA
- **Security**: Spring Security (OAuth2 Client)
- **Build Tool**: Gradle
- **API Documentation**: SpringDoc (Swagger UI)
- **Etc**: Lombok

## 📂 프로젝트 구조

프로젝트의 주요 디렉토리 구조와 각 역할은 다음과 같습니다.

```
src/
├── main
│   ├── java/com/example/demo
│   │   ├── domain/             # 도메인별 비즈니스 로직
│   │   │   ├── community/     # Q&A 커뮤니티
│   │   │   ├── company/       # 기업 정보 및 채용 공고
│   │   │   ├── mentoring/     # 멘토링
│   │   │   ├── project/       # 프로젝트/스터디
│   │   │   └── user/          # 사용자 정보 및 프로필
│   │   │       ├── controller # API 엔드포인트 정의
│   │   │       ├── service    # 비즈니스 로직 구현
│   │   │       ├── repository # 데이터베이스 연동
│   │   │       ├── entity     # DB 테이블과 매핑되는 객체
│   │   │       └── dto        # 데이터 전송 객체 (Request/Response)
│   │   │
│   │   └── global/             # 전역 모듈
│   │       ├── common/        # 공통 응답 객체, 유틸리티
│   │       ├── config/        # 애플리케이션 주요 설정
│   │       ├── exception/     # 전역 예외 처리
│   │       └── security/      # Spring Security 설정
│   │
│   └── resources/              # 설정 및 정적 파일
│       ├── static/            # 정적 리소스 (CSS, JS, 이미지 등)
│       ├── templates/         # 서버 사이드 템플릿
│       └── application.yaml   # Spring 설정 (local, prod 프로필로 분리)
│
└── test
    └── java/com/example/demo   # 단위, 통합 테스트 코드
```

## 🚀 실행 방법

### 사전 요구사항

- Java 21
- Gradle

### 로컬 환경에서 실행하기

1.  **저장소 복제**
    ```bash
    git clone {저장소 URL}
    cd graduation_project
    ```

2.  **애플리케이션 실행**
    프로젝트는 기본적으로 `local` 프로필을 사용하며, 별도의 DB 설정 없이 내장된 H2 데이터베이스로 실행됩니다.
    ```bash
    ./gradlew bootRun
    ```

3.  **애플리케이션 확인**
    - 애플리케이션은 `http://localhost:8080` 에서 실행됩니다.

## 📚 API 문서

SpringDoc을 통해 API 문서를 자동으로 생성합니다. 애플리케이션 실행 후, 아래 주소에서 Swagger UI를 통해 API를 확인하고 테스트할 수 있습니다.

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

