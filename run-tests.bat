@echo off
echo ========================================
echo  Ejecutando Pruebas Unitarias
echo ========================================
echo.

echo [1/3] Limpiando proyecto...
call mvnw.cmd clean

echo.
echo [2/3] Ejecutando pruebas y generando reporte de cobertura...
call mvnw.cmd test jacoco:report

echo.
echo [3/3] Abriendo reporte de cobertura en el navegador...
timeout /t 2 /nobreak > nul
start target\site\jacoco\index.html

echo.
echo ========================================
echo  Pruebas completadas!
echo ========================================
echo.
echo El reporte de cobertura se ha abierto en tu navegador.
echo Archivo: target\site\jacoco\index.html
echo.
pause

