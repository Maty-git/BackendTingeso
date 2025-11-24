#!/bin/bash

echo "========================================"
echo " Ejecutando Pruebas Unitarias"
echo "========================================"
echo ""

echo "[1/3] Limpiando proyecto..."
./mvnw clean

echo ""
echo "[2/3] Ejecutando pruebas y generando reporte de cobertura..."
./mvnw test jacoco:report

echo ""
echo "[3/3] Abriendo reporte de cobertura en el navegador..."
sleep 2

# Detectar sistema operativo y abrir el reporte
if [[ "$OSTYPE" == "linux-gnu"* ]]; then
    xdg-open target/site/jacoco/index.html
elif [[ "$OSTYPE" == "darwin"* ]]; then
    open target/site/jacoco/index.html
fi

echo ""
echo "========================================"
echo " Pruebas completadas!"
echo "========================================"
echo ""
echo "El reporte de cobertura se ha abierto en tu navegador."
echo "Archivo: target/site/jacoco/index.html"
echo ""

