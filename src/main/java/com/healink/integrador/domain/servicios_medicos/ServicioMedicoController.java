package com.healink.integrador.domain.servicios_medicos;

import com.healink.integrador.core.controller.ControladorGenerico;
import io.swagger.v3.oas.annotations.tags.Tag;
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
