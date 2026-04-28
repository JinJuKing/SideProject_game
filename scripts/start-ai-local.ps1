$ErrorActionPreference = "Stop"

$python = $env:PYTHON
if (-not $python) {
    $pythonCommand = Get-Command python -ErrorAction SilentlyContinue
    if ($pythonCommand) {
        $python = $pythonCommand.Source
    }
}

if (-not $python) {
    $pyCommand = Get-Command py -ErrorAction SilentlyContinue
    if ($pyCommand) {
        $python = $pyCommand.Source
    }
}

if (-not $python) {
    throw "Python 실행 파일을 찾지 못했습니다. Python을 PATH에 추가하거나 PYTHON 환경 변수를 설정하세요."
}

$projectRoot = Split-Path -Parent $PSScriptRoot
$aiPath = Join-Path $projectRoot "ai-python"

Set-Location $aiPath
& $python local_server.py
