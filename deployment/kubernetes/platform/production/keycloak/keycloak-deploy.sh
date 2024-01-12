#!/bin/bash

set -euo pipefail

echo "\n🗝️  Keycloak deployment started.\n"


# Set the path to Helm executable
helm_executable="/mnt/c/Users/tomer/Downloads/helm-v3.13.2-windows-amd64/windows-amd64/helm.exe"

# Set the path to kubectl executable
kubectl_executable="/mnt/c/Users/tomer/Downloads/kubectl/kubectl.exe"

# Add the directories to the PATH
export PATH="$PATH:/mnt/c/Users/tomer/Downloads/helm-v3.13.2-windows-amd64/windows-amd64:/mnt/c/Users/tomer/Downloads/kubectl"

echo "\n🗝️  Keycloak deployment started.\n"

clientSecret=$(echo $ random | openssl md5 | head -c 29 | cut -c 10-)

echo "Client Secret: $clientSecret"

"$kubectl_executable" apply -f resources/namespace.yml
sed "s/electro-keycloak-secret/$clientSecret/" resources/keycloak-config.yml | "$kubectl_executable" apply -f -

# Use the full path to Helm executable
"$helm_executable" repo add bitnami https://charts.bitnami.com/bitnami
"$helm_executable" repo update
"$helm_executable" upgrade --install electro-keycloak bitnami/keycloak \
  --values values.yml \
  --namespace keycloak-system --version 10.1.8

echo "\n⌛ Waiting for Keycloak to be deployed..."

sleep 15

while [ $($kubectl_executable get pod -l app.kubernetes.io/component=keycloak -n keycloak-system | wc -l) -eq 0 ] ; do
  sleep 15
done

echo "\n⌛ Waiting for Keycloak to be ready..."

"$kubectl_executable" wait \
  --for=condition=ready pod \
  --selector=app.kubernetes.io/component=keycloak \
  --timeout=600s \
  --namespace=keycloak-system

echo "\n✅  Keycloak cluster has been successfully deployed."

echo "\n🔐 Your Keycloak Admin credentials...\n"

echo "Admin Username: user"
echo "Admin Password: $($kubectl_executable get secret --namespace keycloak-system electro-keycloak -o jsonpath="{.data.admin-password}" | base64 --decode)"

echo "\n🔑 Generating Secret with Keycloak client secret."

"$kubectl_executable" delete secret electro-keycloak-client-credentials || true

"$kubectl_executable" create secret generic electro-keycloak-client-credentials \
    --from-literal=spring.security.oauth2.client.registration.keycloak.client-secret="$clientSecret"

echo "\n🍃 A 'electro-keycloak-client-credentials' has been created for Spring Boot applications to interact with Keycloak."

echo "\n🗝️  Keycloak deployment completed.\n"
