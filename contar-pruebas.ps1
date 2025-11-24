$total = 0
$failures = 0
$errors = 0

Get-ChildItem "target\surefire-reports\*.txt" | ForEach-Object {
    $content = Get-Content $_.FullName
    $line = $content | Select-String "Tests run:"
    if ($line) {
        if ($line -match "Tests run: (\d+), Failures: (\d+), Errors: (\d+)") {
            $total += [int]$matches[1]
            $failures += [int]$matches[2]
            $errors += [int]$matches[3]
        }
    }
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host " RESULTADO DE LAS PRUEBAS" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Tests ejecutados: $total" -ForegroundColor Green
Write-Host "Failures: $failures" -ForegroundColor $(if ($failures -eq 0) { "Green" } else { "Red" })
Write-Host "Errors: $errors" -ForegroundColor $(if ($errors -eq 0) { "Green" } else { "Red" })
Write-Host "========================================" -ForegroundColor Cyan

if ($failures -eq 0 -and $errors -eq 0) {
    Write-Host "✓ TODAS LAS PRUEBAS PASARON" -ForegroundColor Green
} else {
    Write-Host "✗ HAY PRUEBAS FALLIDAS" -ForegroundColor Red
}

