# 🔍 Beam Search Questionnaire Agent

## 📋 Información General

- **Algoritmo**: Beam Search
- **Responsable**: Desarrollador B
- **Objetivo**: Optimizar secuencias de cuestionarios para máxima información diagnóstica
- **Implementación**: JavaScript en n8n
- **Integración**: Con K-Means Agent para personalización

## 🎯 Arquitectura del Agente

### **Problema a Resolver**
Dado un paciente clasificado en un cluster específico, encontrar la **secuencia óptima de preguntas** que maximice la probabilidad de obtener información diagnóstica útil.

### **Beam Search Algorithm**
```javascript
function beamSearchOptimalQuestionnaire(patientProfile, availableQuestions, beamWidth = 3) {
    // Beam Width: Mantener las 3 mejores secuencias en cada nivel
    // Heurística: Probabilidad de obtener información diagnóstica útil
    // Objetivo: Maximizar información con mínimo número de preguntas
}
```

## 🧮 Implementación JavaScript para n8n

### **Estructura de Datos**

```javascript
// Perfil del paciente desde K-Means
const patientProfile = {
    cluster_assigned: "MODERADO_JOVEN",
    patient_features: {
        riesgo_cardiovascular: 45.0,
        riesgo_hospitalizacion: 25.0,
        edad_normalizada: 0.25,
        factores_riesgo_count: 3.0,
        adherencia_historica: 80.0,
        dias_ultima_consulta: 45
    },
    questionnaire_types: ["consolidacion_habitos", "evolucion_cesacion_tabaco", "consolidacion_peso"]
};

// Catálogo de preguntas disponibles por tipo
const questionCatalog = {
    "consolidacion_habitos": [
        {
            id: "habit_1",
            text: "¿Has mantenido ejercicio regular?",
            information_value: 0.8,
            diagnostic_weight: 0.9,
            follow_up_potential: 0.7
        },
        {
            id: "habit_2", 
            text: "¿Has seguido dieta recomendada?",
            information_value: 0.7,
            diagnostic_weight: 0.8,
            follow_up_potential: 0.8
        }
    ],
    "evolucion_cesacion_tabaco": [
        {
            id: "tobacco_1",
            text: "¿Has fumado en las últimas 48h?",
            information_value: 0.9,
            diagnostic_weight: 1.0,
            follow_up_potential: 0.6
        }
    ]
};
```

### **Función Heurística**

```javascript
function calculateQuestionHeuristic(question, patientProfile, questionHistory) {
    // Factors influencing question value:
    
    // 1. Information Value (0-1): Cuánta información diagnóstica aporta
    let informationValue = question.information_value;
    
    // 2. Patient Cluster Relevance: Relevancia para el cluster del paciente
    let clusterRelevance = getClusterRelevance(question, patientProfile.cluster_assigned);
    
    // 3. Risk Factor Alignment: Alineación con factores de riesgo específicos
    let riskAlignment = getRiskAlignment(question, patientProfile.patient_features);
    
    // 4. Redundancy Penalty: Penalizar preguntas similares ya hechas
    let redundancyPenalty = calculateRedundancyPenalty(question, questionHistory);
    
    // 5. Follow-up Potential: Potencial para seguimientos futuros
    let followUpPotential = question.follow_up_potential;
    
    // Weighted combination
    let heuristic = (
        informationValue * 0.3 +
        clusterRelevance * 0.25 +
        riskAlignment * 0.25 +
        (1 - redundancyPenalty) * 0.1 +
        followUpPotential * 0.1
    );
    
    return heuristic;
}

function getClusterRelevance(question, cluster) {
    const clusterRelevanceMap = {
        "CRITICO_AGUDO": {
            "adherencia_urgente": 1.0,
            "evolucion_inmediata": 1.0,
            "consolidacion_habitos": 0.2
        },
        "MODERADO_JOVEN": {
            "consolidacion_habitos": 1.0,
            "evolucion_cesacion_tabaco": 0.8,
            "consolidacion_peso": 0.9
        },
        "ALTO_RIESGO_ESTABLE": {
            "adherencia_hipertension": 1.0,
            "evolucion_sintomas_cardiacos": 0.9
        }
    };
    
    return clusterRelevanceMap[cluster]?.[question.category] || 0.5;
}
```

### **Algoritmo Beam Search Principal**

```javascript
function beamSearchOptimalSequence(patientProfile, maxQuestions = 5, beamWidth = 3) {
    // Inicialización
    let beam = [
        {
            sequence: [],
            score: 0,
            information_gathered: 0,
            estimated_completion: 1.0
        }
    ];
    
    for (let level = 0; level < maxQuestions; level++) {
        let candidates = [];
        
        // Expandir cada secuencia en el beam
        for (let sequence of beam) {
            let availableQuestions = getAvailableQuestions(
                patientProfile.questionnaire_types,
                sequence.sequence
            );
            
            // Generar candidatos agregando cada pregunta disponible
            for (let question of availableQuestions) {
                let newSequence = {
                    sequence: [...sequence.sequence, question],
                    score: sequence.score + calculateQuestionHeuristic(
                        question, 
                        patientProfile, 
                        sequence.sequence
                    ),
                    information_gathered: sequence.information_gathered + question.information_value,
                    estimated_completion: estimateCompletionProbability(
                        [...sequence.sequence, question],
                        patientProfile
                    )
                };
                
                candidates.push(newSequence);
            }
        }
        
        // Seleccionar los mejores candidatos (Beam Width)
        candidates.sort((a, b) => b.score - a.score);
        beam = candidates.slice(0, beamWidth);
        
        // Condición de parada: Si tenemos suficiente información
        if (beam[0].information_gathered >= 0.8) {
            break;
        }
    }
    
    // Retornar la mejor secuencia
    return beam[0];
}

function estimateCompletionProbability(questionSequence, patientProfile) {
    // Estimar probabilidad de que el paciente complete esta secuencia
    // Basado en: longitud, tipo de preguntas, perfil del paciente
    
    let lengthPenalty = Math.max(0, 1 - (questionSequence.length * 0.1));
    let difficultyPenalty = calculateDifficultyPenalty(questionSequence);
    let adherenceFactor = patientProfile.patient_features.adherencia_historica / 100;
    
    return lengthPenalty * (1 - difficultyPenalty) * adherenceFactor;
}
```

### **Integración con n8n**

```javascript
// Código para nodo n8n - Function Node
const inputData = $input.all();
const patientData = inputData[0].json;

// Ejecutar Beam Search
const optimalSequence = beamSearchOptimalSequence(
    patientData.patient_profile,
    5,  // máximo 5 preguntas
    3   // beam width de 3
);

// Preparar salida para siguiente nodo
return [
    {
        json: {
            patient_id: patientData.patient_id,
            cluster: patientData.patient_profile.cluster_assigned,
            optimal_sequence: optimalSequence.sequence,
            expected_information: optimalSequence.information_gathered,
            completion_probability: optimalSequence.estimated_completion,
            total_score: optimalSequence.score,
            algorithm_used: "Beam Search",
            beam_width: 3,
            timestamp: new Date().toISOString()
        }
    }
];
```

## 📊 Métricas de Evaluación

### **Métricas del Algoritmo**
- **Information Gain**: Información diagnóstica total obtenida
- **Completion Rate**: % pacientes que completan secuencia optimizada
- **Beam Efficiency**: Calidad vs computational cost
- **Sequence Length**: Promedio de preguntas necesarias

### **Comparación con Secuencias Fijas**
```javascript
// Función para evaluar mejora vs secuencias tradicionales
function compareWithTraditional(patientId, beamResult, traditionalResult) {
    return {
        patient_id: patientId,
        beam_search: {
            questions_count: beamResult.sequence.length,
            information_gathered: beamResult.information_gathered,
            completion_probability: beamResult.estimated_completion
        },
        traditional: {
            questions_count: traditionalResult.questions.length,
            information_gathered: traditionalResult.estimated_information,
            completion_probability: traditionalResult.historical_completion
        },
        improvement: {
            information_gain: beamResult.information_gathered - traditionalResult.estimated_information,
            efficiency_gain: beamResult.information_gathered / beamResult.sequence.length,
            completion_improvement: beamResult.estimated_completion - traditionalResult.historical_completion
        }
    };
}
```

## 🔗 Integración con K-Means Agent

### **Flujo de Datos**
1. **K-Means Agent** clasifica paciente en cluster
2. **Beam Search Agent** recibe perfil del cluster
3. **Optimización** de secuencia de cuestionarios
4. **Retorno** de secuencia óptima para ejecución

### **Estructura de Comunicación**
```javascript
// Input desde K-Means Agent
const kmeansOutput = {
    paciente_id: 123,
    assigned_cluster: "MODERADO_JOVEN",
    cluster_strategy: {
        questionnaire_types: ["consolidacion_habitos", "evolucion_cesacion_tabaco"],
        frequency_days: 10,
        priority: 0.5
    },
    patient_features: { /* features del paciente */ }
};

// Output hacia sistema de cuestionarios
const beamSearchOutput = {
    paciente_id: 123,
    optimal_questionnaire_sequence: [
        { type: "consolidacion_habitos", question_id: "habit_1", priority: 1 },
        { type: "evolucion_cesacion_tabaco", question_id: "tobacco_1", priority: 2 },
        { type: "consolidacion_habitos", question_id: "habit_2", priority: 3 }
    ],
    estimated_completion_time: "8-12 minutos",
    information_value: 0.85,
    algorithm_metadata: {
        beam_width: 3,
        iterations: 4,
        alternatives_explored: 12
    }
};
```

## 🎓 Valor Académico

### **Conceptos de Sistemas Inteligentes Aplicados**
- ✅ **Búsqueda heurística** con pruning por beam width
- ✅ **Optimización multi-objetivo** (información vs completion)
- ✅ **Heurística específica del dominio** médico
- ✅ **Integración multi-agente** con K-Means

### **Extensiones Posibles**
- **A* Search**: Para óptimos garantizados
- **Genetic Algorithm**: Para exploración más amplia
- **Reinforcement Learning**: Para adaptación basada en resultados

---

**🔄 Estado**: Pendiente de implementación por Desarrollador B
**🎯 Deadline**: 2 días restantes
**📚 Recursos**: Documentación completa disponible 