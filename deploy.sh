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

changed() { [ "$FORCE" = "--force" ] || ! git diff --quiet "$OLD" "$NEW" -- "$1"; }

if changed equipment-maintenance-control-backend; then
  echo ">>> Backend: reconstruindo imagem e recriando o container"
  cd "$BACK_DIR"
  docker compose up -d --build

  echo ">>> Aguardando a API responder em $HEALTH_URL"
  for i in $(seq 1 40); do
    if curl -fsS -o /dev/null "$HEALTH_URL"; then
      echo "API no ar."
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