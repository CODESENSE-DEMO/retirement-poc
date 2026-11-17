# Retirement POC — 노후(은퇴)설계 JSP 화면

사내 표준 프레임워크 **Union Framework**(`io.union:union-starter`) 위에 Maven + JSP 로 만든 노후설계 화면입니다.
화면 문구는 모두 `src/main/resources/messages/result_ko.properties` 의 메시지 코드(10000~)로 렌더링합니다.

## 구성

- Java 17 · Spring Boot 2.7.18 (로컬에 설치된 union-framework 1.0.0-SNAPSHOT 과 같은 javax 라인) · `war` 패키징
- JSP + JSTL + `<spring:message code="..."/>`, 정적 리소스 `static/css`, `static/js`
- 레이어: `api`(REST) → `spec`/`impl`(서비스) — customer-poc 과 같은 패키지 규칙

| 경로 | 화면/기능 | 사용 메시지 |
|------|-----------|-------------|
| `GET /retirement` | 노후설계 소개 · 유의사항. 미연동 시 로그인 확인 모달 | 10165~10167, 10094, 10162~10164 |
| `GET /ar-link/complete` | AR회원 연동완료 → 액션B 시작하기 | 10000~10003 |
| `GET /retirement/plan` | 은퇴설계 입력 · 결과(필요/준비/부족자금, 달성도, 준비자산 비율) | 10012~10017, 10042~10083, 10094~10116 등 |
| `POST /api/v1/retirement-plans/simulations` | 은퇴설계 계산 API (ApiResponse 자동 래핑) | — |

에러: `RETIREMENT_001`(은퇴연령 ≤ 현재연령), `RETIREMENT_002`(기대수명 ≤ 은퇴연령), 입력 검증 실패 `COMMON_400`.

## 프로젝트 초기 구성

도구 설치(Homebrew/winget, SSH 키, GitHub 로그인)는 워크스페이스 루트의 `SETUP.md` 0장을 따릅니다. 이 프로젝트에 필요한 것만 정리하면:

### 1. 필수 도구

| 도구 | 버전 | 확인 |
|------|------|------|
| JDK | 17 (`JAVA_HOME` 설정) | `java -version` |
| Maven | 3.9.x | `mvn -v` 에 Java version 17 |
| Git | 최신 | `git --version` |

### 2. 프레임워크 로컬 설치 (최초 1회, 프레임워크 변경 시마다)

`io.union:union-starter:1.0.0-SNAPSHOT` 은 사내 원격 저장소가 아니라 **로컬 `~/.m2`** 에서 가져옵니다.
이 프로젝트는 Boot 2.7 / `javax.*` 라인에 맞춰져 있으므로, 설치된 프레임워크도 같은 라인인지 확인합니다.

```bash
cd ../union-framework && mvn -q -DskipTests install
grep -A2 '<artifactId>spring-boot-starter-parent' ~/.m2/repository/io/union/union-framework/1.0.0-SNAPSHOT/*.pom   # 2.7.x 여야 함
```

### 3. 환경 변수

| 변수 | 기본값 | 비고 |
|------|--------|------|
| `JWT_SECRET` | 개발용 더미 값 | 운영/공유 환경에서는 반드시 32바이트 이상으로 주입 (FW-08) |
| `DB_USERNAME` / `DB_PASSWORD` | `sa` / 빈 값 | H2 in-memory. 현재 엔티티 없음 |

### 4. Git 저장소 초기화

```bash
git init && git checkout -b main
git add . && git commit -m "chore: retirement-poc 초기 구성"
```

- 브랜치·커밋 규칙은 `wiki-coding-convention` 05장(GIT-01: `feature/{이슈키}-{요약}`)을 따릅니다.
- `.gitignore` — 빌드 산출물(`target/`), IDE 설정, `graft/`(코드 그래프 캐시), `.coa/`(coa CLI 로컬 상태)는 커밋하지 않습니다.
- `.ignore` — ripgrep 용. gitignore 된 `graft/` 카드는 검색되게 다시 포함하고, `.coa/` 는 검색에서도 뺍니다.

### 5. IDE

- IntelliJ: `pom.xml` 을 Maven 프로젝트로 열고, Lombok 플러그인 + *Enable annotation processing* 을 켭니다.
- Eclipse/STS: *Import > Existing Maven Projects*, Lombok 설치(`java -jar lombok.jar`).
- 파일 인코딩은 UTF-8 (JSP·properties 포함). `result_ko.properties` 는 UTF-8 그대로 두고 native2ascii 변환하지 않습니다 (`spring.messages.encoding=UTF-8`).

### 6. 보조 CLI (선택)

```bash
pip install coa-cli==0.4.2   # AI 코드 점검 → .coa/ 생성
npm i -g @nanonets/graft     # 코드 그래프 → graft/ 생성 (graft build)
```

## 빌드 & 실행

```bash
# 1. 프레임워크를 로컬 저장소에 설치 (최초 1회)
cd ../union-framework && mvn -q -DskipTests install

# 2. 빌드 & 테스트 (JDK 17)
cd ../retirement-poc && mvn clean package

# 3. 실행 → http://localhost:8080/
java -jar target/retirement-poc-0.0.1-SNAPSHOT.war
```

## 메모

- 메시지 번들: `spring.messages.basename=messages/result`, 로케일은 `ko` 고정. 빈 `result.properties` 는 Spring Boot 가 MessageSource 를 자동 구성하는 조건이라 남겨 둡니다.
- 메시지 값에 HTML(`<br>`, `<strong>`)이 들어 있어 이스케이프 없이 출력합니다. 번들은 신뢰된 정적 리소스로만 관리하세요.
- JSP 를 새로 만들 때 첫 줄에 `<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>` 를 넣어야 JSP 안의 한글 리터럴이 깨지지 않습니다 (include 된 header 의 지시자는 그 조각 파일에만 적용됨).
- AR회원 연동은 외부 시스템 몫이라 완료 화면만 있고, 연동 여부는 `sessionStorage` 플래그로만 분기합니다. 서버 인가가 필요한 기능(예: `[재무설계 정보저장]` 10159)을 붙일 때 JWT 로그인으로 교체해야 합니다.
- 사용하지 않은 코드: 투자성향·상품위험등급(10120~10141), 펀드 제안(10127~10136), 자녀·결혼·교육비 목표(10026~10033, 10145~10158) 등 — 상품 제안/세부목표 화면 범위.
