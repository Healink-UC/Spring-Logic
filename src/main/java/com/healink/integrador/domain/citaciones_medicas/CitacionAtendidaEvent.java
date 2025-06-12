package com.healink.integrador.domain.citaciones_medicas;

import org.springframework.context.ApplicationEvent;

/**
 * Evento publicado cuando una citación médica es marcada como atendida
 */
public class CitacionAtendidaEvent extends ApplicationEvent {
    
    private final CitacionMedica citacionMedica;
    
    public CitacionAtendidaEvent(CitacionMedica citacionMedica) {
        super(citacionMedica);
        this.citacionMedica = citacionMedica;
    }
    
    public CitacionMedica getCitacionMedica() {
        return citacionMedica;
    }
} 