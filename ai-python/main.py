from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field


app = FastAPI(title="SideProject Game AI", version="0.1.0")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=False,
    allow_methods=["*"],
    allow_headers=["*"],
)


class AnalyzeRequest(BaseModel):
    survival_time_seconds: float = Field(gt=0)
    level_reached: int = Field(ge=1)


class AnalyzeResponse(BaseModel):
    grade: str
    summary: str
    weakness: str
    recommendation: str


@app.get("/api/health")
def health() -> dict[str, str]:
    return {"status": "ok"}


@app.post("/api/analyze", response_model=AnalyzeResponse)
def analyze(request: AnalyzeRequest) -> AnalyzeResponse:
    time = request.survival_time_seconds
    level = request.level_reached

    if time < 10:
        return AnalyzeResponse(
            grade="입문",
            summary=f"{time:.1f}초 생존했습니다. 아직 초반 탄 속도에 적응하는 단계입니다.",
            weakness="초반 조준탄을 피할 때 이동 방향이 늦게 결정되는 편입니다.",
            recommendation="처음부터 화면 중앙 근처를 유지하고, 한 방향으로 길게 도망가기보다 짧게 방향을 바꿔보세요.",
        )

    if time < 25:
        return AnalyzeResponse(
            grade="도전자",
            summary=f"{time:.1f}초 생존했고 레벨 {level}까지 도달했습니다.",
            weakness="웨이브 탄막이 들어올 때 벽 쪽으로 몰릴 가능성이 있습니다.",
            recommendation="탄막 사이 빈 공간을 먼저 보고 움직이면 더 오래 버틸 수 있습니다.",
        )

    if time < 45:
        return AnalyzeResponse(
            grade="숙련",
            summary=f"{time:.1f}초 생존했습니다. 기본 회피 흐름은 안정적입니다.",
            weakness="고속 탄이 섞이는 구간에서 이동 경로가 좁아질 수 있습니다.",
            recommendation="위험한 탄 하나만 보지 말고, 다음 1초 동안 지나갈 공간을 미리 확보하세요.",
        )

    return AnalyzeResponse(
        grade="고수",
        summary=f"{time:.1f}초 생존했습니다. 높은 난이도에서도 침착하게 버틴 기록입니다.",
        weakness="장기 생존 구간에서는 작은 실수 하나가 바로 사망으로 이어집니다.",
        recommendation="중앙 유지, 짧은 대각선 이동, 웨이브 빈틈 선점을 계속 연습하면 기록을 더 늘릴 수 있습니다.",
    )
