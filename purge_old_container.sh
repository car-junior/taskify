#!/bin/bash

container_name="taskify"

# Verifica se existe alguma container com o nome informado
if docker inspect "$container_name" &> /dev/null; then

    # Verifica se existe um container em execução com o nome informado
    if docker ps -f name="$container_name" --format '{{.Names}}' | grep -q "$container_name"; then
      echo "Parando container $container_name..."
      docker stop "$container_name"
      echo "Container parado com sucesso!"
    fi

    echo "Removendo container $container_name..."
    docker rm "$container_name"
    echo "Container removido com sucesso!"
fi