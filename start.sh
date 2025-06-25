#!/bin/bash

# Carregar variáveis do .env
export $(grep -v '^#' .env | xargs)

# Rodar a aplicação com Maven
mvn spring-boot:run
