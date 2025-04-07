package com.healink.integrador.domain.servicios_medicos;

import com.healink.integrador.core.controller.ControladorGenerico;
import com.healink.integrador.domain.entidades_salud.EntidadSalud;
import com.healink.integrador.domain.entidades_salud.EntidadSaludDTO;
import com.healink.integrador.domain.entidades_salud.EntidadSaludMapper;
import com.healink.integrador.domain.entidades_salud.EntidadSaludService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/servicios_medicos")
@Tag(name = "ServiciosMedicos", description = "API para gestión de servicios medicos")
public class ServicioMedicoController extends ControladorGenerico<ServicioMedico, ServicioMedicoDTO> {

    private final ServicioMedicoService servicioMedicoService;

    public ServicioMedicoController(ServicioMedicoService servicioMedicoService, ServicioMedicoMapper servicioMedicoMapper) {
        super(servicioMedicoService, servicioMedicoMapper);
        this.servicioMedicoService = servicioMedicoService;
    }
}
