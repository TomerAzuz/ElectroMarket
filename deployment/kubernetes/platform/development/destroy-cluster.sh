#!/bin/sh

echo "\n🏴️ Destroying Kubernetes cluster...\n"

minikube stop --profile electro

minikube delete --profile electro

echo "\n🏴️ Cluster destroyed\n"