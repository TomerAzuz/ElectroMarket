#!/bin/sh

set -euo pipefail

echo "\n📦 Deploying Electro UI..."

kubectl apply -f resources

echo "⌛ Waiting for Electro UI to be deployed..."

while [ $(kubectl get pod -l app=electro-ui | wc -l) -eq 0 ] ; do
  sleep 5
done

echo "\n⌛ Waiting for Electro UI to be ready..."

kubectl wait \
  --for=condition=ready pod \
  --selector=app=electro-ui \
  --timeout=180s

echo "\n📦 Electro UI deployment completed.\n"