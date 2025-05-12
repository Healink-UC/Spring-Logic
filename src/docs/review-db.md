# Revisión de Inconsistencias entre Código y Diagrama ER

## Metodología
- Se analizará cada entidad en el directorio `domain`
- Se comparará con su representación en el diagrama ER (`database.md`)
- Se documentarán las diferencias encontradas

## Formato de Reporte
Para cada inconsistencia se usará el siguiente formato:

```
### [Nombre de la Entidad]
#### En el Código
- Campo/relación como está implementado

#### En el Diagrama
- Campo/relación como está en el diagrama

#### Observaciones
- Cuál implementación parece más correcta y por qué
- Impacto potencial del cambio
```

## Inconsistencias Encontradas

### Prediccion
#### En el Código
- `valorPrediccion` es de tipo `float`
- `confianza` es de tipo `float`
- `fechaPrediccion` es de tipo `LocalDateTime`
- `factoresInfluyentes` es de tipo `JsonNode` con `columnDefinition = "jsonb"`
- `recomendaciones` es de tipo `JsonNode` con `columnDefinition = "jsonb"`
- Incluye relaciones `@ManyToOne` con `Paciente` y `Campana`

#### En el Diagrama
- `valor_prediccion` es de tipo `decimal`
- `confianza` es de tipo `decimal`
- `fecha_prediccion` es de tipo `timestamp`
- `factores_influyentes` es de tipo `jsonb` con comentario específico
- `recomendaciones` es de tipo `jsonb` con comentario `[string]`
- No muestra explícitamente las relaciones `@ManyToOne`

#### Observaciones
- La implementación en código es más específica y completa
- El uso de `float` vs `decimal` podría ser un problema de precisión
- El diagrama debería actualizarse para mostrar las relaciones `@ManyToOne`
- Los tipos `JsonNode` son correctos para manejar JSONB en PostgreSQL

### Triaje
#### En el Código
- Tiene campos comentados que fueron movidos a otras entidades:
  - `presionSistolica`
  - `presionDiastolica`
  - `colesterolTotal`
  - `hdl`
  - `imc`
  - `resultadoRiesgoCardiovascular`
  - `resultadoRiesgoCv`
  - `nivelPrioridad`
- `fechaTriaje` es de tipo `LocalDate`
- Incluye relación `@ManyToOne` con `Paciente`

#### En el Diagrama
- No tiene los campos que fueron movidos (correcto)
- `fecha_triaje` es de tipo `date`
- No muestra explícitamente la relación `@ManyToOne`

#### Observaciones
- El código mantiene campos comentados que deberían eliminarse ya que fueron movidos
- La implementación en el diagrama es más limpia respecto a la separación de responsabilidades
- El diagrama debería actualizarse para mostrar la relación `@ManyToOne`
- Los tipos de fecha son consistentes (`LocalDate` ↔ `date`)

### DatosClinicos
#### En el Código
- Nombre de tabla en mayúsculas: `DATOS_CLINICOS`
- Todos los campos numéricos son de tipo `Double`
- `observaciones` tiene `columnDefinition = "TEXT"`
- `fechaMedicion` es de tipo `LocalDate`
- No tiene relación explícita con `Paciente` (solo ID)

#### En el Diagrama
- Nombre de tabla en minúsculas: `datos_clinicos`
- Todos los campos numéricos son de tipo `decimal`
- `observaciones` es de tipo `text`
- `fecha_medicion` es de tipo `date`
- Muestra relación con `pacientes` en el diagrama

#### Observaciones
- La convención de nombres de tablas debe estandarizarse (preferiblemente minúsculas)
- El uso de `Double` vs `decimal` podría ser un problema de precisión
- Los tipos de fecha son consistentes (`LocalDate` ↔ `date`)
- La relación con `Paciente` debería implementarse en el código

### Paciente
#### En el Código
- `localidad` es de tipo `String` y se usa como campo directo
- `estado` es de tipo `String` con valor por defecto "ACTIVO"
- `genero` es un enum `GeneroBiologico`
- No tiene campo `direccion` en la entidad pero sí en el DTO
- No tiene relación explícita con `Localizacion`

#### En el Diagrama
- `localizacion_id` es una FK a la tabla `localizacion`
- `estado` es un enum con valores específicos "ACTIVO|INACTIVO|SUSPENDIDO|PENDIENTE"
- `genero` es un enum con valores "MASCULINO|FEMENINO"
- No tiene campo `direccion`
- Muestra relación con `localizacion`

#### Observaciones
- La implementación de `localidad` vs `localizacion_id` es inconsistente
- El enum `estado` debería implementarse en el código
- El campo `direccion` en el DTO no tiene correspondencia en la entidad ni en el diagrama
- La relación con `Localizacion` debería implementarse correctamente en el código

### Campana
#### En el Código
- Tiene restricciones de longitud: `nombre(50)`, `descripcion(350)`
- Tiene campo `fechaLimite` en el DTO pero no en la entidad
- Tiene relaciones `@ManyToOne` con `Localizacion` y `EntidadSalud`
- `estado` es un enum `EstadoCampana` con valores "POSTULADA|EJECUCION|FINALIZADA"
- Tiene campos de fecha: `fechaInicio` y `fechaLimiteInscripcion`

#### En el Diagrama
- No especifica longitudes de campos
- No tiene campo `fechaLimite`
- Muestra relaciones con `localizacion` y `entidades_salud`
- `estado` es un enum con valores "POSTULADA|EJECUCION|FINALIZADA|CANCELADA"
- Tiene campos de fecha: `fecha_inicio` y `fecha_limite_inscripcion`

#### Observaciones
- El diagrama incluye el estado "CANCELADA" que no está en el código
- El campo `fechaLimite` en el DTO no tiene correspondencia en la entidad ni en el diagrama
- Las restricciones de longitud deberían documentarse en el diagrama
- Los tipos de fecha son consistentes (`LocalDate` ↔ `date`)

### FactorRiesgo
#### En el Código
- Tiene restricciones de longitud: `nombre(50)`, `descripcion(350)`
- `tipo` es un enum `TipoFactorRiesgo` con valores "SOCIAL|AMBIENTAL|RACIAL"
- Extiende `EntidadAuditable` (campos de auditoría)
- No tiene relaciones explícitas con otras entidades

#### En el Diagrama
- No especifica longitudes de campos
- `tipo` es un enum con valores "SOCIAL|AMBIENTAL|RACIAL"
- No muestra campos de auditoría
- Muestra relaciones con otras entidades a través de tablas intermedias

#### Observaciones
- Las restricciones de longitud deberían documentarse en el diagrama
- Los valores del enum `tipo` son consistentes
- Los campos de auditoría deberían documentarse en el diagrama
- Las relaciones con otras entidades deberían implementarse en el código

### Diagnostico
#### En el Código
- Nombre de tabla en mayúsculas: `DIAGNOSTICOS`
- Tiene campo `fecha_diagnostico` de tipo `LocalDate`
- `severidad` es un enum `SeveridadDiagnostico` con valores "LEVE|MODERADA|GRAVE"
- Tiene relación `@ManyToOne` con `AtencionMedica`
- Usa snake_case en algunos campos: `es_principal`, `fecha_diagnostico`

#### En el Diagrama
- Nombre de tabla en minúsculas: `diagnosticos`
- No tiene campo `fecha_diagnostico`
- `severidad` es un enum con valores "LEVE|MODERADA|GRAVE"
- Muestra relación con `atenciones_medicas`
- Usa snake_case consistentemente

#### Observaciones
- La convención de nombres de tablas debe estandarizarse (preferiblemente minúsculas)
- El campo `fecha_diagnostico` no está en el diagrama y debería agregarse
- La convención de nombres de campos debe estandarizarse (preferiblemente camelCase)
- Los valores del enum `severidad` son consistentes

### Recomendacion
#### En el Código
- Nombre de tabla en mayúsculas: `RECOMENDACIONES`
- `nivel_importancia` es un enum `NivelPrioridad` con valores "ALTA|MEDIA|BAJA"
- `tipo` es un enum `TipoRecomendaciones` con valores "MEDICAMENTO|ESTILO_VIDA|PREVENCION"
- Tiene relación `@ManyToOne` con `Diagnostico`
- Tiene campo `fecha_creacion` en el DTO pero no en la entidad
- Usa snake_case en algunos campos: `nivel_importancia`

#### En el Diagrama
- Nombre de tabla en minúsculas: `recomendaciones`
- `nivel_importancia` es un enum con valores "ALTA|MEDIA|BAJA"
- `tipo` es un enum con valores "MEDICAMENTO|ESTILO_VIDA|PREVENCION"
- Muestra relación con `diagnosticos`
- No tiene campo `fecha_creacion`
- Usa snake_case consistentemente

#### Observaciones
- La convención de nombres de tablas debe estandarizarse (preferiblemente minúsculas)
- El campo `fecha_creacion` está en el DTO pero no en la entidad ni en el diagrama
- La convención de nombres de campos debe estandarizarse (preferiblemente camelCase)
- Los valores de los enums son consistentes
- La entidad ya extiende `EntidadAuditable` que incluye `fecha_creacion`

### Usuario
#### En el Código
- Nombre de tabla en mayúsculas: `USUARIOS`
- Implementa `UserDetails` para Spring Security
- `tipoIdentificacion` es un enum con valores "CC|TI|NIT|RCN"
- `estado` es un enum con valores "ACTIVO|INACTIVO|SUSPENDIDO|PENDIENTE"
- Tiene restricción única en `identificacion` y `correo`
- Tiene relación `@ManyToOne` con `Rol`
- No tiene campo `ultimo_acceso`

#### En el Diagrama
- Nombre de tabla en minúsculas: `usuarios`
- No menciona implementación de `UserDetails`
- `tipo_identificacion` es un enum con valores "cc|ti|nit|rcn"
- `estado` es un enum con valores "ACTIVO|INACTIVO|SUSPENDIDO|PENDIENTE"
- No muestra restricciones únicas
- Muestra relación con `roles`
- Tiene campo `ultimo_acceso` de tipo `timestamp`

#### Observaciones
- La convención de nombres de tablas debe estandarizarse (preferiblemente minúsculas)
- Los valores de los enums son consistentes pero difieren en capitalización
- Las restricciones únicas deberían documentarse en el diagrama
- El campo `ultimo_acceso` debería implementarse en el código
- La implementación de `UserDetails` es una decisión de implementación que no afecta al modelo

### Rol
#### En el Código
- Nombre de tabla en mayúsculas: `ROLES`
- `nombre` tiene restricción de longitud (50)
- `permisos` es de tipo `JsonNode` con `columnDefinition = "jsonb"`
- Extiende `EntidadAuditable`
- No tiene valores predefinidos para `nombre`

#### En el Diagrama
- Nombre de tabla en minúsculas: `roles`
- No especifica longitud para `nombre`
- `permisos` es de tipo `jsonb`
- No muestra campos de auditoría
- `nombre` tiene valores predefinidos "administrador|desarrollador|entidad_salud|medico|auxiliar|paciente|embajador"

#### Observaciones
- La convención de nombres de tablas debe estandarizarse (preferiblemente minúsculas)
- Las restricciones de longitud deberían documentarse en el diagrama
- Los valores predefinidos de `nombre` deberían implementarse como enum en el código
- Los campos de auditoría deberían documentarse en el diagrama
- El tipo `JsonNode` es correcto para manejar JSONB en PostgreSQL

### AtencionMedica
#### En el Código
- Nombre de tabla en mayúsculas: `ATENCIONES_MEDICAS`
- Usa `Timestamp` para campos de fecha/hora
- `estado` es un enum `EstadoAtencionMedica` con valores "EN_PROCESO|COMPLETADA|CANCELADA"
- No tiene relación explícita con `Citacion` (solo ID)
- Extiende `EntidadAuditable`

#### En el Diagrama
- Nombre de tabla en minúsculas: `atenciones_medicas`
- Usa `timestamp` para campos de fecha/hora
- `estado` es un enum con valores "EN_PROCESO|COMPLETADA|CANCELADA"
- Muestra relación con `citaciones`
- No muestra campos de auditoría

#### Observaciones
- La convención de nombres de tablas debe estandarizarse (preferiblemente minúsculas)
- Los tipos de fecha son consistentes (`Timestamp` ↔ `timestamp`)
- Los valores del enum `estado` son consistentes
- La relación con `Citacion` debería implementarse en el código
- Los campos de auditoría deberían documentarse en el diagrama

### CitacionMedica
#### En el Código
- Nombre de tabla en mayúsculas: `CITACIONES_MEDICAS`
- Usa `LocalDateTime` para campos de fecha/hora
- `estado` es un enum con valores "AGENDADA|ATENDIDA|CANCELADA"
- `prediccionAsistencia` es `BigDecimal` con precisión(5,2)
- Tiene relaciones `@ManyToOne` con `Campana`, `Paciente` y `PersonalMedico`
- Tiene comentarios sobre unidades (minutos) y rangos (0-100%)
- Extiende `EntidadAuditable`

#### En el Diagrama
- Nombre de tabla en minúsculas: `citaciones`
- Usa `timestamp` para campos de fecha/hora
- `estado` es un enum con valores "AGENDADA|ATENDIDA|CANCELADA"
- `prediccion_asistencia` es `decimal` con comentario "0-100%"
- Muestra relaciones con `campanas`, `pacientes` y `personal_medico`
- Tiene comentario "minutos" para `duracion_estimada`
- No muestra campos de auditoría

#### Observaciones
- La convención de nombres de tablas debe estandarizarse (preferiblemente minúsculas)
- Los tipos de fecha son consistentes (`LocalDateTime` ↔ `timestamp`)
- Los valores del enum `estado` son consistentes
- La precisión de `prediccionAsistencia` debería documentarse en el diagrama
- Los campos de auditoría deberían documentarse en el diagrama
- Los comentarios sobre unidades y rangos están presentes en ambos lados

[Continuaré analizando más entidades...] 