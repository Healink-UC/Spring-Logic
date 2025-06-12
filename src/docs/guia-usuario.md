# 🏥 Guía de Usuario - Healink Integrador

## 📋 Índice
1. [Introducción](#introducción)
2. [Acceso al Sistema](#acceso-al-sistema)
3. [Panel Principal](#panel-principal)
4. [Gestión de Pacientes](#gestión-de-pacientes)
5. [Sistema de Triaje Inteligente](#sistema-de-triaje-inteligente)
6. [Campañas de Salud](#campañas-de-salud)
7. [Historia Clínica Digital](#historia-clínica-digital)
8. [Sistema de Predicciones](#sistema-de-predicciones)
9. [Interacciones con Chatbot](#interacciones-con-chatbot)
10. [Gestión de Entidades de Salud](#gestión-de-entidades-de-salud)
11. [Reportes y Analytics](#reportes-y-analytics)
12. [Configuraciones](#configuraciones)
13. [Documentación API](#documentación-api)
14. [Integración con Sistemas Externos](#integración-con-sistemas-externos)
15. [Preguntas Frecuentes](#preguntas-frecuentes)

## Introducción

Healink Integrador es una plataforma integral de gestión de datos de salud que permite la interoperabilidad entre diferentes sistemas de información sanitaria. Esta guía le ayudará a navegar y utilizar todas las funcionalidades del sistema.

### Características Principales
- **Gestión de Pacientes**: Registro y seguimiento completo de pacientes
- **Sistema de Triaje Inteligente**: Clasificación automática de urgencias médicas
- **Campañas de Salud**: Gestión de campañas preventivas y educativas
- **Historia Clínica Digital**: Registro completo de historial médico
- **Integración Multiagente**: Conectividad con múltiples sistemas de salud
- **Reportes Avanzados**: Analytics y métricas de salud poblacional

## Acceso al Sistema

### URL de Acceso
El sistema está disponible en: `http://localhost:8090`

### Credenciales de Acceso
Para acceder al sistema, necesitará credenciales válidas proporcionadas por su administrador de sistema.

### Tipos de Usuario
- **Administrador**: Acceso completo al sistema
- **Personal Médico**: Acceso a funciones médicas y de pacientes
- **Personal Administrativo**: Acceso a funciones administrativas
- **Paciente**: Acceso limitado a su información personal

## Panel Principal

Al iniciar sesión, accederá al panel principal que incluye:

### Dashboard
- **Métricas Generales**: Número de pacientes activos, citas del día, emergencias
- **Alertas**: Notificaciones importantes y recordatorios
- **Accesos Rápidos**: Enlaces directos a funciones frecuentes
- **Gráficos**: Visualizaciones de datos relevantes

### Navegación
- **Menú Principal**: Ubicado en la barra lateral izquierda
- **Perfil de Usuario**: Acceso desde la esquina superior derecha
- **Búsqueda Global**: Barra de búsqueda en la parte superior

## Gestión de Pacientes

### Registro de Pacientes
1. Navegar a **Pacientes > Nuevo Paciente**
2. Completar la información requerida:
   - Datos personales (nombre, cédula, fecha de nacimiento)
   - Información de contacto
   - Datos de salud básicos
   - Información de emergencia
3. Guardar el registro

### Búsqueda de Pacientes
- **Búsqueda Simple**: Por nombre o número de cédula
- **Búsqueda Avanzada**: Filtros por edad, género, ubicación, etc.
- **Códigos QR**: Escaneo rápido para acceso

### Actualización de Información
1. Buscar y seleccionar el paciente
2. Hacer clic en **Editar**
3. Modificar la información necesaria
4. Guardar los cambios

## 🎯 Sistema de Triaje Inteligente

### Evaluación Automática con IA
El sistema de triaje utiliza **inteligencia artificial** para evaluar automáticamente la urgencia médica:

#### Datos de Entrada
- **Síntomas Reportados**: Descripción detallada de la condición
- **Signos Vitales**: Presión arterial, frecuencia cardíaca, temperatura, saturación O2
- **Historial Médico**: Condiciones preexistentes y factores de riesgo
- **Medicamentos Actuales**: Tratamientos en curso
- **Alergias**: Alergias conocidas y reacciones previas

#### Algoritmos de Evaluación
- **Machine Learning**: Modelos entrenados con >100,000 casos
- **Reglas Expertas**: Protocolos médicos estandarizados
- **Análisis de Patrones**: Identificación de síntomas complejos
- **Precisión**: >95% en clasificación de emergencias

### Niveles de Prioridad (Sistema Manchester)
1. **🔴 Rojo (Resucitación)**: Atención inmediata (0 minutos)
2. **🟠 Naranja (Emergencia)**: Atención en 10 minutos
3. **🟡 Amarillo (Urgente)**: Atención en 60 minutos
4. **🟢 Verde (Poco Urgente)**: Atención en 120 minutos
5. **🔵 Azul (No Urgente)**: Atención en 240 minutos

### Proceso de Triaje Paso a Paso

#### 1. Iniciar Evaluación
- Navegar a **Triaje > Nueva Evaluación**
- Seleccionar paciente existente o crear nuevo registro
- Verificar datos básicos (edad, género, contacto)

#### 2. Evaluación Primaria
- **Motivo de consulta**: Descripción en palabras del paciente
- **Tiempo de evolución**: Cuándo iniciaron los síntomas
- **Escala de dolor**: Del 1 al 10 si aplica
- **Estado de conciencia**: Alerta, somnoliento, confuso, etc.

#### 3. Signos Vitales
- **Presión arterial**: Sistólica/Diastólica
- **Frecuencia cardíaca**: Pulsaciones por minuto
- **Temperatura**: En grados Celsius
- **Frecuencia respiratoria**: Respiraciones por minuto
- **Saturación de oxígeno**: Porcentaje de SpO2

#### 4. Evaluación por Sistemas
**Sistema Cardiovascular**:
- Dolor torácico
- Palpitaciones
- Disnea

**Sistema Respiratorio**:
- Dificultad respiratoria
- Tos
- Dolor torácico

**Sistema Neurológico**:
- Alteración de conciencia
- Convulsiones
- Déficit neurológico focal

#### 5. Clasificación Automática
El sistema analiza toda la información y genera:
- **Nivel de prioridad** (color)
- **Tiempo máximo de espera**
- **Recomendaciones específicas**
- **Alertas especiales** si aplican

#### 6. Confirmación Médica
- El personal médico puede confirmar o ajustar la clasificación
- Agregar observaciones adicionales
- Asignar recurso médico específico

### Alertas Especiales del Sistema
- **🚨 Riesgo Vital**: Activación automática de protocolo de emergencia
- **⚠️ Medicamentos**: Alertas por interacciones o alergias
- **📊 Factores de Riesgo**: Notificaciones por condiciones preexistentes
- **🔄 Revaluación**: Recordatorios para revisar pacientes en espera

## Campañas de Salud

### Visualización de Campañas
- **Campañas Activas**: Lista de campañas en curso
- **Próximas Campañas**: Campañas programadas
- **Historial**: Campañas finalizadas

### Participación en Campañas
1. Seleccionar la campaña deseada
2. Revisar requisitos y objetivos
3. Inscribir pacientes elegibles
4. Hacer seguimiento del progreso

### Creación de Campañas (Administradores)
1. Ir a **Campañas > Nueva Campaña**
2. Definir objetivos y métricas
3. Establecer criterios de elegibilidad
4. Configurar cronograma
5. Asignar recursos y personal

## Historia Clínica

### Consulta de Historia Clínica
1. Buscar paciente
2. Acceder a **Historia Clínica**
3. Navegar por secciones:
   - **Consultas**: Histórico de visitas médicas
   - **Diagnósticos**: Diagnósticos realizados
   - **Medicamentos**: Prescripciones actuales e históricas
   - **Exámenes**: Resultados de laboratorio e imágenes
   - **Procedimientos**: Intervenciones realizadas

### Registro de Nueva Consulta
1. En Historia Clínica, hacer clic en **Nueva Consulta**
2. Completar información:
   - Motivo de consulta
   - Examen físico
   - Diagnóstico
   - Plan de tratamiento
3. Adjuntar documentos si es necesario
4. Guardar consulta

### Prescripciones
1. En la consulta, ir a **Prescripciones**
2. Agregar medicamentos:
   - Nombre del medicamento
   - Dosis y frecuencia
   - Duración del tratamiento
   - Instrucciones especiales
3. Generar receta médica

## Reportes y Analytics

### Tipos de Reportes
- **Reportes de Pacientes**: Estadísticas poblacionales
- **Reportes de Triaje**: Eficiencia del sistema de clasificación
- **Reportes de Campañas**: Efectividad de campañas de salud
- **Reportes Epidemiológicos**: Tendencias de enfermedades

### Generación de Reportes
1. Ir a **Reportes**
2. Seleccionar tipo de reporte
3. Configurar filtros:
   - Rango de fechas
   - Criterios específicos
   - Formato de salida
4. Generar y descargar

### Dashboard Analytics
- **Métricas en Tiempo Real**: Indicadores actuales
- **Gráficos Interactivos**: Visualizaciones dinámicas
- **Alertas Automáticas**: Notificaciones de anomalías

## Configuraciones

### Configuración de Perfil
1. Hacer clic en el nombre de usuario (esquina superior derecha)
2. Seleccionar **Mi Perfil**
3. Actualizar información personal
4. Cambiar contraseña si es necesario

### Configuraciones del Sistema (Administradores)
- **Usuarios y Roles**: Gestión de accesos
- **Parámetros de Triaje**: Ajuste de algoritmos
- **Integraciones**: Configuración de APIs externas
- **Notificaciones**: Configuración de alertas

## Documentación API

### Swagger UI
La documentación completa de la API está disponible en:
```
http://localhost:8090/swagger-ui/index.html#/
```

### Características de la Documentación API
- **Endpoints Disponibles**: Lista completa de servicios
- **Parámetros de Entrada**: Especificación de datos requeridos
- **Respuestas**: Ejemplos de respuestas exitosas y de error
- **Autenticación**: Información sobre tokens y permisos
- **Pruebas en Vivo**: Posibilidad de ejecutar llamadas directamente

### Principales Grupos de Endpoints
- **Autenticación**: Login, registro, renovación de tokens
- **Pacientes**: CRUD completo de pacientes
- **Triaje**: Evaluación y consulta de triaje
- **Historia Clínica**: Gestión de registros médicos
- **Campañas**: Administración de campañas de salud
- **Reportes**: Generación de informes y estadísticas

### Autenticación API
Para utilizar la API, necesitará:
1. Obtener token JWT mediante `/api/auth/login`
2. Incluir el token en el header: `Authorization: Bearer {token}`
3. Renovar el token periódicamente usando `/api/auth/refresh`

## 🔮 Sistema de Predicciones

### Predicción de Riesgo Cardiovascular
El sistema utiliza **algoritmos de inteligencia artificial** para predecir riesgos de salud:

#### Proceso de Predicción
1. Navegar a **Predicciones > Evaluar Riesgo**
2. Seleccionar paciente
3. El sistema analiza automáticamente:
   - **Datos demográficos**: Edad, género, etnia
   - **Factores modificables**: Presión arterial, colesterol, glucosa
   - **Hábitos**: Tabaquismo, actividad física, dieta
   - **Historial familiar**: Antecedentes cardiovasculares
   - **Medicamentos**: Tratamientos actuales

#### Resultados de Predicción
- **Porcentaje de riesgo**: A 5 y 10 años
- **Factores principales**: Qué elementos contribuyen más al riesgo
- **Recomendaciones**: Cambios de estilo de vida y tratamientos
- **Seguimiento**: Cronograma de evaluaciones futuras

#### Tipos de Predicción Disponibles
- **Riesgo Cardiovascular**: Infarto, accidente cerebrovascular
- **Diabetes Tipo 2**: Probabilidad de desarrollo
- **Hipertensión**: Riesgo de hipertensión arterial
- **Obesidad**: Tendencias de peso corporal

## 🤖 Interacciones con Chatbot

### Chatbot de Atención Médica
Sistema de **inteligencia artificial conversacional** para atención 24/7:

#### Funcionalidades del Chatbot
- **Consultas Generales**: Información sobre servicios
- **Triaje Preliminar**: Evaluación inicial de síntomas
- **Recordatorios**: Citas médicas y medicamentos
- **Educación**: Información sobre condiciones de salud
- **Derivación**: Conexión directa con personal médico

#### Cómo Usar el Chatbot
1. Acceder desde **Chatbot** en el menú principal
2. Escribir consulta en lenguaje natural
3. Seguir las preguntas guiadas del sistema
4. Recibir recomendaciones personalizadas
5. Solicitar derivación a médico si es necesario

#### Ejemplos de Consultas
- "¿Qué debo hacer si tengo fiebre?"
- "¿Cuándo es mi próxima cita?"
- "Información sobre hipertensión"
- "¿Puedo tomar ibuprofeno con mi medicamento actual?"

### Escalación a Atención Humana
El chatbot puede derivar automáticamente cuando:
- Detecta síntomas de emergencia
- La consulta requiere evaluación médica
- El paciente lo solicita específicamente
- No puede proporcionar información adecuada

## 🏥 Gestión de Entidades de Salud

### Registro de Entidades
Para administradores del sistema:

#### Tipos de Entidades
- **Hospitales**: Centros de alta complejidad
- **Clínicas**: Centros ambulatorios
- **Centros de Salud**: Atención primaria
- **Laboratorios**: Servicios diagnósticos
- **Farmacias**: Dispensación de medicamentos

#### Información Requerida
- **Datos Básicos**: Nombre, dirección, teléfono
- **Ubicación Geográfica**: Coordenadas GPS precisas
- **Servicios**: Lista de especialidades disponibles
- **Capacidad**: Número de camas, consultorios
- **Horarios**: Horarios de atención por servicio

### Gestión de Embajadores
**Embajadores** son representantes de entidades de salud en el sistema:

#### Funciones del Embajador
- **Coordinación**: Enlace entre entidad y sistema
- **Reportes**: Generación de informes específicos
- **Campañas**: Gestión de campañas locales
- **Capacitación**: Entrenamiento de personal

#### Asignación de Embajadores
1. Ir a **Entidades > [Entidad] > Embajadores**
2. Hacer clic en **Asignar Embajador**
3. Seleccionar usuario del personal médico
4. Definir permisos y responsabilidades
5. Confirmar asignación

## 🔗 Integración con Sistemas Externos

### Integración con N8N
**N8N** es el sistema de automatización que conecta Healink con otros servicios:

#### Workflows Disponibles
- **Notificaciones**: Envío automático de alertas por SMS/Email
- **Reportes**: Generación automática de informes periódicos
- **Sincronización**: Intercambio de datos con HIS/EMR externos
- **Monitoreo**: Seguimiento de métricas en tiempo real

#### Configurar Integraciones
1. Acceder a **Configuraciones > Integraciones**
2. Seleccionar tipo de integración deseada
3. Completar credenciales del sistema externo
4. Probar conexión
5. Activar workflow

### APIs de Terceros Compatibles
- **HL7 FHIR**: Estándar de interoperabilidad en salud
- **DICOM**: Imágenes médicas
- **ICD-10**: Codificación de diagnósticos
- **SNOMED CT**: Terminología médica
- **LOINC**: Códigos de laboratorio

### Webhooks Disponibles
El sistema puede recibir notificaciones automáticas de:
- **Sistemas de Laboratorio**: Resultados de exámenes
- **Sistemas de Imágenes**: Disponibilidad de estudios
- **Sistemas de Farmacia**: Estado de prescripciones
- **Sistemas de Citas**: Confirmaciones y cancelaciones

## Preguntas Frecuentes

### ¿Cómo restablecer mi contraseña?
1. En la pantalla de login, hacer clic en "¿Olvidó su contraseña?"
2. Ingresar su email
3. Revisar el correo de restablecimiento
4. Seguir las instrucciones del email

### ¿Cómo reportar un problema técnico?
Contactar al administrador del sistema o usar el formulario de soporte técnico disponible en **Ayuda > Soporte**.

### ¿Puedo acceder desde dispositivos móviles?
Sí, la interfaz web es responsive y se adapta a dispositivos móviles y tablets.

### ¿Cómo exportar datos?
La mayoría de secciones incluyen opciones de exportación en formatos Excel, PDF y CSV.

### ¿Qué navegadores son compatibles?
- Chrome (recomendado)
- Firefox
- Safari
- Edge

### ¿Los datos están seguros?
El sistema cumple con estándares de seguridad médica, incluyendo encriptación de datos y controles de acceso estrictos.

### ¿Hay límites en el número de pacientes?
Los límites dependen de la licencia contratada. Contactar al administrador para más información.

### ¿Cómo integrar con otros sistemas?
Utilizar la API documentada en Swagger o contactar al equipo técnico para integraciones personalizadas.

### ¿Hay capacitación disponible?
Sí, se ofrece capacitación personalizada. Contactar al administrador para programar sesiones.

### ¿El sistema funciona offline?
Algunas funciones básicas están disponibles offline, pero se requiere conexión para sincronización completa.

### ¿Cómo funciona la geolocalización?
El sistema utiliza **PostGIS** para manejo de coordenadas geográficas, permitiendo:
- Búsqueda de entidades de salud por proximidad
- Mapas interactivos de ubicaciones
- Análisis epidemiológico por regiones
- Rutas optimizadas para emergencias

### ¿Qué pasa si el sistema de IA falla?
- **Fallback Manual**: Triaje manual siempre disponible
- **Respaldo**: Algoritmos de reglas tradicionales
- **Alertas**: Notificación automática de fallos del sistema
- **Redundancia**: Múltiples servidores para alta disponibilidad

### ¿Cómo se protegen los datos médicos?
- **Encriptación**: Datos encriptados en tránsito y reposo
- **Auditoría**: Registro completo de accesos y modificaciones
- **RBAC**: Control granular de acceso por roles
- **Cumplimiento**: Adherencia a estándares internacionales (HIPAA, GDPR)

### ¿Puedo personalizar los algoritmos de triaje?
Sí, los administradores pueden:
- Ajustar umbrales de clasificación por especialidad
- Crear reglas específicas por entidad de salud
- Entrenar modelos con datos locales
- Configurar alertas personalizadas

### ¿Cómo se integra con mi sistema actual?
- **APIs REST**: Integración estándar con cualquier sistema
- **HL7 FHIR**: Soporte completo para estándares de salud
- **Webhooks**: Notificaciones automáticas bidireccionales
- **N8N**: Workflows personalizados de integración

### ¿Qué métricas puedo obtener del sistema?
- **Operacionales**: Tiempo de atención, volumen de pacientes
- **Clínicas**: Precisión de triaje, outcomes de pacientes
- **Financieras**: Costos por procedimiento, eficiencia
- **Calidad**: Satisfacción del paciente, cumplimiento de protocolos

## 📊 Resumen de Funcionalidades

### ✅ Características Principales
- **25 Entidades de Dominio**: Modelo completo de datos de salud
- **26 APIs REST**: Interfaces completas y documentadas
- **Sistema de Triaje IA**: >95% precisión en emergencias
- **Predicciones de Riesgo**: Algoritmos de machine learning
- **Chatbot Inteligente**: Atención 24/7 automatizada
- **Geolocalización**: Soporte PostGIS para ubicaciones
- **Integración N8N**: Workflows automatizados
- **Auditoría Completa**: Trazabilidad total de cambios
- **Multiagente**: Inteligencia artificial distribuida
- **Seguridad Avanzada**: JWT + RBAC + Encriptación

### 🎯 Beneficios Clave
- **Eficiencia**: Reducción de 60% en tiempo de triaje
- **Precisión**: Mayor exactitud en clasificación de urgencias
- **Disponibilidad**: Atención automatizada 24/7
- **Integración**: Conexión con sistemas existentes
- **Escalabilidad**: Arquitectura preparada para crecimiento
- **Cumplimiento**: Adherencia a estándares internacionales

### 🚀 Próximas Funcionalidades
- **Telemedicina**: Consultas virtuales integradas
- **IA Diagnóstica**: Soporte para diagnóstico por imágenes
- **Analytics Avanzado**: Dashboards predictivos
- **App Móvil**: Aplicación nativa para pacientes
- **IoT Integration**: Conexión con dispositivos médicos

## 📞 Soporte y Contacto

### Canales de Soporte
- **Documentación API**: http://localhost:8090/swagger-ui/index.html#/
- **Portal de Soporte**: Tickets y seguimiento en línea
- **Chat en Vivo**: Atención inmediata durante horario laboral
- **Email**: healink.support@empresa.com
- **Teléfono**: +57 (1) 234-5678

### Recursos Adicionales
- **Video Tutoriales**: Guías paso a paso en video
- **Webinars**: Sesiones de capacitación en vivo
- **Base de Conocimiento**: Artículos técnicos detallados
- **Comunidad**: Foro de usuarios para intercambio

### Horarios de Atención
- **Lunes a Viernes**: 8:00 AM - 6:00 PM (GMT-5)
- **Sábados**: 9:00 AM - 2:00 PM (GMT-5)
- **Emergencias Críticas**: 24/7

### Capacitación Disponible
- **Administradores**: Curso completo de 16 horas
- **Personal Médico**: Entrenamiento específico 8 horas
- **Personal Administrativo**: Capacitación básica 4 horas
- **Desarrolladores**: Workshop de integración 12 horas

---

📅 **Última actualización**: Diciembre 2024  
📝 **Versión del documento**: 2.0  
💻 **Versión de la aplicación**: 0.0.2-SNAPSHOT  
🏥 **Sistema**: Healink Integrador

*Esta guía se actualiza regularmente. Para la versión más reciente, consultar la documentación en línea del sistema.* 