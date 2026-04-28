const canvas = document.querySelector("#game");
const ctx = canvas.getContext("2d");
const timeEl = document.querySelector("#time");
const bestEl = document.querySelector("#best");
const levelEl = document.querySelector("#level");
const startButton = document.querySelector("#startButton");
const messageEl = document.querySelector("#message");

const keys = new Set();
const bestKey = "sideproject-game-best-time";

let bestTime = Number(localStorage.getItem(bestKey) || 0);
let state = createInitialState();
let lastFrame = performance.now();

bestEl.textContent = bestTime.toFixed(1);
drawStartScreen();

function createInitialState() {
  return {
    running: false,
    gameOver: false,
    elapsed: 0,
    spawnTimer: 0,
    waveTimer: 4,
    projectiles: [],
    player: {
      x: canvas.width / 2,
      y: canvas.height / 2,
      radius: 13,
      speed: 250,
    },
  };
}

function startGame() {
  state = createInitialState();
  state.running = true;
  lastFrame = performance.now();
  startButton.textContent = "Restart";
  messageEl.textContent = "움직임을 크게 가져가되, 벽에 몰리지 않는 게 핵심입니다.";
  requestAnimationFrame(tick);
}

function tick(now) {
  const dt = Math.min((now - lastFrame) / 1000, 0.033);
  lastFrame = now;

  if (state.running) {
    update(dt);
    draw();
    requestAnimationFrame(tick);
  }
}

function update(dt) {
  state.elapsed += dt;
  const level = getLevel();
  updatePlayer(dt);
  updateProjectiles(dt);

  state.spawnTimer -= dt;
  if (state.spawnTimer <= 0) {
    spawnProjectile(level);
    if (level >= 2 && Math.random() < 0.45) {
      spawnProjectile(level);
    }
    if (level >= 5 && Math.random() < 0.25) {
      spawnProjectile(level);
    }
    state.spawnTimer = Math.max(0.09, 0.46 - level * 0.035);
  }

  state.waveTimer -= dt;
  if (state.waveTimer <= 0) {
    spawnWave(level);
    state.waveTimer = Math.max(2.2, 5.2 - level * 0.22);
  }

  if (state.projectiles.some((p) => isColliding(state.player, p))) {
    endGame();
  }

  timeEl.textContent = state.elapsed.toFixed(1);
  levelEl.textContent = String(level);
}

function updatePlayer(dt) {
  const player = state.player;
  let dx = 0;
  let dy = 0;

  if (keys.has("w")) dy -= 1;
  if (keys.has("s")) dy += 1;
  if (keys.has("a")) dx -= 1;
  if (keys.has("d")) dx += 1;

  if (dx !== 0 || dy !== 0) {
    const length = Math.hypot(dx, dy);
    player.x += (dx / length) * player.speed * dt;
    player.y += (dy / length) * player.speed * dt;
  }

  player.x = clamp(player.x, player.radius, canvas.width - player.radius);
  player.y = clamp(player.y, player.radius, canvas.height - player.radius);
}

function updateProjectiles(dt) {
  for (const projectile of state.projectiles) {
    projectile.x += projectile.vx * dt;
    projectile.y += projectile.vy * dt;
  }

  state.projectiles = state.projectiles.filter((p) => {
    const margin = 80;
    return (
      p.x > -margin &&
      p.x < canvas.width + margin &&
      p.y > -margin &&
      p.y < canvas.height + margin
    );
  });
}

function spawnProjectile(level) {
  const side = Math.floor(Math.random() * 4);
  const radius = randomBetween(8, 17);
  const speed = randomBetween(205, 305) + level * 34;
  const aimScatter = Math.max(22, 90 - level * 7);
  const targetX = state.player.x + randomBetween(-aimScatter, aimScatter);
  const targetY = state.player.y + randomBetween(-aimScatter, aimScatter);
  let x = 0;
  let y = 0;

  if (side === 0) {
    x = randomBetween(0, canvas.width);
    y = -radius;
  } else if (side === 1) {
    x = canvas.width + radius;
    y = randomBetween(0, canvas.height);
  } else if (side === 2) {
    x = randomBetween(0, canvas.width);
    y = canvas.height + radius;
  } else {
    x = -radius;
    y = randomBetween(0, canvas.height);
  }

  const angle = Math.atan2(targetY - y, targetX - x);
  state.projectiles.push({
    x,
    y,
    radius,
    vx: Math.cos(angle) * speed,
    vy: Math.sin(angle) * speed,
  });
}

function spawnWave(level) {
  const horizontal = Math.random() < 0.5;
  const count = Math.min(12, 4 + level);
  const gapIndex = Math.floor(randomBetween(0, count));
  const reverse = Math.random() < 0.5;
  const speed = 190 + level * 30;

  for (let i = 0; i < count; i += 1) {
    if (i === gapIndex || (level < 4 && Math.abs(i - gapIndex) === 1)) {
      continue;
    }

    if (horizontal) {
      const y = ((i + 0.5) / count) * canvas.height;
      state.projectiles.push({
        x: reverse ? canvas.width + 16 : -16,
        y,
        radius: 11,
        vx: reverse ? -speed : speed,
        vy: randomBetween(-20, 20),
      });
    } else {
      const x = ((i + 0.5) / count) * canvas.width;
      state.projectiles.push({
        x,
        y: reverse ? canvas.height + 16 : -16,
        radius: 11,
        vx: randomBetween(-20, 20),
        vy: reverse ? -speed : speed,
      });
    }
  }
}

function draw() {
  ctx.clearRect(0, 0, canvas.width, canvas.height);
  drawGrid();

  for (const projectile of state.projectiles) {
    ctx.beginPath();
    ctx.fillStyle = "#ff5b73";
    ctx.shadowColor = "#ff5b73";
    ctx.shadowBlur = 10;
    ctx.arc(projectile.x, projectile.y, projectile.radius, 0, Math.PI * 2);
    ctx.fill();
  }

  ctx.shadowBlur = 0;
  ctx.beginPath();
  ctx.fillStyle = "#8bf36f";
  ctx.arc(state.player.x, state.player.y, state.player.radius, 0, Math.PI * 2);
  ctx.fill();

  ctx.beginPath();
  ctx.strokeStyle = "#d9ffd3";
  ctx.lineWidth = 3;
  ctx.arc(state.player.x, state.player.y, state.player.radius + 5, 0, Math.PI * 2);
  ctx.stroke();
}

function drawGrid() {
  ctx.fillStyle = "#080a0f";
  ctx.fillRect(0, 0, canvas.width, canvas.height);
  ctx.strokeStyle = "#151b24";
  ctx.lineWidth = 1;

  for (let x = 0; x <= canvas.width; x += 48) {
    ctx.beginPath();
    ctx.moveTo(x, 0);
    ctx.lineTo(x, canvas.height);
    ctx.stroke();
  }

  for (let y = 0; y <= canvas.height; y += 48) {
    ctx.beginPath();
    ctx.moveTo(0, y);
    ctx.lineTo(canvas.width, y);
    ctx.stroke();
  }
}

function drawStartScreen() {
  drawGrid();
  ctx.fillStyle = "#f3f7fb";
  ctx.font = "700 34px Arial";
  ctx.textAlign = "center";
  ctx.fillText("SideProject Survival", canvas.width / 2, canvas.height / 2 - 12);
  ctx.fillStyle = "#9aa7b7";
  ctx.font = "18px Arial";
  ctx.fillText("Start를 누르고 WASD로 피하세요.", canvas.width / 2, canvas.height / 2 + 28);
}

function endGame() {
  state.running = false;
  state.gameOver = true;

  if (state.elapsed > bestTime) {
    bestTime = state.elapsed;
    localStorage.setItem(bestKey, String(bestTime));
    bestEl.textContent = bestTime.toFixed(1);
  }

  draw();
  ctx.fillStyle = "rgba(0, 0, 0, 0.58)";
  ctx.fillRect(0, 0, canvas.width, canvas.height);
  ctx.fillStyle = "#f3f7fb";
  ctx.font = "700 38px Arial";
  ctx.textAlign = "center";
  ctx.fillText("Game Over", canvas.width / 2, canvas.height / 2 - 12);
  ctx.fillStyle = "#9aa7b7";
  ctx.font = "18px Arial";
  ctx.fillText(`${state.elapsed.toFixed(1)}초 생존`, canvas.width / 2, canvas.height / 2 + 28);
  messageEl.textContent = "다시 시작해서 최고 기록을 갱신해보세요.";
}

function getLevel() {
  return Math.floor(state.elapsed / 6) + 1;
}

function isColliding(a, b) {
  return Math.hypot(a.x - b.x, a.y - b.y) < a.radius + b.radius;
}

function randomBetween(min, max) {
  return Math.random() * (max - min) + min;
}

function clamp(value, min, max) {
  return Math.max(min, Math.min(max, value));
}

window.addEventListener("keydown", (event) => {
  const key = event.key.toLowerCase();
  if (["w", "a", "s", "d"].includes(key)) {
    keys.add(key);
    event.preventDefault();
  }
});

window.addEventListener("keyup", (event) => {
  keys.delete(event.key.toLowerCase());
});

startButton.addEventListener("click", startGame);
