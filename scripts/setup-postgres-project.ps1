$ErrorActionPreference = "Stop"

$serviceName = "postgresql-x64-17"
$pgBin = "C:\Program Files\PostgreSQL\17\bin"
$dataPath = "C:\Program Files\PostgreSQL\17\data"
$hbaPath = Join-Path $dataPath "pg_hba.conf"
$backupPath = "$hbaPath.codex.bak"
$projectRoot = Split-Path -Parent $PSScriptRoot
$schemaPath = Join-Path $projectRoot "infra\postgres\init\001_schema.sql"

if (-not (Test-Path $hbaPath)) {
    throw "pg_hba.conf 파일을 찾지 못했습니다: $hbaPath"
}

if (-not (Test-Path $backupPath)) {
    Copy-Item $hbaPath $backupPath -Force
}

$content = Get-Content $hbaPath
$updated = $content | ForEach-Object {
    if ($_ -match "^\s*host\s+all\s+all\s+127\.0\.0\.1/32") {
        "host    all             all             127.0.0.1/32            trust"
    } elseif ($_ -match "^\s*host\s+all\s+all\s+::1/128") {
        "host    all             all             ::1/128                 trust"
    } else {
        $_
    }
}

$updated | Set-Content $hbaPath -Encoding ASCII
Restart-Service $serviceName
Start-Sleep -Seconds 3

$psql = Join-Path $pgBin "psql.exe"
$createdb = Join-Path $pgBin "createdb.exe"

& $psql -U postgres -d postgres -c "ALTER USER postgres WITH PASSWORD 'postgres1234';"
& $psql -U postgres -d postgres -c "DO `$`$ BEGIN IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'sideproject') THEN CREATE USER sideproject WITH PASSWORD 'sideproject1234'; ELSE ALTER USER sideproject WITH PASSWORD 'sideproject1234'; END IF; END `$`$;"

$dbExists = & $psql -U postgres -d postgres -tAc "SELECT 1 FROM pg_database WHERE datname = 'sideproject_game';"
if ($dbExists -ne "1") {
    & $createdb -U postgres -O sideproject sideproject_game
}

& $psql -U sideproject -d sideproject_game -f $schemaPath

Copy-Item $backupPath $hbaPath -Force
Restart-Service $serviceName

Write-Host "PostgreSQL setup complete."
Write-Host "postgres password: postgres1234"
Write-Host "project database: sideproject_game"
Write-Host "project user: sideproject / sideproject1234"
