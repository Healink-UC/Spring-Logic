# 🤖 GUÍA COMPLETA: CONFIGURAR AI AGENT EN N8N

## 📋 **ESTADO ACTUAL:**
- ✅ n8n ejecutándose en: http://localhost:5678
- ✅ Spring Boot ejecutándose en: http://localhost:8090
- ✅ Workflow JSON corregido y listo

---

## 🔑 **PASO 1: OBTENER API KEY GROQ (GRATIS)**

### **1.1 Acceder a Groq Console:**
```
🌐 URL: https://console.groq.com/keys
📝 Registrarse/Iniciar sesión (gratuito)
```

### **1.2 Crear API Key:**
```
1. Clic en "Create API Key"
2. Nombre: "n8n-healink"
3. Clic en "Submit"
4. 📋 COPIAR la API key (empieza con "gsk_...")
```

---

## 🛠️ **PASO 2: ABRIR N8N Y CONFIGURAR**

### **2.1 Acceder a n8n:**
```
🌐 URL: http://localhost:5678
🔐 Si pide login, usar credenciales de setup inicial
```

### **2.2 Configurar Credencial OpenAI (para Groq):**
```
1. En n8n: Menú izquierdo → "Settings" → "Credentials"
2. Clic en "Add credential"
3. Buscar y seleccionar: "OpenAI"
4. Configurar:
   - Name: "Groq API"
   - API Key: [PEGAR_TU_API_KEY_GROQ]
   - Base URL: "https://api.groq.com/openai/v1"
5. Clic en "Save"
```

---

## 📦 **PASO 3: IMPORTAR WORKFLOW**

### **3.1 Importar workflow-ai-agent-seguimientos.json:**
```
1. En n8n: Menú izquierdo → "Workflows"
2. Clic en "Import from file"
3. Seleccionar: Spring-Logic/src/docs/sistema-multiagente/agentes-inteligentes/workflow-ai-agent-seguimientos.json
4. Clic en "Import"
```

### **3.2 Verificar estructura del workflow:**
```
Flujo esperado:
🌐 Webhook → 📋 Preparar → 🤖 AI Agent → 🎯 Seguimientos → ✅ Respuesta
                           ↗️       ↗️
                   💬 Chat Model  🧠 Memory
```

---

## ⚙️ **PASO 4: CONFIGURAR NODOS DEL WORKFLOW**

### **4.1 Configurar Chat Model:**
```
Nodo: 💬 Chat Model
1. Clic en el nodo
2. En "Credential": Seleccionar "Groq API"
3. En "Model": Cambiar a "llama-3.1-8b-instant"
4. En "System Message": Mantener el existente
5. Clic en "Execute node" para probar
```

### **4.2 Configurar AI Agent:**
```
Nodo: 🤖 AI Agent Cardiovascular
1. Verificar que esté conectado a:
   - Chat Model (ai_chatModel)
   - Buffer Memory (ai_memory)
2. Session Key: ={{ $json.paciente_id }}
3. No cambiar otras opciones
```

### **4.3 Configurar Buffer Memory:**
```
Nodo: 🧠 Buffer Memory
1. Session Key: ={{ $('📋 Preparar para AI Agent').item(0).json.paciente_id }}
2. Max Tokens: 1000
3. No cambiar otras opciones
```

---

## 🔄 **PASO 5: ACTIVAR WORKFLOW**

### **5.1 Guardar y activar:**
```
1. Clic en "Save" (arriba derecha)
2. Nombre sugerido: "AI Agent Seguimientos Cardiovasculares"
3. Toggle del switch "Active" a ON
4. Verificar que aparezca "Active" en verde
```

### **5.2 Obtener URL del webhook:**
```
Nodo: 🌐 Webhook Entrada
1. Clic en el nodo
2. Clic en "Execute node" 
3. Copiar URL que aparece: 
   http://localhost:5678/webhook/orquestador-seguimientos-ai
```

---

## 🧪 **PASO 6: PROBAR CONFIGURACIÓN**

### **6.1 Test básico desde PowerShell:**
```powershell
# Ir al directorio Spring-Logic
cd C:\Users\juand\OneDrive\Archivos\UCaldasFiles\ProyectoIntegrador\Proyecto\Spring-Logic

# Probar con datos del paciente 1
Invoke-RestMethod -Uri "http://localhost:8090/api/test/n8n/test-paciente/1" -Method POST -ContentType "application/json"
```

### **6.2 Test directo del webhook:**
```powershell
# Test directo al webhook del AI Agent
$body = @{
    paciente_id = 1
    campana_id = 1
    atencion_id = 123
    historial_clinico = @{
        datos_basicos = @{
            edad = 65
            genero = "M"
        }
        diagnosticos = @{
            codigo_cie10 = "I25.9"
            descripcion = "Enfermedad cardíaca isquémica"
        }
    }
} | ConvertTo-Json -Depth 10

Invoke-RestMethod -Uri "http://localhost:5678/webhook/orquestador-seguimientos-ai" -Method POST -ContentType "application/json" -Body $body
```

### **6.3 Respuesta esperada:**
```json
{
  "status": "completado",
  "cluster_asignado": "ALTO_RIESGO_ESTABLE",
  "score_riesgo": 75,
  "ai_agent_evaluacion": "ALTO",
  "seguimientos": {
    "total_generados": 2,
    "primer_seguimiento": "17/06/2025",
    "ultimo_seguimiento": "30/06/2025",
    "solo_cuestionarios": true,
    "maximo_dias": 45
  },
  "para_companero": {
    "tipo_interaccion": "CUESTIONARIO_CHATBOT",
    "total_cuestionarios_crear": 2,
    "cluster_medico": "ALTO_RIESGO_ESTABLE"
  }
}
```

---

## 🚨 **TROUBLESHOOTING**

### **Error: "Model not found"**
```
✅ Solución: 
- Verificar Base URL: https://api.groq.com/openai/v1
- Usar modelo: llama-3.1-8b-instant
```

### **Error: "API Key invalid"**
```
✅ Solución:
- Regenerar API key en console.groq.com
- Verificar que empiece con "gsk_"
- Configurar nuevamente la credencial
```

### **Error: "Webhook not registered"**
```
✅ Solución:
- Activar workflow (switch a ON)
- Ejecutar nodo Webhook una vez
- Verificar URL correcta
```

---

## ✅ **VERIFICACIÓN FINAL**

### **Checklist de configuración:**
- [ ] API Key Groq obtenida
- [ ] Credencial "Groq API" creada en n8n
- [ ] Workflow importado correctamente
- [ ] Chat Model configurado con Groq
- [ ] AI Agent conectado a Chat Model y Memory
- [ ] Workflow activado (switch ON)
- [ ] URL webhook funcionando
- [ ] Test con paciente 1 exitoso

### **URLs importantes:**
- 🌐 n8n: http://localhost:5678
- 🔗 Spring Boot: http://localhost:8090
- 📡 Webhook AI Agent: http://localhost:5678/webhook/orquestador-seguimientos-ai
- 🔑 Groq Console: https://console.groq.com/keys

¡Una vez completado, tendrás tu sistema multiagente con AI Agent funcionando! 🎉 