# Script para iniciar N8N local
# ===============================

Write-Host "🚀 Iniciando N8N local para HealLink..." -ForegroundColor Green
Write-Host ""

# Verificar si Docker está ejecutándose
try {
    docker --version | Out-Null
    Write-Host "✅ Docker encontrado" -ForegroundColor Green
} catch {
    Write-Host "❌ Docker no está instalado o no está ejecutándose" -ForegroundColor Red
    Write-Host "   Por favor instala Docker Desktop desde: https://www.docker.com/products/docker-desktop"
    exit 1
}

# Verificar si Docker está ejecutándose
try {
    docker info | Out-Null
    Write-Host "✅ Docker está ejecutándose" -ForegroundColor Green
} catch {
    Write-Host "❌ Docker no está ejecutándose" -ForegroundColor Red
    Write-Host "   Por favor inicia Docker Desktop"
    exit 1
}

Write-Host ""
Write-Host "📦 Iniciando contenedores N8N..." -ForegroundColor Yellow

# Iniciar N8N con Docker Compose
docker-compose up -d

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "🎉 N8N iniciado exitosamente!" -ForegroundColor Green
    Write-Host ""
    Write-Host "📋 Información de acceso:" -ForegroundColor Cyan
    Write-Host "   URL: http://localhost:5678" -ForegroundColor White
    Write-Host "   Usuario: admin (primera vez que accedas)" -ForegroundColor White
    Write-Host ""
    Write-Host "🔗 Configuración para Spring Boot:" -ForegroundColor Cyan
    Write-Host "   base-url: http://localhost:5678/webhook" -ForegroundColor White
    Write-Host ""
    Write-Host "📁 Workflows guardados en: ./workflows" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "⚠️  IMPORTANTE: La primera vez debes crear un usuario admin" -ForegroundColor Yellow
    Write-Host "   Abre http://localhost:5678 en tu navegador" -ForegroundColor White
    Write-Host ""
    
    # Esperar a que N8N esté listo
    Write-Host "⏳ Esperando que N8N esté listo..." -ForegroundColor Yellow
    $timeout = 30
    $counter = 0
    
    do {
        Start-Sleep -Seconds 2
        $counter += 2
        try {
            $response = Invoke-WebRequest -Uri "http://localhost:5678" -TimeoutSec 5 -ErrorAction SilentlyContinue
            if ($response.StatusCode -eq 200) {
                Write-Host "✅ N8N está listo!" -ForegroundColor Green
                break
            }
        } catch {
            # Continuar esperando
        }
        
        if ($counter -ge $timeout) {
            Write-Host "⚠️  N8N está tardando en iniciarse, pero probablemente esté funcionando" -ForegroundColor Yellow
            break
        }
    } while ($true)
    
    Write-Host ""
    Write-Host "🌐 Abriendo navegador..." -ForegroundColor Green
    Start-Process "http://localhost:5678"
    
} else {
    Write-Host ""
    Write-Host "❌ Error al iniciar N8N" -ForegroundColor Red
    Write-Host "   Revisa los logs con: docker-compose logs n8n" -ForegroundColor White
} 