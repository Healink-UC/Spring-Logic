package com.healink.integrador.domain.interacciones_chatbot;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.databind.JsonNode;
import com.healink.integrador.core.entity.EntidadAuditable;
import com.healink.integrador.domain.paciente.Paciente;
import com.healink.integrador.domain.seguimientos.Seguimiento;

import java.sql.Timestamp;

@Entity
@Table(name = "INTERACCIONES_CHATBOT")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class InteraccionChatbot extends EntidadAuditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seguimiento_id")
    private Seguimiento seguimiento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @Column(name = "fecha_hora", nullable = false)
    private Timestamp fecha_hora;
    
    @Column(name = "entrada", nullable = false)
    private String entrada;
    
    @Column(name = "respuesta", nullable = false)
    private String respuesta;

    @Column(name = "intent_detectado", nullable = false)
    private String intent_detectado;

    @Column(name = "entidades_detectadas")
    private String entidades_detectadas;

    @Column(name = "contexto_conversacion")
    private String contexto_conversacion;
}
