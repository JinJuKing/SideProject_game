from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
import json


HOST = "127.0.0.1"
PORT = 8000


def build_feedback(survival_time_seconds: float, level_reached: int) -> dict[str, str]:
    if survival_time_seconds < 10:
        return {
            "grade": "입문",
            "summary": f"{survival_time_seconds:.1f}초 생존했습니다. 아직 초반 탄 속도에 적응하는 단계입니다.",
            "weakness": "초반 조준탄을 피할 때 이동 방향이 늦게 결정되는 편입니다.",
            "recommendation": "처음부터 화면 중앙 근처를 유지하고, 한 방향으로 길게 도망가기보다 짧게 방향을 바꿔보세요.",
        }

    if survival_time_seconds < 25:
        return {
            "grade": "도전자",
            "summary": f"{survival_time_seconds:.1f}초 생존했고 레벨 {level_reached}까지 도달했습니다.",
            "weakness": "웨이브 탄막이 들어올 때 벽 쪽으로 몰릴 가능성이 있습니다.",
            "recommendation": "탄막 사이 빈 공간을 먼저 보고 움직이면 더 오래 버틸 수 있습니다.",
        }

    if survival_time_seconds < 45:
        return {
            "grade": "숙련",
            "summary": f"{survival_time_seconds:.1f}초 생존했습니다. 기본 회피 흐름은 안정적입니다.",
            "weakness": "고속 탄이 섞이는 구간에서 이동 경로가 좁아질 수 있습니다.",
            "recommendation": "위험한 탄 하나만 보지 말고, 다음 1초 동안 지나갈 공간을 미리 확보하세요.",
        }

    return {
        "grade": "고수",
        "summary": f"{survival_time_seconds:.1f}초 생존했습니다. 높은 난이도에서도 침착하게 버틴 기록입니다.",
        "weakness": "장기 생존 구간에서는 작은 실수 하나가 바로 사망으로 이어집니다.",
        "recommendation": "중앙 유지, 짧은 대각선 이동, 웨이브 빈틈 선점을 계속 연습하면 기록을 더 늘릴 수 있습니다.",
    }


class Handler(BaseHTTPRequestHandler):
    def do_OPTIONS(self) -> None:
        self.send_json({}, status=204)

    def do_GET(self) -> None:
        if self.path == "/api/health":
            self.send_json({"status": "ok"})
            return

        self.send_json({"message": "not found"}, status=404)

    def do_POST(self) -> None:
        if self.path != "/api/analyze":
            self.send_json({"message": "not found"}, status=404)
            return

        try:
            length = int(self.headers.get("Content-Length", "0"))
            body = self.rfile.read(length).decode("utf-8")
            payload = json.loads(body or "{}")
            feedback = build_feedback(
                float(payload["survival_time_seconds"]),
                int(payload["level_reached"]),
            )
            self.send_json(feedback)
        except (KeyError, TypeError, ValueError, json.JSONDecodeError):
            self.send_json({"message": "invalid request"}, status=400)

    def send_json(self, payload: dict, status: int = 200) -> None:
        body = json.dumps(payload, ensure_ascii=False).encode("utf-8")
        self.send_response(status)
        self.send_header("Access-Control-Allow-Origin", "*")
        self.send_header("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
        self.send_header("Access-Control-Allow-Headers", "Content-Type")
        self.send_header("Content-Type", "application/json; charset=utf-8")
        self.send_header("Content-Length", str(len(body)))
        self.end_headers()
        if status != 204:
            self.wfile.write(body)

    def log_message(self, format: str, *args) -> None:
        return


if __name__ == "__main__":
    server = ThreadingHTTPServer((HOST, PORT), Handler)
    print(f"AI local server running at http://{HOST}:{PORT}")
    server.serve_forever()
