package com.healink.integrador.domain.interacciones_chatbot;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;

import com.healink.integrador.core.mapper.MapeadorGenerico;

@Mapper(componentModel = "spring")
public interface InteraccionChatbotMapper extends MapeadorGenerico<InteraccionChatbot, InteraccionChatbotDTO> {
    
    @Override
    @Mapping(target = "seguimiento_id", source = "seguimiento.id") 
    @Mapping(target = "paciente_id", source = "paciente.id")
    InteraccionChatbotDTO aDTO(InteraccionChatbot entity);

    @Override
    @Mapping(target = "seguimiento.id", source = "seguimiento_id")
    @Mapping(target = "paciente.id", source = "paciente_id")
    InteraccionChatbot aEntidad(InteraccionChatbotDTO dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "seguimiento.id", source = "seguimiento_id")
    @Mapping(target = "paciente.id", source = "paciente_id")
    void actualizarEntidadDesdeDTO(InteraccionChatbotDTO dto, @MappingTarget InteraccionChatbot entity);

}