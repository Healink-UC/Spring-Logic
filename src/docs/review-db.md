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

[Continuaré analizando más entidades...] 