package com.healink.integrador.domain.interacciones_chatbot;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healink.integrador.core.controller.ControladorGenerico;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/interacciones_chatbot")
@Tag(name = "Interacciones con el chatbot", description = "API para gestión de interacciones con el chatbot")
public class InteraccionChatbotController extends ControladorGenerico<InteraccionChatbot, InteraccionChatbotDTO> {

    private final InteraccionChatbotService interaccionChatbotService;

    public InteraccionChatbotController(InteraccionChatbotService interaccionChatbotService, InteraccionChatbotMapper interaccionChatbotMapper) {
        super(interaccionChatbotService, interaccionChatbotMapper);
        this.interaccionChatbotService = interaccionChatbotService;
    }

    @GetMapping("/seguimiento/{seguimiento_id}")
    @Operation(summary = "Buscar interacciones de acuerdo a un seguimiento", description = "Obtiene todas las interacciones de un seguimiento específico")
    public ResponseEntity<List<InteraccionChatbotDTO>> buscarPorSeguimientoId(@PathVariable Long seguimiento_id) {
        List<InteraccionChatbot> interacciones = interaccionChatbotService.buscarPorSeguimientoId(seguimiento_id);
        return ResponseEntity.ok(mapeador.aListaDTO(interacciones));
    }

    @GetMapping("/seguimiento/{seguimiento_id}/paginado")
    @Operation(summary = "Buscar seguimientos por atención médica (paginado)", description = "Obtiene todos los seguimientos de una atención médica específica paginada")
    public ResponseEntity<Page<InteraccionChatbotDTO>> buscarPorSeguimientoIdPaginado(
            @PathVariable Long seguimiento_id, Pageable pageable) {
        Page<InteraccionChatbot> pagina = interaccionChatbotService.buscarPorSeguimientoId(seguimiento_id, pageable);
        return ResponseEntity.ok(pagina.map(interaccion -> mapeador.aDTO(interaccion)));
    }
}