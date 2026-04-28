CREATE TABLE IF NOT EXISTS users (
  id BIGSERIAL PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password_hash VARCHAR(128) NOT NULL DEFAULT '',
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS game_runs (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
  guest_name VARCHAR(50),
  survival_time_seconds NUMERIC(8, 2) NOT NULL,
  level_reached INTEGER NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  CONSTRAINT game_runs_player_check CHECK (user_id IS NOT NULL OR guest_name IS NOT NULL)
);

CREATE INDEX IF NOT EXISTS idx_game_runs_survival_time
  ON game_runs (survival_time_seconds DESC, created_at ASC);

CREATE UNIQUE INDEX IF NOT EXISTS idx_game_runs_unique_user
  ON game_runs (user_id)
  WHERE user_id IS NOT NULL;

CREATE TABLE IF NOT EXISTS ai_feedback (
  id BIGSERIAL PRIMARY KEY,
  game_run_id BIGINT NOT NULL REFERENCES game_runs(id) ON DELETE CASCADE,
  summary TEXT NOT NULL,
  weakness TEXT NOT NULL,
  recommendation TEXT NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

INSERT INTO users (username)
VALUES ('demo')
ON CONFLICT (username) DO NOTHING;
