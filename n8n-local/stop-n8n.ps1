# Script para detener N8N local
# ==============================

Write-Host "🛑 Deteniendo N8N local..." -ForegroundColor Yellow

# Detener contenedores
docker-compose down

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ N8N detenido exitosamente" -ForegroundColor Green
} else {
    Write-Host "❌ Error al detener N8N" -ForegroundColor Red
}

Write-Host ""
Write-Host "📊 Estado de contenedores Docker:" -ForegroundColor Cyan
docker ps -a --filter "name=n8n" 