# 🧠 Sistema Multi-Agente Cardiovascular

## 📋 Índice de Organización

```
docs/sistema-multiagente/
├── README_SISTEMA_MULTIAGENTE.md          # Esta documentación central
├── agentes-inteligentes/                  # 🤖 Algoritmos ML implementados
│   ├── kmeans-clustering-agent.md         # K-Means para clustering de pacientes
│   ├── beam-search-questionnaire.md       # Beam Search para optimización
│   └── algoritmos-comparacion.md          # Comparativa de algoritmos
├── flujos-n8n/                           # 🔄 Flujos y configuraciones n8n
│   ├── orchestrator-agent-config.md      # Configuración agente orquestador
│   ├── webhooks-configuration.md         # Configuración de webhooks
│   └── javascript-agents-code.md         # Códigos JavaScript para n8n
├── integraciones/                         # 🔗 Integraciones entre sistemas
│   ├── spring-fastapi-integration.md     # Integración Spring ↔ FastAPI
│   ├── n8n-backend-integration.md        # Integración n8n ↔ Backend
│   └── endpoints-api-reference.md        # Referencia completa de APIs
├── documentacion-tecnica/                 # 📚 Documentación técnica
│   ├── arquitectura-sistema.md           # Arquitectura completa
│   ├── casos-uso-academicos.md           # Casos de uso para evaluación
│   └── instalacion-configuracion.md      # Guía de instalación
└── comparativas/                         # 📊 Análisis comparativos
    ├── tradicional-vs-inteligente.md     # Comparación de enfoques
    └── metricas-evaluacion.md            # Métricas y resultados
```

## 🎯 Descripción del Sistema

### **Arquitectura Híbrida: Tradicional + Inteligente**

Nuestro sistema implementa **DOS enfoques complementarios**:

1. **🔧 Sistema Tradicional (Base)**: Lógica de negocio robusta con reglas predefinidas
2. **🧠 Sistema Inteligente (Mejorado)**: Agentes ML que optimizan y personalizan

### **Distribución de Responsabilidades**

#### **Desarrollador A (Usuario):**
- 🎯 **Orquestador Master** (n8n + K-Means clustering)
- 📋 **Generador de Seguimientos** (Algoritmo híbrido)
- 🔔 **Sistema de Notificaciones** (Priorización inteligente)

#### **Desarrollador B (Compañero):**
- 💬 **Agente Conversacional** (Beam Search para cuestionarios)
- 🔍 **Analizador de Respuestas** (Procesamiento de datos)
- 💡 **Generador de Recomendaciones** (IA médica)

## 🤖 Agentes Inteligentes Implementados

### **1. K-Means Clustering Agent**
- **Algoritmo**: K-Means con 6 features cardiovasculares
- **Objetivo**: Agrupar pacientes por perfiles de riesgo
- **Features**:
  - Riesgo cardiovascular (0-100%)
  - Probabilidad hospitalización (0-100%)
  - Edad normalizada (18-100 años)
  - Factores de riesgo count (0-10)
  - Adherencia histórica (0-100%)
  - Días desde última consulta (0-365)

### **2. Beam Search Questionnaire Agent (Pendiente)**
- **Algoritmo**: Beam Search con beam width = 3
- **Objetivo**: Optimizar secuencias de preguntas
- **Heurística**: Probabilidad de obtener información diagnóstica útil

## 📊 Integración con Lógica Original

### **¿Cómo K-Means COMPLEMENTA la lógica original?**

| **Aspecto** | **Sistema Original** | **Con K-Means** |
|-------------|---------------------|-----------------|
| **Priorización** | Manual por riesgo CV | **Automática por cluster** |
| **Personalización** | Reglas fijas | **Estrategias por perfil** |
| **Adaptabilidad** | Estática | **Dinámica con aprendizaje** |
| **Escalabilidad** | Limitada | **Escalable con más pacientes** |

### **Flujo Híbrido:**

1. **📡 Webhook n8n** → Recibe trigger de consulta
2. **🧠 K-Means Agent** → Clasifica paciente en cluster
3. **📋 Generador Original** → Usa estrategia del cluster
4. **🔄 Orquestador** → Combina ambos enfoques
5. **📨 Notificaciones** → Prioriza por cluster

## 🚀 Ventajas del Sistema Híbrido

✅ **Mantiene** la lógica de negocio robusta original
✅ **Añade** inteligencia artificial para personalización  
✅ **Mejora** la precisión de seguimientos
✅ **Permite** evaluación académica comparativa
✅ **Escalable** a más algoritmos (Q-Learning, Monte Carlo, etc.)

## 📈 Métricas de Evaluación

### **Para Sistemas Inteligentes:**
- **Precisión de clustering**: Silhouette Score
- **Convergencia**: Iteraciones hasta threshold
- **Personalización**: Varianza entre estrategias
- **Adherencia mejorada**: % comparativo

### **Para Comparación Académica:**
- **Tiempo de respuesta**: Tradicional vs Inteligente
- **Precisión de recomendaciones**: Hit rate
- **Satisfacción del paciente**: Encuestas post-seguimiento
- **Efectividad clínica**: Resultados de salud

## 🛠️ Tecnologías Utilizadas

- **Backend**: Spring Boot + Java 17
- **ML Backend**: FastAPI + Python (predicciones existentes)
- **Orquestación**: n8n (flows + JavaScript agents)
- **Base de Datos**: PostgreSQL
- **Algoritmos**: K-Means, Beam Search (pendiente)
- **APIs**: REST + Webhooks

## 📋 Estado Actual

### ✅ **Implementado:**
- K-Means Clustering Agent (completo)
- Intelligent Follow-Up Generator Service
- Endpoints comparativos tradicional vs inteligente
- Integración con predicciones FastAPI existentes

### 🔄 **En Progreso:**
- Beam Search Agent (Desarrollador B)
- Testing completo del sistema híbrido
- Configuración webhooks n8n

### 📝 **Pendiente:**
- Q-Learning para adaptación dinámica
- Monte Carlo para simulación de resultados
- Alfa-Beta pruning para decisiones médicas

---

**🎓 Nota Académica**: Este sistema demuestra la evolución de un sistema tradicional de reglas hacia un sistema multi-agente inteligente, manteniendo la robustez original mientras añade capacidades de aprendizaje automático. 