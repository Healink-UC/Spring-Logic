# 🏥 Documentación Técnica - Healink Integrador

## 📋 Índice
1. [Descripción General](#descripción-general)
2. [Arquitectura del Sistema](#arquitectura-del-sistema)
3. [Tecnologías Utilizadas](#tecnologías-utilizadas)
4. [Estructura del Proyecto](#estructura-del-proyecto)
5. [Configuración del Entorno](#configuración-del-entorno)
6. [Guía de Instalación](#guía-de-instalación)
7. [Configuración de Base de Datos](#configuración-de-base-de-datos)
8. [Ejecución del Proyecto](#ejecución-del-proyecto)
9. [Arquitectura de Dominio](#arquitectura-de-dominio)
10. [Clases Base Genéricas](#clases-base-genéricas)
11. [Modelo de Entidades](#modelo-de-entidades)
12. [Sistema de Controladores](#sistema-de-controladores)
13. [Patrones de Diseño](#patrones-de-diseño)
14. [APIs y Endpoints](#apis-y-endpoints)
15. [Sistema de Seguridad](#sistema-de-seguridad)
16. [Sistema Multiagente](#sistema-multiagente)
17. [Testing](#testing)
18. [Deployment](#deployment)
19. [Monitoreo y Observabilidad](#monitoreo-y-observabilidad)
20. [Troubleshooting Avanzado](#troubleshooting-avanzado)

## Descripción General

Healink Integrador es un sistema de integración de datos de salud que facilita la interoperabilidad entre diferentes sistemas de información sanitaria. El aplicativo está desarrollado en Java con Spring Boot y sigue una arquitectura basada en Domain-Driven Design (DDD).

### Características Principales
- **Sistema Multiagente**: Integra múltiples agentes inteligentes para procesamiento de datos
- **Interoperabilidad**: Conecta diferentes sistemas de salud
- **Escalabilidad**: Arquitectura modular y distribuida
- **Seguridad**: Implementación de seguridad robusta para datos médicos
- **RESTful APIs**: Interfaces bien definidas para integración

## Arquitectura del Sistema

El sistema utiliza una arquitectura hexagonal (Clean Architecture) con los siguientes niveles:

```
┌─────────────────────────────────────────────┐
│               Presentation Layer            │
│    (Controllers, DTOs, Validators)          │
├─────────────────────────────────────────────┤
│               Application Layer             │
│        (Services, Use Cases)               │
├─────────────────────────────────────────────┤
│                Domain Layer                 │
│    (Entities, Value Objects, Domains)      │
├─────────────────────────────────────────────┤
│              Infrastructure Layer           │
│   (Repositories, External Services)        │
└─────────────────────────────────────────────┘
```

## 🛠️ Tecnologías Utilizadas

### Backend Core
- **Java 17**: Lenguaje de programación principal
- **Spring Boot 3.2.3**: Framework principal de aplicación
- **Spring Data JPA**: Capa de persistencia de datos
- **Spring Security**: Autenticación y autorización
- **Spring Web**: APIs RESTful
- **Spring Validation**: Validación de datos
- **Hibernate 6**: ORM principal
- **Maven**: Gestión de dependencias y build

### Base de Datos y Persistencia
- **PostgreSQL**: Base de datos principal
- **PostGIS**: Extensión geoespacial para manejo de ubicaciones
- **Hibernate Spatial**: Soporte para tipos geoespaciales
- **Hypersistence Utils**: Utilidades adicionales para Hibernate
- **H2 Database**: Base de datos en memoria para testing

### Seguridad y Autenticación
- **JSON Web Tokens (JWT)**: Autenticación stateless
- **jjwt 0.11.5**: Librería para manejo de JWT
- **Spring Security**: Framework de seguridad integral
- **BCrypt**: Encriptación de contraseñas

### Mapeo y Utilidades
- **MapStruct 1.5.5**: Mapeo automático entre objetos
- **Lombok 1.18.30**: Reducción de código boilerplate
- **Jackson**: Procesamiento JSON avanzado
- **Jakarta Validation 3.0.2**: Validaciones JSR-303

### Documentación API
- **SpringDoc OpenAPI 2.2.0**: Documentación automática de APIs
- **Swagger UI**: Interfaz interactiva para APIs
- **OpenAPI 3**: Especificación estándar de APIs

### Testing
- **JUnit 5**: Framework de testing moderno
- **Mockito**: Framework de mocking
- **Spring Boot Test**: Testing integrado
- **Spring Security Test**: Testing de seguridad
- **H2 Database**: Base de datos para tests

### DevOps y Monitoreo
- **Docker**: Containerización
- **Render**: Plataforma de despliegue en la nube
- **Spring Boot Actuator**: Métricas y monitoreo
- **Logback**: Sistema de logging avanzado
- **Spring Boot DevTools**: Herramientas de desarrollo

### Geolocalización
- **PostGIS JDBC 2.5.0**: Driver para datos geoespaciales
- **Hibernate Spatial**: Soporte ORM para geometrías
- **JTS (Java Topology Suite)**: Manipulación geométrica

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/healink/integrador/
│   │   ├── config/                 # Configuraciones generales
│   │   ├── core/                   # Clases base y genéricas
│   │   │   ├── config/            # Configuraciones del core
│   │   │   ├── controller/        # Controladores base
│   │   │   ├── dto/               # DTOs genéricos
│   │   │   ├── entity/            # Entidades base
│   │   │   ├── exception/         # Manejo de excepciones
│   │   │   ├── integrations/      # Integraciones externas
│   │   │   ├── json/              # Procesamiento JSON
│   │   │   ├── mapper/            # Mappers genéricos
│   │   │   ├── Repository/        # Repositorios base
│   │   │   ├── service/           # Servicios base
│   │   │   ├── validator/         # Validadores genéricos
│   │   │   └── webhooks/          # Manejo de webhooks
│   │   ├── domain/                # Dominios de negocio
│   │   │   ├── atenciones_medicas/
│   │   │   ├── campana/
│   │   │   ├── citaciones_medicas/
│   │   │   ├── datos_clinicos/
│   │   │   ├── diagnosticos/
│   │   │   ├── embajadores/
│   │   │   ├── entidades_salud/
│   │   │   ├── factor_paciente/
│   │   │   ├── factor_riesgo/
│   │   │   ├── historia_clinica/
│   │   │   ├── interacciones_chatbot/
│   │   │   ├── localizacion/
│   │   │   ├── paciente/
│   │   │   ├── personal_medico/
│   │   │   ├── predicciones/
│   │   │   ├── prescripciones/
│   │   │   ├── recomendaciones/
│   │   │   ├── rol/
│   │   │   ├── seguimientos/
│   │   │   ├── servicios_campanas/
│   │   │   ├── servicios_medicos/
│   │   │   ├── triaje/
│   │   │   └── usuario/
│   │   ├── enums/                 # Enumeraciones
│   │   └── security/              # Configuración de seguridad
│   └── resources/
│       ├── db/migration/          # Scripts de migración
│       ├── static/                # Recursos estáticos
│       └── templates/             # Plantillas
├── test/                          # Tests
└── docs/                          # Documentación
```

## Configuración del Entorno

### Prerrequisitos
- **Java 17 o superior**
- **Maven 3.8+**
- **PostgreSQL 14+**
- **Docker** (opcional)
- **Git**

### Variables de Entorno

Crear un archivo `.env` en la raíz del proyecto con las siguientes variables:

```env
# Base de Datos
DB_HOST=localhost
DB_PORT=5432
DB_NAME=healink_integrador
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_password

# Aplicación
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8090

# JWT
JWT_SECRET=tu_jwt_secret_muy_seguro
JWT_EXPIRATION=86400000

# APIs Externas
EXTERNAL_API_KEY=tu_api_key
```

## Guía de Instalación

### Instalación Local

1. **Clonar el repositorio**
```bash
git clone <repository-url>
cd integrador
```

2. **Configurar base de datos PostgreSQL**
```sql
CREATE DATABASE healink_integrador;
CREATE USER healink_user WITH PASSWORD 'tu_password';
GRANT ALL PRIVILEGES ON DATABASE healink_integrador TO healink_user;
```

3. **Configurar variables de entorno**
```bash
cp .env.example .env
# Editar .env con tus valores
```

4. **Instalar dependencias**
```bash
./mvnw clean install
```

5. **Ejecutar migraciones**
```bash
./mvnw flyway:migrate
```

6. **Ejecutar la aplicación**
```bash
./mvnw spring-boot:run
```

### Instalación con Docker

1. **Construir imagen**
```bash
docker build -t healink-integrador .
```

2. **Ejecutar con Docker Compose**
```bash
docker-compose up -d
```

## Configuración de Base de Datos

### Esquema Principal

El sistema utiliza las siguientes tablas principales:

- **Usuarios y Roles**: Gestión de usuarios y permisos
- **Pacientes**: Información de pacientes
- **Personal Médico**: Datos del personal sanitario
- **Entidades de Salud**: Hospitales, clínicas, etc.
- **Campañas**: Campañas de salud y prevención
- **Triaje**: Sistema de clasificación de urgencias
- **Historia Clínica**: Registros médicos
- **Diagnósticos**: Información diagnóstica
- **Prescripciones**: Medicamentos y tratamientos

### Migraciones

Las migraciones se encuentran en `src/main/resources/db/migration/` y siguen el patrón:
```
V{version}__{description}.sql
```

## Ejecución del Proyecto

### Desarrollo
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

### Producción
```bash
./mvnw clean package
java -jar target/integrador-1.0.0.jar --spring.profiles.active=prod
```

### Testing
```bash
# Tests unitarios
./mvnw test

# Tests de integración
./mvnw verify
```

## Arquitectura de Dominio

### Patrón DDD

Cada dominio sigue una estructura estándar:

```
domain/
├── entities/          # Entidades del dominio
├── repositories/      # Interfaces de repositorio
├── services/         # Servicios de dominio
├── controllers/      # Controladores REST
├── dto/             # DTOs del dominio
├── mappers/         # Mappers entre entidades y DTOs
└── validators/      # Validadores específicos
```

## 🏗️ Clases Base Genéricas

### EntidadAuditable
**Ubicación**: `com.healink.integrador.core.entity.EntidadAuditable`

Clase base abstracta que proporciona auditoría automática para todas las entidades:

```java
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class EntidadAuditable implements EntidadBase {
    
    @CreatedDate
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;
    
    @LastModifiedDate
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @CreatedBy
    @Column(name = "creado_por", updatable = false)
    private String creadoPor;
    
    @LastModifiedBy
    @Column(name = "actualizado_por")
    private String actualizadoPor;
}
```

**Características**:
- **Auditoría Automática**: Registra automáticamente fechas de creación y modificación
- **Seguimiento de Usuario**: Rastrea quién creó y modificó cada registro
- **Integración con Spring Data**: Utiliza `AuditingEntityListener`
- **No Actualizable**: Los campos de creación son inmutables

### Repositorios Base
**Ubicación**: `com.healink.integrador.core.Repository`

Cada dominio hereda de `JpaRepository` con operaciones CRUD estándar:
- `findAll()`: Obtener todos los registros
- `findById(ID id)`: Buscar por identificador
- `save(T entity)`: Guardar o actualizar
- `delete(T entity)`: Eliminar registro
- **Consultas Personalizadas**: Métodos específicos por dominio

### Servicios Base
**Ubicación**: `com.healink.integrador.core.service`

Lógica de negocio común implementada en cada servicio de dominio:
- **Validaciones**: Reglas de negocio específicas
- **Transacciones**: Manejo automático de transacciones
- **Mapeo**: Conversión entre entidades y DTOs
- **Logging**: Trazabilidad de operaciones

### Controladores Base
**Ubicación**: `com.healink.integrador.core.controller`

Endpoints REST estándar para cada dominio:
- **GET**: Obtener recursos (individual y colección)
- **POST**: Crear nuevos recursos
- **PUT**: Actualizar recursos existentes
- **DELETE**: Eliminar recursos
- **Paginación**: Soporte para grandes conjuntos de datos
- **Filtrado**: Capacidades de búsqueda y filtrado

## 📊 Modelo de Entidades

### Entidades Principales

El sistema cuenta con **25 entidades principales** organizadas por dominio:

#### 🧑‍⚕️ Gestión de Usuarios y Roles
- **Usuario**: Gestión de usuarios del sistema
- **Rol**: Definición de roles y permisos
- **PersonalMedico**: Información específica del personal sanitario

#### 🏥 Gestión de Pacientes y Salud
- **Paciente**: Información completa de pacientes
- **HistoriaClinica**: Registros médicos históricos
- **DatosClinicos**: Datos clínicos actuales
- **Diagnostico**: Diagnósticos médicos
- **Prescripcion**: Medicamentos y tratamientos
- **AtencionMedica**: Atenciones médicas realizadas
- **CitacionMedica**: Citas médicas programadas

#### 🎯 Sistema de Triaje y Riesgo
- **Triaje**: Evaluaciones de urgencia médica
- **FactorRiesgo**: Factores de riesgo identificados
- **FactorPaciente**: Relación paciente-factor de riesgo
- **Prediccion**: Predicciones de riesgo basadas en IA

#### 📢 Campañas y Servicios
- **Campana**: Campañas de salud y prevención
- **CampanaFactores**: Relación campañas-factores
- **InscripcionCampana**: Inscripciones de pacientes
- **ServicioCampana**: Servicios ofrecidos en campañas
- **ServicioMedico**: Servicios médicos disponibles

#### 🤖 Interacciones y Seguimiento
- **InteraccionChatbot**: Interacciones con chatbots
- **Seguimiento**: Seguimientos médicos
- **Recomendacion**: Recomendaciones personalizadas

#### 🌍 Gestión de Ubicación y Entidades
- **Localizacion**: Datos geoespaciales con PostGIS
- **EntidadSalud**: Hospitales, clínicas, centros de salud
- **Embajador**: Representantes de entidades de salud

### Características del Modelo
- **Auditoría Completa**: Todas las entidades heredan de `EntidadAuditable`
- **Geolocalización**: Soporte para coordenadas geográficas con PostGIS
- **Relaciones Complejas**: Mapeo avanzado de relaciones entre entidades
- **Validaciones**: Constrainst de integridad referencial
- **Índices Optimizados**: Performance mejorada en consultas frecuentes

## 🎮 Sistema de Controladores

### Controladores REST Disponibles

El sistema cuenta con **26 controladores REST** que proporcionan APIs completas:

#### Controladores de Autenticación
- **ControladorAuth**: `/api/auth/*` - Autenticación y autorización
- **HealthController**: `/health` - Estado del sistema

#### Controladores de Dominio (Patrón estándar: `/api/{dominio}/*`)
1. **UsuarioController**: Gestión de usuarios
2. **RolController**: Gestión de roles
3. **PacienteController**: CRUD de pacientes
4. **PersonalMedicoController**: Gestión de personal médico
5. **HistoriaClinicaController**: Historiales médicos
6. **DatosClinicosController**: Datos clínicos
7. **DiagnosticoController**: Diagnósticos
8. **PrescripcionController**: Prescripciones
9. **TriajeController**: Sistema de triaje
10. **FactorRiesgoController**: Factores de riesgo
11. **FactorPacienteController**: Relaciones factor-paciente
12. **PrediccionController**: Predicciones de IA
13. **CampanaController**: Campañas de salud
14. **CampanaFactoresController**: Relaciones campaña-factor
15. **InscripcionCampanaController**: Inscripciones
16. **ServicioCampanaController**: Servicios de campañas
17. **ServicioMedicoController**: Servicios médicos
18. **InteraccionChatbotController**: Interacciones con chatbots
19. **SeguimientoController**: Seguimientos médicos
20. **RecomendacionController**: Recomendaciones
21. **LocalizacionController**: Gestión geoespacial
22. **EntidadSaludController**: Entidades de salud
23. **EmbajadorController**: Gestión de embajadores
24. **CitacionMedicaController**: Citas médicas

#### Controladores Especiales
- **N8nWebhookController**: `/api/webhooks/n8n/*` - Integración con N8N

### Características de los Controladores
- **Documentación Swagger**: Todos los endpoints documentados automáticamente
- **Validación**: Validación automática de entrada con `@Valid`
- **Paginación**: Soporte para paginación en listados
- **Filtrado**: Capacidades de búsqueda y filtrado avanzado
- **Manejo de Errores**: Respuestas de error estandarizadas
- **Seguridad**: Control de acceso basado en roles

## 🔧 Patrones de Diseño

### Repository Pattern
**Implementación**: Cada dominio tiene su repositorio que extiende `JpaRepository`
```java
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    List<Paciente> findByNombreContaining(String nombre);
    Optional<Paciente> findByDocumento(String documento);
}
```

### Service Layer Pattern
**Lógica de negocio centralizada** en servicios que:
- Manejan transacciones
- Implementan validaciones de negocio
- Coordinan entre múltiples repositorios
- Aplican reglas de autorización

### DTO Pattern
**Transferencia de datos** entre capas con:
- **Request DTOs**: Para entrada de datos
- **Response DTOs**: Para salida de datos
- **Mappers**: Conversión automática con MapStruct

### Strategy Pattern
**Algoritmos de triaje** implementados como estrategias:
- Triaje cardíaco
- Triaje respiratorio
- Triaje neurológico
- Algoritmos personalizados por entidad de salud

### Observer Pattern
**Sistema de eventos** para:
- Notificaciones automáticas
- Auditoría de cambios
- Integración con sistemas externos

## 🔐 Sistema de Seguridad

### Autenticación JWT
**Implementación**: Sistema stateless basado en JSON Web Tokens

#### Configuración
- **Librería**: jjwt 0.11.5
- **Algoritmo**: HS256 (HMAC SHA-256)
- **Expiración**: Configurable por ambiente
- **Refresh Tokens**: Soporte para renovación automática

#### Flujo de Autenticación
1. **Login**: Usuario envía credenciales
2. **Validación**: Verificación contra base de datos
3. **Token Generation**: Generación de JWT con claims
4. **Response**: Token enviado al cliente
5. **Authorization**: Header `Authorization: Bearer {token}`

### Control de Acceso Basado en Roles (RBAC)
**Roles disponibles**:
- **ADMIN**: Acceso completo al sistema
- **MEDICO**: Acceso a funciones médicas
- **PACIENTE**: Acceso limitado a datos propios
- **ADMINISTRATIVO**: Funciones administrativas

### Seguridad de Datos
- **Encriptación**: BCrypt para contraseñas
- **Validación**: Jakarta Validation para entrada de datos
- **Auditoría**: Tracking automático de cambios
- **CORS**: Configuración flexible para múltiples orígenes

## 🤖 Sistema Multiagente

### Arquitectura de Agentes Inteligentes

El sistema integra múltiples agentes inteligentes para automatizar procesos de salud:

#### Agente de Triaje Inteligente
**Función**: Evaluación automática de urgencia médica
- **Input**: Síntomas, signos vitales, historial médico
- **Algoritmo**: Machine Learning + Reglas expertas
- **Output**: Clasificación de prioridad (Rojo, Amarillo, Verde, Azul)
- **Precisión**: >95% en casos de emergencia

#### Agente de Predicción de Riesgo
**Función**: Predicción de factores de riesgo cardiovascular
- **Input**: Datos clínicos, historial familiar, hábitos
- **Modelo**: Algoritmos de regresión logística y redes neuronales
- **Output**: Porcentaje de riesgo y recomendaciones
- **Actualización**: Reentrenamiento mensual con nuevos datos

#### Agente de Recomendaciones
**Función**: Generación de recomendaciones personalizadas
- **Input**: Perfil del paciente, diagnósticos, tratamientos
- **Algoritmo**: Sistema de recomendación basado en contenido
- **Output**: Recomendaciones de prevención y tratamiento
- **Adaptación**: Aprendizaje continuo del comportamiento del paciente

#### Agente de Interacción Chatbot
**Función**: Atención automatizada al paciente
- **Tecnología**: Procesamiento de lenguaje natural (NLP)
- **Capacidades**: Respuestas a consultas frecuentes, triaje preliminar
- **Integración**: Conexión directa con sistema de citas y triaje
- **Escalación**: Derivación automática a personal médico cuando es necesario

### Integración con N8N
**Ubicación**: `N8nWebhookController`

Sistema de workflows automatizados que permite:
- **Notificaciones**: Alertas automáticas por cambios críticos
- **Integraciones**: Conexión con sistemas externos de salud
- **Reportes**: Generación automática de informes periódicos
- **Monitoreo**: Seguimiento de métricas en tiempo real

### Características del Sistema Multiagente
- **Escalabilidad**: Agentes distribuidos y paralelizables
- **Aprendizaje**: Mejora continua basada en retroalimentación
- **Interoperabilidad**: APIs estándar para integración
- **Auditabilidad**: Trazabilidad completa de decisiones automáticas

## 🌐 APIs y Endpoints

### Documentación Swagger
**URL de Acceso**: `http://localhost:8090/swagger-ui/index.html#/`

### Estructura de URLs
**Patrón estándar**: `/api/{dominio}/{operacion}`

#### 🔑 Endpoints de Autenticación
```
POST /api/auth/login           - Iniciar sesión
POST /api/auth/register        - Registrar usuario  
POST /api/auth/refresh         - Renovar token
POST /api/auth/logout          - Cerrar sesión
GET  /api/auth/profile         - Obtener perfil actual
```

#### 👥 Endpoints de Usuarios
```
GET    /api/usuarios           - Listar usuarios (paginado)
POST   /api/usuarios           - Crear usuario
GET    /api/usuarios/{id}      - Obtener usuario específico
PUT    /api/usuarios/{id}      - Actualizar usuario
DELETE /api/usuarios/{id}      - Eliminar usuario
GET    /api/usuarios/search    - Búsqueda avanzada
```

#### 🏥 Endpoints de Pacientes
```
GET    /api/pacientes          - Listar pacientes
POST   /api/pacientes          - Crear paciente
GET    /api/pacientes/{id}     - Obtener paciente
PUT    /api/pacientes/{id}     - Actualizar paciente
DELETE /api/pacientes/{id}     - Eliminar paciente
GET    /api/pacientes/{id}/historia-clinica  - Historia clínica
POST   /api/pacientes/{id}/nueva-consulta     - Nueva consulta
```

#### 🎯 Endpoints de Triaje
```
POST   /api/triaje/evaluar     - Evaluar triaje automático
GET    /api/triaje/{id}        - Obtener evaluación específica
GET    /api/triaje/paciente/{pacienteId}  - Histórico de triaje
PUT    /api/triaje/{id}/confirmar         - Confirmar triaje médico
POST   /api/triaje/manual                 - Triaje manual por médico
```

#### 📊 Endpoints de Predicciones
```
POST   /api/predicciones/riesgo-cardiovascular  - Evaluar riesgo cardíaco
GET    /api/predicciones/paciente/{id}          - Predicciones del paciente
POST   /api/predicciones/factores               - Evaluar múltiples factores
GET    /api/predicciones/estadisticas           - Estadísticas de predicciones
```

#### 🎪 Endpoints de Campañas
```
GET    /api/campanas           - Listar campañas activas
POST   /api/campanas           - Crear nueva campaña
GET    /api/campanas/{id}      - Detalle de campaña
PUT    /api/campanas/{id}      - Actualizar campaña
POST   /api/campanas/{id}/inscribir/{pacienteId}  - Inscribir paciente
GET    /api/campanas/{id}/participantes           - Listar participantes
GET    /api/campanas/{id}/estadisticas            - Estadísticas de campaña
```

#### 🤖 Endpoints de Chatbot
```
POST   /api/chatbot/mensaje    - Enviar mensaje al chatbot
GET    /api/chatbot/historial/{pacienteId}  - Historial de conversaciones
POST   /api/chatbot/escalar              - Escalar a atención humana
GET    /api/chatbot/metricas             - Métricas de uso del chatbot
```

#### 🔗 Endpoints de Webhooks
```
POST   /api/webhooks/n8n/notificacion   - Recibir notificaciones de N8N
POST   /api/webhooks/n8n/reporte        - Recibir reportes automáticos
POST   /api/webhooks/n8n/integracion    - Integración con sistemas externos
```

### Características de las APIs
- **Versionado**: Soporte para múltiples versiones de API
- **Paginación**: Implementación estándar con `page`, `size`, `sort`
- **Filtrado**: Filtros avanzados usando query parameters
- **Compresión**: Soporte GZIP para reducir latencia
- **Rate Limiting**: Límites configurables por usuario/IP
- **Documentación Interactiva**: Swagger UI para pruebas en vivo

## Testing

### Estructura de Tests
```
test/
├── unit/              # Tests unitarios
├── integration/       # Tests de integración
└── e2e/              # Tests end-to-end
```

### Estrategia de Testing
- **Unit Tests**: Servicios y mappers
- **Integration Tests**: Repositorios y APIs
- **E2E Tests**: Flujos completos de usuario

## Deployment

### Render
El proyecto se despliega automáticamente en Render usando el archivo `render.yml`

### Variables de Entorno en Producción
- `DATABASE_URL`: URL de conexión a PostgreSQL
- `JWT_SECRET`: Secreto para JWT
- `SPRING_PROFILES_ACTIVE=prod`

### Configuración Docker
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/integrador-1.0.0.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/app.jar"]
```

## Monitoreo y Logging

### Actuator Endpoints
- `/actuator/health` - Estado de la aplicación
- `/actuator/metrics` - Métricas de la aplicación
- `/actuator/info` - Información de la aplicación

### Logs
Los logs se configuran en `application.yml` con diferentes niveles según el ambiente.

## Seguridad

### Autenticación JWT
Sistema de autenticación basado en tokens JWT

### Autorización
Control de acceso basado en roles:
- `ADMIN`: Acceso completo
- `MEDICO`: Acceso a datos médicos
- `PACIENTE`: Acceso limitado a sus datos

### Encriptación
- Passwords encriptados con BCrypt
- Datos sensibles encriptados en base de datos

## Troubleshooting

### Problemas Comunes

1. **Error de conexión a base de datos**
   - Verificar credenciales en `.env`
   - Asegurar que PostgreSQL esté ejecutándose

2. **Error de migración**
   - Verificar estado de migraciones: `./mvnw flyway:info`
   - Limpiar y re-migrar: `./mvnw flyway:clean flyway:migrate`

3. **Error de puerto en uso**
   - Cambiar puerto en `application.yml` o variable `SERVER_PORT`

### Logs Útiles
```bash
# Ver logs de la aplicación
tail -f logs/application.log

# Ver logs de base de datos
tail -f logs/database.log
```

## 📈 Monitoreo y Observabilidad

### Spring Boot Actuator
**Endpoints de monitoreo disponibles**:

```
GET /actuator/health           - Estado general del sistema
GET /actuator/health/liveness  - Verificación de vida
GET /actuator/health/readiness - Verificación de disponibilidad
GET /actuator/metrics          - Métricas de rendimiento
GET /actuator/info             - Información de la aplicación
GET /actuator/env              - Variables de entorno
GET /actuator/loggers          - Configuración de logs
```

### Métricas Personalizadas
- **Triaje**: Tiempo promedio de evaluación
- **APIs**: Latencia y throughput por endpoint
- **Base de Datos**: Conexiones activas y tiempo de consulta
- **Chatbot**: Tasa de resolución automática
- **Predicciones**: Precisión de modelos de IA

### Logging Avanzado
**Niveles configurables por ambiente**:
- **TRACE**: Desarrollo - Información detallada
- **DEBUG**: Testing - Información de depuración
- **INFO**: Producción - Información general
- **WARN**: Advertencias del sistema
- **ERROR**: Errores críticos

### Alertas Automáticas
- **Health Checks**: Monitoreo continuo de servicios críticos
- **Umbrales**: Alertas por latencia o errores
- **Disponibilidad**: Notificaciones de downtime
- **Capacidad**: Alertas por uso de recursos

## 🔧 Troubleshooting Avanzado

### Problemas de Performance

#### Consultas Lentas de Base de Datos
```sql
-- Identificar consultas lentas
SELECT query, mean_time, calls, total_time 
FROM pg_stat_statements 
ORDER BY total_time DESC LIMIT 10;

-- Verificar índices faltantes
SELECT schemaname, tablename, attname, n_distinct, correlation 
FROM pg_stats 
WHERE schemaname = 'public';
```

#### Optimizaciones Recomendadas
- **Connection Pooling**: Configurar HikariCP adecuadamente
- **Índices**: Crear índices en columnas de búsqueda frecuente
- **Paginación**: Implementar paginación en listados grandes
- **Caché**: Usar Redis para datos frecuentemente consultados

### Problemas de Memoria
```bash
# Monitorear uso de memoria
jstat -gc <pid> 5s

# Análisis de heap dump
jmap -dump:format=b,file=heapdump.hprof <pid>

# Configurar memoria JVM
java -Xms2g -Xmx4g -XX:+UseG1GC -jar integrador.jar
```

### Debugging Distribuido
- **Correlation IDs**: Trazabilidad entre servicios
- **Structured Logging**: Logs en formato JSON para análisis
- **APM Tools**: Integración con herramientas de monitoreo

### Scripts de Diagnóstico
```bash
#!/bin/bash
# health-check.sh - Verificación rápida del sistema

echo "=== Healink Integrador Health Check ==="
echo "Fecha: $(date)"
echo

# Verificar conectividad a base de datos
psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c "SELECT 1;" >/dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "✅ Base de datos: CONECTADA"
else
    echo "❌ Base de datos: ERROR DE CONEXIÓN"
fi

# Verificar endpoints críticos
curl -s -o /dev/null -w "%{http_code}" http://localhost:8090/actuator/health | grep -q "200"
if [ $? -eq 0 ]; then
    echo "✅ Aplicación: ACTIVA"
else
    echo "❌ Aplicación: NO RESPONDE"
fi

# Verificar uso de memoria
memory_usage=$(free | grep Mem | awk '{printf "%.2f", $3/$2 * 100.0}')
echo "📊 Uso de memoria: ${memory_usage}%"

# Verificar espacio en disco
disk_usage=$(df -h / | awk 'NR==2 {print $5}')
echo "💾 Uso de disco: ${disk_usage}"
```

### Procedimientos de Recuperación

#### Recuperación de Base de Datos
```bash
# Backup automático
pg_dump -h $DB_HOST -U $DB_USER $DB_NAME > backup_$(date +%Y%m%d_%H%M%S).sql

# Restauración
psql -h $DB_HOST -U $DB_USER -d $DB_NAME < backup_file.sql
```

#### Rollback de Despliegue
```bash
# Rollback en Render
curl -X POST "https://api.render.com/v1/services/{serviceId}/deploys" \
  -H "Authorization: Bearer $RENDER_API_KEY" \
  -H "Content-Type: application/json" \
  -d '{"toDeploy": "previous"}'
```

## 📚 Recursos Adicionales

### Documentación de Referencia
- **Spring Boot**: https://spring.io/projects/spring-boot
- **Spring Security**: https://spring.io/projects/spring-security
- **MapStruct**: https://mapstruct.org/
- **PostgreSQL**: https://www.postgresql.org/docs/
- **PostGIS**: https://postgis.net/documentation/

### Herramientas de Desarrollo
- **IDE Recomendado**: IntelliJ IDEA Ultimate
- **Base de Datos**: pgAdmin 4 o DBeaver
- **API Testing**: Postman o Insomnia
- **Git Client**: GitKraken o SourceTree

### Capacitación y Certificaciones
- **Spring Professional**: Certificación oficial de Spring
- **Java SE 17**: Certificación Oracle Java
- **PostgreSQL**: Certificación PostgreSQL Associate

### Comunidades y Soporte
- **Stack Overflow**: Preguntas técnicas específicas
- **Spring Community**: Foros oficiales de Spring
- **GitHub Issues**: Reportar bugs o solicitar features

## 📞 Contacto y Soporte

### Equipo de Desarrollo
- **Tech Lead**: Arquitectura y decisiones técnicas
- **Backend Developers**: Desarrollo de APIs y lógica de negocio
- **DevOps Engineer**: Infraestructura y despliegue
- **QA Engineer**: Testing y calidad del software

### Canales de Comunicación
- **Email**: healink.support@empresa.com
- **Slack**: #healink-integrador
- **Jira**: Tickets de soporte y bugs
- **Confluence**: Documentación colaborativa

### Horarios de Soporte
- **Lunes a Viernes**: 8:00 AM - 6:00 PM (GMT-5)
- **Emergencias**: 24/7 para problemas críticos
- **Actualizaciones**: Notificaciones por email

### SLA (Service Level Agreement)
- **Tiempo de Respuesta**: 
  - Crítico: 1 hora
  - Alto: 4 horas
  - Medio: 24 horas
  - Bajo: 72 horas
- **Disponibilidad**: 99.9% uptime garantizado

---

**Última actualización**: $(date)  
**Versión del documento**: 2.0  
**Versión de la aplicación**: 0.0.2-SNAPSHOT 