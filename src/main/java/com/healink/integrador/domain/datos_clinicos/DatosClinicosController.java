package com.healink.integrador.domain.datos_clinicos;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.healink.integrador.core.controller.ControladorGenerico;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/datos-clinicos")
@Tag(name = "Datos Clínicos", description = "API para gestión de datos clínicos de pacientes")
public class DatosClinicosController extends ControladorGenerico<DatosClinicos, DatosClinicosDTO> {

    private final DatosClinicosService datosClinicosService;

    public DatosClinicosController(DatosClinicosService datosClinicosService, DatosClinicosMapper datosClinicosMapper) {
        super(datosClinicosService, datosClinicosMapper);
        this.datosClinicosService = datosClinicosService;
    }

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Buscar datos clínicos por paciente", description = "Obtiene todos los datos clínicos de un paciente específico")
    public ResponseEntity<List<DatosClinicosDTO>> buscarPorPacienteId(@PathVariable Long pacienteId) {
        List<DatosClinicos> datosClinicos = datosClinicosService.buscarPorPacienteId(pacienteId);
        return ResponseEntity.ok(mapeador.aListaDTO(datosClinicos));
    }

    @GetMapping("/paciente/{pacienteId}/paginado")
    @Operation(summary = "Buscar datos clínicos por paciente (paginado)", description = "Obtiene los datos clínicos de un paciente de forma paginada")
    public ResponseEntity<Page<DatosClinicosDTO>> buscarPorPacienteIdPaginado(
            @PathVariable Long pacienteId, Pageable pageable) {
        Page<DatosClinicos> pagina = datosClinicosService.buscarPorPacienteId(pacienteId, pageable);
        return ResponseEntity.ok(pagina.map(entidad -> mapeador.aDTO(entidad)));
    }
}