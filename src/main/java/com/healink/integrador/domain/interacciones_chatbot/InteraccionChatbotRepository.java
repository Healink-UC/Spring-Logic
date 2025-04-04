package com.healink.integrador.domain.interacciones_chatbot;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.healink.integrador.core.Repository.RepositorioGenerico;

@Repository
public interface InteraccionChatbotRepository extends RepositorioGenerico<InteraccionChatbot> {

    List<InteraccionChatbot> findBySeguimientoId(Long seguimiento_id);

    Page<InteraccionChatbot> findBySeguimientoId(Long seguimiento_id, Pageable pageable);
}
