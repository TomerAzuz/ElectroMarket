set -euo pipefail

echo "\n📦 Installing cert-manager..."

kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.13.3/cert-manager.yaml

echo "\n📦 Installation completed.\n"