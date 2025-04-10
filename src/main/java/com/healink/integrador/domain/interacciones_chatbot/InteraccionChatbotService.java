package com.healink.integrador.domain.interacciones_chatbot;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healink.integrador.core.service.ServicioGenerico;

import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class InteraccionChatbotService extends ServicioGenerico<InteraccionChatbot> {

    private final InteraccionChatbotRepository interaccionChatbotRepository;

    public InteraccionChatbotService(InteraccionChatbotRepository interaccionChatbotRepository) {
        super(interaccionChatbotRepository);
        this.interaccionChatbotRepository = interaccionChatbotRepository;
    }

    @Override
    public InteraccionChatbot guardar(InteraccionChatbot interaccionChatbot) {
        return super.guardar(interaccionChatbot);
    }

    @Override
    public InteraccionChatbot obtenerPorId(Long id) {
        return interaccionChatbotRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Interacción con el chat bot no encontrada con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<InteraccionChatbot> buscarPorSeguimientoId(Long seguimiento_id) {
        return interaccionChatbotRepository.findBySeguimientoId(seguimiento_id);
    }

    @Transactional(readOnly = true)
    public Page<InteraccionChatbot> buscarPorSeguimientoId(Long seguimiento_id, Pageable pageable) {
        return interaccionChatbotRepository.findBySeguimientoId(seguimiento_id, pageable);
    }
}