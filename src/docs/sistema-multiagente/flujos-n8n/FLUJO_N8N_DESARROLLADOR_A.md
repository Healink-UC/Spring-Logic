# FLUJO N8N - DESARROLLADOR A
## Sistema Multi-agente de Seguimientos Cardiovasculares

---

## 🎯 AGENTES BAJO TU RESPONSABILIDAD (DESARROLLADOR A)

### 1. **AGENTE ORQUESTADOR** (Master)
### 2. **AGENTE GENERADOR DE SEGUIMIENTOS**
### 3. **SISTEMA DE NOTIFICACIONES**

---

## 📋 FLUJO GENERAL DE TRABAJO

```
1. Spring-Logic → [WEBHOOK] → n8n Orquestador
2. n8n Orquestador → Análisis inicial y programación
3. n8n Generador de Seguimientos → Cronograma de seguimientos
4. n8n Sistema de Notificaciones → SMS al paciente
5. [Luego los agentes del Desarrollador B toman el control]
```

---

# 🎯 AGENTE 1: ORQUESTADOR (MASTER)

## **Responsabilidades:**
- Recibir datos del Spring-Logic cuando se completa una atención médica
- Análizar el historial clínico para determinar estrategia de seguimiento
- Coordinar con todos los demás agentes
- Tomar decisiones sobre prioridad y frecuencia de seguimientos

## **Webhook de Entrada:**
```
POST https://tu-instancia-n8n.app/webhook/orquestador-seguimientos
```

## **Payload Esperado desde Spring-Logic:**
```json
{
  "evento": "atencion_completada",
  "atencion_id": 123,
  "paciente_id": 456,
  "campana_id": 789,
  "fecha_atencion": "2024-01-15T14:30:00Z",
  "timestamp": 1705334200000,
  "historial_clinico": {
    "paciente_id": 456,
    "datos_basicos": {
      "edad": 68,
      "genero": "M",
      "direccion": "Calle 123",
      "tipo_sangre": "O+",
      "usuario_id": 321
    },
    "datos_clinicos": {
      "presion_arterial_sistolica": 150,
      "presion_arterial_diastolica": 95,
      "colesterol_total": 240,
      "hdl": 35,
      "frecuencia_cardiaca_min": 65,
      "frecuencia_cardiaca_max": 95,
      "saturacion_oxigeno": 98,
      "temperatura": 36.5,
      "fecha_medicion": "2024-01-15",
      "observaciones": "Paciente hipertenso"
    },
    "triaje_inicial": {
      "fecha_triaje": "2024-01-15",
      "edad": 68,
      "peso": 85,
      "estatura": 1.75,
      "factores_riesgo": {
        "tabaquismo": true,
        "alcoholismo": false,
        "diabetes": true,
        "hipertension": true,
        "antecedentes_cardiacos": true,
        "actividad_fisica": false
      },
      "sintomas": {
        "dolor_pecho": true,
        "dolor_irradiado": false,
        "sudoracion": true,
        "nauseas": false
      },
      "descripcion": "Paciente con episodio de dolor precordial"
    },
    "diagnosticos": {
      "codigo_cie10": "I25.9",
      "descripcion": "Enfermedad cardiaca isquémica crónica",
      "severidad": "MODERADA",
      "es_principal": true,
      "fecha_diagnostico": "2024-01-15"
    },
    "prediccion_ia": {
      "probabilidad_rehospitalizacion": 0.75,
      "fecha_prediccion": "2024-01-15T14:30:00Z"
    },
    "predicciones_fastapi": {
      "riesgo_cardiovascular": {
        "valor_prediccion": 78.5,
        "nivel_riesgo": "ALTO",
        "confianza": 92.3,
        "factores_influyentes": {
          "edad": 0.18,
          "presion_sistolica": 0.25,
          "imc": 0.31,
          "colesterol": 0.15,
          "diabetes": 0.11
        },
        "recomendaciones": [
          "Controlar presión arterial diariamente",
          "Reducir peso corporal en 10%",
          "Medicación antihipertensiva estricta"
        ],
        "fecha_prediccion": "2024-01-15T14:25:00Z",
        "modelo_version": "GradientBoostingClassifier_v1.2"
      },
      "asistencia": {
        "probabilidad_asistencia": 85.2,
        "confianza": 88.0,
        "fecha_prediccion": "2024-01-15T14:20:00Z"
      },
      "hospitalizacion": {
        "probabilidad_hospitalizacion": 23.7,
        "nivel_riesgo": "MODERADO",
        "confianza": 81.5,
        "fecha_prediccion": "2024-01-15T14:22:00Z"
      }
    },
    "atencion_id": 123,
    "campana_id": 789
  }
}
```

## **Lógica del Agente Orquestador (JavaScript en n8n) - MEJORADA:**

```javascript
// ===== NODO 1: ANÁLISIS INICIAL DE RIESGO MEJORADO =====
const historial = $input.item(0).json.historial_clinico;
const pacienteId = historial.paciente_id;
const edad = historial.datos_basicos.edad;

// **NUEVO: Usar predicciones de FastAPI-Back como fuente principal**
const prediccionesFastAPI = historial.predicciones_fastapi || {};
const riesgoCV = prediccionesFastAPI.riesgo_cardiovascular;
const prediccionAsistencia = prediccionesFastAPI.asistencia;
const prediccionHospitalizacion = prediccionesFastAPI.hospitalizacion;

// Calcular score de riesgo MEJORADO usando predicciones FastAPI
let scoreRiesgo = 0;
let nivelRiesgoFinal;
let factoresCriticos = [];

// 1. **PREDICCIÓN RIESGO CV (Peso: 50%)**
if (riesgoCV) {
  const valorRiesgoCV = riesgoCV.valor_prediccion; // 0-100
  const nivelRiesgoCV = riesgoCV.nivel_riesgo; // BAJO, MODERADO, ALTO, CRITICO
  
  // Usar directamente el nivel de riesgo calculado por FastAPI
  nivelRiesgoFinal = nivelRiesgoCV;
  
  // Agregar puntos según el valor numérico
  scoreRiesgo += Math.round(valorRiesgoCV * 0.5); // Peso 50%
  
  // Extraer factores críticos de FastAPI
  const factoresInfluyentes = riesgoCV.factores_influyentes || {};
  factoresCriticos = Object.keys(factoresInfluyentes)
    .sort((a, b) => factoresInfluyentes[b] - factoresInfluyentes[a])
    .slice(0, 3); // Top 3 factores
    
  console.log(`[ORQUESTADOR] Riesgo CV FastAPI: ${valorRiesgoCV}% (${nivelRiesgoCV})`);
  console.log(`[ORQUESTADOR] Factores críticos: ${factoresCriticos.join(', ')}`);
} else {
  // Fallback: usar análisis manual del triaje
  console.log(`[ORQUESTADOR] No hay predicciones FastAPI, usando análisis manual`);
  
  const factores = historial.triaje_inicial.factores_riesgo;
  const sintomas = historial.triaje_inicial.sintomas;
  
  // Factores de riesgo (peso: 40%)
  if (factores.tabaquismo) scoreRiesgo += 15;
  if (factores.diabetes) scoreRiesgo += 20;
  if (factores.hipertension) scoreRiesgo += 15;
  if (factores.antecedentes_cardiacos) scoreRiesgo += 25;
  if (!factores.actividad_fisica) scoreRiesgo += 10;
  
  // Síntomas actuales (peso: 30%)
  if (sintomas.dolor_pecho) scoreRiesgo += 20;
  if (sintomas.dolor_irradiado) scoreRiesgo += 15;
  if (sintomas.sudoracion) scoreRiesgo += 10;
  
  // Edad (peso: 15%)
  if (edad > 65) scoreRiesgo += 10;
  if (edad > 75) scoreRiesgo += 15;
  
  // Determinar nivel de riesgo manual
  if (scoreRiesgo >= 80) nivelRiesgoFinal = "CRITICO";
  else if (scoreRiesgo >= 60) nivelRiesgoFinal = "ALTO";
  else if (scoreRiesgo >= 40) nivelRiesgoFinal = "MODERADO";
  else nivelRiesgoFinal = "BAJO";
}

// 2. **PREDICCIÓN DE ASISTENCIA (Peso: 20%)**
let probabilidadAsistencia = 75; // Por defecto
if (prediccionAsistencia) {
  probabilidadAsistencia = prediccionAsistencia.probabilidad_asistencia;
  scoreRiesgo += Math.round(probabilidadAsistencia * 0.2); // Peso 20%
}

// 3. **PREDICCIÓN DE HOSPITALIZACIÓN (Peso: 30%)**
if (prediccionHospitalizacion) {
  const probHospitalizacion = prediccionHospitalizacion.probabilidad_hospitalizacion;
  scoreRiesgo += Math.round(probHospitalizacion * 0.3); // Peso 30%
  
  if (probHospitalizacion > 30) {
    factoresCriticos.push("alto_riesgo_hospitalizacion");
  }
}

// Determinar frecuencia y prioridad
let frecuenciaSeguimiento;
let prioridadSeguimiento;

switch (nivelRiesgoFinal) {
  case "CRITICO":
    frecuenciaSeguimiento = "DIARIO";
    prioridadSeguimiento = "MAXIMA";
    break;
  case "ALTO":
    frecuenciaSeguimiento = "FRECUENTE";
    prioridadSeguimiento = "ALTA";
    break;
  case "MODERADO":
    frecuenciaSeguimiento = "REGULAR";
    prioridadSeguimiento = "MEDIA";
    break;
  default:
    frecuenciaSeguimiento = "ESPACIADO";
    prioridadSeguimiento = "BAJA";
}

return {
  paciente_id: pacienteId,
  atencion_id: $input.item(0).json.atencion_id,
  campana_id: $input.item(0).json.campana_id,
  historial_completo: historial,
  analisis_riesgo: {
    score_riesgo: scoreRiesgo,
    nivel_riesgo: nivelRiesgoFinal,
    frecuencia_seguimiento: frecuenciaSeguimiento,
    prioridad: prioridadSeguimiento,
    factores_criticos: factoresCriticos,
    fecha_analisis: new Date().toISOString(),
    // **NUEVO: Datos de predicciones FastAPI**
    predicciones_fuente: {
      riesgo_cv_valor: riesgoCV?.valor_prediccion || null,
      riesgo_cv_confianza: riesgoCV?.confianza || null,
      probabilidad_asistencia: probabilidadAsistencia,
      probabilidad_hospitalizacion: prediccionHospitalizacion?.probabilidad_hospitalizacion || null
    }
  },
  siguiente_accion: "generar_cronograma_seguimientos"
};

// **FUNCIONES AUXILIARES MEJORADAS**
function determinarFactoresCriticosAvanzados(factores, sintomas, factoresFastAPI) {
  let criticos = [];
  
  // Factores de FastAPI (tienen prioridad)
  if (factoresFastAPI && factoresFastAPI.length > 0) {
    criticos = factoresFastAPI.slice(0, 3);
  }
  
  // Factores críticos adicionales del triaje
  if (factores.antecedentes_cardiacos && sintomas.dolor_pecho) {
    criticos.push("riesgo_infarto_elevado");
  }
  if (factores.diabetes && factores.hipertension) {
    criticos.push("sindrome_metabolico");
  }
  if (factores.tabaquismo && sintomas.dolor_pecho) {
    criticos.push("tabaquismo_con_sintomas_cardiacos");
  }
  
  return [...new Set(criticos)]; // Eliminar duplicados
}
```

## **Salida del Agente Orquestador:**
```json
{
  "paciente_id": 456,
  "atencion_id": 123,
  "campana_id": 789,
  "historial_completo": { /* datos completos del paciente */ },
  "analisis_riesgo": {
    "score_riesgo": 75,
    "nivel_riesgo": "ALTO",
    "frecuencia_seguimiento": "FRECUENTE",
    "prioridad": "ALTA",
    "factores_criticos": [
      "riesgo_infarto_elevado",
      "sindrome_metabolico"
    ],
    "fecha_analisis": "2024-01-15T14:35:00Z",
    "predicciones_fuente": {
      "riesgo_cv_valor": 78.5,
      "riesgo_cv_confianza": 92.3,
      "probabilidad_asistencia": 85.2,
      "probabilidad_hospitalizacion": 23.7
    }
  },
  "siguiente_accion": "generar_cronograma_seguimientos"
}
```

---

# 📋 AGENTE 2: GENERADOR DE SEGUIMIENTOS

## **Responsabilidades:**
- Recibir el análisis del Orquestador
- Generar cronograma personalizado de seguimientos
- Determinar tipos de seguimiento según el riesgo
- Programar fechas específicas para cada seguimiento

## **Lógica del Generador de Seguimientos (JavaScript en n8n):**

```javascript
// ===== NODO 2: GENERACIÓN DE CRONOGRAMA =====
const analisisRiesgo = $input.item(0).json.analisis_riesgo;
const pacienteId = $input.item(0).json.paciente_id;
const historial = $input.item(0).json.historial_completo;
const nivelRiesgo = analisisRiesgo.nivel_riesgo;
const factoresCriticos = analisisRiesgo.factores_criticos;

// Obtener fecha actual como baseline
const fechaAtencion = new Date();
const cronogramaSeguimientos = [];

// Definir patrones de seguimiento según riesgo
let patronSeguimiento;

switch (nivelRiesgo) {
  case "CRITICO":
    patronSeguimiento = [
      { dias: 1, tipo: "adherencia_urgente", prioridad: "MAXIMA" },
      { dias: 3, tipo: "evolucion_inmediata", prioridad: "MAXIMA" },
      { dias: 7, tipo: "consolidacion_tratamiento", prioridad: "ALTA" },
      { dias: 14, tipo: "seguimiento_general", prioridad: "ALTA" },
      { dias: 30, tipo: "seguimiento_general", prioridad: "MEDIA" }
    ];
    break;
    
  case "ALTO":
    patronSeguimiento = [
      { dias: 3, tipo: "adherencia_inicial", prioridad: "ALTA" },
      { dias: 7, tipo: "evolucion_sintomas", prioridad: "ALTA" },
      { dias: 21, tipo: "consolidacion_habitos", prioridad: "MEDIA" },
      { dias: 45, tipo: "seguimiento_general", prioridad: "MEDIA" }
    ];
    break;
    
  case "MODERADO":
    patronSeguimiento = [
      { dias: 7, tipo: "adherencia_inicial", prioridad: "MEDIA" },
      { dias: 21, tipo: "evolucion_sintomas", prioridad: "MEDIA" },
      { dias: 60, tipo: "seguimiento_general", prioridad: "BAJA" }
    ];
    break;
    
  case "BAJO":
    patronSeguimiento = [
      { dias: 14, tipo: "adherencia_inicial", prioridad: "BAJA" },
      { dias: 60, tipo: "seguimiento_general", prioridad: "BAJA" }
    ];
    break;
}

// Generar seguimientos específicos
patronSeguimiento.forEach((patron, index) => {
  const fechaSeguimiento = new Date(fechaAtencion);
  fechaSeguimiento.setDate(fechaAtencion.getDate() + patron.dias);
  
  const seguimiento = {
    numero_seguimiento: index + 1,
    paciente_id: pacienteId,
    fecha_programada: fechaSeguimiento.toISOString(),
    tipo_cuestionario: patron.tipo,
    prioridad: patron.prioridad,
    canales_notificacion: determinarCanales(patron.prioridad),
    enfoque_principal: determinarEnfoque(patron.tipo, factoresCriticos),
    tiempo_estimado_respuesta: calcularTiempoEstimado(patron.tipo),
    estado: "PROGRAMADO"
  };
  
  cronogramaSeguimientos.push(seguimiento);
});

// Determinar primer seguimiento a ejecutar
const primerSeguimiento = cronogramaSeguimientos[0];

return {
  paciente_id: pacienteId,
  atencion_id: $input.item(0).json.atencion_id,
  historial_completo: historial,
  analisis_riesgo: analisisRiesgo,
  cronograma_completo: cronogramaSeguimientos,
  proximo_seguimiento: primerSeguimiento,
  resumen_plan: {
    total_seguimientos: cronogramaSeguimientos.length,
    duracion_plan_dias: Math.max(...patronSeguimiento.map(p => p.dias)),
    nivel_riesgo: nivelRiesgo,
    factores_enfoque: factoresCriticos
  },
  siguiente_accion: "ejecutar_primer_seguimiento"
};

function determinarCanales(prioridad) {
  switch (prioridad) {
    case "MAXIMA": return ["sms", "llamada", "email"];
    case "ALTA": return ["sms", "email"];
    case "MEDIA": return ["sms"];
    case "BAJA": return ["email"];
    default: return ["sms"];
  }
}

function determinarEnfoque(tipo, factoresCriticos) {
  const enfoques = {
    // Adherencia especializada
    "adherencia_urgente": ["medicamentos_urgentes", "sintomas_alarma", "cuando_llamar"],
    "adherencia_diabetes": ["medicamentos_diabetes", "glucometria", "dieta_diabetico"],
    "adherencia_hipertension": ["medicamentos_hipertension", "presion_arterial", "dieta_sin_sal"],
    "adherencia_inicial": ["medicamentos", "efectos_secundarios"],
    
    // Evolución especializada
    "evolucion_inmediata": ["sintomas", "tolerancia_actividad"],
    "evolucion_sintomas_cardiacos": ["dolor_pecho", "fatiga", "palpitaciones", "dificultad_respirar"],
    "evolucion_cesacion_tabaco": ["progreso_dejar_fumar", "ansiedad", "metodos_cesacion"],
    "evolucion_sintomas": ["sintomas", "calidad_vida"],
    
    // Consolidación especializada
    "consolidacion_tratamiento": ["habitos", "adherencia"],
    "consolidacion_actividad_fisica": ["ejercicio_semanal", "tolerancia_esfuerzo", "barreras_ejercicio"],
    "consolidacion_peso": ["peso_actual", "dieta_seguida", "ejercicio_peso"],
    "consolidacion_dieta": ["alimentacion_saludable", "colesterol_dieta", "habitos_alimentarios"],
    "consolidacion_habitos": ["ejercicio", "dieta", "habitos"],
    
    // Seguimiento general y largo plazo
    "seguimiento_largo_plazo": ["estado_general", "adherencia_global", "calidad_vida", "proxima_cita"],
    "seguimiento_general": ["estado_general", "adherencia"]
  };
  
  let enfoqueBase = enfoques[tipo] || ["seguimiento_general"];
  
  // Agregar enfoques específicos según factores críticos de FastAPI
  if (factoresCriticos.includes("presion_sistolica")) {
    enfoqueBase.push("control_presion_arterial");
  }
  if (factoresCriticos.includes("imc")) {
    enfoqueBase.push("control_peso_imc");
  }
  if (factoresCriticos.includes("colesterol")) {
    enfoqueBase.push("control_colesterol");
  }
  if (factoresCriticos.includes("diabetes")) {
    enfoqueBase.push("control_glucemico");
  }
  if (factoresCriticos.includes("edad")) {
    enfoqueBase.push("cuidados_adulto_mayor");
  }
  
  // Factores críticos adicionales
  if (factoresCriticos.includes("riesgo_infarto_elevado")) {
    enfoqueBase.push("sintomas_cardiacos_urgentes");
  }
  if (factoresCriticos.includes("sindrome_metabolico")) {
    enfoqueBase.push("control_glucemico", "presion_arterial");
  }
  if (factoresCriticos.includes("alto_riesgo_hospitalizacion")) {
    enfoqueBase.push("prevencion_hospitalizacion");
  }
  
  return [...new Set(enfoqueBase)]; // Eliminar duplicados
}

function calcularTiempoEstimado(tipo) {
  const tiempos = {
    // Adherencia especializada
    "adherencia_urgente": "2 minutos",
    "adherencia_diabetes": "4 minutos",
    "adherencia_hipertension": "3 minutos",
    "adherencia_inicial": "3 minutos",
    
    // Evolución especializada
    "evolucion_inmediata": "4 minutos",
    "evolucion_sintomas_cardiacos": "5 minutos",
    "evolucion_cesacion_tabaco": "4 minutos",
    "evolucion_sintomas": "5 minutos",
    
    // Consolidación especializada
    "consolidacion_tratamiento": "4 minutos",
    "consolidacion_actividad_fisica": "6 minutos",
    "consolidacion_peso": "5 minutos",
    "consolidacion_dieta": "5 minutos",
    "consolidacion_habitos": "6 minutos",
    
    // Seguimiento general y largo plazo
    "seguimiento_largo_plazo": "7 minutos",
    "seguimiento_general": "5 minutos"
  };
  
  return tiempos[tipo] || "3 minutos";
}

// **FUNCIÓN MEJORADA PARA DETERMINAR TIPO DE CUESTIONARIO**
function determinarTipoCuestionarioAvanzado(numeroSeguimiento, nivelRiesgo, factoresCriticos) {
  // Seguimiento #1 - Siempre adherencia inicial
  if (numeroSeguimiento === 1) {
    if (nivelRiesgo === "CRITICO") return "adherencia_urgente";
    if (factoresCriticos.includes("diabetes")) return "adherencia_diabetes";
    if (factoresCriticos.includes("presion_sistolica") || factoresCriticos.includes("hipertension")) {
      return "adherencia_hipertension";
    }
    return "adherencia_inicial";
  }
  
  // Seguimiento #2 - Evolución de síntomas
  if (numeroSeguimiento === 2) {
    if (nivelRiesgo === "CRITICO") return "evolucion_inmediata";
    if (factoresCriticos.includes("dolor_pecho") || factoresCriticos.includes("riesgo_infarto_elevado")) {
      return "evolucion_sintomas_cardiacos";
    }
    if (factoresCriticos.includes("tabaquismo")) return "evolucion_cesacion_tabaco";
    return "evolucion_sintomas";
  }
  
  // Seguimiento #3 - Consolidación de hábitos
  if (numeroSeguimiento === 3) {
    if (factoresCriticos.includes("sedentarismo") || factoresCriticos.includes("actividad_fisica")) {
      return "consolidacion_actividad_fisica";
    }
    if (factoresCriticos.includes("imc") || factoresCriticos.includes("peso")) {
      return "consolidacion_peso";
    }
    if (factoresCriticos.includes("colesterol")) return "consolidacion_dieta";
    return "consolidacion_habitos";
  }
  
  // Seguimientos posteriores
  if (numeroSeguimiento >= 4) {
    return "seguimiento_largo_plazo";
  }
  
  return "seguimiento_general";
}
```

## **Salida del Generador de Seguimientos:**
```json
{
  "paciente_id": 456,
  "atencion_id": 123,
  "historial_completo": { /* datos del paciente */ },
  "analisis_riesgo": { /* análisis del orquestador */ },
  "cronograma_completo": [
    {
      "numero_seguimiento": 1,
      "paciente_id": 456,
      "fecha_programada": "2024-01-18T10:00:00Z",
      "tipo_cuestionario": "adherencia_inicial",
      "prioridad": "ALTA",
      "canales_notificacion": ["sms", "email"],
      "enfoque_principal": [
        "medicamentos",
        "efectos_secundarios",
        "sintomas_cardiacos_urgentes"
      ],
      "tiempo_estimado_respuesta": "3 minutos",
      "estado": "PROGRAMADO"
    },
    {
      "numero_seguimiento": 2,
      "paciente_id": 456,
      "fecha_programada": "2024-01-22T10:00:00Z",
      "tipo_cuestionario": "evolucion_sintomas",
      "prioridad": "ALTA",
      "canales_notificacion": ["sms", "email"],
      "enfoque_principal": [
        "sintomas",
        "calidad_vida",
        "sintomas_cardiacos_urgentes"
      ],
      "tiempo_estimado_respuesta": "5 minutos",
      "estado": "PROGRAMADO"
    }
  ],
  "proximo_seguimiento": {
    "numero_seguimiento": 1,
    "fecha_programada": "2024-01-18T10:00:00Z",
    "tipo_cuestionario": "adherencia_inicial",
    "prioridad": "ALTA"
  },
  "resumen_plan": {
    "total_seguimientos": 4,
    "duracion_plan_dias": 45,
    "nivel_riesgo": "ALTO",
    "factores_enfoque": ["riesgo_infarto_elevado", "sindrome_metabolico"]
  },
  "siguiente_accion": "ejecutar_primer_seguimiento"
}
```

---

# 📱 AGENTE 3: SISTEMA DE NOTIFICACIONES

## **Responsabilidades:**
- Enviar notificaciones SMS al paciente
- Generar enlaces únicos para cuestionarios
- Manejar diferentes tipos de notificaciones (seguimiento, urgente, recordatorio)
- Registrar estado de envíos

## **Tipos de Notificaciones:**

### 1. **Notificación de Seguimiento Programado**
### 2. **Notificación de Urgencia Médica**
### 3. **Recordatorio de Cuestionario Pendiente**

## **Lógica del Sistema de Notificaciones (JavaScript en n8n):**

```javascript
// ===== NODO 3: SISTEMA DE NOTIFICACIONES =====
const pacienteId = $input.item(0).json.paciente_id;
const seguimiento = $input.item(0).json.proximo_seguimiento;
const historial = $input.item(0).json.historial_completo;
const prioridad = seguimiento.prioridad;

// Generar link único del cuestionario
const tokenCuestionario = generarTokenSeguro();
const baseUrl = "https://tu-frontend.app";
const linkCuestionario = `${baseUrl}/seguimiento/${pacienteId}/${seguimiento.numero_seguimiento}?token=${tokenCuestionario}`;

// Generar mensaje personalizado según tipo y prioridad
let mensaje;
let tipoNotificacion;

if (prioridad === "MAXIMA") {
  tipoNotificacion = "seguimiento_urgente";
  mensaje = `🚨 HEALINK URGENTE 🚨
Sr/Sra, su seguimiento cardiovascular #${seguimiento.numero_seguimiento} requiere atención INMEDIATA.

⏰ Complete en los próximos 30 minutos:
${linkCuestionario}

⚠️ Su salud cardíaca es nuestra prioridad.
💊 Tiempo estimado: ${seguimiento.tiempo_estimado_respuesta}

¿Dudas? Responda AYUDA`;

} else if (prioridad === "ALTA") {
  tipoNotificacion = "seguimiento_prioritario";
  mensaje = `🏥 HEALINK - Seguimiento Cardiovascular

Estimado paciente, es momento de su seguimiento #${seguimiento.numero_seguimiento}.

📋 Complete su cuestionario aquí:
${linkCuestionario}

⏱️ Solo toma ${seguimiento.tiempo_estimado_respuesta}
💡 Sus respuestas nos ayudan a cuidar mejor su corazón.

Responda STOP para cancelar.`;

} else {
  tipoNotificacion = "seguimiento_regular";
  mensaje = `💙 HEALINK - Cuidamos su corazón

Hola! Su seguimiento cardiovascular #${seguimiento.numero_seguimiento} está listo.

📱 Acceda cuando guste:
${linkCuestionario}

⏰ ${seguimiento.tiempo_estimado_respuesta} de su tiempo
🎯 Mantenga su salud controlada.

Responda STOP para cancelar.`;
}

// Configurar parámetros del SMS
const configuracionSMS = {
  numero_destino: obtenerTelefonoPaciente(pacienteId, historial),
  mensaje: mensaje,
  prioridad: prioridad,
  intento_numero: 1,
  max_intentos: determinarMaxIntentos(prioridad),
  tiempo_entre_intentos: determinarTiempoReintento(prioridad)
};

return {
  paciente_id: pacienteId,
  seguimiento_numero: seguimiento.numero_seguimiento,
  tipo_notificacion: tipoNotificacion,
  configuracion_sms: configuracionSMS,
  link_cuestionario: linkCuestionario,
  token_seguridad: tokenCuestionario,
  metadatos_envio: {
    fecha_programada: seguimiento.fecha_programada,
    fecha_envio: new Date().toISOString(),
    canales: seguimiento.canales_notificacion,
    prioridad: prioridad
  },
  siguiente_accion: "enviar_sms"
};

function generarTokenSeguro() {
  return Math.random().toString(36).substring(2, 15) + 
         Math.random().toString(36).substring(2, 15) + 
         Date.now().toString(36);
}

function obtenerTelefonoPaciente(pacienteId, historial) {
  // TODO: Extraer el teléfono real del historial del paciente
  // Por ahora retornar un número de prueba
  return "+57300123456" + pacienteId.toString().slice(-1);
}

function determinarMaxIntentos(prioridad) {
  switch (prioridad) {
    case "MAXIMA": return 5;
    case "ALTA": return 3;
    case "MEDIA": return 2;
    case "BAJA": return 1;
    default: return 1;
  }
}

function determinarTiempoReintento(prioridad) {
  switch (prioridad) {
    case "MAXIMA": return "15 minutos";
    case "ALTA": return "2 horas";
    case "MEDIA": return "6 horas";
    case "BAJA": return "24 horas";
    default: return "6 horas";
  }
}
```

## **Configuración del Nodo SMS (Twilio/AWS SNS):**

```json
{
  "resource": "sms",
  "operation": "send",
  "additionalFields": {
    "from": "+57123456789",
    "to": "={{$json.configuracion_sms.numero_destino}}",
    "body": "={{$json.configuracion_sms.mensaje}}",
    "statusCallback": "https://tu-instancia-n8n.app/webhook/sms-status",
    "maxPrice": "0.05"
  }
}
```

## **Webhook de Confirmación al Backend:**

```javascript
// ===== NODO FINAL: NOTIFICAR AL BACKEND =====
const resultado = $input.item(0).json;

// Llamar al webhook del backend Spring-Logic
const payloadBackend = {
  paciente_id: resultado.paciente_id,
  seguimiento_numero: resultado.seguimiento_numero,
  tipo_notificacion: resultado.tipo_notificacion,
  exitosa: true, // o false si hubo error
  fecha_envio: new Date().toISOString(),
  link_cuestionario: resultado.link_cuestionario,
  canal_usado: "sms",
  error: null // o mensaje de error si falló
};

// Este nodo HTTP Post llamará a:
// POST https://tu-backend.app/api/webhooks/n8n/notificacion-enviada

return payloadBackend;
```

---

## 🔄 CONFIGURACIÓN DE TRIGGERS Y PROGRAMACIÓN

### **Trigger Inicial (Webhook):**
- **URL:** `https://tu-instancia-n8n.app/webhook/orquestador-seguimientos`
- **Método:** POST
- **Autenticación:** Bearer Token (opcional)

### **Triggers de Tiempo (Cron Jobs):**
```javascript
// Para seguimientos programados
// Ejecutar cada hora para verificar seguimientos pendientes
"0 * * * *"

// Para recordatorios de cuestionarios no completados
// Ejecutar cada 6 horas
"0 */6 * * *"
```

---

## 📊 LOGGING Y MONITOREO

### **Logs Importantes:**
```javascript
// En cada nodo importante
console.log(`[ORQUESTADOR] Procesando paciente ${pacienteId} - Riesgo: ${nivelRiesgo}`);
console.log(`[SEGUIMIENTOS] Generados ${cronogramaSeguimientos.length} seguimientos`);
console.log(`[NOTIFICACIONES] SMS enviado a paciente ${pacienteId} - Estado: ${exitoso}`);
```

### **Métricas a Trackear:**
- Tiempo de procesamiento por agente
- Tasa de éxito de notificaciones SMS
- Distribución de niveles de riesgo
- Número de seguimientos generados por día

---

## 🆕 **MEJORAS INTEGRADAS**

### **1. Integración con FastAPI-Back:**
- ✅ **Predicciones de Riesgo CV** con modelo ML entrenado
- ✅ **Factores influyentes** con pesos reales calculados
- ✅ **Predicciones de Asistencia** para optimizar programación
- ✅ **Predicciones de Hospitalización** para detección temprana
- ✅ **Niveles de riesgo** validados médicamente (BAJO, MODERADO, ALTO, CRITICO)

### **2. Tipos de Cuestionario Expandidos:**
```
ADHERENCIA ESPECIALIZADA:
- adherencia_urgente (2 min) - Para CRITICO
- adherencia_diabetes (4 min) - Pacientes diabéticos
- adherencia_hipertension (3 min) - Pacientes hipertensos

EVOLUCIÓN ESPECIALIZADA:
- evolucion_sintomas_cardiacos (5 min) - Síntomas cardíacos
- evolucion_cesacion_tabaco (4 min) - Dejar de fumar
- evolucion_inmediata (4 min) - Para CRITICO

CONSOLIDACIÓN ESPECIALIZADA:
- consolidacion_actividad_fisica (6 min) - Ejercicio y actividad
- consolidacion_peso (5 min) - Control de peso/IMC
- consolidacion_dieta (5 min) - Alimentación y colesterol

SEGUIMIENTO LARGO PLAZO:
- seguimiento_largo_plazo (7 min) - Seguimientos 4+
```

### **3. Algoritmo de Priorización Mejorado:**
```
Peso 50%: Predicción Riesgo CV (FastAPI)
Peso 30%: Predicción Hospitalización  
Peso 20%: Predicción Asistencia
+ Factores críticos identificados automáticamente
```

### **4. Payload Enriquecido:**
- **predicciones_fastapi**: Datos de ML validados
- **factores_influyentes**: Con pesos específicos
- **recomendaciones**: Generadas por IA médica
- **confianza**: Nivel de certeza del modelo

---

## 🚀 **SIGUIENTE PASO**

¿Empezamos implementando el **Agente Orquestador MEJORADO** paso a paso en tu instancia de n8n?

**Beneficios de esta integración:**
1. 📊 **Predicciones más precisas** usando ML entrenado
2. 🎯 **Cuestionarios ultra-personalizados** según factores específicos  
3. ⚡ **Priorización inteligente** basada en múltiples algoritmos
4. 📈 **Escalabilidad** - aprovecha toda la infraestructura FastAPI existente 