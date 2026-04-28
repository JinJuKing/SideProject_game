# SideProject Game

Developer portfolio project built around a survival dodging game.

## Concept

The player controls a character with `WASD` and survives as long as possible while projectiles fly in from outside the arena. Difficulty increases over time through faster projectiles, more frequent spawns, and denser attack patterns.

## Planned Stack

- Frontend: browser game UI
- Java: Spring Boot game/user/ranking API
- Python: FastAPI AI analysis service
- DB: PostgreSQL
- Infra: Docker Compose, CI/CD, cloud deployment

## Current MVP

- Playable survival game
- WASD movement
- Increasing difficulty
- Survival time and best score saved locally

Open `frontend/index.html` in a browser to play.

## Local Database

After Docker Desktop is installed, start PostgreSQL:

```powershell
cd C:\SideProject_game\infra
docker compose up -d
```

Connection info:

```text
Host: localhost
Port: 5432
Database: sideproject_game
User: sideproject
Password: sideproject1234
```

## Project Layout

```text
frontend/       Browser game MVP
backend-java/   Spring Boot API server, planned
ai-python/      FastAPI AI service, planned
infra/          Docker/deployment files, planned
docs/           Architecture and planning docs
```
