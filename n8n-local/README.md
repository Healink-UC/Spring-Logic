# N8N Local - HealLink Project

Este directorio contiene la configuración para ejecutar N8N localmente como parte del proyecto HealLink.

## 🚀 Requisitos Previos

- **Docker Desktop** instalado y ejecutándose
- Puerto 5678 disponible

## 📦 Instalación y Configuración

### 1. Verificar Docker
```powershell
docker --version
docker info
```

### 2. Iniciar N8N
```powershell
# Opción A: Usar el script
.\start-n8n.ps1

# Opción B: Manual
docker-compose up -d
```

### 3. Configuración inicial
1. Abre http://localhost:5678 en tu navegador
2. Crea un usuario administrador (primera vez)
3. ¡Ya puedes usar N8N!

## 🔗 Configuración para Spring Boot

Actualiza el archivo `Spring-Logic/src/main/resources/application.yml`:

```yaml
n8n:
  webhook:
    base-url: http://localhost:5678/webhook
    api-key: ""  # No necesario para instalación local
```

## 📁 Estructura de Archivos

```
n8n-local/
├── docker-compose.yml      # Configuración de Docker
├── start-n8n.ps1          # Script de inicio
├── stop-n8n.ps1           # Script de parada
├── workflows/             # Workflows exportados
└── README.md              # Este archivo
```

## 🔧 Comandos Útiles

### Gestión de contenedores
```powershell
# Iniciar N8N
.\start-n8n.ps1

# Detener N8N
.\stop-n8n.ps1

# Ver logs
docker-compose logs n8n

# Ver logs en tiempo real
docker-compose logs -f n8n

# Reiniciar N8N
docker-compose restart n8n
```

### Información del sistema
```powershell
# Estado de contenedores
docker-compose ps

# Uso de recursos
docker stats n8n-healink

# Acceder al contenedor
docker exec -it n8n-healink sh
```

## 🌐 URLs Importantes

- **Interfaz N8N**: http://localhost:5678
- **Webhooks**: http://localhost:5678/webhook/[nombre-del-webhook]
- **API N8N**: http://localhost:5678/rest (si se habilita)

## 📊 Configuración del Webhook para el Proyecto

### En N8N:
1. Crear un workflow nuevo
2. Agregar nodo "Webhook"
3. Configurar:
   - **HTTP Method**: POST
   - **Path**: `orquestador-seguimientos`
   - **Respond**: Immediately
   - **Authentication**: None

### En Spring Boot:
La URL del webhook será:
```
http://localhost:5678/webhook/orquestador-seguimientos
```

## 🔒 Backup y Restauración

### Exportar workflows
Los workflows se guardan automáticamente en `./workflows/`

### Backup completo
```powershell
# Crear backup de datos
docker-compose exec n8n tar -czf /tmp/n8n-backup.tar.gz /home/node/.n8n
docker cp n8n-healink:/tmp/n8n-backup.tar.gz ./backup/
```

## 🐛 Solución de Problemas

### N8N no inicia
1. Verificar que Docker esté ejecutándose
2. Verificar que el puerto 5678 esté libre
3. Revisar logs: `docker-compose logs n8n`

### Error de permisos
```powershell
# En Windows, dar permisos a la carpeta
icacls . /grant Everyone:F /T
```

### Limpiar instalación
```powershell
# Detener y eliminar todo
docker-compose down -v
docker volume rm n8n-local_n8n_data
```

## 🔄 Integración con Spring Boot

Una vez configurado, actualiza Spring Boot y prueba la conexión:

```powershell
# En el directorio Spring-Logic
curl -X POST http://localhost:8090/api/test/n8n/ping
```

## 📝 Notas

- **Datos persistentes**: Los datos de N8N se guardan en un volumen Docker
- **Workflows**: Se sincronizan con la carpeta `./workflows/`
- **Logs**: Disponibles con `docker-compose logs n8n`
- **Puerto**: N8N corre en http://localhost:5678
- **Timezone**: Configurado para America/Bogota 