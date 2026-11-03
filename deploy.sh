#!/usr/bin/env bash
# Deploy automatico: baixa a main e refaz so o que mudou (backend e/ou front).
# Executado na VPS pelo GitHub Actions (.github/workflows/deploy.yml).
# Uso manual:  ./deploy.sh          (so o que mudou)
#              ./deploy.sh --force  (refaz backend e front)
set -euo pipefail

REPO_DIR="${REPO_DIR:-$HOME/equipment-maintenance-control}"
BACK_DIR="$REPO_DIR/equipment-maintenance-control-backend"
FRONT_DIR="$REPO_DIR/equipment-maintenance-control-frontend"
WEB_DIR="${WEB_DIR:-/var/www/emc-front}"
HEALTH_URL="${HEALTH_URL:-http://127.0.0.1:8080/equipment-type}"
FORCE="${1:-}"

# Impede dois deploys ao mesmo tempo (dois pushes seguidos)
exec 9>/tmp/emc-deploy.lock
flock -n 9 || { echo "Ja existe um deploy em andamento."; exit 1; }

cd "$REPO_DIR"
OLD=$(git rev-parse HEAD)
git fetch --quiet origin main
git checkout --quiet main
# Descarta qualquer alteracao local nos arquivos versionados. O .env nao e afetado (esta no .gitignore).
git reset --hard --quiet origin/main
NEW=$(git rev-parse HEAD)
echo "Commit: ${OLD:0:7} -> ${NEW:0:7}"

node_ok() {
  command -v node >/dev/null 2>&1 || return 1
  local major minor patch
  IFS=. read -r major minor patch <<< "$(node -p 'process.versions.node')"
  if [ "$major" -ge 26 ]; then return 0; fi
  if [ "$major" -eq 24 ]; then [ "$minor" -gt 15 ] || { [ "$minor" -eq 15 ] && [ "$patch" -ge 0 ]; }; return; fi
  if [ "$major" -eq 22 ]; then [ "$minor" -gt 22 ] || { [ "$minor" -eq 22 ] && [ "$patch" -ge 3 ]; }; return; fi
  return 1
}

ensure_node() {
  export NVM_DIR="${NVM_DIR:-$HOME/.nvm}"
  if [ -s "$NVM_DIR/nvm.sh" ]; then
    set +u
    . "$NVM_DIR/nvm.sh"
    set -u
  fi
  if node_ok; then return 0; fi
  if command -v nvm >/dev/null 2>&1; then
    echo ">>> Node $(node -v 2>/dev/null || echo ausente) incompativel com o Angular CLI; instalando Node 24 via nvm"
    set +u
    nvm install 24 >/dev/null
    nvm use 24 >/dev/null
    set -u
  fi
  if ! node_ok; then
    echo "ERRO: o Angular CLI exige Node 22.22.3+, 24.15.0+ ou 26+. Versao encontrada: $(node -v 2>/dev/null || echo nenhuma)."
    echo "Instale um Node compativel na VPS (nvm ou NodeSource) e rode o deploy de novo."
    exit 1
  fi
  echo ">>> Usando Node $(node -v)"
}

changed() { [ "$FORCE" = "--force" ] || ! git diff --quiet "$OLD" "$NEW" -- "$1"; }

if changed equipment-maintenance-control-frontend; then
  ensure_node
fi

if changed equipment-maintenance-control-backend; then
  echo ">>> Backend: reconstruindo imagem e recriando o container"
  cd "$BACK_DIR"
  docker compose up -d --build

  echo ">>> Aguardando a API responder em $HEALTH_URL"
  for i in $(seq 1 40); do
    # Qualquer resposta HTTP (200, 401...) prova que a API subiu; 000 = sem conexao.
    CODE=$(curl -s -o /dev/null -w '%{http_code}' "$HEALTH_URL" || true)
    if [ "$CODE" != "000" ]; then
      echo "API no ar (HTTP $CODE)."
      break
    fi
    if [ "$i" = 40 ]; then
      echo "ERRO: a API nao respondeu em 200s. Ultimos logs:"
      docker compose logs --tail 40 backend
      exit 1
    fi
    sleep 5
  done
  docker image prune -f >/dev/null
else
  echo ">>> Backend: sem mudancas, pulando"
fi

if changed equipment-maintenance-control-frontend; then
  echo ">>> Frontend: build e publicacao"
  cd "$FRONT_DIR"
  npm ci --no-audit --no-fund
  npm run build
  rsync -a --delete dist/equipment-maintenance-control-frontend/browser/ "$WEB_DIR/"
else
  echo ">>> Frontend: sem mudancas, pulando"
fi

echo "Deploy concluido."