# 🤖 CONFIGURACIÓN AI AGENT CON GROQ EN N8N

## 📋 **REQUISITOS PREVIOS:**
1. ✅ n8n instalado y funcionando
2. ✅ API Key de Groq (gratuita)
3. ✅ Workflow importado

---

## 🔑 **PASO 1: OBTENER API KEY GROQ**

### **Obtener Groq API Key (GRATIS):**
```
1. Ir a: https://console.groq.com/keys
2. Registrarse/Login
3. Crear nueva API Key
4. Copiar la key (empieza con "gsk_...")
```

---

## ⚙️ **PASO 2: CONFIGURAR CREDENCIALES EN N8N**

### **2.1 Crear Credencial OpenAI (Para Groq):**
```
1. En n8n: Settings → Credentials
2. Agregar nueva credencial
3. Buscar: "OpenAI"
4. Configurar:
   - Name: "Groq API"
   - API Key: [TU_GROQ_API_KEY]
   - Base URL: "https://api.groq.com/openai/v1"
```

### **2.2 Configurar Chat Model:**
```
Nodo: 💬 Chat Model
Configuración:
- Credential: "Groq API" (la que creaste)
- Model: "llama-3.1-8b-instant"
- System Message: "Eres un cardiólogo experto..."
```

---

## 🤖 **PASO 3: CONFIGURAR AI AGENT**

### **3.1 Configuración AI Agent:**
```
Nodo: 🤖 AI Agent Cardiovascular
Parámetros:
- Session Key: ={{ $json.paciente_id }}
- Options: (dejar por defecto)

Conexiones requeridas:
✅ Chat Model → AI Agent (ai_chatModel)
✅ Buffer Memory → AI Agent (ai_memory)
```

### **3.2 Configuración Memory:**
```
Nodo: 🧠 Buffer Memory
Parámetros:
- Session Key: ={{ $('📋 Preparar para AI Agent').item(0).json.paciente_id }}
- Max Tokens: 1000
```

---

## 🔄 **PASO 4: FLUJO DE DATOS**

### **INPUT al AI Agent:**
```javascript
// El nodo "📋 Preparar para AI Agent" envía:
{
  "paciente_id": 1,
  "contexto_medico": { ... },
  "prompt_ai_agent": "Como cardiólogo experto, analiza..."
}
```

### **OUTPUT del AI Agent:**
```javascript
// El AI Agent responde:
{
  "output": "RIESGO: ALTO\nSCORE: 75\nJUSTIFICACIÓN: Paciente con..."
}
```

---

## 🎯 **PASO 5: FLUJO COMPLETO CORRECTO**

```
🌐 Webhook → 📋 Preparar → 🤖 AI Agent → 🎯 Seguimientos → ✅ Respuesta
                           ↗️       ↗️
                    💬 Chat Model  🧠 Memory
```

### **Seguimientos Generados (CORREGIDOS):**
- ✅ **Máximo 3 seguimientos**
- ✅ **Máximo 45 días**
- ✅ **Solo cuestionarios chatbot**
- ✅ **Ejemplo:** Día 7, Día 20

---

## 🧪 **PASO 6: PROBAR CONFIGURACIÓN**

### **Test Request:**
```json
POST http://localhost:5678/webhook/orquestador-seguimientos-ai
{
  "paciente_id": 1,
  "campana_id": 1,
  "atencion_id": 123,
  "historial_clinico": {
    "datos_basicos": {
      "edad": 65,
      "genero": "M"
    },
    "diagnosticos": {
      "codigo_cie10": "I25.9",
      "descripcion": "Enfermedad cardíaca isquémica"
    }
  }
}
```

### **Expected Response:**
```json
{
  "status": "completado",
  "cluster_asignado": "ALTO_RIESGO_ESTABLE",
  "seguimientos": {
    "total_generados": 2,
    "primer_seguimiento": "15/12/2024",
    "ultimo_seguimiento": "28/12/2024",
    "solo_cuestionarios": true,
    "maximo_dias": 45
  },
  "para_companero": {
    "tipo_interaccion": "CUESTIONARIO_CHATBOT",
    "total_cuestionarios_crear": 2
  }
}
```

---

## 🚨 **TROUBLESHOOTING**

### **Error: "Model not found"**
```
Solución: 
- Verificar Base URL: https://api.groq.com/openai/v1
- Usar modelo: "llama-3.1-8b-instant"
```

### **Error: "API Key invalid"**
```
Solución:
- Regenerar API key en console.groq.com
- Verificar que empiece con "gsk_"
```

### **Error: "AI Agent no conecta"**
```
Solución:
- Verificar conexiones: Chat Model → AI Agent
- Verificar conexiones: Memory → AI Agent
```

---

## ✅ **VENTAJAS ESTA CONFIGURACIÓN:**

1. **🆓 Gratuito:** Groq tiene tier generoso
2. **⚡ Rápido:** Llama 3.1 es muy eficiente  
3. **🧠 Inteligente:** AI Agent más flexible que HTTP
4. **📊 Correcto:** 1-3 seguimientos, máximo 45 días
5. **🤖 Solo cuestionarios:** Como especificaste

¿Te ayudo a configurar algún paso específico? 