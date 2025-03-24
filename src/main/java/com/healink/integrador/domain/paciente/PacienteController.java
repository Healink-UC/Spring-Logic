package com.healink.integrador.domain.paciente;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healink.integrador.core.controller.ControladorGenerico;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController extends ControladorGenerico<Paciente, PacienteDTO> {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService, PacienteMapper pacienteMapper) {
        super(pacienteService, pacienteMapper);
        this.pacienteService = pacienteService;
    }

    @GetMapping("/localidad/{localidad}")
    public ResponseEntity<List<PacienteDTO>> getByLocalidad(@PathVariable String localidad) {
        List<Paciente> pacientes = pacienteService.findByLocalidad(localidad);
        return ResponseEntity.ok(mapeador.aListaDTO(pacientes));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<PacienteDTO> getByUsuarioId(@PathVariable Long usuarioId) {
        return pacienteService.findByUsuarioId(usuarioId)
                .map(paciente -> ResponseEntity.ok(mapeador.aDTO(paciente)))
                .orElse(ResponseEntity.notFound().build());
    }
}