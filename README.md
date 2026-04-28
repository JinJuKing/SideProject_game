# SideProject Game

개발자 취업 포트폴리오를 목표로 만드는 생존 회피 게임 프로젝트입니다.

## 프로젝트 컨셉

플레이어는 `WASD`로 캐릭터를 조작하고, 사방에서 날아오는 탄을 최대한 오래 피해야 합니다.
시간이 지날수록 탄 속도, 생성 빈도, 공격 패턴이 강해지며 생존 시간이 점수가 됩니다.

## 사용 예정 기술

- Frontend: 브라우저 기반 게임 화면
- Java: Spring Boot 게임 기록/유저/랭킹 API
- Python: FastAPI 기반 AI 분석 서버
- DB: PostgreSQL
- Infra: Docker Compose, CI/CD, 클라우드 배포

## 현재 구현된 기능

- 브라우저에서 실행 가능한 회피 게임
- `WASD` 캐릭터 이동
- 닉네임/비밀번호 기반 플레이어 가입 및 로그인
- 사방에서 날아오는 탄 생성
- 시간이 지날수록 난이도 상승
- 가로/세로 웨이브 탄막 패턴
- 충돌 시 게임 종료
- 생존 시간 측정
- 로그인 플레이어별 최고 기록 DB 갱신
- 랭킹 조회
- 최고 기록 브라우저 로컬 저장

게임 실행 방법:

```text
C:\SideProject_game\frontend\index.html
```

위 파일을 브라우저로 열고 `Start` 버튼을 누르면 플레이할 수 있습니다.

## 로컬 DB 실행

```powershell
cd C:\SideProject_game\infra
docker compose up -d
```

DB 접속 정보:

```text
Host: localhost
Port: 5433
Database: sideproject_game
User: sideproject
Password: sideproject1234
```

이미 설치된 PostgreSQL을 사용할 경우, PostgreSQL 관리자 계정에서 프로젝트 DB와 계정을 먼저 만들어야 합니다.

```sql
CREATE USER sideproject WITH PASSWORD 'sideproject1234';
CREATE DATABASE sideproject_game OWNER sideproject;
```

## Java 서버 실행

PostgreSQL 실행 후 Spring Boot API 서버를 실행합니다.
JDK 17 이상이 필요합니다.

```powershell
cd C:\SideProject_game\backend-java
.\mvnw.cmd spring-boot:run
```

게임 기록 저장 API:

```text
POST http://localhost:8080/api/players/register
POST http://localhost:8080/api/players/login
POST http://localhost:8080/api/game-runs
GET  http://localhost:8080/api/game-runs/ranking
```

## Python AI 서버 실행

게임 결과를 분석해서 간단한 피드백을 반환하는 FastAPI 서버입니다.

의존성 설치 없이 빠르게 테스트할 때:

```powershell
cd C:\SideProject_game\ai-python
python local_server.py
```

FastAPI 서버로 실행할 때:

```powershell
cd C:\SideProject_game\ai-python
python -m venv .venv
.\.venv\Scripts\activate
pip install -r requirements.txt
uvicorn main:app --reload --port 8000
```

AI 분석 API:

```text
POST http://localhost:8000/api/analyze
```

## 프로젝트 구조

```text
frontend/       브라우저 게임 MVP
backend-java/   Spring Boot API 서버 예정
ai-python/      FastAPI AI 분석 서버 예정
infra/          Docker, DB, 배포 설정
docs/           설계 문서
```

## 앞으로 구현할 기능

- Java Spring Boot API 서버
- PostgreSQL 게임 기록 저장
- Python AI 플레이 분석
- 결과 화면 개선
- Docker 기반 전체 실행 환경
- 클라우드 배포

## 자동 검증

GitHub Actions로 다음 검증을 자동 실행합니다.

- Frontend: `node --check frontend/game.js`
- Java Backend: `./mvnw test`
- Python AI: `python -m py_compile main.py`
