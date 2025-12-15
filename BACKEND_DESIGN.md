# 백엔드 설계서 (Backend Design Document)

## 1. 프로젝트 개요
본 문서는 Certi-Folio 프로젝트의 백엔드 구현을 위한 상세 설계서입니다. MSA를 지향하는 모듈형 아키텍처를 기반으로 하며, 유지보수성과 확장성을 고려하여 설계되었습니다.

## 2. 개발 환경 및 기술 스택 (Tech Stack)

| 구분 | 기술 스택 | 버전 및 비고 |
| :--- | :--- | :--- |
| **Language** | Java | 21 |
| **Framework** | Spring Boot | 4.0.0 |
| **Build Tool** | Gradle | |
| **Data Access** | Spring Data JPA | |
| **Database** | MySQL, H2 | |
| **API Docs** | SpringDoc (OpenAPI) | v2.5.0 |
| **Security** | Spring Security | OAuth2 Client |
| **Utilities** | Lombok | |

## 3. 폴더 구조 (Directory Structure)
도메인 주도 설계(Domain-Driven Design)를 따라 각 도메인별로 기능을 분리했습니다.

```bash
src/main/java/com/example/demo/
├── domain/
│   ├── community/       # 커뮤니티 (Q&A)
│   ├── company/         # 기업 및 채용 정보
│   ├── mentoring/       # 멘토링
│   ├── project/         # 개인/팀 프로젝트
│   └── user/            # 사용자 정보 및 인증
└── global/              # 전역 설정 및 공통 모듈
    ├── common/          # 공통 응답 객체 등
    ├── config/          # 설정 파일
    ├── exception/       # 전역 예외 처리
    └── security/        # Spring Security 설정
```

## 4. 상세 모듈 설계 (Module Specifications)

각 도메인 모듈은 비즈니스 로직을 효율적으로 분리하기 위해 아래와 같은 컴포넌트로 구성됩니다.

*   **Controller**: API 엔드포인트를 정의하고, 클라이언트의 HTTP 요청을 받아 서비스 계층으로 전달하는 진입점입니다.
*   **Service**: 핵심 비즈니스 로직을 구현합니다. 여러 리포지토리를 사용해 데이터를 처리하고, 트랜잭션을 관리합니다.
*   **Repository**: 데이터베이스와 직접 상호작용하며, JPA를 통해 데이터의 CRUD(생성, 조회, 수정, 삭제)를 담당합니다.
*   **Entity**: 데이터베이스 테이블과 1:1로 매핑되는 영속적인 데이터 객체입니다.
*   **DTO (Data Transfer Object)**: 계층 간 데이터 전송을 위한 객체로, API 요청(Request)과 응답(Response)의 규격을 정의합니다.

### 4.1 사용자 모듈 (User)
*   **역할**: 사용자 인증, 프로필, 경력, 학력 등 종합적인 이력 정보를 관리합니다.
*   **Controller**: `UserController.java`
*   **Service**: `UserService.java`
*   **Repository**: `UserRepository.java`
*   **Entity**: `User.java`, `UserCareer.java`, `UserEducation.java`, `UserProfile.java`, `UserProjectHistory.java`
*   **DTO**: `SignupRequest.java`, `UserResponse.java`

### 4.2 커뮤니티 모듈 (Community)
*   **역할**: 사용자 간의 Q&A, 정보 공유 등 커뮤니티 기능을 담당합니다.
*   **Controller**: `QnaController.java`
*   **Service**: `QnaService.java`
*   **Repository**: `QuestionRepository.java`, `AnswerRepository.java`
*   **Entity**: `Question.java`, `Answer.java`, `Post.java`
*   **DTO**: `QuestionRequest.java`, `QuestionResponse.java`, `AnswerRequest.java`, `AnswerResponse.java`

### 4.3 기업 및 채용 모듈 (Company)
*   **역할**: 기업 정보, 채용 공고, 직무 역량 기준 등 채용 관련 기능을 관리합니다.
*   **Controller**: `CompanyController.java`
*   **Service**: `CompanyService.java`
*   **Repository**: `CompanyRepository.java`, `CompanyRecruitRepository.java`, `CompetencyStandardRepository.java`
*   **Entity**: `Company.java`, `CompanyRecruit.java`
*   **DTO**: `CompanyRequest.java`, `CompanyResponse.java`, `RecruitRequest.java`, `RecruitResponse.java`

### 4.4 멘토링 모듈 (Mentoring)
*   **역할**: 멘토-멘티 매칭 및 멘토링 요청, 관리 기능을 제공합니다.
*   **Controller**: `MentoringController.java`
*   **Service**: `MentoringService.java`
*   **Repository**: `MentorRepository.java`, `MentoringRequestRepository.java`
*   **Entity**: `Mentor.java`, `MentoringRequest.java`
*   **DTO**: `MentoringRequest.java`, `MentoringResponse.java`, `MenteeRequest.java`, `MenteeResponse.java`

### 4.5 프로젝트 모듈 (Project)
*   **역할**: 스터디, 사이드 프로젝트 등 사용자 주도 프로젝트의 생성 및 관리를 담당합니다.
*   **Controller**: `ProjectController.java`
*   **Service**: `ProjectService.java`
*   **Repository**: `ProjectRepository.java`
*   **Entity**: `StudyProject.java`
*   **DTO**: `ProjectRequest.java`, `ProjectResponse.java`

## 5. API 인터페이스 설계 (API Specification)
**참고:** 아래 명세는 파일 구조를 기반으로 추론한 **예시**입니다. 실제 구현 시 변경될 수 있습니다.

### 5.1 사용자 (User)
| Method | Endpoint | 설명 | Request Body | Response Data |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/users/me` | 내 정보 조회 | - | `UserResponse` |
| `PUT` | `/api/users/me` | 내 정보 수정 | `SignupRequest` | `UserResponse` |
| `GET` | `/api/users/{userId}` | 특정 사용자 정보 조회 | - | `UserResponse` |

### 5.2 커뮤니티 (Community - Q&A)
| Method | Endpoint | 설명 | Request Body | Response Data |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/questions` | 질문 목록 조회 | - | `List<QuestionResponse>` |
| `POST` | `/api/questions` | 질문 작성 | `QuestionRequest` | `QuestionResponse` |
| `GET` | `/api/questions/{questionId}` | 질문 상세 조회 | - | `QuestionResponse` |
| `PUT` | `/api/questions/{questionId}` | 질문 수정 | `QuestionRequest` | `QuestionResponse` |
| `DELETE` | `/api/questions/{questionId}` | 질문 삭제 | - | `void` |
| `POST` | `/api/questions/{questionId}/answers` | 답변 작성 | `AnswerRequest` | `AnswerResponse` |
| `DELETE` | `/api/answers/{answerId}` | 답변 삭제 | - | `void` |

### 5.3 기업 및 채용 (Company & Jobs)
| Method | Endpoint | 설명 | Request Body | Response Data |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/companies` | 기업 목록 조회 | - | `List<CompanyResponse>` |
| `GET` | `/api/companies/{companyId}` | 기업 상세 조회 | - | `CompanyResponse` |
| `GET` | `/api/recruits` | 채용 공고 목록 조회 | - | `List<RecruitResponse>` |
| `POST` | `/api/recruits` | 채용 공고 등록 | `RecruitRequest` | `RecruitResponse` |
| `GET` | `/api/recruits/{recruitId}` | 채용 공고 상세 조회 | - | `RecruitResponse` |

### 5.4 멘토링 (Mentoring)
| Method | Endpoint | 설명 | Request Body | Response Data |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/mentors` | 멘토 목록 조회 | - | `List<MentorResponse>` |
| `POST` | `/api/mentoring/requests` | 멘토링 요청 | `MentoringRequest` | `MentoringResponse` |
| `GET` | `/api/mentoring/requests` | 멘토링 요청 목록 조회 | - | `List<MentoringResponse>` |
| `PUT` | `/api/mentoring/requests/{requestId}` | 멘토링 요청 상태 변경 | `{ status: "ACCEPTED" }` | `MentoringResponse` |

### 5.5 프로젝트 (Project)
| Method | Endpoint | 설명 | Request Body | Response Data |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/projects` | 프로젝트 목록 조회 | - | `List<ProjectResponse>` |
| `POST` | `/api/projects` | 프로젝트 생성 | `ProjectRequest` | `ProjectResponse` |
| `GET` | `/api/projects/{projectId}` | 프로젝트 상세 조회 | - | `ProjectResponse` |
| `PUT` | `/api/projects/{projectId}` | 프로젝트 수정 | `ProjectRequest` | `ProjectResponse` |
